<template>
  <div class="nftlist-card">
    <van-row class="nftlist-card__row">
      <van-col span="6" class="nftlist-card__row--label">NFT ID:</van-col>
      <van-col span="18" class="nftlist-card__row--value">
        <router-link :to="{ path: `/nft/nft-list/detail/${item.tokenId}` }">
          {{ (item && item.tokenId) || "" }}</router-link
        >
        <img
          class="nftlist-card__row--img0"
          v-if="item.tokenId"
          src="../../../../assets/fuzhi.png"
          @click="onCopy(item.tokenId, 0)"
        />
      </van-col>
    </van-row>
    <van-row class="nftlist-card__row">
      <van-col span="6" class="nftlist-card__row--label">Owner:</van-col>

      <van-col span="18" class="nftlist-card__row--value">
        <!-- <router-link :to="{ path: `/Accountdetails/${item.owner}` }"> -->
        <router-link :to="{ path: `/Accountdetails/${item.owner}` }">
          {{ (item && item.owner) || "" }}</router-link
        >
        <img
          class="nftlist-card__row--img1"
          v-if="item.owner"
          src="../../../../assets/fuzhi.png"
          @click="onCopy(item.owner, 1)"
        />
      </van-col>
    </van-row>
    <van-row class="nftlist-card__row">
      <van-col span="6" class="nftlist-card__row--label">NFT Name:</van-col>
      <van-col span="18" class="nftlist-card__row--value">
        <!-- <router-link :to="{ path: '' }" @click="toIpfs(item.tokenUri)"
          >{{ (item && item.tokenUri) || "" }}
        </router-link> -->
        {{ (item && item.name) || "" }}
        <!-- <img
          class="nftlist-card__row--img2"
          v-if="item.tokenUri"
          src="../../../../assets/fuzhi.png"
          @click="onCopy(item.tokenUri, 2)"
        /> -->
      </van-col>
    </van-row>
    <van-row class="nftlist-card__row">
      <van-col span="6" class="nftlist-card__row--label">Time:</van-col>
      <van-col span="18" class="nftlist-card__row--value time">
        <!-- {{
          (item &&
            item.timestamp &&
            $dayjs(item.timestamp).format("YYYY-MM-DD HH:mm:ss")) ||
          ""
        }} -->
        {{
          `${(item && item.timestamp && $dayjs.utc(item.timestamp)).format(
            "YYYY-MM-DD HH:mm:ss"
          )} UTC` || ""
        }}
      </van-col>
    </van-row>
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
import Clipboard from "clipboard";
import { Toast } from "vant";
export default {
  components: {},
  mixins: [mixinCommon],
  props: {
    item: { type: Object, default: () => {} },
  },
  data() {
    return {
      clipboard: null,
    };
  },
  methods: {
    onCopy(text, index) {
      this.clipboard = new Clipboard(`.nftlist-card__row--img${index}`, {
        // 点击copy按钮，直接通过text直接返回复印的内容
        text: function () {
          return text;
        },
      });
      this.clipboard.on("success", (e) => {
        // this.$message.success(e.text + " 已复制到剪贴板！");
        Toast.success("copy successfully");
        this.clipboard.destroy();
      });

      this.clipboard.on("error", function (e) {
        console.log(e);
      });
    },
  },
  mounted() {},
  created() {},
};
</script>

<style scoped lang="less">
.nftlist-card {
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
    &--img0 {
      width: 16px;
      height: 17px;
      margin: 4px 0 0 10px;
    }
    &--img1 {
      width: 16px;
      height: 17px;
      margin: 4px 0 0 10px;
    }
    &--img2 {
      width: 16px;
      height: 17px;
      margin: 4px 0 0 10px;
    }
    .time {
      color: #9196c1;
    }
  }
}
</style>
