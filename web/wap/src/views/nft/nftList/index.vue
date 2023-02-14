<template>
  <div class="nft-list">
    <div class="nft-list__top">
      <div class="nft-list__top--title">{{ total }} NFTs</div>
      <van-field
        class="nft-list__top--input"
        v-model="inputValue"
        placeholder="Search by NFT Id"
        clearable
      />
      <van-button
        class="nft-list__top--button"
        icon="search"
        type="primary"
        @click="onSearch"
      />
    </div>
    <div class="nft-list__card" v-for="(item, key) in tableData" :key="key">
      <nft-list-card :item="item" />
    </div>
    <div v-if="tableData.length <= 0" class="nft-list__nodata">
      <div>
        <img src="@/assets/nodate.png" />
      </div>
      <div>No Data</div>
    </div>
  </div>
</template>

<script>
import { NftListCard } from "../components";
import { NftApi } from "@/api";
import { mixinCommon } from "@/mixin";
import _ from "lodash";
export default {
  components: { NftListCard },
  mixins: [mixinCommon],
  data() {
    return {
      total: 0,
      pageNum: 1,
      pageSize: 10,
      inputValue: "",
      tableData: [],
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
      this.find();
    },
    //搜索
    onSearch() {
      //输入框有内容
      if (this.inputValue) {
        this.getNftDetail();
      } else {
        //输入框无内容
        this.pageNum = 1;
        this.pageSize = 10;
        this.find();
      }
    },
    //通过id获取nft 详情
    getNftDetail() {
      const obj = {
        tokenId: this.inputValue,
      };
      NftApi.getNftDetail(obj).then((res) => {
        console.log(res, "res===");
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          if (result) {
            this.tableData = [result];
            this.total = 1;
          } else {
            this.tableData = [];
            this.total = 0;
          }
        }
      });
    },
    //列表
    find() {
      const obj = {
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
    this.onDebounceList = _.debounce(this.onDebounceList, 1000);
    this.find();
  },
  created() {},
};
</script>

<style scoped lang="less">
.nft-list {
  background-color: #f8f8f8;
  min-height: calc(100vh - 124px);
  &__top {
    padding: 20px 15px;
    &--title {
      margin: 8px 0 15px 0;
      font-size: 16px;
      font-weight: 600;
      line-height: 20px;
    }
    &--button {
      width: 100%;
      background-color: #633af9;
      border: 0;
      border-radius: 5px;
      margin-top: 15px;
    }
  }
  &__card {
    padding: 10px 15px;
    margin-bottom: 10px;
    background-color: #fff;
  }
  &__nodata {
    padding-top: 60px;

    > div {
      color: rgba(0, 0, 0, 0.5);
      text-align: center;
      margin: 10px 0;
    }
  }
}
</style>
