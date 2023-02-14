<template>
  <div class="container">
    <header>
      <div>{{ coinName }} Rich List Top 100 Addresses by {{ coinName }}</div>
      <span>
        The assets include the balance tokens,Cross-chain assets and Contractual
        assets
      </span>
    </header>
    <div class="tongji-button">
      <button @click="getAddressByAssetsFn('reset')">Updated</button>
    </div>
    <section>
      <div class="list-box" v-for="(j, i) in ByAssetsData" :key="i">
        <div>
          <span>#:</span>
          <div>{{ i + 1 }}</div>
        </div>
        <div>
          <span>Address：</span>
          <div class="colors" @click="$headerRouterFn(j.address)">
            {{ j.addr_jiequ }}
          </div>
        </div>

        <div>
          <span>Amount ({{ coinName }}):</span>
          <div>{{ (j.amount && onFeixedNum(j.amount, 6)) || 0 }}</div>
        </div>
        <div>
          <span>Percentage：</span>
          <div>{{ (j.rate * 100).toFixed(2) }}%</div>
        </div>
      </div>
      <div class="list-load-end" v-if="Maxpages_index == pageIndex">
        <span>--no more--</span>
      </div>
    </section>
    <div class="vloading" v-if="dataBool">
      <van-loading type="spinner" color="#402ED8" />
    </div>
  </div>
</template>

<script>
import { getAddressByAssets } from "@/api/statistics";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      ByAssetsData: [],
      coinName: "",
      pageIndex: 1,
      pageSize: 100,
      Maxpages_index: 0,
      dataBool: false,
    };
  },
  created() {
    this.getAddressByAssetsFn();
    this.ByAssetsData = [];
    this.$scrrollBox((This) => {
      if (This.pageIndex < This.Maxpages_index) {
        This.pageIndex++;
        This.getAddressByAssetsFn();
      }
    });
  },
  methods: {
    getAddressByAssetsFn(reset) {
      if (reset) {
        this.ByAssetsData = [];
      }
      this.dataBool = true;
      getAddressByAssets({
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      }).then((res) => {
        this.ByAssetsData.push(...res.result.records);
        this.Maxpages_index = res.result.pages;
        this.coinName = res.result.coinName.toUpperCase();
        this.ByAssetsData.map((item) => {
          item.addr_jiequ = this.$jiequStrFn(item.address);
        });
        this.dataBool = false;
      });
    },
  },
  components: {},
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
header {
  margin-left: 15 / @rem;
  padding: 40 / @rem 0px 20 / @rem 0px;
  div {
    font-size: 14 / @rem;
    font-weight: 400;
  }
  span {
    display: inline-block;
    font-size: 8 / @rem;
    margin-top: 8 / @rem;
  }
}
.tongji-button {
  text-align: center;
  button {
    width: 363 / @rem;
    height: 40 / @rem;
    background: #633af9;
    border-radius: 5 / @rem;
    color: #fff;
  }
}
</style>
