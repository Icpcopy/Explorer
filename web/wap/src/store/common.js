import { listparams } from "@/api/home";
// import address from "@/utils/address.js";
const common = {
  namespaced: true,
  state: {
    ipfsAddress: null,
    socketAddress: null,
  },
  mutations: {
    SET_IPFS_ADDRESS: (state, data) => {
      state.ipfsAddress = data.configValue;
    },
    SET_BLOCKCHAIN_BROSER_WEBSOCKET_ADDRESS: (state, data) => {
      state.socketAddress = data.configValue;
    },
  },
  actions: {
    getUrl({ commit }, params) {
      return new Promise((resolve, reject) => {
        listparams(params)
          .then((res) => {
            if (res.code >= 200 && res.code < 300) {
              const { data } = res;
              switch (params.configKey) {
                case "ipfs_address_h5":
                  commit("SET_IPFS_ADDRESS", data.data[0]);
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
