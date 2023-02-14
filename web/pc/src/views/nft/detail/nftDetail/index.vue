<template>
  <div class="nft-detail">
    <div class="nft-detail__title">
      <span class="nft-detail__title--text">NFT Details</span>
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
      <div class="nft-detail__table--text">Txs</div>
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
          v-for="(item, key) in transactionColumns"
          :key="key"
          :fixed="item.fixed"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
        >
          <template #default="{ row }">
            <span v-if="item.prop == 'txHash'" class="nft-sell__intro">
              <el-tooltip :content="row['txhash']" placement="top">
                <router-link :to="{ path: `/dealInfo/${row.txhash}` }">
                  {{ (row["txhash"] && row["txhash"].substring(0, 10)) || "" }}
                  ...
                  {{
                    (row["txhash"] &&
                      row["txhash"].substring(
                        row["txhash"].length - 6,
                        row["txhash"].length
                      )) ||
                    "--"
                  }}
                </router-link>
              </el-tooltip>
            </span>
            <span v-if="item.prop == 'type'" class="nft-sell__intro">{{
              (row.event[0].type && row.event[0] && row.event[0].type) || ""
            }}</span>
            <div v-if="item.prop == 'amount'" class="nft-sell__intro">
              <router-link :to="{ path: `/dealInfo/${row.txhash}` }">
                More+
              </router-link>
            </div>
            <span v-if="item.prop == 'form'" class="nft-sell__intro">
              <router-link :to="{ path: `/dealInfo/${row.txhash}` }">
                More+
              </router-link>
            </span>
            <span v-if="item.prop == 'to'" class="nft-sell__intro">
              <router-link :to="{ path: `/dealInfo/${row.txhash}` }">
                More+
              </router-link>
            </span>
            <span v-if="item.prop == 'block'" class="nft-sell__intro">
              <router-link :to="{ path: `/blockchainInfo/${row.height}` }">
                {{ row.height || "--" }}
              </router-link>
            </span>
            <span v-if="item.prop == 'fee'" class="nft-sell__intro">
              {{ (row.fee && onFeixedNum(row.fee, 6)) || "--" }}
              {{ row.feeCoinName }}
            </span>
            <span v-if="item.prop == 'time'" class="nft-sell__intro">
              {{
                `${
                  row.timestamp &&
                  $dayjs.utc(row.timestamp).format("YYYY-MM-DD HH:mm:ss")
                } UTC` || "--"
              }}
            </span>
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
import { NftApi } from "@/api";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
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
        prop: "amount",
        label: "Amount",
      },
      {
        prop: "form",
        label: "Form",
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
        prop: "time",
        label: "Time",
      },
    ];
    return {
      transactionColumns,
      tableData: [],
      loading: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      infoObj: {
        owner: {
          name: "Owner",
          value: "",
          link:"/Accountdetails" // "/tokens/addressdetail/",
        },
        nftId: {
          name: "NFT ID",
          value: "",
        },
        creator: {
          name: "Creator",
          value: "",
          link:"/Accountdetails/" // "/tokens/addressdetail/",
        },
        name: {
          name: "NFT Name",
          value: "",
          // link: "ipfs",
        },
      },
    };
  },
  methods: {
    //返回
    goBack() {
      window.history.back();
    },
    //获取详细信息
    getDetail() {
      const obj = {
        tokenId: this.$route.params.id,
      };
      NftApi.getNftDetail(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          if (result) {
            this.infoObj["owner"].value = result.owner;
            this.infoObj["nftId"].value = result.tokenId;
            this.infoObj["creator"].value = result.creator;
            this.infoObj["name"].value = result.name;
          }
        }
      });
    },
    //获取该nft交易信息
    getTransactionList() {
      const obj = {
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
        tokenId: this.$route.params.id,
      };
      NftApi.getNFTTxByTokenId(obj).then((res) => {
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
      this.getTransactionList();
    },
    handleCurrentChange(e) {
      this.pageNum = e;
      this.getTransactionList();
    },
  },
  mounted() {
    this.getDetail();
    this.getTransactionList();
  },
};
</script>
<style lang="less" scoped>
.nft-detail {
  * a {
    color: #633af9;
    text-decoration: none;
  }
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
