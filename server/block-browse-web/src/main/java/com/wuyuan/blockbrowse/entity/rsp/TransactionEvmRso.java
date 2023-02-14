package com.wuyuan.blockbrowse.entity.rsp;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.util.List;

@ApiModel("EVM交易")
public class TransactionEvmRso {
    @ApiModelProperty( "交易hash")
    private String txHash;
    @ApiModelProperty( "区块编号")
    private Long blockNumber;

    @ApiModelProperty("转出地址")
    private String from;

    @ApiModelProperty("转入地址")
    private String to;

    @ApiModelProperty("交易金额")
    private BigDecimal amount;

    @ApiModelProperty("交易索引")
    private Integer txIndex;

    @ApiModelProperty("交易状态")
    private Boolean isSuccess;

    @ApiModelProperty("使用的手续费")
    private String gasUsed;

    @ApiModelProperty("交易信息")
    private Transaction transaction;
    @ApiModelProperty("交易收据")
    private TransactionReceipt receipt;
    @ApiModelProperty("该hash下的合约交易")
    private List<ContractTransferRsp> contractTransfers;
    @ApiModelProperty("contractType")
    private String contractType;
    private String method;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @ApiModelProperty("input解析")
    private JSONObject inputData;

    public JSONObject getInputData() {
        return inputData;
    }

    public void setInputData(JSONObject inputData) {
        this.inputData = inputData;
    }

    public List<ContractTransferRsp> getContractTransfers() {
        return contractTransfers;
    }

    public void setContractTransfers(List<ContractTransferRsp> contractTransfers) {
        this.contractTransfers = contractTransfers;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionReceipt getReceipt() {
        return receipt;
    }

    public void setReceipt(TransactionReceipt receipt) {
        this.receipt = receipt;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }

    @ApiModelProperty("时间")
    private String blockTime;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTxIndex() {
        return txIndex;
    }

    public void setTxIndex(Integer txIndex) {
        this.txIndex = txIndex;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
}
