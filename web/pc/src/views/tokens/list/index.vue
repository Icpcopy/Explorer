<template>
  <div class="icp-tokens">
    <div class="icp-tokens__tip">
      <div class="icp-tokens__tip--text">
        Token Tracker <span>(Total ICT-20 Token:{{ total || 0 }})</span>
      </div>
      <div class="icp-tokens__tip--search">
        <el-input
          style="width: 300px"
          v-model="param"
          placeholder="Search for Token Name or Address"
        />

        <el-button
          :loading="searchLoading"
          icon="el-icon-search"
          type="primary"
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
                    ? ` /blockchainInfo/${row[item.prop]}`
                    : '',
                }"
              >
                {{ row[item.prop] || "--" }}
              </router-link>
            </span>
            <span v-if="item.prop == 'hash'">
              <router-link
                :to="{
                  path: row[item.prop] ? `/dealInfo/${row[item.prop]}` : '',
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
            </span>
            <span v-if="item.prop == 'from' || item.prop == 'to'">
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
              {{
                `${
                  row[item.prop] &&
                  $dayjs.utc(row[item.prop]).format("YYYY-MM-DD HH:mm:ss")
                } UTC` || "--"
              }}
            </span>
            <span v-if="item.prop == 'tokenInfo'">
              <router-link
                :to="{ path: `/tokens/detail/${row['contractAddress']}` }"
              >
                <!-- <img src="" />{{ row["tokenName"] || "--" }} -->
                <div class="icp-tokens__table--tokenInfo">
                  <div class="ittt-img">
                    <img v-if="row['tokenLogo']" :src="row['tokenLogo']" />
                    <img v-else src="@/assets/icons/default-icon.png" />

                    <!-- (row['tokenLogo'] && row['tokenLogo']) || -->
                  </div>
                  <div class="ittt-box">
                    <div class="ittt-title">
                      {{ row["tokenName"] || "--"
                      }}<span>({{ row["tokenSymbol"] }})</span>
                    </div>
                    <div class="ittt-remark">{{ row["remark"] || "" }}</div>
                  </div>
                </div>
              </router-link>
            </span>
            <span v-if="item.prop == 'amount'">
              {{ (row[item.prop] && onFeixedNum(row[item.prop], 6)) || "--" }}
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
              >{{ row[item.prop] !== 0 ? row[item.prop] || "--" : 0 }}</span
            >
          </template>
        </el-table-column>
      </el-table>
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
        prop: "serailNo",
        label: "#",
        width: 60,
      },
      {
        prop: "tokenInfo",
        label: "Token",
      },
      {
        prop: "",
        label: "Price",
        sorter: true,
      },
      {
        prop: "",
        // width: 420,
        label: "Change（%）",
        sorter: true,
      },
      {
        prop: "",
        label: "Volume (24H)",
        sorter: true,
      },
      {
        prop: "",
        label: "Circulating Market Cap",
        sorter: true,
      },
      {
        prop: "",
        label: "On-Chain Market Cap",
        sorter: true,
      },
      {
        prop: "addresstotal",
        label: "Holders",
        sorter: true,
      },
    ];

    return {
      param: "",
      searchLoading: false,
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
        param: this.param,
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
      };
      this.searchLoading = true;
      Tokens.getTokensList(obj)
        .then((req) => {
          this.searchLoading = false;
          if (req.code >= 200 && req.code < 300) {
            const { result } = req;
            this.tableData =result.records
            Tokens.getTokenList({ listType: 1 }).then((res) => {
              if (res.code >= 200 && res.code < 300) {
                this.tableData = result.records.map((el, index) => {
                  const tokenIndex = res.rows.findIndex((li) => {
                    return li.contractsAddress == el.contractAddress;
                  });
                  if (tokenIndex != -1) {
                    return {
                      ...el,
                      remark: res.rows[tokenIndex].remark,
                      tokenLogo: res.rows[tokenIndex].tokenLogo,
                      tokenName: res.rows[tokenIndex].tokenName,
                      serailNo:
                        index < this.pageSize - 1
                          ? `${
                              !((this.pageNum - 1) * this.pageSize)
                                ? index + 1
                                : Number((this.pageNum - 1) * this.pageSize) +
                                  Number(index + 1)
                            }`
                          : `${this.pageNum * (this.pageSize / 10)}0`,
                    };
                  } else {
                    return {
                      ...el,
                      serailNo:
                        index < this.pageSize - 1
                          ? `${
                              !((this.pageNum - 1) * this.pageSize)
                                ? index + 1
                                : Number((this.pageNum - 1) * this.pageSize) +
                                  Number(index + 1)
                            }`
                          : `${this.pageNum * (this.pageSize / 10)}0`,
                    };
                  }
                });
              }
            });
            this.total = result.total;
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
      > span {
        font-size: 14px;
        font-weight: 400;
      }
    }
    &--search {
      display: flex;
      // line-height: 80px;
      justify-content: start;
      align-items: center;
      > button {
        margin-left: 20px;
        background-color: #633af9;
        height: 40px;
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
      .ittt-box {
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
