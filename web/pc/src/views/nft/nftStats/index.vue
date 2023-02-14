<template>
  <div class="nft-stats">
    <div class="nft-stats__charts">
      <div class="nft-stats__charts--title">
        Daily trading volume
        <!-- and price trends -->
      </div>
      <div class="nft-stats__charts--content">
        <div class="nscc-date">
          <NftDateRange @onChangeTime="onChangeTime" />
        </div>
        <!-- <div class="nscc-charts"> -->
        <nft-stats-charts :dateList="dateList" :dateDataList="dateDataList" />
        <!-- </div> -->
      </div>
    </div>
    <div class="nft-stats__content">
      <div class="nft-stats__content--title">
        <div class="nsct-title">NFT project ranking</div>
        <div class="nsct-select">
          Category:
          <el-select
            v-model="cateId"
            placeholder="Select"
            @change="onChangeSelect"
          >
            <el-option
              v-for="item in productTypeList"
              :key="item.value"
              :label="item.name"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="nft-stats__table">
        <el-table
          v-loading="loading"
          ref="multipleTable"
          :data="tableData"
          tooltip-effect="dark"
          stripe
          header-cell-class-name="nft-stats__table--header"
          @sort-change="onSortChange"
        >
          <el-table-column
            v-for="(item, key) in columns"
            :key="key"
            :fixed="item.fixed"
            :prop="item.prop"
            :label="item.label"
            :width="item.width"
            :sortable="item.sortable"
          >
            <template #default="{ row }">
              <span v-if="item.prop == 'walletAddress'" class="nft-sell__intro">
                <router-link
                  :to="{ path: `/Accountdetails/${row[item.prop]}` }"
                  >{{ row[item.prop] || "--" }}</router-link
                >
              </span>
              <span v-else class="nft-sell__intro">{{
                item.prop !== "totalValue"
                  ? row[item.prop] || "--"
                  : (row[item.prop] && onFeixedNum(row[item.prop], 6)) || 0
              }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <!-- <el-pagination
      background
      class="nft-sell__footer"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination> -->
    <Pagination
      :total="total"
      :pageSize="pageSize"
      :pageIndex="pageNum"
      @currpageChange="handleCurrentChange"
    ></Pagination>
  </div>
</template>

<script>
import { NftStatsCharts, NftDateRange } from "@/components";
import Pagination from "@/components/Pagination";
import { NftApi } from "@/api";
import { mixinCommon } from "@/mixin";
// import { mapState, mapActions } from "vuex";
export default {
  mixins: [mixinCommon],
  components: { NftStatsCharts, Pagination, NftDateRange },
  name: "NftIndex",
  data() {
    const columns = [
      {
        prop: "ranking",
        width: 150,
        label: "Ranking",
      },
      {
        prop: "walletAddress",
        width: 420,
        label: "Wallet Address",
      },
      {
        prop: "category",
        label: "Category",
      },
      {
        prop: "totalQuantity",
        label: "Total Quantity",
        sortable: true,
      },
      {
        prop: "totalValue",
        label: "Total Value(ICT)",
        sortable: true,
      },
    ];
    const orderByEunm = {
      totalQuantity: {
        name: "totalQuantity",
        value: "0",
      },
      totalValue: { name: "totalValue", value: "1" },
    };
    const sortByEunm = {
      ascending: { name: "ascending", value: "1" },
      descending: { name: "ascending", value: "0" },
    };

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
      columns,
      tableData: [],
      options: [],
      dateList: [],
      dateDataList: [],
      cateId: "",
      sort: "desc",
      orderBy: "",
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      orderByEunm,
      sortByEunm,
      productTypeList: [{ name: "All", value: "" }].concat(
        Object.values(productTypeEunm)
      ),
    };
  },
  computed: {},
  watch: {},
  methods: {
    //变化排序
    onSortChange(e) {
      this.sort = e.order;
      this.orderBy = e.prop;
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
        isSort:
          (this.sortByEunm[this.sort] && this.sortByEunm[this.sort].value) ||
          "",
        isNumSort:
          (this.orderByEunm[this.orderBy] &&
            this.orderByEunm[this.orderBy].value) ||
          "",
      };
      NftApi.getNFTStatsList(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          // this.tableData =
          this.tableData = result.records.map((el, index) => {
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
          });
          this.total = result.total;
        }
      });
    },
    //分页管理
    handleSizeChange(e) {
      this.pageNum = 1;
      this.pageSize = e;
      this.find();
    },
    handleCurrentChange(e) {
      this.pageNum = e;
      this.find();
    },
  },
  mounted() {
    this.find();
  },
};
</script>
<style lang="less" scoped>
.nft-stats {
  width: 100%;
  padding: 20px;
  * a {
    color: #633af9;
    text-decoration: none;
  }
  &__charts {
    width: 100%;
    &--title {
      font-size: 24px;
      line-height: 24px;
      color: #2a2a2a;
      margin: 30px 0 24px 0;
      font-weight: 600;
    }
    &--content {
      height: 430px;
      background: #fff;
      border-radius: 5px;
      padding: 20px 20px 40px 20px;
      position: relative;
      box-shadow: 0px 3px 8px rgba(56, 54, 54, 0.06);
      .nscc-date {
        position: absolute;
        top: 20px;
        right: 55px;
        z-index: 100;
      }
    }
  }
  &__content {
    &--title {
      display: flex;
      justify-content: space-between;
      padding: 48px 0 20px 0;
      .nsct-title {
        font-size: 24px;
        line-height: 24px;
        color: #2a2a2a;
        font-weight: 600;
      }
    }
  }
  &__table {
    box-shadow: 0px 3px 8px rgba(56, 54, 54, 0.06);
    /deep/ &--header {
      color: #fff;
      font-weight: 400;
      background-color: #5740a9;
    }
  }
}
</style>
