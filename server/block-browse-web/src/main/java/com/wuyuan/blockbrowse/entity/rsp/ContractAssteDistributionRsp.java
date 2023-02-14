package com.wuyuan.blockbrowse.entity.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("合约资产分布")
public class ContractAssteDistributionRsp {
    @ApiModelProperty( "排序开始值")
    private long startIndex;
    @ApiModelProperty( "排序结束值")
    private long endIndex;
    @ApiModelProperty( "区间内总量金额")
    private BigDecimal totalAmount=BigDecimal.ZERO;
    @ApiModelProperty( "区间地址总数")
    private long totalCount;
}
