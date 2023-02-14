<template>
  <div class="nft-stats">
    <div class="nft-stats__title">Daily trading volume and price trends</div>
    <div class="nft-stats__chartBox">
      <nft-stats-date-range @onChangeTime="onChangeTime" />
      <div class="nft-stats__chartBox--chart">
        <nft-stats-chart :dateList="dateList" :dateDataList="dateDataList" />
      </div>
    </div>
    <div class="nft-stats__title">NFT project ranking</div>
    <div>
      <div class="nft-stats__Category">
        Category:
        <div
          class="nft-stats__Category--select"
          @click="showActionSheet = true"
        >
          {{ initCategory }} <van-icon name="arrow-down" />
        </div>
      </div>
      <van-action-sheet
        v-model:show="showActionSheet"
        :actions="productTypeList"
        cancel-text="Cancel"
        close-on-click-action
        @cancel="onCancelActionSheet"
        @select="onSelect"
      />
    </div>
    <div class="nft-stats__card" v-for="(item, key) in tableData" :key="key">
      <nft-stats-card :item="item" />
    </div>
    <div v-if="tableData.length <= 0" class="nft-stats__nodata">
      <div>
        <img src="@/assets/nodate.png" />
      </div>
      <div>No Data</div>
    </div>
  </div>
</template>

<script>
import { NftStatsChart, NftStatsCard, NftStatsDateRange } from "../components";

import { NftApi } from "@/api";
import { mixinCommon } from "@/mixin";
import _ from "lodash";
export default {
  components: { NftStatsChart, NftStatsCard, NftStatsDateRange },
  mixins: [mixinCommon],
  data() {
    const productTypeEunm = {
      "001": {
        name: "Virtual merchandise", //"Ticketing",
        value: "001",
      },
      // "002": {
      //   name: "Collection", // "Collection",
      //   value: "002",
      // },
      "002": {
        name: "Metaverse", // "Art",
        value: "002",
      },
      "003": {
        name: "Game", // "Light Luxury",
        value: "003",
      },
    };
    return {
      tableData: [],
      options: [],
      dateList: [],
      dateDataList: [],
      cateId: "",
      pageNum: 1,
      pageSize: 10,
      total: 0,
      productTypeList: [{ name: "All", value: "" }].concat(
        Object.values(productTypeEunm)
      ),
      showActionSheet: false,
    };
  },
  computed: {
    initCategory() {
      if (this.cateId) {
        return this.productTypeList.filter((el) => el.value == this.cateId)[0]
          .name;
      } else {
        return "All";
      }
    },
  },
  watch: {},
  methods: {
    onSelect(item) {
      // 默认情况下点击选项时不会自动收起
      // 可以通过 close-on-click-action 属性开启自动收起
      this.cateId = item.value;
      this.pageNum = 1;
      this.pageSize = 10;
      this.tableData = [];
      this.find();
      this.showActionSheet = false;
    },
    onCancelActionSheet() {
      this.showActionSheet = false;
    },
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
    //变化分类筛选列表
    onChangeSelect() {
      this.pageNum = 1;
      this.pageSize = 10;
      this.find();
    },
    //切换时间更新图表
    onChangeTime(startTime, endTime) {
      const obj = {
        beginDate: this.$dayjs(startTime).format("YYYY-MM-DD"),
        endDate: this.$dayjs(endTime).format("YYYY-MM-DD"),
      };
      NftApi.getDealList(obj).then((res) => {
        console.log(res, "res===");
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          if (result) {
            this.dateList = result.map((el) => {
              return el.date;
            });
            this.dateDataList = result.map((el) => {
              return el.dealAmount;
            });
          }
        }
      });
    },
    //获取nft价值排行
    find() {
      const obj = {
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
        cateId: this.cateId,
      };
      NftApi.getNFTStatsList(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          // this.tableData =
          this.tableData = this.tableData.concat(
            result.records.map((el, index) => {
              return {
                ...el,
                ranking:
                  index < this.pageSize - 1
                    ? `${
                        !((this.pageNum - 1) * this.pageSize)
                          ? index + 1
                          : Number((this.pageNum - 1) * this.pageSize) +
                            Number(index + 1)
                      }`
                    : `${this.pageNum * (this.pageSize / 10)}0`,
              };
            })
          );
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
.nft-stats {
  background-color: #f8f8f8;
  min-height: calc(100vh - 124px);
  padding-bottom: 20px;
  &__title {
    padding: 27px 16px 22px 16px;
    font-size: 16px;
    font-weight: 600;
    color: #2a2a2a;
  }
  &__chartBox {
    background-color: #fff;
    padding: 10px;
    &--chart {
      margin-top: 20px;
      width: calc(100vw - 30px);
      height: 360px;
    }
  }
  &__Category {
    display: flex;
    line-height: 40px;
    padding: 0 20px;
    margin-bottom: 10px;
    &--select {
      margin-left: 10px;
      width: 172px;
      height: 40px;
      background: #ffffff;
      border-radius: 5px;
      border: 1px solid #ededed;
      display: flex;
      padding: 0 10px;
      justify-content: space-between;
      > i {
        margin-top: 13px;
      }
    }
  }
  &__card {
    padding: 10px 15px;
    margin-bottom: 10px;
    background-color: #fff;
  }
  &__nodata {
    padding: 60px 0 40px 0;

    > div {
      color: rgba(0, 0, 0, 0.5);
      text-align: center;
      margin: 10px 0;
    }
  }
}
</style>
