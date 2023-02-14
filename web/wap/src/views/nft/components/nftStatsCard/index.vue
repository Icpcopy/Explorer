<template>
  <div class="nftstats-card">
    <van-row class="nftstats-card__row">
      <van-col span="8" class="nftstats-card__row--label">Ranking:</van-col>
      <van-col span="16" class="nftstats-card__row--value">
        {{ (item && item.ranking) || "" }}
      </van-col>
    </van-row>
    <van-row class="nftstats-card__row">
      <van-col span="8" class="nftstats-card__row--label"
        >Wallet Address:</van-col
      >
      <van-col span="16" class="nftstats-card__row--value">
        <!-- <router-link :to="{ path: `/Accountdetails/${item.walletAddress}` }"> -->
        <router-link
          :to="{ path: `/Accountdetails/${item.walletAddress}` }"
        >
          {{ (item && item.walletAddress) || "" }}</router-link
        >
      </van-col>
    </van-row>
    <van-row class="nftstats-card__row">
      <van-col span="8" class="nftstats-card__row--label">Category:</van-col>
      <van-col span="16" class="nftstats-card__row--value">
        {{ (item && item.category) || "" }}
      </van-col>
    </van-row>
    <van-row class="nftstats-card__row">
      <van-col span="8" class="nftstats-card__row--label"
        >Total Quantity:</van-col
      >
      <van-col span="16" class="nftstats-card__row--value">
        {{ (item && item.totalQuantity) || "" }}
      </van-col>
    </van-row>
    <van-row class="nftstats-card__row">
      <van-col span="8" class="nftstats-card__row--label"
        >Total Value(ICT):</van-col
      >
      <van-col span="16" class="nftstats-card__row--value">
        {{ (item && item.totalValue) || "" }}</van-col
      >
    </van-row>
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
export default {
  components: {},
  mixins: [mixinCommon],
  props: {
    item: { type: Object, default: () => {} },
  },
  data() {
    return {};
  },
  methods: {},
  mounted() {},
  created() {},
};
</script>

<style scoped lang="less">
.nftstats-card {
  &__row {
    &--label {
      font-size: 14px;
      line-height: 30px;
      font-weight: 600;
    }
    &--value {
      word-break: break-all;
      line-height: 30px;
      > a {
        color: #561bf8;
      }
    }
    .time {
      color: #9196c1;
    }
  }
}
</style>
