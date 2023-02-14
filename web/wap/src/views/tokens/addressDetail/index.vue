<template>
  <div class="icp-addressDetail">
    <div class="icp-addressDetail__title">
      <span class="icp-addressDetail__title--title">Address Details</span>
      <!-- <van-button
        class="icp-addressDetail__title--button"
        icon="replay"
        type="primary"
      /> -->
    </div>
    <van-row class="icp-addressDetail__row">
      <van-col
        span="8"
        :class="`icp-addressDetail__row--col ${
          activedTab == 'Asset' ? 'is-activeTab' : ''
        }`"
        @click="
          () => {
            activedTab = 'Asset';
          }
        "
      >
        Asset
      </van-col>
      <van-col
        span="8"
        :class="`icp-addressDetail__row--col ${
          activedTab == 'NFT' ? 'is-activeTab' : ''
        }`"
        @click="
          () => {
            activedTab = 'NFT';
          }
        "
        >On chain NFT</van-col
      >
      <van-col
        span="8"
        :class="`icp-addressDetail__row--col ${
          activedTab == 'Txs' ? 'is-activeTab' : ''
        }`"
        @click="
          () => {
            activedTab = 'Txs';
          }
        "
        >Txs</van-col
      >
    </van-row>
    <Assets
      v-if="activedTab == 'Asset'"
      :defaultValue="assetsInfo"
      :tokenList="tokenList"
      :activedTab="activedAssetsTab"
      :defaultType="defaultType"
      :delegationsTotal="delegationTotal"
      :delegationsPageNum="delegationPageNum"
      :delegationsPageSize="delegationpageSize"
      :delegationTableData="delegationTableData"
      :unbondingTableData="unbondingTableData"
      :unbondingTotal="unbondingTotal"
      :unbondingPageNum="unbondingPageNum"
      :unbondingPageSize="unbondingPageSize"
      @onChangeAssets="onChangeAssets"
      @onChangeType="onChangeType"
      @delegationsCurrpageChange="delegationsCurrpageChange"
      @unbondingCurrpageChange="unbondingCurrpageChange"
    />
    <Nft v-if="activedTab == 'NFT'" />
    <Txs
      v-if="activedTab == 'Txs'"
      :evmAddress="evmAddress"
      :chainAddress="chainAddress"
    />
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
import { getAddressInfo, getAddressDelegateInfo } from "@/api/deal";
import { Assets, Nft, Txs } from "./components";
import { Tokens } from "@/api";
// import Clipboard from "clipboard";
// import { Toast } from "vant";
export default {
  components: { Assets, Nft, Txs },
  mixins: [mixinCommon],
  data() {
    return {
      activedTab: "Asset",
      activedAssetsTab: "evm",
      defaultType: "erc20",
      address: "",
      assetsInfo: null,
      tokenList: [],
      chainAddress: "",
      evmAddress: "",
      delegationTotal: 0,
      delegationPageNum: 1,
      delegationpageSize: 10,
      unbondingTotal: 0,
      unbondingPageNum: 1,
      unbondingPageSize: 10,
    };
  },
  watch: {
    $route() {
      // this.$forceUpdate();
      window.scrollTo(0, 0);
      this.activedTab = "Asset";
      this.address = this.$route.params.address;
      this.find();
    },
  },
  methods: {
    // getAddressInfoFn() {
    //   getAddressInfo({ address: this.$route.params.id }).then((res) => {
    //     this.result = res.result;
    //     this.result.balances.map((item) => {
    //       if (item.denom == "uatom") {
    //         this.result.amount = item.amount;
    //       }
    //     });
    //   });
    // },
    delegationsCurrpageChange(e) {
      this.delegationsPageNum = e;
      this.initDelegationList();
    },
    unbondingCurrpageChange(e) {
      this.unbondingPageNum = e;
      this.initUnbondingList();
    },
    //切换erc类型
    onChangeType(type) {
      this.defaultType = type;
      if (type == "erc20") {
        this.getEvmAddressInfo();
      } else if (type == "erc721") {
        this.getEvmNftAssetsList();
      }
    },
    //资产详情delegation 列表
    initDelegationList() {
      const obj = {
        type: "delegator",
        address: this.chainAddress,
        pageIndex: this.delegationPageNum,
        pageSize: this.delegationpageSize,
      };
      getAddressDelegateInfo(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.delegationTableData = result.records;

          this.delegationTotal = result.total;
          // console.log(this.delegationTotal, "  this.delegationTotal =====");
        }
      });
    },
    //资产详情unbonding 列表
    initUnbondingList() {
      const obj = {
        type: "undelegator",
        address: this.chainAddress,
        pageIndex: this.unbondingPageNum,
        pageSize: this.unbondingPageSize,
      };
      getAddressDelegateInfo(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.unbondingTableData = result.records;
          this.unbondingTotal = result.total;
        }
      });
    },

    //资产详情
    initAssetsInfo() {
      const obj = {
        address: this.address,
      };
      getAddressInfo(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.assetsInfo = result;
          this.tokenList = result.tokenBalances;
          this.chainAddress = result.address;
          console.log(result.evmAddress,'result.evmAddress====')
          this.evmAddress = result.evmAddress;
          this.initDelegationList();
          this.initUnbondingList();
          if (this.activedAssetsTab == "evm") {
            this.onChangeAssets("evm");
          }
          //   this.unbondingTotal = result.total;
        }
      });
    },

    handleDelegationChange(e) {
      this.delegationPageNum = e;
      this.initDelegationList();
    },
    handleCurrentChange(e) {
      this.pageNum = e;
      this.initUnbondingList();
    },
    //列表
    find() {
      this.initAssetsInfo();
    },
    //查询evmtoken地址信息
    getEvmAddressInfo() {
      const obj = { address: this.evmAddress };
      Tokens.getAddressInfo(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.tokenList = result.tokens;
          //   this.unbondingTotal = result.total;
        }
      });
    },

    //查询erc721evm合约资产列表
    getEvmNftAssetsList() {
      const obj = { address: this.evmAddress };
      Tokens.getEvmNftAssetsList(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.tokenList = result;
          //   this.unbondingTotal = result.total;
        }
      });
    },

    //tab切换展示token列表
    onChangeAssets(e) {
      this.activedAssetsTab = e;
      if (e == "chain") {
        this.initAssetsInfo();
      } else {
        if (this.defaultType == "erc20") {
          this.getEvmAddressInfo();
        } else if (this.defaultType == "erc721") {
          this.getEvmNftAssetsList();
        }
        // this.getEvmAddressInfo();
      }
    },
  },
  mounted() {
    window.scrollTo(0, 0);
    this.address = this.$route.params.address;
    this.find();
  },
  created() {},
};
</script>

<style scoped lang="less">
.icp-addressDetail {
  &__title {
    padding: 20px 15px 10px;
    display: flex;
    justify-content: space-between;
    &--title {
      font-size: 14px;
      color: #2a2a2a;
      font-weight: 600;
      line-height: 35px;
    }
    .van-button {
      height: 35px;
      background-color: #5841ac;
      border: 0;
    }
  }
  &__row {
    font-size: 14px;

    &--col {
      text-align: center;
      background-color: #fff;
      color: #2a2a2a;
      padding: 10px 0;
      line-height: 20px;
      border: 1px solid #ededed;
    }
    &--col:nth-child(1) {
      border-left: 0;
    }
    &--col:nth-child(2) {
      border-left: 0;
      border-right: 0;
    }
    &--col:nth-child(3) {
      border-right: 0;
    }
    .is-activeTab {
      background-color: #5841ac;
      color: #fff;
      border: 1px solid #5841ac;
    }
  }
}
</style>
