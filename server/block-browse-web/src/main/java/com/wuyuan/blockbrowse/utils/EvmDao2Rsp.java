package com.wuyuan.blockbrowse.utils;

import com.wuyuan.blockbrowse.entity.rsp.*;
import com.wuyuan.database.entity.EVMEvents;
import com.wuyuan.database.entity.EVMosContract;
import com.wuyuan.database.entity.EvmAddress;
import com.wuyuan.database.util.AbiUtil;
import com.wuyuan.database.util.BigDecimalUtil;
import com.wuyuan.database.util.ContractTypeEnum;
import com.wuyuan.database.util.Erc20AddressUtil;
import io.cosmos.util.AmountUtil;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;

public class EvmDao2Rsp {
    public static EvmContractRsp getEvmContractRsp(EVMosContract contract, BigDecimal totalAmount,BigDecimal circulation){
        EvmContractRsp rsp=new EvmContractRsp();
        rsp.setContractAddress(contract.getContractAddress());
        rsp.setAddresstotal(contract.getAddresstotal());
        rsp.setDecimal(contract.getDecimal());
        rsp.setHash(contract.getHash());
        rsp.setOwner(contract.getOwner());
        rsp.setTodayTotalTransactionAmount(totalAmount);

        if(StringUtils.isBlank(contract.getTokenName())||"not_erc20".equalsIgnoreCase(contract.getTokenName())){
            rsp.setTokenName(contract.getContractAddress());
        }else{
            rsp.setTokenName(contract.getTokenName());
        }
        if(StringUtils.isBlank(contract.getTokenSymbol())||"not_erc20".equalsIgnoreCase(contract.getTokenSymbol())){
            rsp.setTokenSymbol("");
        }else{
            rsp.setTokenSymbol(contract.getTokenSymbol());
        }

        if(new BigDecimal(contract.getTotalSupply()).compareTo(BigDecimal.ZERO)==-1){
            rsp.setTotalSupply("0");
        }else{
            rsp.setTotalSupply(contract.getTotalSupply());
        }

        rsp.setTransactiontotalCount(contract.getTransactiontotalCount());
        rsp.setCirculation(circulation);
        rsp.setIssuerTimestamp(contract.getTime().longValue());
        rsp.setContractType(ContractTypeEnum.getContractType(contract.getContractType()));
        rsp.setAbi(contract.getAbi());
        return rsp;
    }
    public static EvmAddressTokenAssetsRsp getEvmTokenR(EvmAddress address){
        EvmAddressTokenAssetsRsp rsp=new EvmAddressTokenAssetsRsp();
        rsp.setAddress(address.getAddress());
        rsp.setContactAddress(address.getContactAddress());
        rsp.setBalance(new BigDecimal(address.getBalance()));
        rsp.setSymbol(address.getSymbol());
        rsp.setDecimal(address.getDecimal());
        return rsp;
    }
    public static ContractTransferRsp ReceiptLog2ContractTransfer(Log log, String timestamp, String type, EVMosContract contract,EVMEvents evmEvents) {
        if (contract == null) {
            contract = new EVMosContract();
            contract.setDecimal(1);
            contract.setContractAddress("");
            contract.setTokenName("");
        }
        ContractTransferRsp transfer = new ContractTransferRsp();
        transfer.setBlockNumber(log.getBlockNumber().longValue());
        String[] datas = decodeData(log.getData());
        BigDecimal amoutByDecimal = BigDecimal.ZERO;
        if (datas.length > 0) {
            amoutByDecimal = AmountUtil.getAmoutByDecimal(
                    new BigDecimal(Numeric.decodeQuantity(datas[0])), contract.getDecimal()
            );
        }
        transfer.setContractAddress(contract.getContractAddress());
        transfer.setAmount(amoutByDecimal.toPlainString());
        if(log.getTopics().size() > 1){
            if(evmEvents!=null){
                String inputType=AbiUtil.getInputType(evmEvents.getAbi(),0);
                if("address".equalsIgnoreCase(inputType)){
                    transfer.setFrom(Erc20AddressUtil.topic2Address(log.getTopics().get(1)));
                }
            }else {
                transfer.setFrom(Erc20AddressUtil.topic2Address(log.getTopics().get(1)));
            }

        }
        if (log.getTopics().size() > 2) {
            if(evmEvents!=null){
                String inputType=AbiUtil.getInputType(evmEvents.getAbi(),1);
                if("address".equalsIgnoreCase(inputType)){
                    transfer.setTo(Erc20AddressUtil.topic2Address(log.getTopics().get(2)));
                }
            }else {
                transfer.setTo(Erc20AddressUtil.topic2Address(log.getTopics().get(2)));
            }

        }
        if(contract.getContractType()== ContractTypeEnum.erc721.getType()){
            if(evmEvents==null){
                transfer.setNftId("");
            }else if(log.getTopics().size()>3){
                transfer.setNftId(Numeric.toBigInt(log.getTopics().get(3)).toString());
            }else if(log.getData().length()>0&&datas.length>0&&datas[0].length()>2){
                transfer.setNftId(Numeric.toBigInt(datas[0].substring(2)).toString());
            }else{
                transfer.setNftId("0");
            }

        }
        transfer.setEvent(type);
        if(contract.getTokenSymbol()==null||"not_erc20".equalsIgnoreCase(contract.getTokenSymbol())){
            transfer.setSymbol("");
        }else {
            transfer.setSymbol(contract.getTokenSymbol());
        }

        transfer.setHash(log.getTransactionHash());
        transfer.setTransferTime(timestamp);
        transfer.setTokenName(contract.getTokenName());
        return transfer;
    }

    public static TransactionEvmRso getTransactionEvmRso(Transaction transaction, TransactionReceipt receipt, String timestamp, int decim, boolean isList, EVMEvents events) {
        TransactionEvmRso rso = new TransactionEvmRso();
        rso.setAmount(BigDecimalUtil.computeDecimal(new BigDecimal(transaction.getValue().toString()),decim));
        rso.setBlockNumber(transaction.getBlockNumber().longValue());
        rso.setFrom(transaction.getFrom());
        rso.setBlockTime(timestamp);
        rso.setSuccess(receipt.isStatusOK());

        rso.setTo(transaction.getTo());
        if(StringUtils.isBlank(transaction.getTo())){
            rso.setTo(receipt.getContractAddress());
        }
        rso.setGasUsed(BigDecimalUtil.computeDecimal(new BigDecimal(transaction.getGas().multiply(transaction.getGasPrice()).toString()), decim).toPlainString());
        rso.setTxHash(transaction.getHash());
        rso.setTxIndex(transaction.getTransactionIndex().intValue());

//        System.out.println(JSONObject.toJSONString(transaction));
        if(!isList){
            if(transaction.getMaxFeePerGasRaw()==null){
                transaction.setMaxFeePerGas("0x0");
            }
            if(transaction.getMaxPriorityFeePerGasRaw()==null){
                transaction.setMaxPriorityFeePerGas("0x0");
            }
            rso.setTransaction(transaction);
            rso.setReceipt(receipt);
        }

        if(events!=null){
            rso.setInputData(AbiMethodUtil.inputDecode(transaction.getInput(),events.getAbi(),events.getName(),events.getCode()));
        }
        return rso;

    }
    public static String[] decodeData(String data) {
        if (data.startsWith("0x")) {
            data = data.substring(2);
        }
        String[] datas = new String[data.length() / 64];
        for (int i = 0; i < datas.length; i++) {
//            if(i< datas.length-1){
            datas[i] = "0x"+data.substring(64 * i, 64 * (i + 1));
//            }else {
//                datas[i]=data.substring(64*i);
//            }

        }
        return datas;
    }

}
