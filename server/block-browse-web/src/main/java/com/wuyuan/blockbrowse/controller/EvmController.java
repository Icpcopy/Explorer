package com.wuyuan.blockbrowse.controller;

import com.wuyuan.blockbrowse.entity.rsp.*;
import com.wuyuan.blockbrowse.utils.ApiResult;
import com.wuyuan.blockbrowse.utils.EvmDao2Rsp;
import com.wuyuan.blockbrowse.utils.TransferUtil;
import com.wuyuan.database.entity.*;
import com.wuyuan.database.sevice.ConfigService;
import com.wuyuan.database.sevice.evmos.*;
import com.wuyuan.database.util.*;
import io.cosmos.util.AddressUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "EVMOS模块相关")
@RestController
@RequestMapping("evm")
public class EvmController {
    @Autowired
    private EVMosContractService evMosContractService;
    @Autowired
    private EVMosService evMosService;
    @Autowired
    private EVMosTransactionReceiptService evMosTransactionReceiptService;
    @Autowired
    private EVMosTransactionService evMosTransactionService;
    @Autowired
    private EVMosBlockService evMosBlockService;
    @Autowired
    private EVMosLogsService evMosLogsService;
    @Autowired
    private EVMosAddressService evMosAddressService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private EVMosContranctStatsService evMosContranctStatsService;
    @Autowired
    private EVMosEventsService evMosEventsService;
    @Autowired
    private EVMNftService evmNftService;


    @ApiOperation(value = "根据交易总数排序查询erc20 token合约", nickname = "根据交易总数排序查询erc20 token合约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "搜索参数", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int"),
    })
    @GetMapping("getTokenContractsList")
    public ApiResult<PageModel<EvmContractRsp>> getEvmContract(int pageIndex, int pageSize, String param) {
        PageModel<EVMosContract> contracts = evMosContractService.getContracts(pageSize, pageIndex, param, 0);
        List<EvmContractRsp> results = new ArrayList<>();
        for (int i = 0; i < contracts.getRecords().size(); i++) {
            BigDecimal toDayAmount = BigDecimal.ZERO;
            EVMosContranctStats stats = evMosContranctStatsService.getByDayAncContractAddress(DateFormatUtil.timeToUTCFormat(System.currentTimeMillis()), contracts.getRecords().get(i).getContractAddress());
            if (stats != null && stats.getAmountTotal() != null) {
                toDayAmount = new BigDecimal(stats.getAmountTotal());
            }
            EvmContractRsp evmContractRsp = EvmDao2Rsp.getEvmContractRsp(contracts.getRecords().get(i), toDayAmount, evMosAddressService.getTotalAmountByContact(contracts.getRecords().get(i).getContractAddress()));
            if (contracts.getRecords().get(i).getContractType() == ContractTypeEnum.erc721.getType()) {
                evmContractRsp.setAddresstotal(evmNftService.getHolders(contracts.getRecords().get(i).getContractAddress()));
            }
            results.add(evmContractRsp);
        }
        return new ApiResult<>(200, "request success", new PageModel<>(results, pageIndex, pageSize, contracts.getTotal()));
    }

    @ApiOperation(value = "根据交易总数排序查询erc721 token合约", nickname = "根据交易总数排序查询erc20 token合约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "搜索参数", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int"),
            @ApiImplicitParam(name = "contractType", value = "合约类型 ERC721:2  ERC20: 0", required = true, dataType = "int"),
    })
    @GetMapping("getTokenContractsListByContractType")
    public ApiResult<PageModel<EvmContractRsp>> getEvmContract(int pageIndex, int pageSize, String param, int contractType) throws Exception {
        PageModel<EVMosContract> contracts = evMosContractService.getContracts(pageSize, pageIndex, param, contractType);
        List<EvmContractRsp> results = new ArrayList<>();
        for (int i = 0; i < contracts.getRecords().size(); i++) {
            BigDecimal toDayAmount = BigDecimal.ZERO;
            EVMosContranctStats stats = evMosContranctStatsService.getByDayAncContractAddress(DateFormatUtil.timeToUTCFormat(System.currentTimeMillis()), contracts.getRecords().get(i).getContractAddress());
            if (stats != null && stats.getAmountTotal() != null) {
                toDayAmount = new BigDecimal(stats.getAmountTotal());
            }
            EvmContractRsp evmContractRsp = EvmDao2Rsp.getEvmContractRsp(contracts.getRecords().get(i), toDayAmount, evMosAddressService.getTotalAmountByContact(contracts.getRecords().get(i).getContractAddress()));
            if (contracts.getRecords().get(i).getContractType() == ContractTypeEnum.erc721.getType()) {
                evmContractRsp.setAddresstotal(evmNftService.getHolders(contracts.getRecords().get(i).getContractAddress()));
            }
            results.add(evmContractRsp);
        }
        return new ApiResult<>(200, "request success", new PageModel<>(results, pageIndex, pageSize, contracts.getTotal()));
    }

    @ApiOperation(value = "查询token合约详情", nickname = "查询token合约详情")
    @GetMapping("getTokenContractInfo")
    public ApiResult<EvmContractRsp> getContractInfo(String contractAddress) throws Exception {
        EVMosContract contract = evMosContractService.getEvmosContractByContractAddress(contractAddress);
        if (contract == null) {
            return new ApiResult<>(400, "contract does not exist");
        }
        BigDecimal toDayAmount = BigDecimal.ZERO;
        EVMosContranctStats stats = evMosContranctStatsService.getByDayAncContractAddress(DateFormatUtil.timeToUTCFormat(System.currentTimeMillis()), contract.getContractAddress());
        if (stats != null && stats.getAmountTotal() != null) {
            toDayAmount = new BigDecimal(stats.getAmountTotal());
        }
        BigDecimal totalSupply = evMosService.getTotalSupply(contractAddress, contract.getDecimal(), contract.getOwner());
        evMosContractService.updateTotalSupply(totalSupply, contract.getIds());
        EvmContractRsp evmContractRsp = EvmDao2Rsp.getEvmContractRsp(contract, toDayAmount, evMosAddressService.getTotalAmountByContact(contractAddress));

        if (totalSupply.compareTo(BigDecimal.ZERO) == -1) {
            evmContractRsp.setTotalSupply(BigDecimal.ZERO.toPlainString());
        } else {
            evmContractRsp.setTotalSupply(totalSupply.toPlainString());
        }
        if (contract.getContractType() == ContractTypeEnum.erc721.getType()) {
            evmContractRsp.setAddresstotal(evmNftService.getHolders(contractAddress));
            evmContractRsp.setTotalSupply(evmNftService.getNftByContract(contractAddress, 1, 1).getTotal() + "");
        }
        return new ApiResult<EvmContractRsp>(200, "request success", evmContractRsp);
    }

    @ApiOperation(value = "查询地址信息", nickname = "查询地址信息")
    @GetMapping("getAddressInfo")
    public ApiResult<EvmAddressRsp> getAddressAssets(String address) throws IOException {
        List<EvmAddress> addressList = evMosAddressService.getAddressList(address);
        BigInteger balances = evMosService.getBalances(address);
        EvmAddressRsp res = new EvmAddressRsp();
        res.setAddress(address);
        res.setBalance(BigDecimalUtil.computeDecimal(new BigDecimal(balances.toString()), configService.getEvmosDecimal()));
        List<EvmAddressTokenAssetsRsp> rsps = new ArrayList<>();
        for (EvmAddress evmAddress : addressList) {
            BigDecimal tokenBalance = evMosService.getTokenBalance(evmAddress.getAddress(), evmAddress.getContactAddress(), evmAddress.getDecimal());
            if (tokenBalance.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            } else {
                evmAddress.setBalance(tokenBalance.doubleValue());
                evMosAddressService.update(evmAddress);
                rsps.add(EvmDao2Rsp.getEvmTokenR(evmAddress));
            }
        }
        EVMosContract contract = evMosContractService.getContract(address);
        res.setContract(false);
        if (contract != null) {
            res.setContract(true);
        }
        res.setTokens(rsps);
        return new ApiResult<>(200, "request success", res);
    }

    @ApiOperation(value = "合约资产持有者统计", nickname = "合约资产持有者统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contractAddress", value = "合约地址", required = true, dataType = "String"),
    })
    @GetMapping("contractAssetsDistribution")
    public ApiResult<ContractAssetsHolderRsp> getContractAssteStats(String contractAddress) throws Exception {
        EVMosContract contract = evMosContractService.getEvmosContractByContractAddress(contractAddress);
        if (contract == null) {
            return new ApiResult<>(400, "contract does not exist");
        }
        List<EvmAddress> addressesByContact = evMosAddressService.getAddressesByContact(contractAddress, 1, 1000);
        List<EvmAddressTokenAssetsRsp> topList = new ArrayList<>();
        List<ContractAssteDistributionRsp> assteDistribution = new ArrayList<>();
        ContractAssetsHolderRsp rsp = new ContractAssetsHolderRsp();
        rsp.setContractAddress(contractAddress);
        BigDecimal totalSupply = evMosService.getTotalSupply(contractAddress, contract.getDecimal(), contract.getOwner());
        if (totalSupply.compareTo(BigDecimal.ZERO) == -1) {
            rsp.setTotalSupply(BigDecimal.ZERO);
        } else {
            rsp.setTotalSupply(totalSupply);
        }
        evMosContractService.updateTotalSupply(totalSupply, contract.getIds());
        ContractAssteDistributionRsp top10 = new ContractAssteDistributionRsp();
        top10.setStartIndex(1L);
        top10.setEndIndex(10L);
        top10.setTotalCount(0L);
        top10.setTotalAmount(BigDecimal.ZERO);
        ContractAssteDistributionRsp top50 = new ContractAssteDistributionRsp();
        top50.setStartIndex(11L);
        top50.setEndIndex(50L);
        top50.setTotalCount(0L);
        top50.setTotalAmount(BigDecimal.ZERO);
        ContractAssteDistributionRsp top100 = new ContractAssteDistributionRsp();
        top100.setStartIndex(51L);
        top100.setEndIndex(100L);
        top100.setTotalCount(0L);
        top100.setTotalAmount(BigDecimal.ZERO);
        ContractAssteDistributionRsp top500 = new ContractAssteDistributionRsp();
        top500.setStartIndex(101L);
        top500.setEndIndex(500L);
        top500.setTotalCount(0L);
        ContractAssteDistributionRsp top1000 = new ContractAssteDistributionRsp();
        top1000.setStartIndex(501L);
        top1000.setEndIndex(1000L);
        top1000.setTotalCount(0L);
        BigDecimal toatalAmountTop1000 = BigDecimal.ZERO;
        long addressesCountByContact = evMosAddressService.getAddressesCountByContact(contractAddress);
        BigDecimal totalAmount = evMosAddressService.getTotalAmountByContact(contractAddress);
        rsp.setCirculation(totalAmount);
        for (int i = 0; i < addressesByContact.size(); i++) {
            if (i < 100) {
                topList.add(EvmDao2Rsp.getEvmTokenR(addressesByContact.get(i)));
            }
            BigDecimal amount = new BigDecimal(String.valueOf(addressesByContact.get(i).getBalance())).setScale(contract.getDecimal(), RoundingMode.HALF_UP);
            if (i < 10) {
                top10.setTotalAmount(top10.getTotalAmount().add(amount));
                top10.setTotalCount(top10.getTotalCount() + 1);
            } else if (i < 50) {
                top50.setTotalAmount(top50.getTotalAmount().add(amount));
                top50.setTotalCount(top50.getTotalCount() + 1);
            } else if (i < 100) {
                top100.setTotalAmount(top100.getTotalAmount().add(amount));
                top100.setTotalCount(top100.getTotalCount() + 1);
            } else if (i < 500) {
                top500.setTotalAmount(top500.getTotalAmount().add(amount));
                top500.setTotalCount(top500.getTotalCount() + 1);
            } else if (i < 1000) {
                top1000.setTotalAmount(top1000.getTotalAmount().add(amount));
                top1000.setTotalCount(top1000.getTotalCount() + 1);
            }
            toatalAmountTop1000 = toatalAmountTop1000.setScale(contract.getDecimal(), RoundingMode.HALF_UP).add(amount);
        }
        assteDistribution.add(top10);
        assteDistribution.add(top50);
        assteDistribution.add(top100);
        assteDistribution.add(top500);
        assteDistribution.add(top1000);
        ContractAssteDistributionRsp others = new ContractAssteDistributionRsp();
        others.setTotalAmount(rsp.getCirculation().subtract(toatalAmountTop1000));
        others.setStartIndex(1001L);
        assteDistribution.add(others);

        top1000.setTotalCount(addressesCountByContact - addressesByContact.size());

        rsp.setTopList(topList);

        rsp.setHolderCount(addressesCountByContact);
        if (contract.getContractType() == ContractTypeEnum.erc721.getType()) {
            rsp.setHolderCount(evmNftService.getHolders(contract.getContractAddress()));
            rsp.setTotalSupply(new BigDecimal(evmNftService.getNftByContract(contractAddress, 1, 1).getTotal()));
        }

        rsp.setAssteDistribution(assteDistribution);
        return new ApiResult(200, "request success", rsp);
    }


    @ApiOperation(value = "查询交易详情", nickname = "查询交易详情")
    @GetMapping("getEvmTransactionByHash")
    public ApiResult<TransactionEvmRso> getTransactionByHash(String hash) throws IOException {
        Transaction transaction = evMosTransactionService.getTransaction(hash);
        if (transaction == null) {
            return new ApiResult(400, "Transaction does not exist");
        }
        TransactionReceipt transactionReceipt = evMosTransactionReceiptService.getTransactionReceipt(hash);
        EthBlock.Block block = evMosBlockService.getBlock(transaction.getBlockNumber());
        List<Log> logs = evMosLogsService.get20And721LogByHash(hash);
        EVMEvents events = null;
        EVMosContract to = evMosContractService.getContract(transaction.getTo());
        if (StringUtils.isNotBlank(transaction.getInput()) && to != null) {
            String input = transaction.getInput();
            String method = input.substring(0, 10);

            events = evMosEventsService.getEventsByCode(method, ContractTypeEnum.getContractType(to.getContractType()));
            if (events == null) {
                events = evMosEventsService.getEventsByCode(method);
            }
        }
        TransactionEvmRso transactionEvmRso = EvmDao2Rsp.getTransactionEvmRso(
                transaction, transactionReceipt,
                block.getTimestamp().toString(), configService.getEvmosDecimal(), false, events);
        List<ContractTransferRsp> list = new ArrayList<>();
        transactionEvmRso.setContractTransfers(list);

        for (Log log : logs) {

            EVMosContract contract = evMosContractService.getContract(log.getAddress());
            EVMEvents evmEvents = evMosEventsService.getEventsByCode(log.getTopics().get(0), ContractTypeEnum.getContractType(contract.getContractType()));
            String type = log.getTopics().get(0);
            if (evmEvents == null) {
                evmEvents = evMosEventsService.getEventsByCode(type);
            }
            if (evmEvents != null) {
                type = evmEvents.getName();
            }
            if (evmEvents == null || ContractTypeEnum.other.getName().equalsIgnoreCase(evmEvents.getContractType())) {
                continue;
            }
            ContractTransferRsp contractTransferRsp = EvmDao2Rsp.ReceiptLog2ContractTransfer(log, block.getTimestamp().toString(), type, contract, evmEvents);
            list.add(contractTransferRsp);
        }

        if (to != null) {
            transactionEvmRso.setContractType(ContractTypeEnum.getContractType(to.getContractType()));
        } else {
            transactionEvmRso.setContractType(ContractTypeEnum.other.getName());
        }

        return new ApiResult(200, "request success", transactionEvmRso);
    }

    @ApiOperation(value = "查询交易", nickname = "查询交易")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hash", value = "交易hash", required = false, dataType = "string"),
            @ApiImplicitParam(name = "address", value = "账户地址", required = false, dataType = "string"),
            @ApiImplicitParam(name = "isOutputOnly", value = "是否转入 0转出 1转入 其他全部", required = false, dataType = "int"),
            @ApiImplicitParam(name = "blockNumber", value = "区块编号", required = false, dataType = "string"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int"),

    })
    @PostMapping("queryEvmTransaction")
    public ApiResult<PageModel<TransactionEvmRso>> queryTransaction(String hash, String address, Integer isOutputOnly, String blockNumber, int pageIndex, int pageSize) {
        PageModel<Transaction> transactionPageModel = evMosTransactionService.queryTransaction(hash, address, isOutputOnly, blockNumber, pageIndex, pageSize);
        List<Transaction> records = transactionPageModel.getRecords();
        List<TransactionEvmRso> list = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            TransactionReceipt transactionReceipt = evMosTransactionReceiptService.getTransactionReceipt(records.get(i).getHash());
            EthBlock.Block block = evMosBlockService.getBlock(records.get(i).getBlockNumber());
            TransactionEvmRso transactionEvmRso = EvmDao2Rsp.getTransactionEvmRso(records.get(i),
                    transactionReceipt, block.getTimestamp().toString(), configService.getEvmosDecimal(), true, null);
            if (StringUtils.isNotBlank(records.get(i).getInput())
                    && records.get(i).getInput().length() > 10
                    &&( !records.get(i).getInput().startsWith("0x")
                    || !records.get(i).getInput().startsWith("0X"))) {
                EVMEvents eventsByCode = evMosEventsService.getEventsByCode(records.get(i).getInput().substring(0, 10));
                if(eventsByCode!=null){
                    transactionEvmRso.setMethod(eventsByCode.getName());
                }else {
                    if(StringUtils.isBlank(transactionReceipt.getTo())&&StringUtils.isNotBlank(transactionReceipt.getContractAddress())){
                        transactionEvmRso.setMethod("Deployment contract");
                    }else{
                        transactionEvmRso.setMethod("transfer");
                    }
                }
            }else{
                transactionEvmRso.setMethod("transfer");
            }
            list.add(transactionEvmRso);
        }
        PageModel<TransactionEvmRso> pageModel = new PageModel<TransactionEvmRso>(list, pageIndex, pageSize, transactionPageModel.getTotal());
        return new ApiResult<>(200, "request success", pageModel);
    }

    @ApiOperation(value = "查询合约交易", nickname = "查询合约交易")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hash", value = "交易hash", required = false, dataType = "string"),
            @ApiImplicitParam(name = "address", value = "账户地址", required = false, dataType = "string"),
            @ApiImplicitParam(name = "isOutputOnly", value = "是否转入 0转出 1转入 其他全部", required = false, dataType = "int"),
            @ApiImplicitParam(name = "transactionType", value = "交易类型，从全部交易事件中获取", required = false, dataType = "string"),
            @ApiImplicitParam(name = "blockNumber", value = "区块编号", required = false, dataType = "string"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int"),
            @ApiImplicitParam(name = "contractAddress", value = "合约地址", required = false, dataType = "string"),
            @ApiImplicitParam(name = "contractType", value = "合约类型 erc20, erc721", required = false, dataType = "string"),
            @ApiImplicitParam(name = "nftId", value = "nftid", required = false, dataType = "string"),

    })
    @PostMapping("queryEvmContractTransfer")
    public ApiResult<PageModel<ContractTransferRsp>> queryContractTransfer(
            String hash, String address, Integer isOutputOnly, String transactionType,
            String blockNumber, int pageIndex, int pageSize, String contractAddress,
            String contractType, String nftId) {
        PageModel<Log> logPageModel = evMosLogsService.queryErc20And721Logs(hash, address, isOutputOnly,
                transactionType, blockNumber, pageIndex, pageSize, contractAddress,
                contractType, nftId);
        List<ContractTransferRsp> list = new ArrayList<>();
        for (int i = 0; i < logPageModel.getRecords().size(); i++) {
            Log log = logPageModel.getRecords().get(i);
            EthBlock.Block block = evMosBlockService.getBlock(log.getBlockNumber());

            EVMosContract contract = evMosContractService.getContract(log.getAddress());
            EVMEvents evmEvents = evMosEventsService.getEventsByCode(log.getTopics().get(0), ContractTypeEnum.getContractType(contract.getContractType()));
            if (evmEvents == null) {
                evmEvents = evMosEventsService.getEventsByCode(log.getTopics().get(0));
            }
            String type = log.getTopics().get(0);
            if (evmEvents != null) {
                type = evmEvents.getName();
            }
            ContractTransferRsp contractTransferRsp = EvmDao2Rsp.ReceiptLog2ContractTransfer(log, block.getTimestamp().toString(), type, contract, evmEvents);
            list.add(contractTransferRsp);
        }
        return new ApiResult(200, "request success", new PageModel<>(list, pageIndex, pageSize, logPageModel.getTotal()));
    }

    @ApiOperation(value = "EVM和原生链地址转换", nickname = "EVM和原生链地址转换")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "账户地址", required = true, dataType = "string"),
    })
    @GetMapping("addressTransfer")
    public ApiResult addressTransfer(String address) throws Exception {
        if (address.startsWith("0x") || address.startsWith("0X")) {
            address = AddressUtil.convertEthAddressToCosmos(address, configService.getChainName());
        } else {
            address = AddressUtil.convertCosmosToEthAddress(address);
        }
        return new ApiResult(200, "request success", address);
    }

    @ApiOperation(value = "根据合约和账号查询NFT", nickname = "根据合约地址和账号地址查询合约资产")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "账户地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "contractAddress", value = "合约地址", required = false, dataType = "string"),
    })
    @PostMapping("getNftAssets")
    public ApiResult<List<EVMNftRsp>> getNftAssets(String address, String contractAddress) {
        List<EVMNft> nfts = evmNftService.getNftByOwnerAndContract(address, contractAddress);
        List<EVMNftRsp> list = new ArrayList<>();
        for (EVMNft nft : nfts) {
            list.add(TransferUtil.getEvmNftRsp(nft));
        }
        return new ApiResult(200, "request success", list);
    }

    @ApiOperation(value = "查询nft详情", nickname = "查询nft详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nftId", value = "nftId", required = true, dataType = "string"),
            @ApiImplicitParam(name = "contractAddress", value = "合约地址", required = true, dataType = "string"),
    })
    @GetMapping("getNftInfo")
    public ApiResult<EVMNftRsp> getNftInfo(String nftId, String contractAddress) {
        EVMNft nft = evmNftService.getNftByIdAndContract(nftId, contractAddress);
        if(nft==null){
            return new ApiResult<>(400,"token does not exist");
        }
        return new ApiResult(200, "request success", TransferUtil.getEvmNftRsp(nft));
    }

    @ApiOperation(value = "查询合约中的nft列表", nickname = "查询合约中的nft列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contractAddress", value = "合约地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "int"),
    })
    @PostMapping("getEvmNft")
    public ApiResult<PageModel<EVMNftRsp>> getEvmNftByContractAddress(String contractAddress, int pageIndex, int pageSize) {
        PageModel<EVMNft> nfts = evmNftService.getNftByContract(contractAddress, pageIndex, pageSize);
        List<EVMNft> records = nfts.getRecords();
        List<EVMNftRsp> list = new ArrayList<>();
        for (EVMNft nft : records) {
            list.add(TransferUtil.getEvmNftRsp(nft));
        }
        return new ApiResult<>(200, "request success", new PageModel<>(list, pageIndex, pageSize, nfts.getTotal()));
    }

    @GetMapping("getEvmContractStatistics")
    @ApiOperation(value = "统计合约每天交易数据", nickname = "统计合约每天交易数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contractAddress", value = "合约地址", required = true, dataType = "string"),
    })
    public ApiResult<List<EvmContranctStatsRsp>> getEvmNftByContractAddress(String contractAddress, int count) {
        List<EVMosContranctStats> byContractAddress = evMosContranctStatsService.getByContractAddress(count, contractAddress);
        List<EvmContranctStatsRsp> list = new ArrayList<>();
        for (EVMosContranctStats stats : byContractAddress) {
            EvmContranctStatsRsp res = new EvmContranctStatsRsp();
            BeanUtils.copyProperties(stats, res);
            String amount = new BigDecimal(stats.getAmountTotal()).toPlainString();
            System.out.println(new BigDecimal(stats.getAmountTotal()).toPlainString());
            list.add(res);
        }
        return new ApiResult<>(200, "request success", list);
    }
    @PostMapping("uploadContractAbi")
    @ApiOperation(value = "上传合约ABI", nickname = "上传合约ABI")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contractAddress", value = "合约地址", required = true, dataType = "string"),
            @ApiImplicitParam(name = "abi", value = "abiJSON", required = true, dataType = "string"),
    })
    public ApiResult uploadContractAbi(String contractAddress,String abi){
        EVMosContract contract = evMosContractService.getContract(contractAddress);
        if(contract==null){
            return  new ApiResult<>(400,  "contract does not exist", "");
        }
        if(StringUtils.isNotBlank(contract.getAbi())){
            return  new ApiResult<>(400,  "ABI has been uploaded", "");
        }

        AbiUtil.analysisAbi(abi,evMosEventsService, ContractTypeEnum.getContractType(contract.getContractType()));
        contract.setAbi(abi);
        evMosContractService.updateAbi(abi,contract.getIds());
        return  new ApiResult<>(200, "request success", "");
    }
}
