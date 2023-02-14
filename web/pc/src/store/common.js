import { NftCommon } from "@/api";
import { listparams } from "@/api/home";
// import address from "@/utils/address.js";
const common = {
  namespaced: true,
  state: {
    productTypeList: [],
    auditStatusList: [],
    castStatusList: [],
    saleTypeList: [],
    deliveryStatusList: [],
    transactionList: [],
    // addressList: address,
    merchantsValues: null,
    nftAddress: null,
    ipfsAddress: null,
    socketAddress: null,
  },
  mutations: {
    SET_PRODUCT_TYPE_EUNM: (state, data) => {
      state.productTypeList = data;
    },
    SET_AUDIT_STATUS_EUNM: (state, data) => {
      state.auditStatusList = data;
    },
    SET_CAST_STATUS_EUNM: (state, data) => {
      state.castStatusList = data;
    },
    SET_SALE_TYPE_EUNM: (state, data) => {
      state.saleTypeList = data;
    },
    SET_DELIVERY_STATUS_EUNM: (state, data) => {
      state.deliveryStatusList = data;
    },
    SET_TRANSACTION_STATUS_EUNM: (state, data) => {
      state.transactionList = data;
    },
    SET_MERCHANTS_VALUES: (state, data) => {
      state.merchantsValues = data;
    },
    SET_IPFS_ADDRESS: (state, data) => {
      state.ipfsAddress = data.configValue;
    },
    SET_BLOCKCHAIN_BROSER_ADDRESS: (state, data) => {
      state.nftAddress = data.configValue;
    },
    SET_BLOCKCHAIN_BROSER_WEBSOCKET_ADDRESS: (state, data) => {
      state.socketAddress = data.configValue;
    },
  },
  actions: {
    // 字典
    getDictList({ commit }, params) {
      return new Promise((resolve, reject) => {
        NftCommon.listData(params)
          .then((res) => {
            //过滤禁用的字典
            let rows = res.rows.filter((el) => el.status !== "1");
            switch (params.dictType) {
              case "nft_product_type":
                commit("SET_PRODUCT_TYPE_EUNM", rows);
                break;
              case "nft_audit_stauts":
                commit("SET_AUDIT_STATUS_EUNM", rows);
                break;
              case "nft_cast_status":
                commit("SET_CAST_STATUS_EUNM", rows);
                break;
              case "nft_sale_type":
                commit("SET_SALE_TYPE_EUNM", rows);
                break;
              case "nft_delivery_status":
                commit("SET_DELIVERY_STATUS_EUNM", rows);
                break;
              case "nft_transaction_status":
                commit("SET_TRANSACTION_STATUS_EUNM", rows);
                break;
            }
            resolve(rows);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    setMerchants({ commit }, data) {
      commit("SET_MERCHANTS_VALUES", data);
    },
    getUrl({ commit }, params) {
      return new Promise((resolve, reject) => {
        console.log(params, "params===");
        listparams(params)
          .then((res) => {
            if (res.code >= 200 && res.code < 300) {
              const { data } = res;
              switch (params.configKey) {
                case "ipfs_address":
                  commit("SET_IPFS_ADDRESS", data.data[0]);
                  break;
                case "blockchain_browser_address":
                  commit("SET_BLOCKCHAIN_BROSER_ADDRESS", data.data[0]);
                  break;
                case "blockchain_browser_websocket_address":
                  commit(
                    "SET_BLOCKCHAIN_BROSER_WEBSOCKET_ADDRESS",
                    data.data[0]
                  );
                  break;
              }

              resolve(res);
            } else {
              reject(res);
            }
          })
          .catch((err) => {
            reject(err);
          });
      });
    },
  },
  getters: {},
};

export default common;
