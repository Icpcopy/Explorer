<template>
  <div class="nft-nftTab">
    <div class="nft-nftTab__table">
      <el-table
        v-loading="loading"
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        stripe
        header-cell-class-name="nft-nftTab__table--header"
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
            <span v-if="item.prop == 'creator'">
              <!-- <router-link :to="{ path: `/blockchainInfo/${row[item.prop]}` }"> -->
              {{ row[item.prop] || "--" }}
              <!-- </router-link> -->
            </span>
            <span v-if="item.prop == 'owner'">
              <!-- <router-link :to="{ path: `/dealInfo/${row[item.prop]}` }"> -->
              {{ row[item.prop] || "--" }}
              <!-- </router-link> -->
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
import Pagination from "@/components/Pagination";
import { NftApi } from "@/api";
import { mixinCommon } from "@/mixin";
export default {
  props: {},
  components: { Pagination },
  mixins: [mixinCommon],
  name: "NftTab",
  data() {
    const columns = [
      {
        prop: "tokenId",
        width: 200,
        label: "NFT ID",
      },
      {
        prop: "owner",
        label: "Owner",
      },
      {
        prop: "creator",
        label: "Creator",
      },
      {
        prop: "name",
        label: "NFT Name",
      },
    ];
    return {
      columns,
      tableData: [{}],
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
    };
  },
  methods: {
    //获取一个address的nft相关交易
    find() {
      const obj = {
        address: this.$route.params.id,
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
<style lang="less" scoped>
.nft-nftTab {
  * a {
    color: #633af9;
    text-decoration: none;
  }
  &__table {
    margin-top: 20px;
    /deep/ &--header {
      color: #fff;
      font-weight: 400;
      background-color: #5740a9;
    }
  }
}
</style>
