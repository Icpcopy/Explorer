<template>
  <div class="container">
    <header>
      <div>Address Details</div>
      <span>{{ $route.params.id }}</span>
    </header>
    <section>
      <van-tabs type="card">
        <van-tab title="Asset">
          <Asset :result="result"></Asset>
        </van-tab>
        <van-tab title="NFT">
          <Nft :result="result"></Nft>
        </van-tab>
        <van-tab title="Issue">
          <Issue :result="result"></Issue>
        </van-tab>
        <van-tab title="Deal">
          <Deal :result="result"></Deal>
        </van-tab>
      </van-tabs>
    </section>
  </div>
</template>

<script>
import { getAddressInfo } from "@/api/deal";
import Asset from "./components/asset";
import Issue from "./components/issue";
import Deal from "./components/deal";
import Nft from "./components/Nft";
export default {
  data() {
    return {
      result: { balances: [[]], tokenBalances: [] },
    };
  },
  created() {
    this.getAddressInfoFn();
  },
  watch: {
    $route(from, to) {
      if (from.name == "Accountdetails") {
        this.getAddressInfoFn();
      }
    },
  },
  methods: {
    getAddressInfoFn() {
      getAddressInfo({ address: this.$route.params.id }).then((res) => {
        this.result = res.result;
        this.result.balances.map((item) => {
          if (item.denom == "uatom") {
            this.result.amount = item.amount;
          }
        });
      });
    },
  },
  components: {
    Asset,
    Issue,
    Deal,
    Nft,
  },
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
header {
  padding: 20 / @rem 0px;
  margin-left: 15 / @rem;
  div {
    font-size: 14 / @rem;
  }
  span {
    display: inline-block;
    color: #696969;
    margin-top: 7 / @rem;
  }
}
.card-content {
  padding-top: 20 / @rem;
}
.list-box {
  margin-top: 0px;
  margin-bottom: 15 / @rem;
}
</style>
