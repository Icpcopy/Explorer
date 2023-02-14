<template>
  <div class="container-Verifierrecord">
    <InputsSreach @clickParentFn="clickParentFn" :state="2"></InputsSreach>
    <section v-if="tableData.length">
      <div class="list-box" v-for="(item, key) in tableData" :key="key">
        <div>
          <span>TXHash:</span>
          <div class="colors">
            <span class="maodian" @click="$clickHshRouterFn(item)">{{
              item.txhash_jiequ
            }}</span>
            <span v-if="item.event.length > 1" class="tiaoshu"
              >+{{ item.event.length }}</span
            >
          </div>
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
          <span>Moniker:</span>
          <div class="colors">
            <span v-if="item.event.length > 1">--</span>
            <div v-else>
              <div v-if="item.event[0].toIsNull == 2">
                <div
                  class="maodian minimgbox"
                  @click="$userInfoRouterFn(item.event[0].to.operatorAddress)"
                >
                  <img class="moniker_img" :src="item.event[0].to.icon" />
                  <span>{{ item.event[0].to.moniker }}</span>
                </div>
              </div>
              <div v-else-if="item.event[0].toIsNull == 0">
                <span
                  class="maodian"
                  @click="$headerRouterFn(item.event[0].to)"
                  >{{ item.event[0].to_jiequ }}</span
                >
              </div>
              <div v-else-if="item.event[0].fromIsNull == 0">
                <span
                  class="maodian"
                  @click="$headerRouterFn(item.event[0].from)"
                  >{{ item.event[0].to_jiequ }}</span
                >
              </div>
              <div v-else-if="item.event[0].fromIsNull == 2">
                <div
                  class="maodian minimgbox"
                  @click="$userInfoRouterFn(item.event[0].from.operatorAddress)"
                >
                  <img class="moniker_img" :src="item.event[0].from.icon" />
                  <span>{{ item.event[0].from.moniker }}</span>
                </div>
              </div>
              <div v-else>--</div>
            </div>
          </div>
        </div>
        <div>
          <span>Self-Bonded:</span>
          <div>
            <span v-if="item.event.length > 1">--</span>
            <div v-else>
              <div v-if="item.event[0].fromIsNull == 2">
                <span>{{
                  (item.event[0].from.selfBonded &&
                    onFeixedNum(item.event[0].from.selfBonded, 6)) ||
                  0
                }}</span>
              </div>
              <div v-else-if="item.event[0].toIsNull == 2">
                <span>{{
                  (item.event[0].to.selfBonded &&
                    onFeixedNum(item.event[0].to.selfBonded, 6)) ||
                  0
                }}</span>
              </div>

              <div v-else>--</div>
            </div>
          </div>
        </div>

        <div>
          <span>TXtype:</span>
          <div>
            <span v-if="item.event.length > 1">--</span>
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
    </section>
    <div class="date-no-img" v-else>
      <NoData></NoData>
    </div>
    <div class="list-load-end" v-if="Maxpages_index == pageIndex">
      <span>--no more--</span>
    </div>
  </div>
</template>

<script>
import InputsSreach from "@/components/inputsSreach";
import { getValidatorRelatedTx } from "@/api/transaction";
import NoData from "@/components/nodate";
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
    };
  },
  created() {
    this.getValidatorRelatedTxFn();
    var vue_This = this;
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
        bigType: "validation",
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
        smallType: this.smallType,
        status: this.status,
        startTime: this.startTime,
        endTime: this.endTime,
      }).then((res) => {
        this.tableData.push(...res.result.records);
        this.Maxpages_index = res.result.pages;
        if (res.result.coinName) {
          this.coinName = res.result.coinName.toUpperCase();
        }
        this.tableData.map((item) => {
          if (item.event.length == 1) {
            if (item.event[0].fromIsNull == 0) {
              item.event[0].from_jiequ = this.$jiequStrFn(item.event[0].from);
            }
            if (item.event[0].toIsNull == 0) {
              item.event[0].to_jiequ = this.$jiequStrFn(item.event[0].to);
            }
          }
          item.txhash_jiequ = this.$jiequStrFn(item.txhash);
          item.timestamp = this.$getUTCTimeFn(item.timestamp);
        });
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
.list-box {
  padding-left: 15 / @rem;
  span {
    text-indent: 0px !important;
  }
}
.container-Verifierrecord {
  position: absolute;
  width: 100%;
  min-height: 100%;
  background: #f8f8f8;
}
</style>
