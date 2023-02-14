<template>
  <div class="container">
    <section v-if="Delegator_tableData.length">
      <div
        class="list-ul-box"
        v-for="(item, key) in Delegator_tableData"
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
              (item.balance.amount && onFeixedNum(item.balance.amount, 6)) || 0
            }}</span>
          </li>
          <li>
            <div class="li-text">Shares:</div>
            <span>{{
              (item.delegation.shares &&
                onFeixedNum(item.delegation.shares, 2)) ||
              0
            }}</span>
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
import Pagination from "@/components/Pagination";
import { getAddressDelegateInfo } from "@/api/deal";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      Delegator_tableData: [],
      coinName: "",
      pageSize: 5,
      pageIndex: 1,
      total: 0,
    };
  },
  created() {
    this.getDelegatorByOperatorAddressFn();
  },
  watch: {
    $route(from, to) {
      if (from.name == "Accountdetails") {
        this.getDelegatorByOperatorAddressFn();
      }
    },
  },
  methods: {
    currpageChange(val) {
      this.pageIndex = val;
      this.getDelegatorByOperatorAddressFn();
    },
    getDelegatorByOperatorAddressFn() {
      getAddressDelegateInfo({
        type: "delegator",
        address: this.$route.params.id,
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
      }).then((res) => {
        this.Delegator_tableData = res.result.records;
        if (res.result.records[0].coinName) {
          this.coinName = res.result.records[0].coinName.toUpperCase();
        }
        this.Delegator_tableData.map((items) => {
          items.delegation.delegator_address_jiequ = this.$jiequStrFn(
            items.delegation.delegator_address
          );
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
</style>
