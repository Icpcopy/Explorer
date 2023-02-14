<template>
  <div class="container-deal">
    <header>
      <div class="title-text">Transactions</div>
      <InputsSreach @clickParentFn="clickParentFn" :state="4"></InputsSreach>
    </header>
    <section v-if="Table_dataBool">
      <ul v-for="(item, key) in Table_data" :key="key">
        <li>
          <span>TXHash:</span>
          <div @click="$clickHshRouterFn(item)">
            <span class="maodian">{{ item.txhash_jiequ }}</span>
            <span class="tiaoshu" v-if="item.event.length > 1"
              >+{{ item.event.length }}</span
            >
          </div>
        </li>
        <li>
          <span>TXType:</span>
          <div>
            <span v-if="item.event.length > 1">--</span>
            <div v-else>
              <span>{{ item.event[0].type }}</span>
            </div>
          </div>
        </li>
        <li>
          <span>Amount:</span>
          <div>
            <span
              @click="$clickHshRouterFn(item)"
              v-if="item.isMore"
              class="tiaoshu cursor_"
              >More+</span
            >
            <span v-else-if="item.event[0].amount">
              {{
                (item.event[0].amount &&
                  onFeixedNum(item.event[0].amount, 6)) ||
                0
              }}
              {{ item.event[0].coinName }}
            </span>
            <span v-else>--</span>
          </div>
        </li>
        <li>
          <span>From:</span>
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
                style="flex: 1; width: auto"
                class="maodian minimgbox"
                @click="$userInfoRouterFn(item.event[0].from.operatorAddress)"
              >
                <img class="moniker_img" :src="item.event[0].from.icon" />
                <span style="flex: 1">{{ item.event[0].from.moniker }}</span>
              </div>
            </div>
          </div>
        </li>
        <li>
          <span>To:</span>
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
                style="flex: 1"
                class="maodian minimgbox"
                @click="$userInfoRouterFn(item.event[0].to.operatorAddress)"
              >
                <img class="moniker_img" :src="item.event[0].to.icon" />
                <span class="usernamelength">{{
                  item.event[0].to.moniker
                }}</span>
              </div>
            </div>
          </div>
        </li>
        <li>
          <span>Block:</span>
          <div class="maodian" @click="$blockChainInfosRouterFn(item.height)">
            {{ item.height }}
          </div>
        </li>
        <li>
          <span>Fee:</span>
          <span style="flex: 1"
            >{{ (item.fee && onFeixedNum(item.fee, 6)) || 0 }}
            {{ item.feeCoinName }}</span
          >
        </li>
        <li>
          <span>Time:</span>
          <div class="huise">{{ item.timestamp_utc }} UTC</div>
        </li>
      </ul>
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
import Pagination from "@/components/Pagination";
import NoData from "@/components/nodate";
import { getByBlockNumber } from "@/api/deal";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      Table_data: [],
      Table_dataBool: false,
      pageIndex: 1,
      pageSize: 5,
      bigType: "",
      endTime: "",
      startTime: "",
      status: "",
      Maxpages_index: 0,
    };
  },
  created() {
    (this.Table_data = []), this.getByBlockNumberFn();
    var vue_This = this;
    this.$scrrollBox((This) => {
      if (This.pageIndex < This.Maxpages_index) {
        This.pageIndex++;

        This.getByBlockNumberFn();
      }
    });
  },
  methods: {
    clickParentFn(date) {
      (this.bigType = date.typetext),
        (this.endTime = date.end),
        (this.startTime = date.start),
        (this.status = date.statetext);
      this.Table_data = [];
      this.pageIndex = 1;
      this.getByBlockNumberFn();
    },
    getByBlockNumberFn() {
      getByBlockNumber({
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
        bigType: this.bigType,
        endTime: this.endTime,
        startTime: this.startTime,
        status: this.status,
      }).then((res) => {
        if (res.result.records.length) {
          this.Table_dataBool = true;
        } else {
          this.Table_dataBool = false;
        }
        this.Table_data.push(...res.result.records);

        this.Maxpages_index = res.result.pages;

        res.result.records.map((item) => {
          item.timestamp_hsm = this.$TimeremainingFn(item.timestamp);
          item.timestamp_utc = this.$getUTCTimeFn(item.timestamp);
          item.txhash_jiequ = this.$jiequStrFn(item.txhash);
          item.event.map((children, key) => {
            if (children.from != null && children.from.icon == undefined) {
              children.from_jiequ = this.$jiequStrFn(children.from);
            }
            if (children.to != null && children.to.icon == undefined) {
              children.to_jiequ = this.$jiequStrFn(children.to);
            }
          });
        });

        // this.total = res.result.total;
      });
    },
  },
  components: {
    InputsSreach,
    Pagination,
    NoData,
  },
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
.usernamelength {
  width: 150 / @rem;
  display: inline-block;
  overflow: hidden; //超出的文本隐藏
  text-overflow: ellipsis; //溢出用省略号显示
  white-space: nowrap; //溢出不换行
}
.container-deal {
  position: absolute;
  width: 100%;
  min-height: 100%;
  background: #f8f8f8;
}
header {
  padding: 10 / @rem;
}
section {
  ul {
    padding: 0px 15 / @rem;
    background: #fff;
    border-top: 1px solid #ededed;
    border-bottom: 1px solid #ededed;
    margin-bottom: 10 / @rem;
  }
  ul li {
    display: flex;
    height: 35 / @rem;
    line-height: 35 / @rem;
  }
  span {
    width: 20%;
    margin-right: 18 / @rem;
    font-size: 14 / @rem;
    font-weight: 500;
  }
}
</style>
