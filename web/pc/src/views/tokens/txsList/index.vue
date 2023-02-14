<template>
  <div class="icp-tokens">
    <div class="icp-tokens__tip">
      <div class="icp-tokens__tip--text">
        ICT-20 Txs List {{ total || 0 }} Txs
      </div>
      <div class="icp-tokens__tip--search">
        <el-select v-model="transactionType" placeholder="All Txtype">
          <el-option
            v-for="item in typeEunm"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>

        <!-- <el-col :span="10">
            <el-select v-model="value" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-col> -->
        <!-- <el-col :span="6">
            <el-date-picker v-model="value1" type="date" placeholder="选择日期">
            </el-date-picker>
          </el-col>
          <el-col :span="5">
            <el-input
              v-model="inputValue"
              placeholder="Search for Token Name or Address"
            />
          </el-col> -->

        <el-button
          class="itts-button"
          icon="el-icon-search"
          type="primary"
          :loading="searchLoading"
          @click="onSearch"
        ></el-button>
      </div>
    </div>
    <div class="icp-tokens__table">
      <el-table
        v-loading="loading"
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        stripe
        header-cell-class-name="icp-tokens__table--header"
        :loading="tableLoading"
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
          <template #default="{ row }">
            <span v-if="item.prop == 'blockNumber'">
              <router-link
                :to="{
                  path: row[item.prop]
                    ? `/blockchainInfo/${row[item.prop]}`
                    : '',
                }"
              >
                {{ row[item.prop] || "--" }}
              </router-link>
            </span>
            <span v-if="item.prop == 'hash'">
              <el-tooltip
                class="item"
                effect="dark"
                placement="top"
                :content="row[item.prop]"
              >
                <router-link
                  :to="{
                    path: row[item.prop]
                      ? `/tokens/transactionDetail/${row[item.prop]}?type=erc20`
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
            <span v-if="item.prop == 'from' || item.prop == 'to'">
              <el-tooltip
                class="item"
                effect="dark"
                placement="top"
                :content="row[item.prop]"
              >
                <router-link
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
            <span v-if="item.prop == 'tokenId'">
              <!-- <el-tooltip :content="row[item.prop]" placement="top"> -->
              <router-link :to="{ path: `/tokens/detail/${row[item.prop]}` }">
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
            <span v-if="item.prop == 'transfer'">
              <img
                v-if="row.code == 0"
                class="jiantou"
                src="@/assets/jiantou_success.png"
              />
              <img v-else class="jiantou" src="@/assets/jiantou_error.png" />
            </span>

            <span v-if="item.prop == 'transferTime'" class="icp-tokens__intro">
              <!-- {{ $dayjs(row[item.prop] * 1000).format("YYYY-MM-DD HH:mm:ss") }} -->
              {{
                `${
                  row[item.prop] &&
                  $dayjs.utc(row[item.prop] * 1000).format("YYYY-MM-DD HH:mm:ss")
                } UTC` || "--"
              }}
            </span>
            <span v-if="item.prop == 'tokenInfo'">
              <router-link
                :to="{ path: `/tokens/detail/${row['contractAddress']}` }"
              >
                <!-- <img src="" />{{ row["symbol"] || "--" }} -->
                <div class="icp-tokens__table--tokenInfo">
                  <div class="ittt-img">
                    <img
                      v-if="row['tokenLogo']"
                      :src="row['tokenLogo'] || ''"
                    />
                    <img v-else src="@/assets/icons/default-icon.png" />
                  </div>
                  <div>
                    <div class="ittt-title">
                      {{ row["tokenName"] || "--" }}
                      <span v-if="row['symbol']">({{ row["symbol"] }})</span>
                    </div>
                    <div class="ittt-remark">{{ row["remark"] || "" }}</div>
                  </div>
                </div>
              </router-link>
            </span>
            <span v-if="item.prop == 'amount'">
              {{ (row[item.prop] && onFeixedNum(row[item.prop], 6)) || "--" }}
              {{ row["symbol"] }}
            </span>
            <span
              v-if="
                item.prop !== 'transfer' &&
                item.prop !== 'hash' &&
                item.prop !== 'from' &&
                item.prop !== 'to' &&
                item.prop !== 'transferTime' &&
                item.prop !== 'tokenInfo' &&
                item.prop !== 'blockNumber' &&
                item.prop !== 'amount'
              "
              >{{ row[item.prop] || "--" }}</span
            >
          </template>
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
// import { FormSearch } from "@/components";
import Pagination from "@/components/Pagination";
import { Tokens } from "@/api";
import { mixinCommon } from "@/mixin";
export default {
  components: { Pagination }, //FormSearch
  mixins: [mixinCommon],
  name: "NftIndex",
  data() {
    const columns = [
      {
        prop: "hash",
        label: "TXHash",
      },
      {
        prop: "blockNumber",
        label: "Block",
      },
      {
        prop: "event",
        label: "TXType",
        sorter: true,
      },
      {
        prop: "from",
        // width: 420,
        label: "From",
      },
      //   {
      //     prop: "transfer",
      //     width: 100,
      //     label: "",
      //   },
      {
        prop: "to",
        label: "To",
        sorter: true,
      },
      {
        prop: "amount",
        label: "Amount",
        sorter: true,
      },
      {
        prop: "tokenInfo",
        label: "Token",
        sorter: true,
      },
      {
        prop: "transferTime",
        label: "Age",
        sorter: true,
      },
    ];
    const typeEunm = [
      { label: "All Txtype", value: "" },
      { label: "Transfer", value: "Transfer" },
      { label: "Approval", value: "Approval" },
    ];

    return {
      searchLoading: false,
      transactionType: "",
      typeEunm,
      value2: [],
      columns,
      tableData: [],
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      formValue: {
        keyWord: "",
      },
      tableLoading: false,
    };
  },
  methods: {
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
      //输入框无内容
      this.pageNum = 1;
      this.pageSize = 10;
      this.find();
    },
    //列表
    find() {
      const obj = {
        contractType: "erc20",
        transactionType: this.transactionType,
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
      };
      this.searchLoading = true;
      this.tableLoading = true;
      Tokens.getEvmTransferList(obj)
        .then((req) => {
          this.searchLoading = false;
          if (req.code >= 200 && req.code < 300) {
            const { result } = req;
            this.tableData = result.records;
            this.total = result.total;
            Tokens.getTokenList({ listType: 1 })
              .then((res) => {
                if (res.code >= 200 && res.code < 300) {
                  this.tableLoading = false;
                  this.tableData = result.records.map((el) => {
                    const tokenIndex = res.rows.findIndex((li) => {
                      return li.contractsAddress == el.contractAddress;
                    });
                    if (tokenIndex != -1) {
                      return {
                        ...el,
                        remark: res.rows[tokenIndex].remark,
                        tokenLogo: res.rows[tokenIndex].tokenLogo,
                        tokenName: res.rows[tokenIndex].tokenName,
                      };
                    } else {
                      return {
                        ...el,
                      };
                    }
                  });
                }
              })
              .catch(() => {
                this.tableLoading = false;
              });
          }
        })
        .catch(() => {
          this.searchLoading = false;
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
      this.tableData = [];
      this.find();
    },
  },
  mounted() {
    this.find();
  },
};
</script>
<style scoped lang="less">
.icp-tokens {
  // background-color: #fff;
  * a {
    color: #633af9;
    text-decoration: none;
  }
  &__tip {
    width: inherit;
    display: flex;
    justify-content: space-between;
    height: 80px;
    &--text {
      font-size: 24px;
      line-height: 80px;
      color: #2a2a2a;
      font-weight: 600;
    }
    &--search {
      // display: flex;
      line-height: 80px;
      justify-content: start;
      > button {
        margin-left: 20px;
        background-color: #633af9;
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
    &--tokenInfo {
      display: flex;
      justify-content: start;
      .ittt-img {
        flex-shrink: 0;
        width: 25px;
        height: 25px;
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 10px;
        overflow: hidden;
        > img {
          width: 100%;
        }
      }
      .ittt-title {
        font-size: 11px;
        color: #7f58e0;
      }
      .ittt-remark {
        color: #2a2a2a;
        font-size: 8px;
      }
    }
  }
}
</style>
