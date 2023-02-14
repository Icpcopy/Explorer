package com.wuyuan.blockbrowse.entity.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("地址token合约资产")
@Data
public class EvmAddressTokenAssetsRsp {
    @ApiModelProperty( "token余额")
    private BigDecimal balance;
    @ApiModelProperty( "用户地址")
    private String address;
    @ApiModelProperty( "合约标识")
    private String symbol;
    @ApiModelProperty( "合约地址")
    private String contactAddress;
    @ApiModelProperty( "合约精度")
    private int decimal;
}
