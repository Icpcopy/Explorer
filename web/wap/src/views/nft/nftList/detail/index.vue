<template>
  <div class="nft-detail">
    <div class="nft-detail__top">
      <span class="nft-detail__top--title">NFT Details</span>
      <span class="nft-detail__top--button" @click="goBack">return</span>
    </div>
    <div class="nft-detail__card">
      <nft-detail-card :item="defaultObj" />
    </div>
    <div class="nft-detail__top">
      <span class="nft-detail__top--title">Txs</span>
      <span></span>
    </div>
    <div class="nft-detail__card" v-for="(item, key) in tableData" :key="key">
      <nft-detail-txs-card :item="item" />
    </div>
  </div>
</template>

<script>
import { NftDetailCard, NftDetailTxsCard } from "../../components";
import { NftApi } from "@/api";
import { mixinCommon } from "@/mixin";
import _ from "lodash";
export default {
  components: { NftDetailCard, NftDetailTxsCard },
  mixins: [mixinCommon],
  data() {
    return {
      total: 0,
      pageNum: 1,
      pageSize: 10,
      inputValue: "",
      tableData: [],
      defaultObj: null,
    };
  },
  methods: {
    //滚动时间
    handleScroll(e) {
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
      this.getTransactionList();
    },
    //返回
    goBack() {
      window.history.back();
    },
    //获取详细信息
    getDetail() {
      const obj = {
        tokenId: this.$route.params.id,
      };
      NftApi.getNftDetail(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          if (result) {
            this.defaultObj = result;
          }
        }
      });
    },
    //获取该nft交易信息
    getTransactionList() {
      const obj = {
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
        tokenId: this.$route.params.id,
      };
      NftApi.getNFTTxByTokenId(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.tableData = this.tableData.concat(result.records);
          this.total = result.total;
        }
      });
    },
  },
  mounted() {
    this.getDetail();
    this.getTransactionList();
    this.onDebounceList = _.debounce(this.onDebounceList, 1000);
  },
  created() {},
};
</script>

<style scoped lang="less">
.nft-detail {
  background-color: #f8f8f8;
  min-height: calc(100vh - 124px);
  &__top {
    padding: 0 15px;
    display: flex;
    justify-content: space-between;
    &--title {
      font-size: 16px;
      font-weight: 600;
      line-height: 20px;
      margin: 23px 0;
    }
    &--button {
      width: 68px;
      height: 40px;
      background-color: #633af9;
      border-radius: 5px;
      font-size: 14px;
      text-align: center;
      line-height: 40px;
      color: #fff;
      margin-top: 13px;
    }
  }
  &__card {
    padding: 10px 15px;
    margin-bottom: 10px;
    background-color: #fff;
  }
}
</style>
