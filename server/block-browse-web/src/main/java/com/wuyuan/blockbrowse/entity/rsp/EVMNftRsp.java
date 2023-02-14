package com.wuyuan.blockbrowse.entity.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
/**
 * @author admin
 */
@ApiModel("token合约交易")
public class EVMNftRsp {
    @ApiModelProperty( "合约地址")
    private String contentAddress;
    @ApiModelProperty( "所有人")
    private String owner;
    @ApiModelProperty( "nftid")
    private String nftId;
    @ApiModelProperty( "铸造时间")
    private String mintTime;
    @ApiModelProperty( "发行人")
    private String issuer;
    @ApiModelProperty( "交易数量")
    private long transactionCount;

    public String getContentAddress() {
        return contentAddress;
    }

    public void setContentAddress(String contentAddress) {
        this.contentAddress = contentAddress;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNftId() {
        return nftId;
    }

    public void setNftId(String nftId) {
        this.nftId = nftId;
    }

    public String getMintTime() {
        return mintTime;
    }

    public void setMintTime(String mintTime) {
        this.mintTime = mintTime;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(long transactionCount) {
        this.transactionCount = transactionCount;
    }
}
