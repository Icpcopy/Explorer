package com.wuyuan.blockbrowse.job;


import com.wuyuan.database.entity.*;
import com.wuyuan.database.sevice.BlockService;
import com.wuyuan.database.sevice.ConfigService;
import com.wuyuan.database.sevice.evmos.*;
import com.wuyuan.database.util.*;
import io.cosmos.util.AmountUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
public class EvmStatsJob {
    @Autowired
    private EVMosAddressService evMosAddressService;
    @Autowired
    private EVMosContractService evMosContractService;
    @Autowired
    private EVMosContranctStatsService evMosContranctStatsService;
    private EVMosService evMosService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private EVMosTransactionReceiptService evMosTransactionReceiptService;
    @Autowired
    private EVMosEventsService evMosEventsService;
    @Autowired
    private EVMosBlockService evMosBlockService;

    @SneakyThrows
    @Scheduled(fixedDelay = 1000L)
    private void evmStats(){
        String is_evmos = configService.getConfig("is_evmos");
        if(!"1".equalsIgnoreCase(is_evmos)){
            return;
        }
        evMosService=new EVMosService(configService.getConfig("web3jUrl"));
        GlobalBlock global= blockService.getGlobal(null);
        if(global==null){
            return;
        }
        if(global.getEvmStatsNumber()==null||global.getEvmStatsNumber()==0){
            global.setEvmStatsNumber(0L);
            blockService.updateGlobalEvmStats(global);
        }
        long newBlock = global.getEvmOsNumber()-3;
        if(newBlock>global.getEvmStatsNumber()+1000){
            newBlock=global.getEvmStatsNumber()+1000;
        }
        for (long i = global.getEvmStatsNumber()+1; i < newBlock ; i++) {
            List<TransactionReceipt> receipts = evMosTransactionReceiptService.getTransactionsByBlockNumber(i);
            EthBlock.Block block = evMosBlockService.getBlock(new BigInteger(String.valueOf(i)));
            for (TransactionReceipt receipt:receipts){
                for (Log log:receipt.getLogs()){
                    String contractAddress = log.getAddress();
                    EVMosContract contract = evMosContractService.getContract(contractAddress);
                    if(contract==null){
                        throw new RuntimeException("合约不存在："+contractAddress);
                    }
                    String eventCode=log.getTopics().get(0);
                    int addCount=0;
                    for (int j = 0; j < log.getTopics().size(); j++) {
                        if(contract.getContractType()!=ContractTypeEnum.erc20.getType()){
                            break;
                        }
                        if(j==0){
                            continue;
                        }
                        String addresses=Erc20AddressUtil.topic2Address(log.getTopics().get(j));
                        if("0x0000000000000000000000000000000000000000".equalsIgnoreCase(addresses)){
                            continue;
                        }
                        EvmAddress address=evMosAddressService.getAddressByAddressAndContractAddress(addresses,contractAddress);
                        BigDecimal tokenBalance = evMosService.getTokenBalance(addresses, contractAddress, contract.getDecimal());


                        if(address==null&&tokenBalance.compareTo(BigDecimal.ZERO)>0){
                            address=new EvmAddress();
                            address.setAddress(addresses);
                            address.setContactAddress(contractAddress);

                            address.setBalance(tokenBalance.doubleValue());
                            address.setDecimal(contract.getDecimal());
                            address.setStartBlockNumber(receipt.getBlockNumber().longValue());
                            address.setSymbol(contract.getTokenSymbol());
                            evMosAddressService.save(address);
                            addCount+=1;
                        }else if(tokenBalance.compareTo(BigDecimal.ZERO)>0||address!=null){
                            address.setBalance(tokenBalance.doubleValue());
                            evMosAddressService.update(address);
                        }

                    }
                    EVMEvents eventByCode = evMosEventsService.getEventByCode(eventCode, ContractTypeEnum.init(contract.getContractType()).getName());
                    if(eventByCode==null){
//                        throw new RuntimeException("不支持的合约事件："+contractAddress+";hash："+receipt.getTransactionHash());
                        return;
                    }

                    if(ContractTypeEnum.other.getName().equalsIgnoreCase(eventByCode.getContractType())){
                        return;
                    }

                    BigDecimal amount = BigDecimal.ZERO;
                    String[] data = ERC20Util.decodeData(log.getData());
                    if (data.length > 0&&contract.getContractType()==ContractTypeEnum.erc20.getType()&&log.getTopics().get(0).equals(ERC20Util.TRANSFER_EVENT_ID)) {
                        amount = BigDecimalUtil.computeDecimal(
                                new BigDecimal(Numeric.decodeQuantity(data[0])), contract.getDecimal()
                        );
                    }
                    evMosContractService.updateStats(1,addCount,contract.getIds());
                    String day = DateFormatUtil.timeToUTCFormat(block.getTimestamp().multiply(new BigInteger("1000")).longValue());
                    EVMosContranctStats stats = evMosContranctStatsService.getByDayAncContractAddress(day, contractAddress);
                    if(stats==null){
                        stats=new EVMosContranctStats();
                        stats.setAmountTotal(amount.doubleValue());
                        stats.setContactAddress(contractAddress);
                        stats.setDay(day);
                        stats.setEndBlockNumber(block.getNumber().longValue());
                        stats.setStartBlockNumber(block.getNumber().longValue());
                        stats.setTransactionCount(1);
                        evMosContranctStatsService.save(stats);
                    }else {
                        evMosContranctStatsService.update(stats.getId(), 1,amount,block.getNumber().longValue());
                    }
                }
            }
            global.setEvmStatsNumber(i);
            blockService.updateGlobalEvmStats(global);
        }
    }

}
