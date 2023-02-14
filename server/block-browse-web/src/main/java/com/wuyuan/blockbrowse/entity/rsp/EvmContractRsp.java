package com.wuyuan.blockbrowse.entity.rsp;

import com.wuyuan.database.util.BigDecimalUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("合约")
@Data
public class EvmContractRsp {
    @ApiModelProperty( "合约发行人")
    private String owner;
    @ApiModelProperty( "合约发标识")
    private String tokenSymbol;
    @ApiModelProperty( "合约名称")
    private String tokenName;
    @ApiModelProperty( "创建hash")
    private String hash;
    @ApiModelProperty( "合约地址")
    private String contractAddress;
    @ApiModelProperty( "总发行量")
    private String totalSupply;
    @ApiModelProperty( "精度")
    private int decimal;
    @ApiModelProperty( "交易总数")
    private long transactiontotalCount;
    @ApiModelProperty( "地址总数")
    private long addresstotal;
    @ApiModelProperty( "当天交易额")
    private BigDecimal todayTotalTransactionAmount;
    @ApiModelProperty( "流通量")
    private BigDecimal circulation;
    @ApiModelProperty("发行时间")
    private long issuerTimestamp;
    @ApiModelProperty("合约类型")
    private String contractType;
    @ApiModelProperty("合约abi")
    private String abi;


}
