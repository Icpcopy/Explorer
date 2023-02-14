package com.wuyuan.blockbrowse.controller;

import com.alibaba.fastjson.JSONObject;
import com.wuyuan.blockbrowse.utils.ApiResult;
import com.wuyuan.database.entity.NFT;
import com.wuyuan.database.entity.NFTDealAmount;
import com.wuyuan.database.entity.NFTStats;
import com.wuyuan.database.sevice.NFTDealAmountService;
import com.wuyuan.database.sevice.NFTService;
import com.wuyuan.database.sevice.NFTStatsService;
import com.wuyuan.database.sevice.TransactionService;
import com.wuyuan.database.util.PageModel;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "nft模块相关")
@RestController
@RequestMapping("nft")
public class NFTController {

    @Resource
    private NFTService nftService;

    @Resource
    private TransactionService transactionService;

    @Resource
    private NFTDealAmountService nftDealAmountService;

    @Resource
    private NFTStatsService nftStatsService;

    @ApiOperation(value = "获取nft列表(可以根据地址获取)", nickname = "查询NFT")
    @ApiResponses({
            @ApiResponse(code = 200, message = "" ,response = NFT.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条数据", required = true, dataType = "int"),
            @ApiImplicitParam(name = "address", value = "钱包地址", required = false, dataType = "String"),
    })
    @GetMapping("getNFTList")
    public ApiResult<PageModel<NFT>> getNFTList(@RequestParam(defaultValue = "1") Integer pageIndex,@RequestParam(defaultValue = "20") Integer pageSize,String address) {
        return new ApiResult(200,"查询成功",nftService.getNFTList(pageSize,pageIndex,address));
    }

    @ApiOperation(value = "通过tokenId获取NFT详情", nickname = "查询NFT")
    @ApiResponses({
            @ApiResponse(code = 200, message = "" ,response = NFT.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tokenId", value = "NFT的tokenId", required = true, dataType = "String"),
    })
    @GetMapping("getNFT")
    public ApiResult<NFT> getNFT(String tokenId) {
        return new ApiResult(200,"查询成功",nftService.getNFT(tokenId));
    }

    @ApiOperation(value = "获取一个nft的相关交易", nickname = "查询NFT的交易")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tokenId", value = "NFT的tokenId", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pageIndex", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条数据", required = true, dataType = "int"),
    })
    @GetMapping("getNFTTxByTokenId")
    public ApiResult getNFTTxByTokenId(String tokenId,@RequestParam(defaultValue = "1") Integer pageIndex,@RequestParam(defaultValue = "20") Integer pageSize) {
        return new ApiResult(200,"查询成功",transactionService.getNFTTxByTokenId(tokenId,pageIndex,pageSize));
    }

//    @ApiOperation(value = "获取一个address的nft相关交易", nickname = "查询NFT的交易")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "address", value = "钱包地址", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "pageIndex", value = "页数", required = true, dataType = "int"),
//            @ApiImplicitParam(name = "pageSize", value = "每页几条数据", required = true, dataType = "int"),
//    })
//    @GetMapping("getNFTTxByAddress")
//    public ApiResult getNFTTxByAddress(String address,@RequestParam(defaultValue = "1") Integer pageIndex,@RequestParam(defaultValue = "20")Integer pageSize) {
//        return new ApiResult(200,"查询成功",transactionService.getTransaction(null,"nft",null,address,null,null,null,pageIndex,pageSize,null,null,null));
//    }

    @ApiOperation(value = "获取nft价值排行", nickname = "排行")
    @ApiResponses({
            @ApiResponse(code = 200, message = "" ,response = NFTStats.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条数据", required = true, dataType = "int"),
            @ApiImplicitParam(name = "cateId", value = "(001 票务,002 收藏品,003 艺术品,004 轻奢品),查询类型", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isNumSort", value = "是否数量排序(传1为数量排序，不传或传0为价值排序)", required = false, dataType = "int"),
            @ApiImplicitParam(name = "isSort", value = "是否倒序(传1从小到大，不传或传0从大到小)", required = false, dataType = "int"),
    })
    @GetMapping("getNFTStatsList")
    public ApiResult getNFTStatsList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "20")Integer pageSize, String cateId,Integer isNumSort, Integer isSort) {
        return new ApiResult(200,"查询成功",nftStatsService.getNFTStatsList(pageIndex,pageSize,cateId,isNumSort,isSort));
    }

    @ApiOperation(value = "获取图表数据（成交额）", nickname = "查询成交额")
    @ApiResponses({
            @ApiResponse(code = 200, message = "" ,response = NFTDealAmount.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginDate", value = "开始时间(2020-10-20)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间(2020-10-20),查一天的时候这个不用传", required = false, dataType = "String"),
    })
    @GetMapping("getDealList")
    public ApiResult<List<NFTDealAmount>>getDealList(String beginDate, String endDate) {
        return new ApiResult(200,"查询成功",nftDealAmountService.getDealList(beginDate,endDate));
    }

//    @ApiOperation(value = "xxx", nickname = "查询NFT")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "" ,response = NFT.class)
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "address", value = "钱包地址", required = true, dataType = "String"),
//    })
//    @GetMapping("getNftStats")
//    public void getNftStats(String address) {
//       nftService.getNftStats(address);
//    }
}
