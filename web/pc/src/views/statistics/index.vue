<template>
  <div class="container-children-box">
    <header>
      <div class="h-text">
        <h3>{{ coinName }} Rich List</h3>
        <div class="titles">
          <div class="blod">Top 100 Addresses by {{ coinName }}</div>
          <div class="zhushi">
            （The assets include the balance tokens,Cross-chain assets and
            Contractual assets）
          </div>
        </div>
      </div>
      <button size="small" @click="getAddressByAssetsFnGo">Updated</button>
    </header>
    <section>
      <el-table
        :data="tableData_tj"
        style="width: 100%"
        :header-cell-style="tableHeaderColor"
      >
        <el-table-column
          prop="date"
          label="#"
          align="center"
          type="index"
          width="180"
        >
        </el-table-column>
        <el-table-column prop="name" label="Address" width="450" align="center">
          <template #default="scope">
            <span
              class="lianjie cursor_"
              @click="$headerRouterFn(scope.row.address)"
              >{{ scope.row.address }}</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="address" align="center">
          <template #header>
            <div>
              Amount（<span style="margin-left: 2px">{{ coinName }}</span
              >）
              <span style="font-size: 10px"> </span>
            </div>
          </template>
          <template #default="scope">
            <span>
              {{
                (scope.row.amount && onFeixedNum(scope.row.amount, 6)) || 0
              }}</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="address" label="Percentage" align="center">
          <template #default="scope">
            <span>{{ (scope.row.rate * 100).toFixed(2) }}%</span>
          </template>
        </el-table-column>
      </el-table>
    </section>
    <!-- <Pagination
      :pageIndex="pageIndex"
      :pageSize="pageSize"
      :total="total"
    ></Pagination> -->
  </div>
</template>

<script>
// import Pagination from "@/components/Pagination";
import { getAddressByAssets } from "@/api/tongji";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      pageIndex: 1,
      tableData_tj: [],
      pageSize: 100,
      total: 0,
      coinName: "",
    };
  },
  created() {
    this.getAddressByAssetsFn();
  },
  methods: {
    getAddressByAssetsFnGo() {
      this.getAddressByAssetsFn();
      setTimeout(() => {
        this.$message({
          showClose: true,
          message: "Refresh the success!",
          type: "success",
        });
      });
    },
    addressclick(add) {
      this.$router.push({
        path: "/Accountdetails",
        query: { address: add },
      });
    },
    tableHeaderColor({ row, column, rowIndex, columnIndex }) {
      if (rowIndex === 0) {
        return "background-color: #5841ac;color: #fff;font-weight: 500;";
      }
    },
    getAddressByAssetsFn() {
      getAddressByAssets({
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      }).then((res) => {
        this.total = res.result.total;
        if (res.result.coinName) {
          this.coinName = res.result.coinName.toUpperCase();
        }

        this.tableData_tj = res.result.records;
      });
    },
  },
  // components: { Pagination }
};
</script>

<style scoped lang="less">
header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 20px 0px;
  .titles {
    border-left: 1px solid #b7b7b7;
    margin-left: 10px;
    padding-left: 10px;
    display: flex;
    color: #2a2a2a;
    div {
      font-size: 12px;
    }
    .zhushi {
      margin-left: 5px;
      font-size: 10px;
    }
  }
  .h-text {
    display: flex;
    width: 1000px;
  }
  button {
    background: #633af9;
    border-radius: 5px;
    color: #fff;
  }
}
button {
  background: #633af9;
  border-radius: 5px;
  color: #fff;
  cursor: pointer;
  padding: 8px;
  font-size: 12px;
}
</style>
