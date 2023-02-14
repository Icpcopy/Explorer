<template>
  <div class="container">
    <div class="infos-title">Transaction records</div>
    <van-tabs @click="TabonClick">
      <van-tab v-for="(item, index) in tbsData" :title="item" :key="index">
        <section v-if="ByBlockNumberData.length">
          <div class="list-box" v-for="(j, i) in ByBlockNumberData" :key="i">
            <div>
              <span>TXHash:</span>
              <div>
                <span class="colors" @click="$clickHshRouterFn(j)">
                  {{ j.txhash_jiequ }}
                </span>
                <span class="tiaoshu" v-if="j.event.length > 1"
                  >+{{ j.event.length }}</span
                >
              </div>
            </div>
            <div>
              <span>TXType:</span>
              <div>{{ j.event[0].type }}</div>
            </div>
            <div>
              <span>From:</span>

              <span v-if="j.event.length > 1">--</span>
              <div v-else>
                <div v-if="j.event[0].fromIsNull == 0" @click="clickloadingFn">
                  <span
                    class="colors"
                    @click="$headerRouterFn(j.event[0].from, j.event[0].from)"
                    >{{ j.event[0].from_jiequ }}</span
                  >
                </div>
                <div v-if="j.event[0].fromIsNull == 1">--</div>
                <div v-if="j.event[0].fromIsNull == 2">
                  <div
                    class="minimgbox colors"
                    @click="$userInfoRouterFn(j.event[0].from.operatorAddress)"
                  >
                    <img :src="j.event[0].from.icon" />
                    <span>{{ j.event[0].from.moniker }}</span>
                  </div>
                </div>
              </div>
            </div>
            <div>
              <span>To:</span>
              <span v-if="j.event.length > 1">--</span>
              <div v-else>
                <div v-if="j.event[0].toIsNull == 0" @click="clickloadingFn">
                  <span
                    class="colors"
                    @click="$headerRouterFn(j.event[0].to, true)"
                    >{{ j.event[0].to_jiequ }}</span
                  >
                </div>
                <div v-if="j.event[0].toIsNull == 1">--</div>
                <div v-if="j.event[0].toIsNull == 2">
                  <div
                    class="minimgbox colors"
                    @click="$userInfoRouterFn(j.event[0].to.operatorAddress)"
                  >
                    <img :src="j.event[0].to.icon" />
                    <span>{{ j.event[0].to.moniker }}</span>
                  </div>
                </div>
              </div>
            </div>
            <div>
              <span>Amount({{ j.event[0].coinName }}):</span>
              <span v-if="j.isMore" class="tiaoshu">More+</span>
              <div v-else-if="j.event[0].amount">
                {{
                  (j.event[0].amount && onFeixedNum(j.event[0].amount, 6)) || 0
                }}
              </div>
              <div v-else>--</div>
            </div>
            <div>
              <span>Fee({{ j.feeCoinName }}):</span>
              <div>{{ (j.fee && onFeixedNum(j.fee, 6)) || 0 }}</div>
            </div>

            <div>
              <span>Time:</span>
              <div class="huise">{{ j.timestamp }} UTC</div>
            </div>
          </div>
        </section>
        <div class="date-no" v-else>No Data</div>
      </van-tab>
    </van-tabs>
    <div class="vloading" v-if="dataBool">
      <van-loading type="spinner" class="spinner" color="#402ED8" />
    </div>
  </div>
</template>

<script>
import { getAllTypeCount, getTransactionByBlockNumber } from "@/api/block";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  props: ["isStateTxt"],
  data() {
    return {
      tbsData: [],
      bigType: "transfer",
      blockVal: "",
      ByBlockNumberData: [],
      pageIndex: 1,
      pageSize: 5,
      Maxpages_index: 0,
      dataBool: false,
    };
  },
  watch: {
    $route(from, to) {
      if (from.name == "BlockchainDetails") {
        this.ByBlockNumberData = [];
        this.getAllTypeCountFn();
        this.getTransactionByBlockNumberFn();
        this.$scrrollBox((This) => {
          if (This.pageIndex < This.Maxpages_index) {
            This.pageIndex++;
            This.getTransactionByBlockNumberFn();
          }
        });
      }
    },
  },
  mounted() {
    this.ByBlockNumberData = [];
    this.getAllTypeCountFn();
    this.getTransactionByBlockNumberFn();
    this.$scrrollBox((This) => {
      if (This.pageIndex < This.Maxpages_index) {
        This.pageIndex++;
        This.getTransactionByBlockNumberFn();
      }
    });
  },
  methods: {
    clickloadingFn() {
      this.dataBool = true;
      setTimeout(() => {
        this.dataBool = false;
      }, 1000);
    },
    TabonClick(val) {
      switch (val) {
        case 0:
          this.bigType = "transfer";
          break;
        case 1:
          this.bigType = "staking";
          break;
        case 2:
          this.bigType = "reward";
          break;
        case 3:
          this.bigType = "vote";
          break;
        case 4:
          this.bigType = "business";
          break;
        case 5:
          this.bigType = "jail";
          break;
      }
      this.ByBlockNumberData = [];
      this.pageIndex = 1;
      this.getTransactionByBlockNumberFn();
    },
    getAllTypeCountFn() {
      getAllTypeCount({
        blockHeight:
          this.isStateTxt == "height" ? this.$route.params.id : undefined,
        address:
          this.isStateTxt == "address" ? this.$route.params.id : undefined,
        bigType: this.bigType,
      }).then((res) => {
        var data = res.result;
        this.tbsData = [
          "Transfer(" + data.transferCount + ")",
          "Staking(" + data.delegatorCount + ")",
          "Distribution(" + data.rewardCount + ")",
          "Governance(" + data.voteCount + ")",
          "Business(" + data.businessCount + ")",
          "Slashing(" + data.jailCount + ")",
        ];
      });
    },
    getTransactionByBlockNumberFn() {
      getTransactionByBlockNumber({
        blockHeight:
          this.isStateTxt == "height" ? this.$route.params.id : undefined,
        address:
          this.isStateTxt == "address" ? this.$route.params.id : undefined,
        bigType: this.bigType,
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      }).then((res) => {
        res.result.records.map((item) => {
          item.timestamp = this.$getUTCTimeFn(item.timestamp);
          item.txhash_jiequ = this.$jiequStrFn(item.txhash);
          item.feeCoinName = item.feeCoinName.toUpperCase();
          item.event.map((children, key) => {
            if (children.from != null && children.from.icon == undefined) {
              children.from_jiequ = this.$jiequStrFn(children.from);
            }
            if (children.to != null && children.to.icon == undefined) {
              children.to_jiequ = this.$jiequStrFn(children.to);
            }
            children.coinName = children.coinName.toUpperCase();
          });
        });
        this.ByBlockNumberData.push(...res.result.records);
        this.Maxpages_index = res.result.pages;
      });
    },
  },
  components: {},
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
.list-box {
  padding-left: 15 / @rem;
  span {
    text-indent: 0px !important;
  }
}
.vloading {
  position: fixed;
  top: 0px;
  left: 0px;
  width: 100%;
  height: 100%;
  //   background: rgba(0, 0, 0, 0.3);
  margin-left: 0px;
  .spinner {
    margin-top: 200 / @rem;
  }
}
</style>
