package com.wuyuan.blockbrowse.entity.rsp;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel("地址资产详情")
@Data
public class EvmAddressRsp {
    @ApiModelProperty( "地址")
    private String address;
    @ApiModelProperty( "原生资产余额")
    private BigDecimal balance;
    @ApiModelProperty( "token合约资产列表")
    private List<EvmAddressTokenAssetsRsp> tokens;
    @ApiModelProperty( "是否是合约地址")
    private boolean isContract;


}
