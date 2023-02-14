<template>
  <div class="container">
    <div class="pub-section-title">Balance</div>
    <div class="list-ul-box">
      <ul>
        <li>
          <div>Available:</div>
          <span
            >{{ (result.balance && onFeixedNum(result.balance, 6)) || 0
            }}{{ result.coinName }}</span
          >
        </li>
        <li>
          <div>Delegated:</div>
          <span
            >{{
              (result.delegator_balance &&
                onFeixedNum(result.delegator_balance, 6)) ||
              0
            }}{{ result.coinName }}</span
          >
        </li>
        <li>
          <div>Unbonding:</div>
          <span
            >{{
              (result.unBonding_balance &&
                onFeixedNum(result.unBonding_balance, 6)) ||
              0
            }}{{ result.coinName }}</span
          >
        </li>
        <li>
          <div>Rewards:</div>
          <span
            >{{ (result.rewards && onFeixedNum(result.rewards, 6)) || 0
            }}{{ result.coinName }}</span
          >
        </li>
        <li>
          <div>Commission:</div>
          <span>--</span>
        </li>
      </ul>
    </div>
    <div class="pub-section-title">Delegations</div>
    <section>
      <Delegations></Delegations>
    </section>
    <div class="pub-section-title">Unbonding Delegations</div>
    <section>
      <UnbondingDelegations></UnbondingDelegations>
    </section>
    <section>
      <Transactionrecords :isStateTxt="'address'"></Transactionrecords>
    </section>
  </div>
</template>

<script>
import Delegations from "./Delegations";
import UnbondingDelegations from "./UnbondingDelegations";
import Transactionrecords from "@/components/Transactionrecords";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  props: ["result"],
  data() {
    return {};
  },
  components: {
    Delegations,
    UnbondingDelegations,
    Transactionrecords,
  },
};
</script>

<style scoped lang="less">
@import url("../../../css/index.less");
.container {
  padding-top: 20 / @rem;
}
.list-ul-box {
  margin-top: 0px;
  padding: 10 / @rem 15 / @rem;
}
.pub-section-title {
}
</style>
