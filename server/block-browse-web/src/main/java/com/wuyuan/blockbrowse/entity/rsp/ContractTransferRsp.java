package com.wuyuan.blockbrowse.entity.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("token合约交易")
public class ContractTransferRsp {
    @ApiModelProperty( "转出地址")
    private String from;
    @ApiModelProperty( "转入地址")
    private String to;
    @ApiModelProperty( "金额")
    private String amount;
    @ApiModelProperty( "合约标识")
    private String symbol;
    @ApiModelProperty( "合约地址")
    private String contractAddress;
    @ApiModelProperty( "交易事件")
    private String event;
    @ApiModelProperty( "交易时间")
    private String transferTime;
    @ApiModelProperty( "交易hash")
    private String hash;
    @ApiModelProperty( "区块编号")
    private long blockNumber;
    @ApiModelProperty("交易类型")
    private String function;
    @ApiModelProperty("nftId")
    private String nftId;
    @ApiModelProperty("合约名称")
    private String tokenName;

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getNftId() {
        return nftId;
    }

    public void setNftId(String nftId) {
        this.nftId = nftId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }


    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }
}
