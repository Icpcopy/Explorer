package com.wuyuan.database.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "NFT商品")
public class NFT {

    @ApiModelProperty(value="用户id",name="_id")
    private String _id;

    @ApiModelProperty(value="NFT的唯一标识（名字）",name="tokenId")
    private String tokenId;

    @ApiModelProperty(value="NFT拥有者",name="owner")
    private String owner;

    @ApiModelProperty(value="NFT创建者",name="creator")
    private String creator;

    @ApiModelProperty(value="tokenUri（未解析）",name="tokenUri")
    private String tokenUri;

    @ApiModelProperty(value="companyUri（未解析）",name="companyUri")
    private String companyUri;

    @ApiModelProperty(value="NFT的区分标识",name="cateId")
    private String cateId;

    @ApiModelProperty(value="nft的状态",name="tokenStatus")
    private int tokenStatus;

    @ApiModelProperty(value="是否是平台",name="isPlatform")
    private boolean isPlatform;

    @ApiModelProperty(value="增值税",name="valueAddedTax")
    private String valueAddedTax;

    @ApiModelProperty(value="版权税",name="copyrightTax")
    private String copyrightTax;

    @ApiModelProperty(value="NFT当前价值",name="nowValue")
    private BigDecimal nowValue;

    @ApiModelProperty(value="当前价值的单位",name="valueSymbol")
    private String valueSymbol;

    @ApiModelProperty(value="创建NFT的hash",name="mintHash")
    private String mintHash;

    @ApiModelProperty(value="该NFT最近交易的时间",name="timestamp")
    private long timestamp;

    @ApiModelProperty(value="名称",name="名称")
    private String name;
}
