import request from "@/utils/request";
export function getBlockByBlockNumber(data) {
  console.log(data);

  return request({
    url: "/api/block/getBlockByBlockNumber",
    method: "get",
    params: data,
  });
}
export function getTransactionByHash(data) {
  return request({
    url: "/api/transaction/getTransactionByHash",
    method: "get",
    params: data,
  });
}
export function getAddressInfo(data) {
  return request({
    url: "/api/address/getAddressInfo",
    method: "get",
    params: data,
  });
}
export function getValidatorsByMoniker(data) {
  return request({
    url: "/api/validator/getValidatorsByMoniker",
    method: "get",
    params: data,
  });
}
