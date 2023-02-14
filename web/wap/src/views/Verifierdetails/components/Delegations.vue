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
            <div>Adderss:</div>
            <span
              @click="$headerRouterFn(item.delegation.delegator_address)"
              class="colors"
              >{{ item.delegation.delegator_address_jiequ }}</span
            >
          </li>
          <li>
            <div>Amount({{ coinName }}):</div>
            <span>{{
              (item.balance.amount &&
                onFeixedNum(item.balance.amount / 10000000000000000000*10, 6)) ||
              0
            }}</span>
          </li>
          <li>
            <div>Shares:</div>
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
import { getDelegatorByOperatorAddress } from "@/api/identifier";
import Pagination from "@/components/Pagination";
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
  methods: {
    currpageChange(val) {
      this.pageIndex = val;
      this.getDelegatorByOperatorAddressFn();
    },
    getDelegatorByOperatorAddressFn() {
      getDelegatorByOperatorAddress({
        operatorAddress: this.$route.params.id,
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
      }).then((res) => {
        this.Delegator_tableData = res.result.records;
        if (res.result.coinName) {
          this.coinName = res.result.coinName.toUpperCase();
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
