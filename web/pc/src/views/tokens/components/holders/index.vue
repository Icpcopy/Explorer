<template>
  <div class="icp-holders">
    <el-row class="icp-holders__row">
      <el-col :span="12" style="display: flex">
        <div class="icp-holders__row--label">Token Total Supply：</div>
        <div class="icp-holders__row--value">
          {{
            (defaultValue &&
              defaultValue.totalSupply &&
              onFeixedNum(defaultValue.totalSupply, 6)) ||
            0
          }}
        </div>
      </el-col>
      <el-col :span="12" style="display: flex">
        <div class="icp-holders__row--label">Total Token Holders：</div>
        <div class="icp-holders__row--value">
          {{
            (defaultValue &&
              defaultValue.holderCount &&
              onFeixedNum(defaultValue.holderCount, 6)) ||
            0
          }}
        </div>
      </el-col>
    </el-row>
    <el-divider></el-divider>
    <div class="icp-holders__detailrow">
      <div class="icp-holders__detailrow--title">
        Asset Breakdown by Holders
      </div>
      <div style="display: flex">
        <div
          class="icp-holders__detailrow--category"
          v-for="(item, key) in proportionEunm"
          :key="key"
        >
          <span
            class="ihdc-color"
            :style="{ 'background-color': item.color }"
          ></span>
          <span class="ihdc-title">{{ item.label }}</span>
          <span class="ihdc-value">{{ item.value }}</span>
        </div>
      </div>
      <div class="icp-holders__detailrow--progress">
        <span
          :style="{
            'background-color': proportionEunm[1].color,
            width: proportionEunm[1].value,
          }"
        ></span>
        <span
          :style="{
            'background-color': proportionEunm[2].color,
            width: proportionEunm[2].value,
          }"
        ></span>
        <span
          :style="{
            'background-color': proportionEunm[3].color,
            width: proportionEunm[3].value,
          }"
        ></span>
        <span
          :style="{
            'background-color': proportionEunm[4].color,
            width: proportionEunm[4].value,
          }"
        ></span>
        <span
          :style="{
            'background-color': proportionEunm[5].color,
            width: proportionEunm[5].value,
          }"
        ></span>
        <span
          :style="{
            'background-color': proportionEunm[6].color,
            width: proportionEunm[6].value,
          }"
        ></span>
      </div>
    </div>
    <el-divider></el-divider>
    <div class="icp-holders__tableheader">
      <div class="icp-holders__tableheader--left">Top 100 holders</div>
      <el-button
        class="icp-holders__tableheader--right"
        type="info"
        :loading="updateLoading"
        icon="el-icon-refresh"
        @click="onUpdate"
      ></el-button>
    </div>
    <div>
      <el-table
        v-loading="loading"
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        stripe
        header-cell-class-name="icp-holders__table--header"
      >
        <el-table-column
          v-for="(item, key) in listCloumn"
          :key="key"
          :fixed="item.fixed"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
        >
          <template #header v-if="item.prop == 'balance'">
            <span
              >{{ item.label
              }}{{
                (defauleValue &&
                  defauleValue["tokenSymbol"] &&
                  `(${defauleValue["tokenSymbol"]})`) ||
                ""
              }}</span
            >
          </template>
          <template #default="{ row }">
            <span v-if="item.prop == 'address'">
              <el-tooltip
                class="item"
                effect="dark"
                placement="top"
                :content="row[item.prop]"
              >
                <router-link
                  class="icp-holders__link"
                  :to="{ path: `/Accountdetails/${row[item.prop]}` }"
                >
                  {{
                    (row[item.prop] &&
                      ` ${row[item.prop].substring(0, 7)}...${row[
                        item.prop
                      ].substring(
                        row[item.prop].length - 7,
                        row[item.prop].length
                      )}`) ||
                    ""
                  }}
                </router-link>
              </el-tooltip>
            </span>
            <span v-if="item.prop == 'balance'">
              {{
                (row[item.prop] && onFeixedNum(row[item.prop], 6)) || "0"
              }}</span
            >
            <span v-if="item.prop == 'percentage'">
              {{
                (defaultValue &&
                  defaultValue.circulation &&
                  onFeixedNum(
                    (row["balance"] * 100) / defaultValue.circulation,
                    2
                  )) ||
                "0"
              }}
              %</span
            >
            <div
              v-if="
                item.prop !== 'address' &&
                item.prop !== 'balance' &&
                item.prop !== 'percentage'
              "
            >
              {{ row[item.prop] || "--" }}
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- <Pagination
      :total="total"
      :pageSize="pageSize"
      :pageIndex="pageNum"
      @currpageChange="handleCurrentChange"
    ></Pagination> -->
  </div>
</template>

<script>
// import Pagination from "@/components/Pagination";
import { Tokens } from "@/api";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  components: {}, //Pagination
  name: "ListCard",
  props: {
    ListCloumn: {
      type: Array,
      default: () => [],
    },
    loading: { type: Boolean, default: false },
    title: { type: String, default: "" },
    defauleValue: { type: Object, default: () => {} },
  },
  data() {
    return {
      proportionEunm: {
        1: { label: "NO.1~10:", value: "0%", color: "#3E91FF" },
        2: { label: "NO.11~50:", value: "0%", color: "#83C958" },
        3: { label: "NO.51~100:", value: "0%", color: "#7F5FE7" },
        4: { label: "NO.101~500:", value: "0%", color: "#F08856" },
        5: { label: "NO.501~1000:", value: "0%", color: "#F2D015" },
        6: { label: "NO.1000~:", value: "0%", color: "#BF5ECB" },
      },
      listCloumn: [
        { label: "#", prop: "serailNo" },
        { label: "Address", prop: "address" },
        { label: "Quantity", prop: "balance" },
        { label: "Percentage", prop: "percentage" },
      ],
      tableData: [],
      total: 0,
      contractAddress: "",
      defaultValue: "",
      updateLoading: false,
      type: "",
    };
  },
  watch: {},
  methods: {
    handleCurrentChange() {},
    //更新
    onUpdate() {
      this.getAssteDistribution();
    },
    //获取持有者详情
    getAssteDistribution() {
      const obj = {
        contractAddress: this.contractAddress,
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
      };
      this.updateLoading = true;
      Tokens.getAssteDistribution(obj)
        .then((res) => {
          this.updateLoading = false;
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.defaultValue = result;
            result.assteDistribution.map((el, index) => {
              this.proportionEunm[index + 1].label = `NO.${el.startIndex} ~${
                el.endIndex || ""
              }`;
              this.proportionEunm[index + 1].value = ` ${
                (el.totalAmount &&
                  this.onFeixedNum(
                    (el.totalAmount / result.circulation) * 100,
                    2
                  )) ||
                0
              }%`;
              return el;
            });
            this.tableData =
              result &&
              result.topList &&
              result.topList.map((el, index) => {
                return {
                  ...el,
                  serailNo: index + 1,
                };
              });

            // this.total = result.total;
          }
        })
        .catch(() => {
          this.updateLoading = false;
        });
    },
  },
  mounted() {
    this.contractAddress = this.$route.params.id;
    this.type = this.$route.query.type;
    if (this.type) {
      this.listCloumn = this.listCloumn.map((el) => {
        if (el.prop == "balance") {
          return { label: "Quantity", prop: "balance" };
        } else {
          return { ...el };
        }
      });
    }
    // console.log(this.type,'type===')
    this.getAssteDistribution();
  },
};
</script>
<style lang="less" scoped>
.icp-holders {
  &__table {
    /deep/ &--header {
      color: #fff;
      font-weight: 400;
      background-color: #5740a9;
    }
  }
  &__link {
    text-decoration: none;
    color: #5740a9;
  }
  &__row {
    padding: 10px 0 0 0;
    &--label {
      color: #2a2a2a;
      font-size: 16px;
      font-weight: 600;
    }
    &--value {
      line-height: 20px;
      margin-left: 10px;
    }
  }
  &__detailrow {
    &--title {
      font-size: 16px;
      font-weight: bold;
      color: #2a2a2a;
    }
    &--category {
      //   float: left;
      display: flex;
      padding: 20px 0;
      .ihdc-color {
        width: 14px;
        height: 14px;
        display: block;
      }
      .ihdc-title {
        margin: 0 10px;
        font-size: 16px;
        color: #969ac3;
      }
      .ihdc-value {
        margin-right: 20px;
      }
    }
    &--progress {
      height: 46px;
      display: flex;
      > span {
        display: block;
        height: 100%;
      }
    }
  }
  &__tableheader {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    &--left {
      font-size: 16px;
      line-height: 40px;
      font-weight: bold;
      color: #2a2a2a;
    }
    &--right {
      background-color: #5b1cf6;
    }
  }
}
</style>
<style >
.el-tabs__header {
  border-bottom: 0 !important;
}
.el-tabs--card > .el-tabs__header .el-tabs__item {
  border-bottom: 1px solid #e4e7ed;
}
.el-tabs__item .is-active {
  border-bottom: 1px solid #e4e7ed;
}
.el-tabs--card > .el-tabs__header .el-tabs__item.is-active {
  border-bottom-color: #e4e7ed;
  background-color: #5841ac;
  color: #fff;
}
.el-tabs__item:hover {
  background-color: #5841ac;
  color: #fff;
}
</style>
