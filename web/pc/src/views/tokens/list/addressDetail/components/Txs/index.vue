<template>
  <div class="icp-tokensTxs">
    <div class="icp-tokensTxs__card">
      <!-- <div class="icp-tokensTxs__card--line">
        <span>Txs:</span><span>100</span>
      </div>
      <el-divider></el-divider> -->
      <div class="icp-tokensTxs__card--line">
        <el-radio-group v-model="checkRadio" @change="onChangeRadio">
          <el-radio label="chain">Chain Txs</el-radio>
          <el-radio label="contract">Evm Txs</el-radio>
        </el-radio-group>
      </div>
    </div>
    <div
      style="display: flex; justify-content: space-between; margin-bottom: 20px"
    >
      <div class="icp-tokensTxs__title">Transaction records</div>
      <el-button
        class="icp-tokensTxs___button"
        type="info"
        :loading="updateLoading"
        icon="el-icon-refresh"
        @click="onUpdate"
      ></el-button>
    </div>

    <el-row class="icp-tokensTxs__row" v-if="checkRadio !== 'chain'">
      <el-col
        :span="4"
        :class="currentIndex == 1 ? 'is-active' : ''"
        @click="onChangeTab(1)"
        >Transactions</el-col
      >
      <el-col
        :span="4"
        :class="currentIndex == 2 ? 'is-active' : ''"
        @click="onChangeTab(2)"
        >Ict20 Txs</el-col
      >
      <el-col
        :span="4"
        :class="currentIndex == 3 ? 'is-active' : ''"
        @click="onChangeTab(3)"
        >Ict721 Txs</el-col
      >
    </el-row>

    <div class="icp-tokensTxs__model" v-if="checkRadio == 'chain'">
      <el-radio-group
        v-model="bigType"
        @change="
          () => {
            (pageNum = 1), find();
          }
        "
        style="margin-bottom: 20px"
      >
        <el-radio-button
          :label="item.prop"
          v-for="(item, key) in modelEunm"
          :key="key"
          >{{ item.label }}{{ `(${item.value || 0})` }}</el-radio-button
        >
      </el-radio-group>
    </div>
    <el-table
      v-if="checkRadio == 'chain'"
      v-loading="loading"
      ref="multipleTable"
      :data="chainTableData"
      tooltip-effect="dark"
      stripe
      header-cell-class-name="icp-tokensTxs__table--header"
    >
      <el-table-column
        align="center"
        v-for="(item, key) in columns"
        :key="key"
        :fixed="item.fixed"
        :prop="item.prop"
        :label="item.label"
        :width="item.width"
      >
        <template v-if="item.prop == 'amount' && checkRadio == 'chain'" #header>
          Amount (ict)
        </template>
        <template #default="{ row }">
          <span v-if="item.prop == 'status' && checkRadio == 'chain'">
            <img
              v-if="row.code == 0"
              class="jiantou"
              src="@/assets/jiantou_success.png"
            />
            <img v-else class="jiantou" src="@/assets/jiantou_error.png" />
          </span>
          <span v-if="item.prop == 'txhash'">
            <el-tooltip
              class="item"
              effect="dark"
              placement="top"
              :content="row[item.prop]"
            >
              <router-link
                style="text-decoration: none; color: #5841ac"
                :to="{
                  path:
                    checkRadio == 'chain'
                      ? `/dealInfo/${row[item.prop]}`
                      : `/tokens/transactionDetail/${row[item.prop]}`,
                }"
              >
                {{
                  (row[item.prop] &&
                    `${row[item.prop].substring(0, 7)}...${row[
                      item.prop
                    ].substring(
                      row[item.prop].length - 9,
                      row[item.prop].length
                    )}`) ||
                  ""
                }}
              </router-link>
            </el-tooltip>
          </span>
          <span v-if="item.prop == 'fee' && checkRadio == 'chain'">
            {{ (row[item.prop] && onFeixedNum(row[item.prop], 6)) || 0 }}
          </span>
          <span v-if="item.prop == 'amount'">
            {{
              (row.event &&
                row.event[0] &&
                row.event[0][item.prop] &&
                onFeixedNum(row.event[0][item.prop], 6)) ||
              (row[item.prop] && onFeixedNum(row[item.prop], 6)) ||
              0
            }}
            <span v-if="checkRadio !== 'chain'">
              {{ row.symbol || "" }}
            </span>
          </span>
          <span v-if="item.prop == 'from'">
            <el-tooltip
              class="item"
              effect="dark"
              placement="top"
              :content="
                (row.event &&
                  row.event[0] &&
                  row.event[0][item.prop] &&
                  row.event[0][item.prop].operatorAddress) ||
                (row.event && row.event[0] && row.event[0][item.prop]) ||
                row[item.prop] ||
                ''
              "
            >
              <router-link
                style="text-decoration: none; color: #5841ac"
                :to="{
                  path: `/Accountdetails/${
                    (row.event &&
                      row.event[0] &&
                      row.event[0][item.prop] &&
                      row.event[0][item.prop].operatorAddress) ||
                    (row.event && row.event[0] && row.event[0][item.prop]) ||
                    row[item.prop] ||
                    ''
                  }`,
                }"
              >
                {{
                  (row.event &&
                    row.event[0] &&
                    row.event[0][item.prop] &&
                    row.event[0][item.prop] &&
                    row.event[0][item.prop].operatorAddress &&
                    row.event[0][item.prop].operatorAddress &&
                    `${row.event[0][item.prop].operatorAddress.substring(
                      0,
                      7
                    )}...${row.event[0][item.prop].operatorAddress.substring(
                      row.event[0][item.prop].operatorAddress.length - 9,
                      row.event[0][item.prop].operatorAddress.length
                    )} `) ||
                  (row.event &&
                    row.event[0] &&
                    row.event[0][item.prop] &&
                    row.event[0][item.prop] &&
                    row.event[0][item.prop] &&
                    row.event[0][item.prop] &&
                    `${row.event[0][item.prop].substring(
                      0,
                      7
                    )}...${row.event[0][item.prop].substring(
                      row.event[0][item.prop].length - 9,
                      row.event[0][item.prop].length
                    )}`) ||
                  (row[item.prop] &&
                    `${row[item.prop].substring(0, 7)}...${row[
                      item.prop
                    ].substring(
                      row[item.prop].length - 9,
                      row[item.prop].length
                    )}`) ||
                  ""
                }}
              </router-link>
            </el-tooltip>
          </span>
          <span v-if="item.prop == 'to'">
            <el-tooltip
              class="item"
              effect="dark"
              placement="top"
              :content="
                (row.event &&
                  row.event[0] &&
                  row.event[0][item.prop] &&
                  row.event[0][item.prop].operatorAddress) ||
                (row.event && row.event[0] && row.event[0][item.prop]) ||
                row[item.prop] ||
                ''
              "
            >
              <router-link
                style="text-decoration: none; color: #5841ac"
                :to="{
                  path: `/Accountdetails/${
                    (row.event &&
                      row.event[0] &&
                      row.event[0][item.prop] &&
                      row.event[0][item.prop].operatorAddress) ||
                    (row.event && row.event[0] && row.event[0][item.prop]) ||
                    row[item.prop] ||
                    ''
                  }`,
                }"
              >
                {{
                  (row.event &&
                    row.event[0] &&
                    row.event[0][item.prop] &&
                    row.event[0][item.prop] &&
                    row.event[0][item.prop].operatorAddress &&
                    row.event[0][item.prop].operatorAddress &&
                    `${row.event[0][item.prop].operatorAddress.substring(
                      0,
                      7
                    )}...${row.event[0][item.prop].operatorAddress.substring(
                      row.event[0][item.prop].operatorAddress.length - 9,
                      row.event[0][item.prop].operatorAddress.length
                    )} `) ||
                  (row.event &&
                    row.event[0] &&
                    row.event[0][item.prop] &&
                    row.event[0][item.prop] &&
                    row.event[0][item.prop] &&
                    row.event[0][item.prop] &&
                    `${row.event[0][item.prop].substring(
                      0,
                      7
                    )}...${row.event[0][item.prop].substring(
                      row.event[0][item.prop].length - 9,
                      row.event[0][item.prop].length
                    )}`) ||
                  (row[item.prop] &&
                    `${row[item.prop].substring(0, 7)}...${row[
                      item.prop
                    ].substring(
                      row[item.prop].length - 9,
                      row[item.prop].length
                    )}`) ||
                  ""
                }}
              </router-link>
            </el-tooltip>
          </span>
          <span v-if="item.prop == 'timestamp'">
            {{
              `${
                row.timestamp &&
                $dayjs.utc(row.timestamp).format("YYYY-MM-DD HH:mm:ss")
              } UTC` || "--"
            }}
          </span>
          <span
            v-if="
              item.prop !== 'txhash' &&
              item.prop !== 'status' &&
              item.prop !== 'fee' &&
              item.prop !== 'timestamp' &&
              item.prop !== 'amount' &&
              item.prop !== 'to' &&
              item.prop !== 'from'
            "
          >
            {{
              (row.event && row.event[0] && row.event[0][item.prop]) ||
              row[item.prop]
            }}
          </span>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
      v-if="checkRadio == 'chain'"
      :total="chainTableTotal"
      :pageSize="pageSize"
      :pageIndex="pageNum"
      @currpageChange="handleCurrentChange"
    ></Pagination>
    <transaction
      v-if="checkRadio !== 'chain'"
      :cloumn="
        currentIndex == 3
          ? defaultIct721Cloumn
          : currentIndex == 2
          ? defaultIct20Cloumn
          : []
      "
      :uint="currentIndex == 2 ? true : false"
      :tableData="
        currentIndex == 1
          ? tableData['transcation']
          : currentIndex == 2
          ? tableData['erc20']
          : tableData['erc721']
      "
      :pageNum="transactionPageNum"
      :pageSize="transactionPageSize"
      :total="total"
      @onChangeTransactionPage="onChangeTransactionPage"
    />
    <!-- :defauleValue="defaultValue"
      :type="defaultValue && defaultValue.contractType" -->
  </div>
</template>

<script>
import { Tokens } from "@/api";
import { getByBlockNumber, getAllTypeCount } from "@/api/deal";
import { Transaction } from "../../../../components";
import Pagination from "@/components/Pagination";
import { mixinCommon } from "@/mixin";
export default {
  components: { Pagination, Transaction }, //FormSearch
  mixins: [mixinCommon],
  props: {
    chainAddress: { type: String, default: "" },
    evmAddress: { type: String, default: "" },
  },
  name: "NftIndex",
  data() {
    const columns = [
      { prop: "txhash", label: "TXHash" },
      { prop: "type", label: "TXtype" },
      { prop: "from", label: "From" },
      { prop: "to", label: "To" },
      { label: "Amount", prop: "amount" },
      { prop: "timestamp", label: "Time" },
    ];

    const transactionColumns = [
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
    const defaultIct20Cloumn = [
      { label: "TXHash", prop: "hash" },
      { label: "TXtype", prop: "event" },
      // { label: "Block", prop: "block" },
      { label: "From", prop: "from" },
      { label: "To", prop: "to" },
      { label: "Amount", prop: "amount" },
      { label: "Token", prop: "tokenInfo" },
      { label: "timestamp", prop: "transferTime", width: 200 },
    ];
    const defaultIct721Cloumn = [
      { label: "TXHash", prop: "hash" },
      { label: "TXtype", prop: "event" },
      // { label: "Block", prop: "block" },
      { label: "From", prop: "from" },
      { label: "To", prop: "to" },
      { label: "Token ID", prop: "nftId" },
      // { label: "Token", prop: "tokenInfo" },
      { label: "timestamp", prop: "transferTime", width: 200 },
    ];
    const tableData = {
      erc20: [],
      erc721: [],
      transcation: [],
    };

    return {
      inputValue: "",
      columns,
      transactionColumns,
      chainTableData: [],
      chainTableTotal: 0,
      tableData,
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      checkRadio: "chain",
      bigType: "transfer",
      modelEunm: {
        transfer: { label: "Transfer", prop: "transfer", value: "" },
        Staking: { label: "Staking", prop: "Staking", value: "" },
        reward: { label: "Distribution", prop: "reward", value: "" },
        vote: { label: "Governance", prop: "vote", value: "" },
        Siashjailing: { label: "Slashing", prop: "jail", value: "" },
        Nft: { label: "Nft", prop: "nft", value: "" },
        Issues: { label: "Issues", prop: "issues", value: "" },
        defi: { label: "Defi", prop: "defi", value: "" },
      },
      defaultIct20Cloumn,
      defaultIct721Cloumn,
      currentIndex: 1,
      contractAddress: "",
      updateLoading: false,
      transactionPageNum: 1,
      transactionPageSize: 10,
    };
  },
  watch: {
    $route: {
      handler() {
        console.log(this.$router, "router====");
        window.location.reload();
      },
    },
    chainAddress: {
      handler() {
        this.find();
      },
    },
    evmAddress: {
      handler() {},
    },
  },
  methods: {
    onChangeTab(e) {
      this.currentIndex = e;
      this.transactionPageNum = 1;
      this.tableData = {
        erc20: [],
        erc721: [],
        transcation: [],
      };
      this.total = 0;
      switch (e) {
        case 1:
          this.getEvmTransaction();
          break;
        case 2:
          this.getEvmTransferList("erc20");
          break;
        case 3:
          this.getEvmTransferList("erc721");
          break;
      }
    },
    //获取合约交易
    getEvmTransferList(type) {
      const obj = {
        contractType: type,
        address: this.evmAddress,
        // contractAddress: this.contractAddress,
        pageIndex: this.transactionPageNum,
        pageSize: this.transactionPageSize,
      };
      Tokens.getEvmTransferList(obj)
        .then((res) => {
          this.updateLoading = false;
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.tableData[type] = result.records;
            this.total = result.total;
          }
        })
        .catch(() => {
          this.updateLoading = false;
        });
    },

    //获取交易列表
    getEvmTransaction() {
      const obj = {
        nftId: this.nftId,
        address: this.evmAddress, // this.contractAddress,
        pageIndex: this.transactionPageNum,
        pageSize: this.transactionPageSize,
      };
      //查询交易
      Tokens.getEvmTransactionList(obj)
        .then((res) => {
          this.updateLoading = false;
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            // this.tableData = result.records;
            this.tableData["transcation"] = result.records;
            this.total = result.total;
          }
        })
        .catch(() => {
          this.updateLoading = false;
        });
    },
    //交易分页
    onChangeTransactionPage(e) {
      this.transactionPageNum = e;

      switch (this.currentIndex) {
        case 1:
          this.getEvmTransaction();
          break;
        case 2:
          this.getEvmTransferList("erc20");
          break;
        case 3:
          this.getEvmTransferList("erc721");
          break;
      }
    },
    //更新
    onUpdate() {
      this.find();
    },
    initCloumns(e) {
      if (e == "chain") {
        if (this.columns.filter((el) => el.prop == "status").length == 0) {
          this.columns.splice(3, 0, {
            prop: "status",
            label: "",
          });
        }

        if (this.columns.filter((el) => el.prop == "fee").length == 0) {
          this.columns.splice(6, 0, {
            prop: "fee",
            label: "Fee",
          });
        }
      } else {
        this.columns = this.columns.filter((el) => {
          return el.prop !== "status" && el.prop !== "fee";
        });
      }
    },
    //切换链和合约
    onChangeRadio(e) {
      this.tableData = {
        erc20: [],
        erc721: [],
        transcation: [],
      };
      this.chainTableData = [];
      this.initCloumns(e);
      this.find();
    },
    //禁用时间
    disabledDate(e) {
      console.log(e, "e====", this.value2, "value2===");
    },
    //聚焦时间
    onChangeTime(e) {
      console.log(e, "e----");
    },
    //搜索
    onSearch() {
      this.tableData = {
        erc20: [],
        erc721: [],
        transcation: [],
      };
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
    //获取链列表
    initChainList(obj) {
      getByBlockNumber({
        ...obj,
        bigType: this.bigType,
        address: this.chainAddress,
      })
        .then((res) => {
          this.updateLoading = false;
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.chainTableData = result.records;
            this.chainTableTotal = result.total;
          }
        })
        .catch(() => {
          this.updateLoading = false;
        });
    },
    //列表
    find() {
      let obj = {
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
      };
      this.updateLoading = true;
      if (this.checkRadio == "chain") {
        getAllTypeCount({ address: this.chainAddress }).then((res) => {
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            for (var item in this.modelEunm) {
              switch (item) {
                case "transfer":
                  this.modelEunm[item].value = result.transferCount || 0;
                  break;
                case "Staking":
                  this.modelEunm[item].value = result.delegatorCount || 0;
                  break;
                case "reward":
                  this.modelEunm[item].value = result.rewardCount || 0;
                  break;
                case "vote":
                  this.modelEunm[item].value = result.voteCount || 0;
                  break;
                case "Siashjailing":
                  this.modelEunm[item].value = result.jailCount || 0;
                  break;
                case "nft":
                  this.modelEunm[item].value = result.nftCount || 0;
                  break;
                case "defi":
                  this.modelEunm[item].value = result.defiCount || 0;
                  break;
                case "issues":
                  this.modelEunm[item].value = result.issuesCount || 0;
                  break;
              }
            }
            this.initChainList(obj);
          }
        });
      } else {
        switch (this.currentIndex) {
          case 1:
            this.getEvmTransaction();
            break;
          case 2:
            this.getEvmTransferList("erc20");
            break;
          case 3:
            this.getEvmTransferList("erc721");
            break;
        }
        // Tokens.getEvmTransferList({
        //   ...obj,
        //   address: this.evmAddress,
        // })
        //   .then((res) => {
        //     this.updateLoading = false;
        //     if (res.code >= 200 && res.code < 300) {
        //       const { result } = res;
        //       this.tableData = result.records.map((el) => {
        //         return {
        //           txhash: el.hash,
        //           type: el.event,
        //           from: el.from,
        //           to: el.to,
        //           amount: el.amount,
        //           timestamp: el.transferTime * 1000,
        //           code: "",
        //           symbol: el.symbol,
        //         };
        //       });
        //       this.total = result.total;
        //     }
        //   })
        //   .catch(() => {
        //     this.updateLoading = false;
        //   });
      }
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
    this.contractAddress = this.$route.params.id;
    this.initCloumns(this.checkRadio);
  },
};
</script>
<style scoped lang="less">
.icp-tokensTxs {
  &__table {
    /deep/ &--header {
      color: #fff;
      font-weight: 400;
      background-color: #5841ac;
    }
  }
  &__row {
    height: 48px;
    background: rgba(255, 255, 255, 0.39);
    border: 1px solid #eaebec;
    line-height: 48px;
    font-size: 18px;
    line-height: 40px;
    color: #2a2a2a;
    text-align: center;
    // > *div {
    cursor: pointer;
    // }

    .is-active {
      color: #5841ac;
    }
  }
  &__card {
    background-color: #fff;
    margin-bottom: 20px;
    padding: 24px 0;
    &--line {
      padding: 0 50px;
    }
  }
  &__title {
    font-size: 20px;
    margin: 14px 0;
    line-height: 24px;
    color: #2a2a2a;
  }
  &___button {
    background-color: #5841ac;
  }
  &__model {
    display: flex;
    height: 48px;
    background-color: #fff;
    &--span {
      display: block;
      line-height: 48px;
      padding: 0 30px;
    }
  }
}
</style>
