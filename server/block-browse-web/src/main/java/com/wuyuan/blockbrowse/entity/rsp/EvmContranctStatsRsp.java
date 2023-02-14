package com.wuyuan.blockbrowse.entity.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("token合约交易")
public class EvmContranctStatsRsp {
    @ApiModelProperty( "日期")
    private String day;
    @ApiModelProperty( "交易条数")
    private long transactionCount;
    @ApiModelProperty( "合约地址")
    private String contactAddress;
}
