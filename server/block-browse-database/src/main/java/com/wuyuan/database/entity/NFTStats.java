package com.wuyuan.database.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "每个地址拥有的NFT价值")
public class NFTStats {
    @ApiModelProperty(value="用户id",name="_id")
    private String _id;

    @ApiModelProperty(value="钱包地址",name="address")
    private String address;

    @ApiModelProperty(value="所拥有的票务类型NFT的价值",name="TicketingQuantity")
    private Double TicketingQuantity;

    @ApiModelProperty(value="所拥有的轻奢品类型NFT的价值",name="LigthLuxuryQuantity")
    private Double LigthLuxuryQuantity;

    @ApiModelProperty(value="所拥有的艺术品类型NFT的价值",name="ArtQuantity")
    private Double ArtQuantity;

    @ApiModelProperty(value="所拥有的收藏品类型NFT的价值",name="CollectionQuantity")
    private Double CollectionQuantity;

    @ApiModelProperty(value="所有类型NFT的价值",name="AllQuantity")
    private Double AllQuantity;

    @ApiModelProperty(value="所拥有的票务类型NFT的数量",name="TicketingQuantity")
    private Long TicketingQuantityNum;

    @ApiModelProperty(value="所拥有的轻奢品类型NFT的数量",name="LigthLuxuryQuantity")
    private Long LigthLuxuryQuantityNum;

    @ApiModelProperty(value="所拥有的艺术品类型NFT的数量",name="ArtQuantity")
    private Long ArtQuantityNum;

    @ApiModelProperty(value="所拥有的收藏品类型NFT的数量",name="CollectionQuantity")
    private Long CollectionQuantityNum;

    @ApiModelProperty(value="所有类型NFT的数量",name="AllQuantity")
    private Long AllQuantityNum;

}
