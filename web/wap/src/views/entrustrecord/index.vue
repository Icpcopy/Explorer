<template>
  <div class="container entrustrecord">
    <div class="header-text">Delegation Txs List {{ total }} Txs</div>
    <InputsSreach @clickParentFn="clickParentFn" :state="1"></InputsSreach>
    <section v-if="tableData.length">
      <div class="list-box" v-for="(item, key) in tableData" :key="key">
        <div>
          <span>TXHash:</span>
          <span class="maodian" @click="$clickHshRouterFn(item)">{{
            item.txhash_jiequ
          }}</span>
          <span class="tiaoshu" v-if="item.event.length > 1"
            >+ {{ item.event.length }}</span
          >
        </div>
        <div>
          <span>Block:</span>
          <div class="colors">
            <span
              class="maodian"
              @click="$blockChainInfosRouterFn(item.height)"
              >{{ item.height }}</span
            >
          </div>
        </div>
        <div>
          <span>From:</span>
          <div class="colors">
            <span v-if="item.event.length > 1">--</span>
            <div v-else>
              <div v-if="item.event[0].fromIsNull == 0">
                <span
                  class="maodian"
                  @click="$headerRouterFn(item.event[0].from)"
                  >{{ item.event[0].from_jiequ }}</span
                >
              </div>
              <div v-if="item.event[0].fromIsNull == 1">--</div>
              <div v-if="item.event[0].fromIsNull == 2">
                <div
                  class="minimgbox"
                  @click="$userInfoRouterFn(item.event[0].from.operatorAddress)"
                >
                  <img class="moniker_img" :src="item.event[0].from.icon" />
                  <span>{{ item.event[0].from.moniker }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div>
          <span>To:</span>
          <div class="colors">
            <span v-if="item.event.length > 1">--</span>
            <div v-else>
              <div v-if="item.event[0].toIsNull == 0">
                <span
                  class="maodian"
                  @click="$headerRouterFn(item.event[0].to)"
                  >{{ item.event[0].to_jiequ }}</span
                >
              </div>
              <div v-if="item.event[0].toIsNull == 1">--</div>
              <div v-if="item.event[0].toIsNull == 2">
                <div
                  class="minimgbox"
                  @click="$userInfoRouterFn(item.event[0].to.operatorAddress)"
                >
                  <img class="moniker_img" :src="item.event[0].to.icon" />
                  <span>{{ item.event[0].to.moniker }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div>
          <span>Amount(ICT):</span>
          <div>
            <span v-if="item.isMore" class="tiaoshu">More+</span>
            <span v-else>{{
              (item.event[0].amount && onFeixedNum(item.event[0].amount, 6)) ||
              0
            }}</span>
          </div>
        </div>
        <div>
          <span>TXtype:</span>
          <div>
            <div v-if="item.event.length > 1">--</div>
            <span v-else>{{ item.event[0].type }}</span>
          </div>
        </div>
        <div>
          <span>Fee(ICT):</span>
          <div>{{ (item.fee && onFeixedNum(item.fee, 6)) || 0 }}</div>
        </div>

        <div>
          <span>Time:</span>
          <div class="huise">{{ item.timestamp }} UTC</div>
        </div>
      </div>
      <div class="list-load-end" v-if="Maxpages_index == pageIndex">
        <span>--no more--</span>
      </div>
    </section>
    <div class="date-no-img" v-else>
      <NoData></NoData>
    </div>
  </div>
</template>

<script>
import InputsSreach from "@/components/inputsSreach";
import NoData from "@/components/nodate";
import { getValidatorRelatedTx } from "@/api/transaction";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      tableData: [],
      coinName: "",
      pageSize: 30,
      pageIndex: 1,
      endTime: "",
      startTime: "",
      status: "",
      smallType: "",
      Maxpages_index: 0,
      total: "",
    };
  },
  created() {
    this.getValidatorRelatedTxFn();
    this.tableData = [];
    this.$scrrollBox((This) => {
      if (This.pageIndex < This.Maxpages_index) {
        This.pageIndex++;
        This.getValidatorRelatedTxFn();
      }
    });
  },
  methods: {
    clickParentFn(date) {
      (this.endTime = date.end),
        (this.startTime = date.start),
        (this.status = date.statetext),
        (this.smallType = date.typetext),
        (this.tableData = []);
      this.getValidatorRelatedTxFn();
    },
    getValidatorRelatedTxFn() {
      getValidatorRelatedTx({
        bigType: "delegation",
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
        smallType: this.smallType,
        status: this.status,
        startTime: this.startTime,
        endTime: this.endTime,
      }).then((res) => {
        this.tableData.push(...res.result.records);
        this.Maxpages_index = res.result.pages;
        this.coinName = res.result.coinName.toUpperCase();
        this.tableData.map((item) => {
          item.event.map((children, key) => {
            if (children.from != null && children.from.icon == undefined) {
              children.from_jiequ = this.$jiequStrFn(children.from);
            }
            if (children.to != null && children.to.icon == undefined) {
              children.to_jiequ = this.$jiequStrFn(children.to);
            }
          });
          item.txhash_jiequ = this.$jiequStrFn(item.txhash);
          item.timestamp = this.$getUTCTimeFn(item.timestamp);
        });
        this.total = res.result.total;
      });
    },
  },
  components: {
    InputsSreach,
    NoData,
  },
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
.entrustrecord {
  position: absolute;
  width: 100%;
  min-height: 100%;
  background: #f8f8f8;
}
.header-text {
  background: #fff;
  line-height: 40 / @rem;
  padding-left: 15 / @rem;
  padding-top: 5 / @rem;
  font-size: 14 / @rem;
}
.list-box {
  padding-left: 15 / @rem;
}
.list-box span {
  text-indent: 0px !important;
}
</style>
