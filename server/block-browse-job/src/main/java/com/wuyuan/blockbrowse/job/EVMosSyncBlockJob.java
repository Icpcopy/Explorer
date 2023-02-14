package com.wuyuan.blockbrowse.job;

import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.entity.*;
import com.wuyuan.database.sevice.BlockService;
import com.wuyuan.database.sevice.ConfigService;
import com.wuyuan.database.sevice.evmos.*;
import com.wuyuan.database.util.ContractTypeEnum;
import com.wuyuan.database.util.ERC20Util;
import com.wuyuan.database.util.Erc20AddressUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class EVMosSyncBlockJob {
    private static final Logger log = LoggerFactory.getLogger(EVMosSyncBlockJob.class);

    @Autowired
    private EVMosTransactionService evMosTransactionService;
    @Autowired
    private EVMosTransactionReceiptService evMosTransactionReceiptService;
    @Autowired
    private EVMosContractService evMosContractService;
    @Resource
    private BlockService blockService;
    @Autowired
    private EVMosEventsService evMosEventsService;
    @Autowired
    private EVMosBlockService evMosBlockService;
    @Autowired
    private EVMosLogsService evMosLogsService;
    @Autowired
    private ConfigService configService;

    private EVMosService evMosService;

    @Autowired
    private EVMNftService evmNftService;
    @SneakyThrows
    @Scheduled(fixedDelay = 1000L)
    public void syncEvmBlock() throws IOException, ExecutionException, InterruptedException {
        String is_evmos = configService.getConfig("is_evmos");
        if (!"1".equalsIgnoreCase(is_evmos)) {
            return;
        }
        evMosService = new EVMosService(configService.getConfig("web3jUrl"));
        GlobalBlock global = blockService.getGlobal(null);
        log.info("syncEvmBlock start");
        if (global == null) {
            return;
        }
        if (global.getEvmOsNumber() == null || global.getEvmOsNumber() == 0) {
            global.setEvmOsNumber(1L);
            blockService.updateGlobalEvmGlobal(global);
        }
        long newBlock = evMosService.getNewBlock();
        if(newBlock>global.getEvmOsNumber()+1000){
            newBlock=global.getEvmOsNumber()+1000;
        }
        for (long i = global.getEvmOsNumber(); i < newBlock; i++) {
            EthBlock.Block block = evMosService.getEVMosBlock(i);
            log.info("evmBlock: {}", block.getNumber().longValue());
            for (EthBlock.TransactionResult transactionResult: block.getTransactions()){
                if (transactionResult instanceof EthBlock.TransactionObject) {
                    EthBlock.TransactionObject transactionObject = (EthBlock.TransactionObject) transactionResult;
                    try {

                        transactionReceipt(evMosService.getEVMosTransactions(transactionObject.getHash()), block.getTimestamp());
                        Transaction evMosTransactions = evMosService.getEVMosTransactions(transactionObject.getHash());

                        Transaction transaction = evMosTransactionService.getTransaction(transactionObject.getHash());
                        if (transaction == null) {
                            evMosTransactionService.save(evMosTransactions);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("同步交易失败：" + transactionObject.getHash());
                    }
                }
            }
            EthBlock.Block block1 = evMosBlockService.getBlock(block.getNumber());
            if (block1 == null) {
                evMosBlockService.save(block);
            }
            global.setEvmOsNumber(block.getNumber().longValue());
            blockService.updateGlobalEvmGlobal(global);
        }
    }

    public void transactionReceipt( Transaction transaction, BigInteger timestamp) throws Exception {
        String hash=transaction.getHash();
        TransactionReceipt receipt = evMosService.getEVMosTransactionReceipt(hash);
        if (StringUtils.isBlank(receipt.getTo()) && StringUtils.isNotBlank(receipt.getContractAddress())) {
            EVMosContract contract = evMosContractService.getContract(receipt.getContractAddress());
            if(receipt.getContractAddress().equalsIgnoreCase("0x78eb56acd864c3ec0aba277cb1c7e37b75684ecb")){
                System.out.println("hash");
            }
            if (contract == null) {
                String contractType = chechContractType(hash, receipt.getContractAddress());
                saveContract(receipt.getContractAddress(), hash, receipt.getFrom(), contractType,transaction.getBlockNumber().longValue(),timestamp);
            }
        }
        if (receipt.getLogs().size() > 0) {
            List<Log> logs = receipt.getLogs();
            for (Log log : logs) {
                EVMosContract contractLog = evMosContractService.getContract(log.getAddress());
                if (contractLog == null) {
                    String contractType = chechContractType(hash, log.getAddress());
                    saveContract(log.getAddress(), null, null, contractType,transaction.getBlockNumber().longValue(),timestamp);
                    contractLog = evMosContractService.getContract(log.getAddress());
                }

                if (contractLog.getContractType() ==0) {
                    if (StringUtils.isBlank(log.getData()) || "0x".equalsIgnoreCase(log.getData())) {
                        continue;
                    }
                } else if(contractLog.getContractType() ==2){
                    String input = transaction.getInput();
                    if (input.startsWith(ERC20Util.ERC721_MINT_METHOD_ID)) {
                        List<String> inputs = ERC20Util.getInput(input);
                        EVMNft nft = new EVMNft();
                        nft.setIssuer(transaction.getFrom());
                        nft.setOwner(Erc20AddressUtil.topic2Address(inputs.get(0)));
                        nft.setNftId(Numeric.toBigInt(inputs.get(1)).toString());
                        nft.setContentAddress(transaction.getTo());
                        nft.setMintTime(String.valueOf(timestamp));
                        nft.setSaveTime(new Date());
                        nft.setHash(hash);
                        nft.setTransactionCount(1);
                        EVMNft nftByIdAndContract = evmNftService.getNftByIdAndContract(Numeric.toBigInt(inputs.get(1)).toString(), transaction.getTo());
                        if(nftByIdAndContract==null){
                            evmNftService.save(nft);
                        }
                    } else {
                        if ("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef".equalsIgnoreCase(log.getTopics().get(0))
                                || "0x8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925".equalsIgnoreCase(log.getTopics().get(0))) {
                            String nftId;
                            if(log.getTopics().size()<4){
                                nftId=Numeric.toBigInt(log.getData()).toString();
                            }else{
                                nftId= Numeric.toBigInt(log.getTopics().get(3)).toString();
                            }
                            EVMNft nft = evmNftService.getNftByIdAndContract(nftId, log.getAddress());
                            if (nft != null) {
                                nft.setTransactionCount(nft.getTransactionCount() + 1);
                                if ("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef".equalsIgnoreCase(log.getTopics().get(0))) {
                                    evmNftService.updateOwner(nft.getHash(), Erc20AddressUtil.topic2Address(log.getTopics().get(2)));
                                }
                                evmNftService.updateTranctionCount(nft.getHash(), nft.getTransactionCount());
                            }else {
                                nft=new EVMNft();
                                nft.setTransactionCount(1);
                                nft.setNftId(nftId);
                                nft.setHash(hash);
                                nft.setOwner(evMosService.getNFTOwnerOf(log.getAddress(), nftId));
                                nft.setIssuer(transaction.getFrom());
                                nft.setSaveTime(new Date());
                                nft.setMintTime(String.valueOf(timestamp));
                                nft.setContentAddress(log.getAddress());
                                evmNftService.save(nft);
                            }

                        }
                    }
                }
//                EVMEvents eventByCode = evMosEventsService.getEventByCode(log.getTopics().get(0));
//                if (eventByCode == null) {
//                    break;
//                }
                Log l = evMosLogsService.getLogByHashAndIndex(hash, log.getLogIndex());
                if (l == null) {
                    contractLog.setTransactionCount(contractLog.getTransactionCount() + 1);
                    evMosContractService.updateTranctionCount(contractLog.getIds(), contractLog.getTransactionCount());
                    JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(log));
                    jsonObject.put("contractType", contractLog.getContractType());
                    evMosLogsService.save(jsonObject);
                }
            }
        }

        TransactionReceipt transactionReceipt = evMosTransactionReceiptService.getTransactionReceipt(hash);
        if (transactionReceipt == null) {
            evMosTransactionReceiptService.save(receipt);
        }

    }

    @Autowired
    private EVMosAddressService evMosAddressService;
    public void saveContract(String address, String hash, String owner, String contractType,long blockNumber,BigInteger timestamp) throws Exception {
        if (StringUtils.isBlank(owner)) {
            owner = evMosService.getOwner(address);
        }

        EVMosContract contractLog = evMosContractService.getContract(address);
        if (contractLog == null) {
            contractLog = evMosService.erc20Name(address, owner);
            contractLog.setHash(hash);
            contractLog.setSaveTime(new Date());
            contractLog.setAbi("");
            if (contractLog.getContractType() == 1) {
                ContractTypeEnum contractTypeEnum = ContractTypeEnum.valueOf(contractType);
                contractLog.setContractType(contractTypeEnum.getType());
            }else{
                if(contractLog.getContractType()==ContractTypeEnum.erc20.getType()){
                    String owner1 = contractLog.getOwner();
                    BigDecimal tokenBalance = evMosService.getTokenBalance(owner1, contractLog.getContractAddress(), contractLog.getDecimal());
                    EvmAddress addressBalance=evMosAddressService.getAddressByAddressAndContractAddress(owner,contractLog.getContractAddress());

                    if(addressBalance==null){
                        addressBalance=new EvmAddress();
                        addressBalance.setAddress(owner);
                        addressBalance.setContactAddress(contractLog.getContractAddress());

                        addressBalance.setBalance(tokenBalance.doubleValue());
                        addressBalance.setDecimal(contractLog.getDecimal());
                        addressBalance.setStartBlockNumber(blockNumber);
                        addressBalance.setSymbol(contractLog.getTokenSymbol());
                        evMosAddressService.save(addressBalance);
                    }else {
                        addressBalance.setBalance(tokenBalance.doubleValue());
                        evMosAddressService.update(addressBalance);
                    }
                }
            }
            contractLog.setTime(timestamp);
            evMosContractService.save(contractLog);
        }
    }

    public String chechContractType(String hash, String contractAddress) throws Exception {
        Transaction transaction = evMosService.getEVMosTransactions(hash);
        boolean isErc721;
        isErc721 = isErc721(contractAddress);
        if (isErc721) {
            return ContractTypeEnum.erc721.getName();
        }
        return ContractTypeEnum.other.getName();
    }

    private boolean isErc721(String contractAddress) throws Exception {
        boolean b = evMosService.supportsInterface(Numeric.hexStringToByteArray(ERC20Util.ERC721_SUPPORTS_INTERFACE), contractAddress);
        return b;
    }
}
