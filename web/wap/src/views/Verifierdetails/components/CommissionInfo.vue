<template>
  <div class="container">
    <div class="info-title">
      <div>Commission Rate:</div>
      <span>
        {{ (userInfoData.commission.commission_rates.rate * 100).toFixed(2) }}%
      </span>
    </div>
    <!-- <div class="info-list-text">
      <ul>
        <li>
          <div>Self-Rate:</div>
          <span>8%</span>
        </li>
        <li>
          <div>Redistribution-Rate：</div>
          <span>3.6%：8.4%</span>
        </li>
      </ul>

      <span></span>
    </div>-->
    <div class="info-title">
      <div>Bonded Tokens:</div>
      <span
        >{{
          (userInfoData.tokens &&
            onFeixedNum(userInfoData.tokens / 10000000000000000000*10, 6)) ||
          0
        }}
        {{ coinName }}</span
      >
    </div>
    <div class="info-list-text">
      <ul>
        <li>
          <div>Self-Bonded:</div>
          <span>
            {{
              (userInfoData.selfDelegateAmount &&
                onFeixedNum(userInfoData.selfDelegateAmount, 6)) ||
              0
            }}
            {{ coinName }}
          </span>
        </li>
        <li>
          <div>Delegator Bonded:</div>
          <span>
            {{
              (userInfoData.tokens / 10000000000000000000*10 -
                userInfoData.selfDelegateAmount &&
                onFeixedNum(
                  userInfoData.tokens / 10000000000000000000*10 -
                    userInfoData.selfDelegateAmount,
                 6
                )) ||
              0
            }}
            {{ coinName }}
          </span>
        </li>
      </ul>
    </div>
    <div class="info-title">
      <div>Delegators:</div>
      <span>{{ userInfoData.totalDelegations }}</span>
    </div>
    <div class="info-title">
      <div>Total Shares:</div>
      <span>{{
        (userInfoData.delegator_shares &&
          onFeixedNum(userInfoData.delegator_shares, 2)) ||
        0
      }}</span>
    </div>
    <div class="info-title">
      <div>Commission Rewards:</div>
      <span>{{
        (userInfoData.commission_rewards &&
          onFeixedNum(userInfoData.commission_rewards, 2)) ||
        0
      }}</span>
    </div>
    <div class="info-title">
      <div>Commission Rate Range:</div>
      <span
        >{{
          (
            userInfoData.commission.commission_rates.max_change_rate * 100
          ).toFixed(2)
        }}%</span
      >
    </div>
    <!-- <div class="info-list-text">
      <ul>
        <li>
          <div>Self-Rewards:</div>
          <span>34855</span>
        </li>
        <li>
          <div>Redistribution-Rewards:</div>
          <span>1100709.61</span>
        </li>
      </ul>
    </div>
    -->
    <div class="info-list-text">
      <ul>
        <li>
          <div>Max Change Rate Everytime:</div>
          <span>1%</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  props: ["userInfoData", "coinName"],
  data() {
    return {};
  },
  components: {},
};
</script>

<style scoped lang="less">
@import url("../../../css/index.less");
.info-title,
.info-list-text ul li {
  display: flex;
}
.info-title {
  font-size: 14 / @rem;
  padding: 10 / @rem 0px;
  margin: 0px 15 / @rem;
  span {
    margin-left: 58 / @rem;
    color: #6e7398;
  }
}
.info-list-text ul {
  padding: 5 / @rem 0px;
  margin: 10 / @rem 15 / @rem;
  border-top: 1px dashed #b7b7b7;
  border-bottom: 1px dashed #b7b7b7;
}
.info-list-text ul li {
  color: #a28bde;
  line-height: 30 / @rem;
}
</style>
