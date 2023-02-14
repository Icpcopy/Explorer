package com.wuyuan.blockbrowse.entity.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("币种资产持有者信息")
public class CoinInfoStatisticsRes {

    @ApiModelProperty("提取佣金总量")
    private String withdrawCommission;
    @ApiModelProperty("链名称")
    private String chainName;
    @ApiModelProperty("锁定总量")
    private String lockedTotal;
    //"withdrawCommission": "103312.6249981000848931",
//    "chainName": "icplaza",
//    "LockedTotal": "0",
//    "withdrawReward": "2733.659136670659272128",
    @ApiModelProperty("提前质押奖励")
    private String withdrawReward;
    //    "CommunityPool": "7824.648594",
    @ApiModelProperty("社区池")
    private String communityPool;
    //    "TotalSupply": "98724816.241472636879689365",
    @ApiModelProperty("总发行量")
    private String totalSupply;
    //    "Bonded": "23500",
    @ApiModelProperty("质押总量")
    private String bonded;
    //    "BondedLocked": "0",
//    "Bonded": "23500",
    @ApiModelProperty("质押锁定总量")
    private String bondedLocked;
    //    "totalUnDelegator": "0",
    @ApiModelProperty("赎回中总量")
    private String totalUnDelegator;
    //    "PosTotal": "113870.932728770744165228",
    @ApiModelProperty("POS总量")
    private String posTotal;
    //    "BondedNotLocked": "23500",
    @ApiModelProperty("质押可赎回总量")
    private String bondedNotLocked;
    //    "coinName": "ict",
    @ApiModelProperty("币种名称")
    private String coinName;
    //    "EcologicalProPool": "0",
    @ApiModelProperty("生态矿池")
    private String ecologicalProPool;
    @ApiModelProperty("锁定24期")
    private String locked24;
    @ApiModelProperty("锁定36期")
    private String locked36;
    @ApiModelProperty("流通量")
    private String circulation;
    private String withdrawalCommunityPool;

}
