import request from "@/utils/request";
import config from "./config";
const server = config.server;

//获取图表数据（成交额）
const getDealList = (params) => {
  return request({
    url: `${server}/nft/getDealList`,
    method: "get",
    params: params,
  });
};

//通过tokenId获取NFT详情
const getNftDetail = (params) => {
  return request({
    url: `${server}/nft/getNFT`,
    method: "get",
    params: params,
  });
};

//获取NFt列表
const getNftList = (params) => {
  return request({
    url: `${server}/nft/getNFTList`,
    method: "get",
    params: params,
  });
};

//获取nft价值排行
const getNFTStatsList = (params) => {
  return request({
    url: `${server}/nft/getNFTStatsList`,
    method: "get",
    params: params,
  });
};

//获取一个address的nft相关交易
const getNFTTxByAddress = (params) => {
  return request({
    url: `${server}/nft/getNFTTxByAddress`,
    method: "get",
    params: params,
  });
};

//获取一个nft的相关交易
const getNFTTxByTokenId = (params) => {
  return request({
    url: `${server}/nft/getNFTTxByTokenId`,
    method: "get",
    params: params,
  });
};
const NftApi = {
  getDealList,
  getNftDetail,
  getNftList,
  getNFTStatsList,
  getNFTTxByAddress,
  getNFTTxByTokenId,
};

export default NftApi;
