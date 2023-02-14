package com.wuyuan.blockbrowse.entity.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("合约资产持有者信息")
public class ContractAssetsHolderRsp {
    @ApiModelProperty( "合约地址")
    private String contractAddress;
    @ApiModelProperty( "发行总量")
    private BigDecimal totalSupply;
    @ApiModelProperty( "流通量")
    private BigDecimal circulation;
    @ApiModelProperty( "持币地址数")
    private long holderCount;
    @ApiModelProperty( "资产分布")
    private List<ContractAssteDistributionRsp> assteDistribution;
    @ApiModelProperty( "前100排名")
    private List<EvmAddressTokenAssetsRsp> topList;
}
