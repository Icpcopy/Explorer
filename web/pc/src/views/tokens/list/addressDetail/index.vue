<template>
  <div class="icp-addressDetail">
    <div class="icp-addressDetail__tile">Address Details</div>
    <el-tabs type="card" v-model="activeName">
      <el-tab-pane label="Asset" name="first">
        <Asset @onGetAddress="onChangeAddress"
      /></el-tab-pane>
      <el-tab-pane label="On Chain NFT" name="second"><NftTab /></el-tab-pane>
      <el-tab-pane label="Txs" name="third"
        ><Txs :chainAddress="chainAddress" :evmAddress="evmAddress"
      /></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { Asset, Txs } from "./components";
// Nft,
import NftTab from "../../../Accountbalance/components/NftTab";
import { mixinCommon } from "@/mixin";
export default {
  components: { Asset, Txs, NftTab }, //FormSearch
  mixins: [mixinCommon],
  name: "NftIndex",
  data() {
    return {
      inputValue: "",
      value2: [],
      activeName: "first",
      tableData: [{ id: 1 }],
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      formValue: {
        keyWord: "",
      },
      chainAddress: "",
      evmAddress: "",
    };
  },
  methods: {
    //获取到address
    onChangeAddress(item) {
      this.chainAddress = item.chainAddress;
      this.evmAddress = item.evmAddress;
    },
  },
  mounted() {
    // this.find();
  },
};
</script>
<style scoped lang="less">
.icp-addressDetail {
  &__tile {
    padding: 30px 0 20px 0;
    font-size: 24px;
    font-weight: bold;
    color: #2a2a2a;
  }
}
</style>
<style>
.el-tabs__header {
  border-bottom: 0 !important;
}
.el-tabs--card > .el-tabs__header .el-tabs__item {
  border-bottom: 1px solid #e4e7ed;
}
.el-tabs__item .is-active {
  border-bottom: 1px solid #e4e7ed;
}
.el-tabs--card > .el-tabs__header .el-tabs__item.is-active {
  border-bottom-color: #e4e7ed;
  background-color: #5841ac;
  color: #fff;
}
.el-tabs__item:hover {
  background-color: #5841ac;
  color: #fff;
}
</style>
