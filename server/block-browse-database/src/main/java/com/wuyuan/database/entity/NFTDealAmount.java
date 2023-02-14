package com.wuyuan.database.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "NFT成交额")
public class NFTDealAmount {
    @ApiModelProperty(value="用户id",name="_id")
    private String _id;

    @ApiModelProperty(value="成交额",name="dealAmount")
    private BigDecimal dealAmount;

    @ApiModelProperty(value="日期",name="date")
    private String date;

    @ApiModelProperty(value="时间戳",name="timestamp")
    private Long timestamp;
}
