<template>
  <div class="tokens-detail">
    <div class="tokens-detail__title">Token Details</div>
    <div class="tokens-detail__card">
      <div class="tokens-detail__card--img">
        <div>
          <img
            v-if="defaultValue && defaultValue.tokenLogo"
            :src="(defaultValue && defaultValue.tokenLogo) || ''"
          />
          <img v-else src="@/assets/icon/default-icon.png" />
        </div>
      </div>
      <div class="tokens-detail__card--title">
        {{ (defaultValue && defaultValue.tokenName) || "" }}
        {{
          (defaultValue &&
            defaultValue.tokenSymbol &&
            `(${defaultValue.tokenSymbol})`) ||
          ""
        }}
        <span>{{ tagEunm["erc721"] }}</span>
      </div>
      <div class="tokens-detail__card--extra">
        {{ (defaultValue && defaultValue.remark) || "" }}
      </div>
    </div>
    <div class="tokens-detail__card">
      <div class="tokens-detail__card--subtitle">Overview</div>
      <div>
        <list-card :listItem="overviewList" :defaultValue="defaultValue">
        </list-card>
      </div>
    </div>
    <div class="tokens-detail__card">
      <div class="tokens-detail__card--subtitle">Basic Info</div>
      <div>
        <list-card :listItem="infoList" :defaultValue="defaultValue">
          <template #contentAddress>
            <div>
              {{
                (defaultValue &&
                  defaultValue.contentAddress &&
                  onFormatStr(defaultValue.contentAddress, 6)) ||
                ""
              }}
              <img
                v-if="defaultValue && defaultValue.contentAddress"
                class="tokens-detail__card--icon"
                @click="
                  onCopy(
                    defaultValue.contentAddress,
                    'tokens-detail__card--icon'
                  )
                "
                src="@/assets/icon/copy.png"
              />
            </div>
          </template>
          <template #issuer>
            <router-link
              :to="{
                path: `/Accountdetails/${
                  (defaultValue && defaultValue.issuer) || ''
                }`,
              }"
            >
              {{
                (defaultValue &&
                  defaultValue.issuer &&
                  onFormatStr(defaultValue.issuer, 6)) ||
                ""
              }}
            </router-link>
            <img
              v-if="defaultValue && defaultValue.issuer"
              class="tokens-detail__card--icon"
              @click="onCopy(defaultValue.issuer, 'tokens-detail__card--icon')"
              src="@/assets/icon/copy.png"
            />
          </template>

          <template #mintTime>
            <span style="color: rgba(0, 0, 0, 0.5)">
              {{
                defaultValue &&
                defaultValue.mintTime &&
                $dayjs(defaultValue.mintTime * 1000).format(
                  "YYYY-MM-DD HH:mm:ss"
                )
              }}
              UTC
            </span>
          </template>
        </list-card>
      </div>
    </div>
    <div class="tokens-detail__subtitle">Transaction records</div>
    <van-tabs v-model="activeTabs" @change="onChangeTab">
      <van-tab title="Transfers" name="Transfers">
        <div
          class="tokens-detail__listCard"
          v-for="(item, key) in tableData"
          :key="key"
        >
          <list-card :listItem="defaultCloumn" :defaultValue="item">
            <template #hash>
              <router-link
                :to="{ path: `/tokens/transactionDetail/${item.hash}` }"
                >{{ onFormatStr(item.hash, 6) }}</router-link
              >
            </template>
            <template #from>
              <router-link :to="{ path: `/Accountdetails/${item.from}` }">
                {{ (item.from && onFormatStr(item.from, 7)) || "" }}
              </router-link>
            </template>
            <template #to>
              <router-link :to="{ path: `/Accountdetails/${item.to}` }">
                {{ onFormatStr(item.to, 7) }}
              </router-link></template
            >
            <template #transferTime>
              {{
                (item &&
                  item.transferTime &&
                  `${$dayjs
                    .utc(item.transferTime * 1000)
                    .format("YYYY-MM-DD HH:mm:ss")} UTC`) ||
                ""
              }}</template
            >
          </list-card>
        </div>
        <div class="tokens-detail__card--nodata" v-if="tableData.length == 0">
          No Data
        </div>
      </van-tab>
      <van-tab title="Inventory" name="Inventory">
        <div class="tokens-detail__listCard">
          <inventory-card :defaultValue="defaultValue" :nftId="nftId">
          </inventory-card>
        </div>
        <!-- <div class="tokens-detail__card--nodata" v-if="tableData.length == 0">
          No Data
        </div> -->
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
import { ListCard, InventoryCard } from "../components";
// import Clipboard from "clipboard";
import { Tokens } from "@/api";
import _ from "lodash";
// import { Toast } from "vant";
export default {
  components: { ListCard, InventoryCard },
  mixins: [mixinCommon],
  props: {},
  data() {
    this.onDebounceList = _.debounce(this.onDebounceList, 500);
    const defaultCloumn = [
      { label: "TXHash", prop: "hash", slot: "hash" },
      { label: "TXtype", prop: "event" },
      { label: "From", prop: "from", slot: "from" },
      // { label: "", prop: "transfer", slot: "transfer" },
      { label: "To", prop: "to", slot: "to" },
      { label: "Token ID", prop: "nftId" },
      { label: "timestamp", prop: "transferTime", slot: "transferTime" },
    ];
    return {
      overviewList: [
        {
          label: "Token ID",
          value: "",
          prop: "nftId",
        },
        {
          label: "Holders",
          value: "",
          prop: "addresstotal",
        },
        {
          label: "Transfers",
          value: "",
          prop: "transactionCount",
        },
      ],
      infoList: [
        {
          label: "Contract",
          value: "",
          prop: "contentAddress",
          slot: "contentAddress",
        },
        {
          label: "Issuer",
          value: "",
          prop: "issuer",
          slot: "issuer",
        },
        {
          label: "Issuing Time",
          value: "",
          prop: "mintTime",
          slot: "mintTime",
        },
      ],
      defaultCloumn,
      tagEunm: {
        erc20: "ICT 20",
        erc721: "ICT 721",
      },
      activeTabs: "Transfers",
      clipboard: null,
      type: "",
      total: 0,
      defaultValue: null,
      contractAddress: "",
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      holdersValue: null,
      transferTableData: [],
      holdersTableData: [],
      rankUpdateDisabled: false,
    };
  },
  watch: {
    $route() {
      // this.nftId = this.$route.params.id;
      // this.contractAddress = this.$route.query.contractAddress;
      // this.type = this.$route.query.type;
      // this.getDetail();
      //   this.getEvmTransaction();
    },
  },
  methods: {
    //tab切换
    onChangeTab(e) {
      console.log(e, "e==");
      this.pageNum = 1;
      this.pageSize = 10;
      this.tableData = [];
      this.total = 0;

      if (e == "Transfers") {
        if (this.contractAddress && this.nftId) {
          this.getEvmTransferList();
        }
      } else {
        //inventory
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
            this.tableData = this.tableData.concat(result.records);
            this.total = result.total;
          }
        })
        .catch(() => {});
    },
    //防抖
    onDebounceList() {
      console.log(this.pageNum, "this.pageNum====");
      if (this.pageNum < Math.ceil(this.total / this.pageSize)) {
        this.pageNum = this.pageNum + 1;
        switch (this.activeTabs) {
          case "Transfers":
            this.getEvmTransferList();
            break;
        }
      }
    },
    //滚动时间
    handleScroll(e) {
      var scrollTop =
        e.target.documentElement.scrollTop || e.target.body.scrollTop;
      const scrollBottm =
        e.target.documentElement.scrollHeight -
        e.target.documentElement.clientHeight -
        scrollTop;
      const totalPage = Math.ceil(this.total / this.pageSize);
      if (this.pageNum < totalPage && scrollBottm < 150) {
        this.onDebounceList();
      }
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
    if (this.contractAddress && this.nftId) {
      this.getDetail();
      this.getEvmTransferList();
    }
  },
  created() {},
};
</script>

<style scoped lang="less">
.tokens-detail {
  min-height: 100vh;
  background: #f8f8f8;
  &__title {
    font-size: 14px;
    font-weight: 600;
    color: #2a2a2a;
    padding: 30px 0 10px 15px;
  }
  &__subtitle {
    font-size: 14px;
    font-weight: 600;
    color: #2a2a2a;
    padding: 10px 0 20px 15px;
  }

  &__card {
    background: #fff;
    margin-bottom: 10px;
    text-align: center;
    padding: 15px;
    &--nodata {
      min-height: 200px;
      font-size: 18px;
      color: #d2d2d2;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    &--subtitle {
      font-size: 14px;
      font-weight: 600;
      color: #2a2a2a;
      text-align: left;
      border-bottom: 1px solid #ededed;
      padding: 0 0 15px 0;
      margin-bottom: 10px;
    }
    &--linkimg {
      width: 30px;
      height: 30px;
      display: flex;
      justify-content: center;
      align-items: center;
      overflow: hidden;
      border-radius: 44px;
      border: 1px dotted #dedede;
      float: left;
      margin-left: 5px;
      > img {
        max-width: 100%;
      }
    }
    &--img {
      display: flex;
      justify-content: center;
      align-items: center;
      > div {
        width: 125px;
        height: 125px;
        border-radius: 50%;
        overflow: hidden;
        > img {
          max-width: 100%;
        }
      }
    }
    &--icon {
      margin-left: 10px;
      margin-top: 8px;
    }
    &--title {
      font-size: 18px;
      font-weight: 600;
      color: #5841ac;
      margin: 10px 0;
      > span {
        background: #fff9d5;
        padding: 3px 5px;
        font-size: 10px;
        border-radius: 20px;
        color: #fcb600;
      }
    }
    &--extra {
      font-size: 12px;
      color: #6e7398;
    }
  }
  &__listCard {
    background: #fff;
    margin-bottom: 10px;
    text-align: center;
    padding: 15px;
  }
  &__row {
    padding: 8px 15px;
    background: #fff;
    &--label {
      font-size: 14px;
      font-weight: 600;
      color: #2a2a2a;
      margin-right: 10px;
    }
  }
  &__holderInfo {
    background: #fff;
    padding: 0 15px 10px 15px;
    .van-divider {
      margin: 0;
      padding: 5px 0 10px 0;
      border-color: inherit;
    }
    &--label {
      font-size: 14px;
      font-weight: 600;
      color: #2a2a2a;
      margin-right: 10px;
    }
    &--category {
      display: flex;
      padding: 10px 0;
      .ihdc-color {
        width: 14px;
        height: 14px;
        display: block;
      }
      .ihdc-title {
        margin: 0 10px;
        font-size: 12px;
        color: #969ac3;
      }
      .ihdc-value {
        margin-right: 20px;
      }
    }
    &--progress {
      height: 34px;
      display: flex;
      > span {
        display: block;
        height: 100%;
      }
    }
  }
  &__holderRank {
    padding: 10px 15px;
    display: flex;
    justify-content: space-between;
    line-height: 30px;
    .van-button {
      height: 30px;
      background-color: #5841ac;
      border: 0;
    }
    &--left {
      font-size: 14px;
      font-weight: 600;
      color: #2a2a2a;
    }
  }
}
</style>
