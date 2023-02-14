<template>
  <div class="nftdetailtxs-card">
    <van-row class="nftdetailtxs-card__row">
      <van-col span="6" class="nftdetailtxs-card__row--label">TXHash:</van-col>
      <van-col span="18" class="nftdetailtxs-card__row--value">
        <router-link :to="{ path: `/tradinginfo/${item.txhash}` }">
          {{ (item && item.txhash) || "" }}</router-link
        >
      </van-col>
    </van-row>
    <van-row class="nftdetailtxs-card__row">
      <van-col span="6" class="nftdetailtxs-card__row--label">Type:</van-col>
      <van-col span="18" class="nftdetailtxs-card__row--value">
        {{ (item && item.event[0] && item.event[0].type) || "" }}
      </van-col>
    </van-row>
    <van-row class="nftdetailtxs-card__row">
      <van-col span="6" class="nftdetailtxs-card__row--label">Amount:</van-col>
      <van-col span="18" class="nftdetailtxs-card__row--value time">
        <router-link :to="{ path: `/tradinginfo/${item.txhash}` }">
          More +
        </router-link>
      </van-col>
    </van-row>
    <van-row class="nftdetailtxs-card__row">
      <van-col span="6" class="nftdetailtxs-card__row--label">From:</van-col>
      <van-col span="18" class="nftdetailtxs-card__row--value time">
        <router-link :to="{ path: `/tradinginfo/${item.txhash}` }">
          More +
        </router-link>
      </van-col>
    </van-row>
    <van-row class="nftdetailtxs-card__row">
      <van-col span="6" class="nftdetailtxs-card__row--label">To:</van-col>
      <van-col span="18" class="nftdetailtxs-card__row--value time">
        <router-link :to="{ path: `/tradinginfo/${item.txhash}` }">
          More +
        </router-link>
      </van-col>
    </van-row>
    <van-row class="nftdetailtxs-card__row">
      <van-col span="6" class="nftdetailtxs-card__row--label">Block:</van-col>
      <van-col span="18" class="nftdetailtxs-card__row--value time">
        <router-link :to="{ path: `/BlockchainDetails/${item.height}` }">
          {{ (item && item.height) || "" }}
        </router-link>
      </van-col>
    </van-row>
    <van-row class="nftdetailtxs-card__row">
      <van-col span="6" class="nftdetailtxs-card__row--label">Fee: </van-col>
      <van-col span="18" class="nftdetailtxs-card__row--value">
        {{ (item && item.fee && onFeixedNum(item.fee, 6)) || "--" }}
        {{ item && item.feeCoinName }}
      </van-col>
    </van-row>
    <van-row class="nftdetailtxs-card__row">
      <van-col span="6" class="nftdetailtxs-card__row--label">Time: </van-col>
      <van-col span="18" class="nftdetailtxs-card__row--value time">
        <!-- {{
          (item &&
            item.timestamp &&
            $dayjs(item.timestamp).format("YYYY-MM-DD HH:mm:ss")) ||
            "--"
        }} -->
        {{
          `${(item && item.timestamp && $dayjs.utc(item.timestamp)).format(
            "YYYY-MM-DD HH:mm:ss"
          )} UTC` || "--"
        }}
      </van-col>
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
.nftdetailtxs-card {
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
