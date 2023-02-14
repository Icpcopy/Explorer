<template>
  <div class="nft-detail">
    <div class="nft-detail__title">
      <span class="nft-detail__title--text">12342526</span>
      <div>
        <el-button class="nft-detail__title--button" @click="goBack"
          >return</el-button
        >
      </div>
    </div>
    <div class="nft-detail__info">
      <nft-detail-card :infoObj="infoObj" />
    </div>
    <div class="nft-detail__table">
      <div class="nft-detail__table--text">Transaction</div>
      <el-table
        v-loading="loading"
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        header-cell-class-name="nft-detail__table--header"
        stripe
      >
        <el-table-column
          v-for="(item, key) in transactionColumns"
          :key="key"
          :fixed="item.fixed"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
        >
          <template #default="{ row }">
            <span class="nft-sell__intro">{{ row[item.prop] || "---" }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="nft-detail__table">
      <div class="nft-detail__table--text">Validator Set</div>
      <el-table
        header-cell-class-name="nft-detail__table--header"
        v-loading="loading"
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        stripe
      >
        <el-table-column
          v-for="(item, key) in columns"
          :key="key"
          :fixed="item.fixed"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
        >
          <template #default="{ row }">
            <span class="nft-sell__intro">{{ row[item.prop] || "---" }}</span>
          </template>
        </el-table-column>
      </el-table>
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
    </div>
    <Pagination
      :total="total"
      :pageSize="pageSize"
      :pageIndex="pageNum"
      @currpageChange="handleCurrentChange"
    ></Pagination>
  </div>
</template>

<script>
import { NftDetailCard } from "@/components";
import Pagination from "@/components/Pagination";
export default {
  components: { NftDetailCard, Pagination },
  name: "NftDetail",
  data() {
    const transactionColumns = [
      {
        prop: "txHash",
        label: "TxHash",
      },
      {
        prop: "type",
        label: "Type",
      },
      {
        prop: "amountForm",
        label: "Amount Form",
      },
      {
        prop: "to",
        label: "To",
      },
      {
        prop: "block",
        label: "Block",
      },
      {
        prop: "fee",
        label: "Fee",
      },
      {
        prop: "age",
        label: "Age",
      },
    ];
    const columns = [
      {
        prop: "moniker",
        label: "Moniker",
      },
      {
        prop: "operator",
        label: "Operator",
      },
      {
        prop: "consensusAddress",
        label: "Consensus Address",
      },
      {
        prop: "proposerPriority",
        label: "Proposer Priority",
      },
      {
        prop: "votingPower",
        label: "Voting Power",
      },
    ];
    return {
      transactionColumns,
      columns,
      tableData: [],
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      infoObj: {
        blockhash: {
          name: "Block Hash",
          value: "sadfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdf",
        },
        proposer: {
          name: "Proposer",
          value: "sadfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdf",
        },
        validators: {
          name: "Validators",
          value: "sadfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdf",
        },
        votingPower: {
          name: "VotingPower",
          value: "sadfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdf",
        },
        transactions: {
          name: "Transactions",
          value: "sadfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdf",
        },
        timestamp: {
          name: "Timestamp",
          value: "sadfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdf",
        },
      },
    };
  },
  methods: {
    //返回
    goBack() {
      window.history.back();
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
  mounted() {},
};
</script>
<style lang="less" scoped>
.nft-detail {
  &__title {
    display: flex;
    justify-content: space-between;
    height: 80px;
    line-height: 80px;
    &--text {
      font-size: 24px;
      color: #2a2a2a;
      font-weight: 600;
    }
    &--button {
      background-color: #633af9;
      color: #fff;
    }
  }
  &__info {
    box-shadow: 0px 3px 8px rgba(56, 54, 54, 0.06);
  }
  &__table {
    box-shadow: 0px 3px 8px rgba(56, 54, 54, 0.06);
    &--text {
      font-size: 24px;
      font-weight: 600;
      line-height: 24px;
      color: #2a2a2a;
      padding: 20px 0;
      margin-top: 20px;
    }
    /deep/ &--header {
      color: #fff;
      font-weight: 400;
      background-color: #5740a9;
    }
  }
}
</style>
