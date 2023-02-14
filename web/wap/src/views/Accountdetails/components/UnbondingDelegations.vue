<template>
  <div class="container">
    <section v-if="UnDelegator_tableData.length">
      <div
        class="list-ul-box"
        v-for="(item, key) in UnDelegator_tableData"
        :key="key"
      >
        <ul>
          <li>
            <div class="li-text">Adderss:</div>
            <div
              class="minimgbox"
              @click="$userInfoRouterFn(item.moniker.operatorAddress)"
            >
              <img :src="item.moniker.icon" />
              <span>{{ item.moniker.moniker }}</span>
            </div>
          </li>
          <li>
            <div class="li-text">Amount({{ coinName }}):</div>
            <span>{{
              (item.countAounmnt && onFeixedNum(item.countAounmnt, 6)) || 0
            }}</span>
          </li>
          <li>
            <div class="li-text">End Time：</div>
            <span class="width_s">{{ item.maxTimeData }} UTC</span>
          </li>
        </ul>
      </div>
      <Pagination
        :total="total"
        :pageSize="pageSize"
        :pageIndex="pageIndex"
        @currpageChange="currpageChange"
      ></Pagination>
    </section>
    <div class="date-no" v-else>No Data</div>
  </div>
</template>

<script>
import { getAddressDelegateInfo } from "@/api/deal";
import Pagination from "@/components/Pagination";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      UnDelegator_tableData: [],
      pageSize: 5,
      pageIndex: 1,
      total: 0,
      coinName: "",
    };
  },
  created() {
    this.getUnDelegatorByOperatorAddressFn();
  },
  watch: {
    $route(from, to) {
      if (from.name == "Accountdetails") {
        this.getUnDelegatorByOperatorAddressFn();
      }
    },
  },
  methods: {
    currpageChange(val) {
      this.pageIndex = val;
      this.getUnDelegatorByOperatorAddressFn();
    },
    getUnDelegatorByOperatorAddressFn(name) {
      getAddressDelegateInfo({
        type: "undelegator",
        address: this.$route.params.id,
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
      }).then((res) => {
        this.UnDelegator_tableData = res.result.records;
        if (res.result.coinName) {
          this.coinName = res.result.coinName.toUpperCase();
        }
        this.UnDelegator_tableData.map((items) => {
          items.delegator_address_jiequ = this.$jiequStrFn(
            items.delegator_address
          );
          items.countAounmnt = 0;
          items.maxTimeArr = [];
          items.entries.map((children) => {
            items.countAounmnt += children.balance * 1;
            items.maxTimeArr.push(this.$timestamp(children.completion_time));
          });
          items.maxTimeData = this.$getUTCTimeFn(Math.max(...items.maxTimeArr));
          //取最大值
        });
        this.total = res.result.total;
      });
    },
  },
  components: {
    Pagination,
  },
};
</script>

<style scoped lang="less">
@import url("../../../css/index.less");
.list-ul-box {
  padding: 10 / @rem 20 / @rem;
  margin-top: 0px;
  margin-bottom: 20 / @rem;
}
li {
  span {
    width: 200 / @rem !important;
  }
}
.width_s {
  display: inline-block;
  width: 230 / @rem;
}
</style>
