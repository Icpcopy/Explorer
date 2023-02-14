import request from "@/utils/request";
import config from "./config";
const server = config.server;

export function getInfo() {
  return request({
    url: "/api/resource/pack/list",
    method: "get",
  });
}

//无需token查询参数
export function listparams(params) {
  return request({
    url: `${server}/chain/getConfigKey`,
    method: "get",
    params,
  });
}
