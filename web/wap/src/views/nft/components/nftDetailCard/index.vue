<template>
  <div class="nftdetail-card">
    <van-row class="nftdetail-card__row">
      <van-col span="6" class="nftdetail-card__row--label">Owner:</van-col>
      <van-col span="18" class="nftdetail-card__row--value">
        <!-- <router-link
          :to="{ path: `/Accountdetails/${(item && item.owner) || ''}` }"
        >
          {{ (item && item.owner) || "" }}</router-link
        > -->
        <router-link
          :to="{ path: `/Accountdetails/${(item && item.owner) || ''}` }"
        >
          {{ (item && item.owner) || "" }}</router-link
        >
      </van-col>
    </van-row>
    <van-row class="nftdetail-card__row">
      <van-col span="6" class="nftdetail-card__row--label">NFT ID:</van-col>
      <van-col span="18" class="nftdetail-card__row--value">
        <!-- <router-link :to="{ path: `/nft/nft-list/detail/${item.tokenId}` }"> -->
        {{ (item && item.tokenId) || "" }}
        <!-- </router-link> -->
      </van-col>
    </van-row>
    <van-row class="nftdetail-card__row">
      <van-col span="6" class="nftdetail-card__row--label">Creator:</van-col>
      <van-col span="18" class="nftdetail-card__row--value time">
        <!-- <router-link
          :to="{ path: `/Accountdetails/${(item && item.creator) || ''}` }"
        >
          {{ (item && item.creator) || "" }}
        </router-link> -->
        <router-link
          :to="{
            path: `/Accountdetails/${(item && item.creator) || ''}`,
          }"
        >
          {{ (item && item.creator) || "" }}
        </router-link>
      </van-col>
    </van-row>
    <van-row class="nftdetail-card__row">
      <van-col span="6" class="nftdetail-card__row--label">NFT Name:</van-col>
      <van-col span="18" class="nftdetail-card__row--value">
        {{ (item && item.name) || "" }}
        <!-- <router-link :to="{ path: '' }" @click="toIpfs(item.tokenUri)"
          >{{ (item && item.tokenUri) || "" }}
        </router-link> -->
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
.nftdetail-card {
  &__row {
    border-bottom: 1px solid #ededed;
    padding: 20px 0;
    &--label {
      font-size: 14px;
      line-height: 20px;
      font-weight: 600;
    }
    &--value {
      word-break: break-all;
      line-height: 20px;
      > a {
        color: #561bf8;
      }
    }
    .time {
      color: #9196c1;
    }
  }
  &__row:last-child {
    border-bottom: 0;
  }
}
</style>
