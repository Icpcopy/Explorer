import request from "@/utils/request";
import config from "./config";
const server = config.server;

//根据交易总数排序查询erc20 token合约  token列表
const getTokensList = (params) => {
  return request({
    url: `${server}/evm/getTokenContractsList`,
    method: "get",
    params: params,
  });
};

//根据交易总数排序查询erc721 token合约
const getTokenContractsList = (params) => {
  return request({
    url: `${server}/evm/getTokenContractsListByContractType`,
    method: "get",
    params: params,
  });
};

//查询token合约详情
const getTokensDetail = (params) => {
  return request({
    url: `${server}/evm/getTokenContractInfo`,
    method: "get",
    params: params,
  });
};

//查询721合约资产列表
const getEvmNftAssetsList = (params) => {
  return request({
    url: `${server}/evm/getNftAssets`,
    method: "post",
    params: params,
  });
};

//查询合约交易
const getEvmTransferList = (params) => {
  return request({
    url: `${server}/evm/queryEvmContractTransfer`,
    method: "post",
    params: params,
  });
};

//查询交易
const getEvmTransactionList = (params) => {
  return request({
    url: `${server}/evm/queryEvmTransaction`,
    method: "post",
    params: params,
  });
};

//查询交易详情
const getEvmTransactionByHash = (params) => {
  return request({
    url: `${server}/evm/getEvmTransactionByHash`,
    method: "get",
    params: params,
  });
};

//查询地址信息
const getAddressInfo = (params) => {
  return request({
    url: `${server}/evm/getAddressInfo`,
    method: "get",
    params: params,
  });
};

//合约资产持有者统计
const getAssteDistribution = (params) => {
  return request({
    url: `${server}/evm/contractAssetsDistribution`,
    method: "get",
    params: params,
  });
};

//EVM和原生链地址转换
const getAddressTransfer = (params) => {
  return request({
    url: `${server}/evm/addressTransfer`,
    method: "get",
    params: params,
  });
};

//查询所有token列表
const getTokenList = (params) => {
  return request({
    url: "/icp-app-api/popularToken/tokenList", //"token" +
    method: "get",
    params: params,
  });
};

//查询交易详情
const getTransactionDetail = (params) => {
  return request({
    url: `${server}/evm/getEvmTransactionByHash`,
    method: "get",
    params: params,
  });
};

//查询NFT详情
const getNftInfo = (params) => {
  return request({
    url: `${server}/evm/getNftInfo`,
    method: "get",
    params: params,
  });
};

//查询合约中的nft列表
const getEvmNftList = (params) => {
  return request({
    url: `${server}/evm/getEvmNft`,
    method: "post",
    params: params,
  });
};

const NftApi = {
  getTokensList,
  getTokensDetail,
  getEvmTransferList,
  getEvmTransactionList,
  getEvmTransactionByHash,
  getAddressInfo,
  getAssteDistribution,
  getAddressTransfer,
  getTokenList,
  getTransactionDetail,
  getTokenContractsList,
  getNftInfo,
  getEvmNftList,
  getEvmNftAssetsList,
};

export default NftApi;
