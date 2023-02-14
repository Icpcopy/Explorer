<template>
  <div class="nft-index">
    <div class="nft-index__tip">
      <div class="nft-index__tip--text">{{ total || 0 }} NFTs</div>
      <div class="nft-index__tip--search">
        <el-input
          style="width: 300px"
          v-model="inputValue"
          placeholder="Search by NFT Id"
        />
        <el-button
          icon="el-icon-search"
          type="primary"
          @click="onSearch"
        ></el-button>
      </div>
    </div>
    <div class="nft-index__table">
      <el-table
        v-loading="loading"
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        stripe
        header-cell-class-name="nft-index__table--header"
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
            <span v-if="item.prop == 'name'">
              <!-- <router-link :to="{ path: '' }" @click="toIpfs(row[item.prop])"> -->
              {{ row[item.prop] || "--" }}
              <!-- </router-link> -->
            </span>
            <span v-if="item.prop == 'tokenId'">
              <el-tooltip :content="row[item.prop]" placement="top">
                <router-link :to="{ path: `/nft/detail/${row[item.prop]}` }">
                  {{
                    (row[item.prop] && row[item.prop].substring(0, 10)) || ""
                  }}
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
              </el-tooltip>
            </span>
            <span v-if="item.prop == 'owner'">
              <el-tooltip :content="row[item.prop]" placement="top">
                <router-link
                  :to="{ path: `/Accountdetails/${row[item.prop]}` }"
                >
                  {{
                    (row[item.prop] && row[item.prop].substring(0, 10)) || ""
                  }}
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
              </el-tooltip>
            </span>

            <span v-if="item.prop == 'timestamp'" class="nft-sell__intro">
              {{
                `${
                  row[item.prop] &&
                  $dayjs.utc(row[item.prop]).format("YYYY-MM-DD HH:mm:ss")
                } UTC` || "--"
              }}
            </span>
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
import { NftApi } from "@/api";
import { mixinCommon } from "@/mixin";
export default {
  components: { Pagination }, //FormSearch
  mixins: [mixinCommon],
  name: "NftIndex",
  data() {
    const columns = [
      {
        prop: "tokenId",
        label: "NFT ID",
      },
      {
        prop: "owner",
        label: "Owner",
      },
      {
        prop: "name",
        // width: 420,
        label: "NFT Name",
      },
      {
        prop: "timestamp",
        label: "Time",
      },
    ];

    return {
      inputValue: "",
      value2: [],
      columns,
      tableData: [],
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      listForm: [
        {
          type: "input",
          placeholder: "Search by NFT Id or NFT Name",
          prop: "keyWord",
        },
      ],
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
        tokenId: (this.inputValue && this.inputValue.trim()) || "",
      };
      NftApi.getNftDetail(obj).then((res) => {
        // console.log(res, "res===");
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
          this.tableData = result.records;
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
<style scoped lang="less">
.nft-index {
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
  }
}
</style>
