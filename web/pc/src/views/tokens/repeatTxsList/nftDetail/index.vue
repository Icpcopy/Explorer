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
          {{ (defaultValue && defaultValue.tokenName) || "--" }}
          <span v-if="defaultValue && defaultValue.tokenSymbol">
            {{
              (defaultValue &&
                defaultValue.tokenSymbol &&
                `(${defaultValue.tokenSymbol})`) ||
              "--"
            }}
          </span>
          <span
            style="
              font-size: 14px;
              color: #f0b820;
              background: rgba(255, 249, 217, 0.39);
              padding: 3px 5px;
              border-radius: 2px;
            "
          >
            {{ tagEunm["erc721"] }}
          </span>
        </div>
        <div class="ittr-extra">
          {{ (defaultValue && defaultValue.remark) || "--" }}
        </div>
      </div>
    </div>
    <div class="icp-tokenDetail__list">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="icp-tokenDetail__list--card">
            <div class="itlc-title">Overview</div>
            <list-card :ListItem="overviewList" :defaultValue="defaultValue" />
          </div>
        </el-col>
        <el-col :span="16">
          <div class="icp-tokenDetail__list--card">
            <div class="itlc-title">Basic Info</div>
            <list-card :ListItem="infoList" :defaultValue="defaultValue" />
          </div>
        </el-col>
      </el-row>
    </div>
    <!-- <div class="icp-tokenDetail__transactiontitle">Transaction records</div> -->
    <el-tabs v-model="activeName" type="card" @tab-click="onChangeTab">
      <el-tab-pane label="Transfers" name="first">
        <transaction
          :nftId="nftId"
          :tableData="tableData"
          :pageNum="pageNum"
          :pageSize="pageSize"
          :total="total"
          :defauleValue="defaultValue"
          :contract="contractAddress"
          type="erc721"
          :cloumn="defaultCloumn"
      /></el-tab-pane>
      <el-tab-pane label="Inventory" name="second">
        <el-row>
          <el-col :span="5">
            <Inventory :defaultValue="defaultValue" :nftId="nftId" />
          </el-col>
        </el-row>
      </el-tab-pane>
      <!-- <el-tab-pane label="分析" name="third">角色管理</el-tab-pane> -->
    </el-tabs>
  </div>
</template>

<script>
import { ListCard, Transaction, Inventory } from "../../components";
import { Tokens } from "@/api";
export default {
  components: { ListCard, Transaction, Inventory },
  name: "Nft",
  data() {
    const defaultCloumn = [
      { label: "TXHash", prop: "hash" },
      { label: "TXtype", prop: "event" },
      { label: "From", prop: "from" },
      { label: "", prop: "transfer" },
      { label: "To", prop: "to" },
      { label: "Token ID", prop: "nftId" },
      { label: "timestamp", prop: "transferTime", width: 200 },
    ];
    return {
      overviewList: [
        {
          label: "Token ID:",
          value: "",
          prop: "nftId",
          labelSpan: 8,
          valueSpan: 16,
        },
        {
          label: "Holders:",
          value: "",
          prop: "addresstotal",
          labelSpan: 8,
          valueSpan: 16,
        },
        {
          label: "Transfers:",
          value: "",
          prop: "transactionCount",
          labelSpan: 8,
          valueSpan: 16,
        },
      ],
      infoList: [
        {
          label: "Contract:",
          value: "",
          prop: "contentAddress",
          labelSpan: 5,
          valueSpan: 19,
        },
        {
          label: "Issuer:",
          value: "",
          prop: "issuer",
          labelSpan: 5,
          valueSpan: 19,
        },
        {
          label: "Issuing Time:",
          value: "",
          prop: "mintTime",
          labelSpan: 5,
          valueSpan: 19,
        },
      ],
      defaultCloumn,
      tagEunm: {
        erc20: "ICT 20",
        erc721: "ICT 721",
      },
      tableData: [],
      activeName: "first",
      contractAddress: "",
      nftId: "",
      defaultValue: null,
      pageNum: 1,
      pageSize: 10,
    };
  },
  watch: {},
  methods: {
    //tab切换
    onChangeTab(e) {
      console.log(e.index, "e==");
      this.pageNum = 1;
      this.pageSize = 10;
      this.tableData = [];
      this.total = 0;
      //inventory
      if (e.index == 1) {
        //
      } else {
        this.getEvmTransferList();
      }
    },
    //获取交易列表
    getEvmTransferList() {
      const obj = {
        nftId: this.nftId,
        contractAddress: this.contractAddress,
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
      };
      Tokens.getEvmTransferList(obj)
        .then((res) => {
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.tableData = result.records;
            this.total = result.total;
          }
        })
        .catch(() => {});
    },
    //获取nft详情
    getDetail() {
      const obj = { contractAddress: this.contractAddress, nftId: this.nftId };
      Tokens.getNftInfo(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          this.defaultValue = res.result;
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
        }
      });
    },
  },
  mounted() {
    this.nftId = this.$route.params.id;
    this.contractAddress = this.$route.query.contractAddress;

    this.getDetail();
    this.getEvmTransferList()
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
    margin: 20px 0 40px 0;
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
}
</style>
