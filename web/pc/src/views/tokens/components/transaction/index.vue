<template>
  <div class="icp-evmTransaction">
    <div>
      <el-table
        v-loading="loading"
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        stripe
        header-cell-class-name="icp-evmTransaction__table--header"
      >
        <el-table-column
          align="center"
          v-for="(item, key) in transactionListCloumn"
          :key="key"
          :fixed="item.fixed"
          :prop="item.prop"
          :label="item.label"
          :width="item.width || ''"
        >
          <template #default="{ row }">
            <span v-if="item.prop == 'blockNumber'">
              <router-link
                class="icp-evmTransaction__link"
                :to="{
                  path: row[item.prop]
                    ? `/blockchainInfo/${row[item.prop]}`
                    : '',
                }"
              >
                {{ row[item.prop] || "--" }}
              </router-link>
            </span>
            <span v-if="item.prop == 'txHash' || item.prop == 'hash'">
              <el-tooltip
                class="item"
                effect="dark"
                placement="top"
                :content="row[item.prop]"
              >
                <router-link
                  class="icp-evmTransaction__link"
                  :to="{
                    path: row[item.prop]
                      ? `/tokens/transactionDetail/${row[item.prop]}`
                      : '',
                    query: {
                      type: type,
                    },
                  }"
                >
                  {{
                    (row[item.prop] &&
                      row[item.prop].substring(0, 9) +
                        "..." +
                        row[item.prop].substring(
                          row[item.prop].length - 7,
                          row[item.prop].length
                        )) ||
                    "--"
                  }}
                </router-link>
              </el-tooltip>
            </span>
            <span v-if="item.prop == 'from' || item.prop == 'to'">
              <el-tooltip
                class="item"
                effect="dark"
                placement="top"
                :content="row[item.prop]"
              >
                <router-link
                  class="icp-evmTransaction__link"
                  :to="{
                    path: row[item.prop]
                      ? `/Accountdetails/${row[item.prop]}`
                      : '',
                  }"
                >
                  {{
                    (row[item.prop] &&
                      row[item.prop].substring(0, 9) +
                        "..." +
                        row[item.prop].substring(
                          row[item.prop].length - 7,
                          row[item.prop].length
                        )) ||
                    "--"
                  }}
                </router-link>
              </el-tooltip>
            </span>

            <span v-if="item.prop == 'nftId'">
              <router-link
                v-if="!nftId"
                class="icp-evmTransaction__link"
                :to="{
                  path: `/tokens/repeat-txs/txs/${row[item.prop]}`,
                  query: {
                    contractAddress: row['contractAddress'],
                  },
                }"
              >
                {{ row[item.prop] || "" }}
              </router-link>
              <span v-else> {{ row[item.prop] || "--" }}</span>
            </span>

            <!-- <span v-if="item.prop == 'TokenInfo'"> </span> -->
            <span v-if="item.prop == 'tokenId'">
              <!-- <el-tooltip :content="row[item.prop]" placement="top"> -->
              <router-link
                class="icp-evmTransaction__link"
                :to="{ path: `/tokens/detail/${row[item.prop]}` }"
              >
                {{ (row[item.prop] && row[item.prop].substring(0, 10)) || "" }}
                ...
                {{
                  (row[item.prop] &&
                    row[item.prop].substring(
                      row[item.prop].length - 6,
                      row[item.prop].length
                    )) ||
                  "--"
                }}
              </router-link>
              <!-- </el-tooltip> -->
            </span>
            <span v-if="item.prop == 'transfer' && !nftId">
              <img
                v-if="row.success"
                class="jiantou"
                src="@/assets/jiantou_success.png"
              />
              <img v-else class="jiantou" src="@/assets/jiantou_error.png" />
            </span>

            <span
              v-if="item.prop == 'blockTime' || item.prop == 'transferTime'"
              class="icp-tokens__intro"
            >
              {{
                `${
                  row[item.prop] &&
                  $dayjs
                    .utc(row[item.prop] * 1000)
                    .format("YYYY-MM-DD HH:mm:ss")
                } UTC` || "--"
              }}
            </span>
            <span v-if="item.prop == 'tokenInfo'">
              <router-link
                class="icp-evmTransaction__link"
                :to="{ path: `/tokens/detail/${row['contractAddress']}` }"
              >
                <img
                  style="width: 25px; height: 25px"
                  src="@/assets/icons/default-icon.png"
                />{{ row["tokenName"] || "--"
                }}{{ row["symbol"] && `(${row["symbol"]})` }}
              </router-link>
            </span>
            <span v-if="item.prop == 'amount' || item.prop == 'gasUsed'">
              {{ (row[item.prop] && onFeixedNum(row[item.prop], 6)) || 0 }}
              {{ uint ? row["symbol"] : "ict" }}
            </span>
            <span
              v-if="
                item.prop !== 'transfer' &&
                item.prop !== 'txHash' &&
                item.prop !== 'hash' &&
                item.prop !== 'from' &&
                item.prop !== 'to' &&
                item.prop !== 'blockTime' &&
                item.prop !== 'transferTime' &&
                item.prop !== 'tokenInfo' &&
                item.prop !== 'blockNumber' &&
                item.prop !== 'amount' &&
                item.prop !== 'gasUsed' &&
                item.prop !== 'nftId'
              "
              >{{ row[item.prop] || "--" }}</span
            >
          </template>
          <!-- <template #header v-if="item.prop == 'amount'">
            <span
              >{{ item.label
              }}{{
                (defauleValue &&
                  defauleValue["tokenSymbol"] &&
                  `(${defauleValue["tokenSymbol"]})`) ||
                ""
              }}</span
            >
          </template> -->
        </el-table-column>
      </el-table>
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
import Pagination from "@/components/Pagination";
import { mixinCommon } from "@/mixin";
// import { Tokens } from "@/api";
export default {
  components: { Pagination },
  mixins: [mixinCommon],
  name: "ListCard",
  props: {
    uint: { type: Boolean, default: false },
    loading: { type: Boolean, default: false },
    title: { type: String, default: "" },
    defauleValue: { type: Object, default: () => {} },
    cloumn: { type: Array, default: () => [] },
    contract: { type: String, default: "" },
    type: { type: String, default: "" },
    nftId: { type: String, default: "" },
    pageSize: { type: Number, default: 10 },
    pageNum: { type: Number, default: 1 },
    tableData: { type: Array, default: () => [] },
    total: { type: Number, default: 0 },
  },
  data() {
    const defaultCloumn = [
      { label: "TXHash", prop: "txHash" },
      { label: "TXtype", prop: "method" },
      { label: "Block", prop: "blockNumber" },
      { label: "From", prop: "from" },
      { label: "", prop: "transfer" },
      { label: "To", prop: "to" },
      { label: "Amount", prop: "amount" },
      { label: "Fee", prop: "gasUsed" },
      { label: "timestamp", prop: "blockTime", width: 200 },
    ];
    return {
      transactionListCloumn:
        this.cloumn.length > 0 ? this.cloumn : defaultCloumn,

      // total: 0,
      // tableData: [],
      contractAddress: "",
      defaultCloumn,
    };
  },
  watch: {
    contract: {
      handler() {
        this.contractAddress = this.contract;
        // this.find();
      },
    },
    cloumn: {
      handler() {
        this.transactionListCloumn =
          this.cloumn.length > 0 ? this.cloumn : this.defaultCloumn;
      },
    },
  },
  methods: {
    handleCurrentChange(e) {
      this.$emit("onChangeTransactionPage", e);
      // this.pageNum = e;
      // this.find();
    },
  },
  mounted() {
    this.contractAddress = this.contract
      ? this.contract
      : this.$route.params.id;
    // this.find();
  },
};
</script>
<style lang="less" scoped>
.icp-evmTransaction {
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
}
</style>
