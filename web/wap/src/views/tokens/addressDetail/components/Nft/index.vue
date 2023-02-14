<template>
  <div>
    <div>
      <div class="nft-card" v-for="(item, key) in tableData" :key="key">
        <div class="nft-card__card">
          <van-row class="nft-card__row">
            <van-col span="6" class="nft-card__row--label">NFT ID:</van-col>
            <van-col span="18" class="nft-card__row--value">
              <router-link
                :to="{ path: `/nft/nft-list/detail/${item.tokenId}` }"
              >
                {{ (item && item.tokenId) || "" }}</router-link
              >
            </van-col>
          </van-row>
          <van-row class="nft-card__row">
            <van-col span="6" class="nft-card__row--label">Owner:</van-col>
            <van-col span="18" class="nft-card__row--value">
              {{ (item && item.owner) || "" }}
            </van-col>
          </van-row>
          <van-row class="nft-card__row">
            <van-col span="6" class="nft-card__row--label">Creator:</van-col>
            <van-col span="18" class="nft-card__row--value">
              {{ (item && item.creator) || "" }}
            </van-col>
          </van-row>
          <van-row class="nft-card__row">
            <van-col span="6" class="nft-card__row--label">NFT Name:</van-col>
            <van-col span="18" class="nft-card__row--value">
              {{ (item && item.name) || "" }}
              <!-- <router-link :to="{ path: '' }" @click="toIpfs(item.tokenUri)"
            >{{ (item && item.tokenUri) || "" }}
          </router-link> -->
            </van-col>
          </van-row>
        </div>
      </div>
    </div>
    <div v-if="tableData.length <= 0" class="nft-nodata">
      <div>
        <img src="@/assets/nodate.png" />
      </div>
      <div>No Data</div>
    </div>
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
import { NftApi } from "@/api";
import _ from "lodash";
export default {
  components: {},
  mixins: [mixinCommon],
  props: {
    item: { type: Object, default: () => {} },
  },
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
    };
  },
  methods: {
    //滚动时间
    handleScroll(e) {
      console.log(e, "e===");
      var scrollTop =
        e.target.documentElement.scrollTop || e.target.body.scrollTop;
      const scrollBottm =
        e.target.documentElement.scrollHeight -
        e.target.documentElement.clientHeight -
        scrollTop;
      const totalPage = Math.ceil(this.total / this.pageSize);
      if (this.pageNum < totalPage && scrollBottm < 150) {
        this.onDebounceList();
      }
    },
    //防抖
    onDebounceList() {
      this.pageNum = this.pageNum + 1;
      this.find();
    },
    //获取一个address的nft相关交易
    find() {
      const obj = {
        // address: this.$route.params.id,
        address: this.$route.params.address,
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
      };
      NftApi.getNftList(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.tableData = this.tableData.concat(result.records);
          this.total = result.total;
        }
      });
    },
  },
  mounted() {
    this.find();
    this.onDebounceList = _.debounce(this.onDebounceList, 1000);
  },
  created() {},
};
</script>

<style scoped lang="less">
.nft-card {
  background-color: #f8f8f8;
  &__card {
    padding: 15px;
    margin-top: 15px;
    background-color: #fff;
  }
  &__row {
    padding: 5px 0;
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
  &__nodata {
    padding-top: 60px;

    > div {
      color: rgba(0, 0, 0, 0.5);
      text-align: center;
    }
  }
}
.nft-nodata {
  padding-top: 160px;
  background-color: #fff;

  > div {
    margin: 10px 0;
    color: rgba(0, 0, 0, 0.5);
    text-align: center;
  }
}
</style>
