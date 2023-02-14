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
            <div>Adderss:</div>
            <span
              class="colors"
              @click="$headerRouterFn(item.delegator_address)"
              >{{ item.delegator_address_jiequ }}</span
            >
          </li>
          <li>
            <div>Amount({{ coinName }}):</div>
            <span>{{
              (item.countAounmnt &&
                onFeixedNum(item.countAounmnt / 10000000000000000000*10, 6)) ||
              0
            }}</span>
          </li>
          <li class="endTtime">
            <div>End Time：</div>
            <span>{{ item.maxTimeData }} UTC</span>
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
import { getUnDelegatorByOperatorAddress } from "@/api/identifier";
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
  methods: {
    currpageChange(val) {
      this.pageIndex = val;
      this.getUnDelegatorByOperatorAddressFn();
    },
    getUnDelegatorByOperatorAddressFn(name) {
      getUnDelegatorByOperatorAddress({
        operatorAddress: this.$route.params.id,
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
      }).then((res) => {
        this.UnDelegator_tableData = res.result.records;
        this.UnDelegator_tableData.map((items) => {
          items.delegator_address_jiequ = this.$jiequStrFn(
            items.delegator_address
          );
          items.countAounmnt = 0;
          items.maxTimeArr = [];

          if (res.result.coinName) {
            this.coinName = res.result.coinName.toUpperCase();
          }
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
.endTtime {
  span {
    display: inline-block !important;
    width: 270 / @rem !important;
  }
}
</style>
