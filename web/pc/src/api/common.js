import request from "@/utils/request";
// import config from "./config";
const server = "/api"; // config.server;

// 查询字典数据列表
const listData = (query) => {
  return request({
    url: `${server}/system/dict/data/list`,
    method: "get",
    params: query,
  });
};

export default {
  listData,
};
