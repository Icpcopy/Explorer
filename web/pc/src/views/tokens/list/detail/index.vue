<template>
  <div class="icp-tokenDetail">
    <div class="icp-tokenDetail__title">Token Details</div>
    <div class="icp-tokenDetail__top">
      <div class="icp-tokenDetail__top--left">
        <img
          v-if="defaultValue && defaultValue.tokenLogo"
          :src="(defaultValue && defaultValue.tokenLogo) || ''"
        />
        <img v-else src="@/assets/icons/default-icon.png" />
      </div>
      <div class="icp-tokenDetail__top--right">
        <div class="ittr-title">
          {{ (defaultValue && defaultValue.tokenName) || "" }}
          {{
            (defaultValue &&
              defaultValue.tokenSymbol &&
              `(${defaultValue.tokenSymbol})`) ||
            ""
          }}
          <span
            style="
              font-size: 14px;
              color: #f0b820;
              background: rgba(255, 249, 217, 0.39);
              padding: 3px 5px;
              border-radius: 2px;
            "
          >
            {{ defaultValue && tagEunm[defaultValue.contractType] }}
          </span>
        </div>
        <div class="ittr-extra">
          {{ (defaultValue && defaultValue.remark) || "" }}
        </div>
      </div>
    </div>
    <div class="icp-tokenDetail__list">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="icp-tokenDetail__list--card">
            <div class="itlc-title">Overview</div>
            <list-card
              :ListItem="
                defaultValue &&
                ercDetailListEunm[defaultValue.contractType].overviewList
              "
              :defaultValue="defaultValue"
            />
          </div>
        </el-col>
        <el-col :span="8">
          <div class="icp-tokenDetail__list--card">
            <div class="itlc-title">Basic Info</div>
            <list-card
              :ListItem="
                defaultValue &&
                ercDetailListEunm[defaultValue.contractType].infoList
              "
              :defaultValue="defaultValue"
            />
          </div>
        </el-col>
        <el-col :span="8">
          <div class="icp-tokenDetail__list--card">
            <div class="itlc-title">More</div>
            <list-card
              :ListItem="
                defaultValue &&
                ercDetailListEunm[defaultValue.contractType].moreList
              "
              :defaultValue="defaultValue"
            />
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="icp-tokenDetail__transactiontitle">Transaction records</div>
    <el-tabs v-model="activeName" type="card" @tab-click="onChangeTab">
      <el-tab-pane label="Transactions" name="first">
        <!-- :cloumn="
            defaultValue && defaultValue.contractType == 'erc721'
              ? defaultCloumn
              : []
          " -->.

        <transaction
          :tableData="tableData"
          :pageNum="transactionPageNum"
          :pageSize="transactionPageSize"
          :total="transactionTotal"
          :defauleValue="defaultValue"
          :type="defaultValue && defaultValue.contractType"
          @onChangeTransactionPage="onChangeTransactionPage"
      /></el-tab-pane>
      <el-tab-pane label="Ict20 Txs" name="second">
        <!-- <Holders :defauleValue="defaultValue" /> -->
        <transaction
          :uint="true"
          :cloumn="defaultIct20Cloumn"
          :tableData="tableData"
          :pageNum="transactionPageNum"
          :pageSize="transactionPageSize"
          :total="transactionTotal"
          :defauleValue="defaultValue"
          :type="defaultValue && defaultValue.contractType"
          @onChangeTransactionPage="onChangeTransactionPage"
        />
      </el-tab-pane>
      <el-tab-pane label="Ict721 Txs" name="thrid">
        <!-- <Holders :defauleValue="defaultValue" /> -->
        <transaction
          :cloumn="defaultIct721Cloumn"
          :tableData="tableData"
          :pageNum="transactionPageNum"
          :pageSize="transactionPageSize"
          :total="transactionTotal"
          :defauleValue="defaultValue"
          :type="defaultValue && defaultValue.contractType"
          @onChangeTransactionPage="onChangeTransactionPage"
        />
      </el-tab-pane>
      <el-tab-pane label="Holdres" name="four"
        ><Holders :defauleValue="defaultValue"
      /></el-tab-pane>
      <el-tab-pane
        label="Inventory"
        name="third"
        v-if="defaultValue && defaultValue.contractType == 'erc721'"
      >
        <div style="margin: 20px 0; font-size: 16px; color: #2a2a2a">
          A total {{ total }} NFT Tokens. Only the first 10000 data are
          displayed
        </div>
        <el-row gutter="20" v-if="nftList.length > 0">
          <el-col
            class="icp-tokenDetail__col"
            :span="6"
            v-for="(item, key) in nftList"
            :key="key"
          >
            <Inventory :defaultValue="item" />
          </el-col>
        </el-row>
        <div class="icp-tokenDetail__nodata" v-if="nftList.length == 0">
          <div class="icp-tokenDetail__nodata--img">
            <img src="@/assets/images/nodata.png" />
          </div>
          <div class="icp-tokenDetail__nodata--text">~ No Data ~</div>
        </div>
        <Pagination
          :total="total"
          :pageSize="pageSize"
          :pageIndex="pageNum"
          @currpageChange="handleCurrentChange"
        ></Pagination>
      </el-tab-pane>
      <!-- <el-tab-pane label="分析" name="third">角色管理</el-tab-pane> -->
    </el-tabs>
  </div>
</template>

<script>
import { ListCard, Transaction, Holders, Inventory } from "../../components";
import Pagination from "@/components/Pagination";
import { Tokens } from "@/api";
export default {
  components: { ListCard, Transaction, Holders, Inventory, Pagination },
  name: "Nft",
  data() {
    const defaultCloumn = [
      { label: "TXHash", prop: "hash" },
      { label: "TXtype", prop: "event" },
      // { label: "Block", prop: "block" },
      { label: "From", prop: "from" },
      { label: "", prop: "transfer" },
      { label: "To", prop: "to" },
      { label: "Token ID", prop: "nftId" },
      { label: "timestamp", prop: "transferTime", width: 200 },
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
    const erc20Obj = {
      overviewList: [
        { label: "--", value: "--", prop: "", labelSpan: 10, valueSpan: 14 },
        {
          label: "Total Supply:",
          value: "",
          prop: "totalSupply",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Circulating Supply:",
          value: "",
          prop: "circulation",
          labelSpan: 12,
          valueSpan: 12,
        },
        {
          label: "Total Market Value:",
          value: "",
          prop: "",
          labelSpan: 12,
          valueSpan: 12,
        },
        {
          label: "Market Cap:",
          value: "",
          prop: "",
          labelSpan: 10,
          valueSpan: 14,
        },
      ],
      infoList: [
        {
          label: "Contract:",
          value: "",
          prop: "contractAddress",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Issuer:",
          value: "",
          prop: "owner",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Issuing Time:",
          value: "",
          prop: "issuerTimestamp",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Decimals:",
          value: "",
          prop: "decimal",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Symbol:",
          value: "",
          prop: "tokenSymbol",
          labelSpan: 10,
          valueSpan: 14,
        },
        // { label: "Minimum unit", value: "22", prop: "" },
      ],
      moreList: [
        {
          label: "Holders:",
          value: "",
          prop: "addresstotal",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Transfers:",
          value: "",
          prop: "transactiontotalCount",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Official Website:",
          value: "",
          prop: "officialAddress",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "White Paper:",
          value: "",
          prop: "whitePaperAddress",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Social Profiles:",
          value: "",
          prop: "linkList",
          labelSpan: 10,
          valueSpan: 14,
        },
      ],
    };
    const erc721Obj = {
      overviewList: [
        {
          label: "Total Supply:",
          value: "",
          prop: "totalSupply",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Holders:",
          value: "",
          prop: "addresstotal",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Transfers:",
          value: "",
          prop: "transactiontotalCount",
          labelSpan: 10,
          valueSpan: 14,
        },
      ],
      infoList: [
        {
          label: "Contract:",
          value: "",
          prop: "contractAddress",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Issuer:",
          value: "",
          prop: "owner",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Issuing Time:",
          value: "",
          prop: "issuerTimestamp",
          labelSpan: 10,
          valueSpan: 14,
        },
      ],
      moreList: [
        {
          label: "Official Website:",
          value: "",
          prop: "officialAddress",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "White Paper:",
          value: "",
          prop: "whitePaperAddress",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Social Profiles:",
          value: "",
          prop: "linkList",
          labelSpan: 10,
          valueSpan: 14,
        },
      ],
    };
    return {
      ercDetailListEunm: {
        erc20: erc20Obj,
        erc721: erc721Obj,
      },
      tagEunm: {
        erc20: "ICT 20",
        erc721: "ICT 721",
      },
      defaultIct20Cloumn,
      defaultIct721Cloumn,
      tableData: [],
      transactionPageNum: 1,
      transactionPageSize: 10,
      transactionTotal: 0,
      activeName: "first",
      contractAddress: "",
      defaultValue: null,
      nftList: [],
      // nftTotal: 0,
      pageNum: 1,
      pageSize: 12,
      total: 0,
      type: "",
      defaultCloumn,
    };
  },
  watch: {
    $route() {
      this.contractAddress = this.$route.params.id;
      this.type = this.$route.query.type;
      this.getDetail();
      this.getEvmTransaction();
    },
  },
  methods: {
    //tab切换
    onChangeTab(e, event) {
      console.log(event, "event===");
      this.transactionPageNum = 1;
      this.transactionPageSize = 10;
      this.tableData = [];
      this.transactionTotal = 0;
      switch (e.index) {
        case "0":
          this.getEvmTransaction();
          break;
        case "1":
          this.getEvmTransferList("erc20");
          break;
        case "2":
          this.getEvmTransferList("erc721");
          break;
        case "3":
          break;
      }
      console.log(e.index, "e===");
      // //获取库存
      if (e.props.label == "Inventory") {
        this.getNftList();
      }
    },

    //交易列表分页
    onChangeTransactionPage(e) {
      this.transactionPageNum = e;
      switch (this.activeName) {
        case "first":
          this.getEvmTransaction();
          break;
        case "second":
          this.getEvmTransferList("erc20");
          break;
        case "thrid":
          this.getEvmTransferList("erc721");
          break;
      }
    },

    //获取合约交易
    getEvmTransferList(type) {
      const obj = {
        contractType: type,
        contractAddress: this.contractAddress,
        pageIndex: this.transactionPageNum,
        pageSize: this.transactionPageSize,
      };
      Tokens.getEvmTransferList(obj)
        .then((res) => {
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.tableData = result.records;
            this.transactionTotal = result.total;
          }
        })
        .catch(() => {});
    },

    //获取交易列表
    getEvmTransaction() {
      const obj = {
        nftId: this.nftId,
        address: this.contractAddress,
        pageIndex: this.transactionPageNum,
        pageSize: this.transactionPageSize,
      };
      //查询交易
      Tokens.getEvmTransactionList(obj)
        .then((res) => {
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.tableData = result.records;
            this.transactionTotal = result.total;
          }
        })
        .catch(() => {});
    },
    //获取inventory列表
    getNftList() {
      const obj = {
        contractAddress: this.contractAddress,
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
      };
      Tokens.getEvmNftList(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.nftList = result.records;
          this.total = result.total;
        }
        console.log(res, "res===");
      });
    },
    //nft分页
    handleCurrentChange(e) {
      this.pageNum = e;
      this.getNftList();
    },

    //获取NFT详情

    //详情
    getDetail() {
      const obj = { contractAddress: this.contractAddress };
      Tokens.getTokensDetail(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          this.defaultValue = res.result;
          if (this.defaultValue.contractType == "erc721") {
            Tokens.getTokenList({ listType: 2 }).then((req) => {
              if (req.code >= 200 && req.code < 300) {
                const index = req.rows.findIndex((li) => {
                  return this.contractAddress == li.contractsAddress;
                });

                if (index != -1) {
                  this.defaultValue = {
                    ...this.defaultValue,
                    tokenName: req.rows[index].tokenName,
                    remark: req.rows[index].remark,
                    tokenLogo: req.rows[index].tokenLogo,
                    linkList: req.rows[index].linkList,
                    officialAddress: req.rows[index].officialAddress,
                    whitePaperAddress: req.rows[index].whitePaperAddress,
                  };
                  console.log(this.defaultValue, "this.defaultValue =====");
                }
              }
            });
          } else {
            Tokens.getTokenList({ listType: 1 }).then((req) => {
              if (req.code >= 200 && req.code < 300) {
                const index = req.rows.findIndex((li) => {
                  return this.contractAddress == li.contractsAddress;
                });
                if (index != -1) {
                  this.defaultValue = {
                    ...this.defaultValue,
                    tokenName: req.rows[index].tokenName,
                    remark: req.rows[index].remark,
                    tokenLogo: req.rows[index].tokenLogo,
                    linkList: req.rows[index].linkList,
                    officialAddress: req.rows[index].officialAddress,
                    whitePaperAddress: req.rows[index].whitePaperAddress,
                  };
                }
              }
            });
          }
        }
      });
    },
  },
  mounted() {
    this.contractAddress = this.$route.params.id;
    this.type = this.$route.query.type;
    this.getDetail();
    this.getEvmTransaction();
  },
};
</script>
<style lang="less" scoped>
.icp-tokenDetail {
  padding: 0 10% 20px;
  &__title {
    font-size: 24px;
    margin: 30px 0 20px 0;
    line-height: 24px;
    color: #2a2a2a;
  }
  &__top {
    background: #fff;
    padding: 20px 40px;
    display: flex;
    &--left {
      width: 78px;
      height: 78px;
      display: flex;
      justify-content: center;
      align-items: center;
      // text-align: center;
      // line-height: 78px;
      // background: rgba(234, 227, 252, 0.39);
      border-radius: 50%;
      overflow: hidden;
      color: #7f58e0;
      > img {
        width: 100%;
      }
    }
    &--right {
      margin-left: 20px;
      .ittr-title {
        font-size: 20px;
        line-height: 18px;
        color: #5b1cf6;
        margin-top: 15px;
      }
      .ittr-extra {
        font-size: 16px;
        font-weight: 400;
        line-height: 18px;
        color: #2a2a2a;
        margin-top: 10px;
      }
    }
  }
  &__list {
    margin: 20px 0 0 0;
    &--card {
      background-color: #fff;
      border-radius: 8px;
      overflow: hidden;
      .itlc-title {
        background: rgba(88, 65, 172, 1);
        color: #fff;
        font-size: 16px;
        padding: 14px 24px;
      }
    }
  }
  &__transactiontitle {
    margin: 20px 0;
    font-size: 24px;
    line-height: 24px;
    color: #2a2a2a;
  }
  &__col {
    margin-bottom: 20px;
  }
  &__nodata {
    min-height: 300px;
    text-align: center;
    padding: 60px 0 20px 0;
    &--img {
      display: flex;
      justify-content: center;
      align-items: center;
      > img {
        max-width: 100%;
      }
    }
  }
}
</style>
