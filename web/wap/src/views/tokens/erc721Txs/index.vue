<template>
  <div class="token-list">
    <div class="token-list__top">
      <div class="token-list__top--title">
        ICT-20 Txs List {{ total || 0 }} Txs
      </div>
      <div
        class="token-list__top--select"
        @click="
          () => {
            selectDown = !selectDown;
          }
        "
      >
        <span>{{ typeEunm[activeSelect].label }}</span>
        <van-icon v-if="selectDown" name="arrow-up" />
        <van-icon v-if="!selectDown" name="arrow-down" />
      </div>
      <div
        class="token-list__top--position"
        :style="selectDown ? 'display:block;' : 'display:none;'"
      >
        <div class="tltp-select">
          <div
            v-for="(item, key) in typeEunm"
            :key="key"
            @click="
              () => {
                activeSelect = key;
                selectDown = false;
              }
            "
          >
            {{ item.label }}
          </div>
        </div>
      </div>
      <!-- <van-select v-model="transactionType" placeholder="All Txtype">
        <van-option
          v-for="item in typeEunm"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </van-option>
      </van-select> -->
      <van-row :gutter="20">
        <van-col span="24">
          <van-button
            class="token-list__top--button"
            icon="search"
            type="primary"
            :loading="searchLoading"
            @click="onSearch"
          />
        </van-col>
        <!-- <van-col span="4">
          <van-button
            class=" token-list__top--button"
            type="primary"
            :loading="updateLoading"
            icon="replay"
            @click="onSearch"
          ></van-button>
        </van-col> -->
      </van-row>
    </div>
    <div class="token-list__card" v-for="(item, key) in tableData" :key="key">
      <list-card :listItem="columns" :defaultValue="item">
        <template #blockNumber>
          <router-link :to="{ path: '/BlockchainDetails/' + item.blockNumber }"
            >{{ item.blockNumber }}
          </router-link>
        </template>
        <template #nftId>
          <router-link
            :to="{
              path: `/tokens/nft-detail/${item['nftId']}`,
              query: {
                contractAddress: item['contractAddress'],
              },
            }"
          >
            {{ item.nftId || "" }}
          </router-link>
        </template>

        <template #tokenInfo>
          <router-link
            :to="{
              path: `/tokens/detail/${item['contractAddress']}`,
              query: {
                type: 'erc721',
              },
            }"
          >
            <div class="icp-tokens__table--tokenInfo">
              <img
                v-if="item && item.tokenLogo"
                style="width: 25px; height: 25px; border-radius: 20px"
                :src="item.tokenLogo"
              />
              <img
                v-if="item && !item.tokenLogo"
                style="width: 25px; height: 25px; border-radius: 20px"
                src="@/assets/icon/default-icon.png"
              />
              <div class="ittt-box">
                <div class="ittt-title">
                  {{ item["tokenName"] || "--"
                  }}<span v-if="item['tokenSymbol']"
                    >({{ item["tokenSymbol"] }})</span
                  >
                </div>
              </div>
            </div>
            <div class="ittt-remark">{{ item["remark"] || "" }}</div>
          </router-link>
        </template>
        <template #hash>
          <router-link
            :to="{ path: `/tokens/transactionDetail/${item.hash}` }"
            >{{ onFormatStr(item.hash, 6) }}</router-link
          >
        </template>
        <template #amount>
          {{ (item.amount && onFeixedNum(item.amount, 6)) || 0 }}
          {{ item.symbol }}
        </template>

        <template #event>
          {{ item.event.length > 12 ? onFormatStr(item.event, 6) : item.event }}
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
            `${(
              item &&
              item.transferTime &&
              $dayjs.utc(item.transferTime * 1000)
            ).format("YYYY-MM-DD HH:mm:ss")} UTC` || ""
          }}</template
        >
      </list-card>
    </div>
    <div v-if="tableData.length <= 0" class="token-list__nodata">
      <div>
        <img src="@/assets/nodate.png" />
      </div>
      <div>No Data</div>
    </div>
  </div>
</template>

<script>
import { ListCard } from "../components";
// import { NftApi } from "@/api";
import { Tokens } from "@/api";
import { mixinCommon } from "@/mixin";
import _ from "lodash";
export default {
  components: { ListCard },
  mixins: [mixinCommon],
  data() {
    const columns = [
      {
        prop: "hash",
        label: "TXHash",
        slot: "hash",
      },
      {
        prop: "blockNumber",
        label: "Block",
        slot: "blockNumber",
      },
      {
        prop: "event",
        label: "TXType",
        slot: "event",
      },
      {
        prop: "from",
        label: "From",
        slot: "from",
      },
      {
        prop: "to",
        label: "To",
        slot: "to",
      },
      {
        prop: "nftId",
        label: "Token ID",
        slot: "nftId",
      },
      {
        prop: "tokenInfo",
        label: "Token",
        slot: "tokenInfo",
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
      activeSelect: 0,
      selectDown: false,
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
    //防抖
    onDebounceList() {
      this.pageNum = this.pageNum + 1;
      this.find();
    },
    //搜索
    onSearch() {
      this.tableData = [];
      //输入框有内容
      if (this.param) {
        this.pageNum = 1;
        this.pageSize = 10;
        this.find();
      } else {
        //输入框无内容
        this.pageNum = 1;
        this.pageSize = 10;
        this.find();
      }
    },
    // //通过id获取nft 详情
    // getNftDetail() {
    //   const obj = {
    //     tokenId: this.inputValue,
    //   };
    //   Tokens.getNftDetail(obj).then((res) => {
    //     console.log(res, "res===");
    //     if (res.code >= 200 && res.code < 300) {
    //       const { result } = res;
    //       if (result) {
    //         this.tableData = [result];
    //         this.total = 1;
    //       } else {
    //         this.tableData = [];
    //         this.total = 0;
    //       }
    //     }
    //   });
    // },
    //列表
    //列表
    find() {
      const obj = {
        contractType: "erc721",
        transactionType: this.typeEunm[this.activeSelect].value, // this.transactionType
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
            // this.tableData = result.records;
            this.tableData = this.tableData.concat(result.records);
            this.total = result.total;
            Tokens.getTokenList({ listType: 2 })
              .then((res) => {
                if (res.code >= 200 && res.code < 300) {
                  this.tableLoading = false;
                  // this.tableData = this.tableData.concat(result.records);
                  this.tableData = this.tableData.map((el) => {
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
                this.searchLoading = false;
              });
          }
        })
        .catch(() => {
          this.searchLoading = false;
        });
    },
  },
  mounted() {
    this.onDebounceList = _.debounce(this.onDebounceList, 500);
    window.addEventListener("scroll", this.handleScroll);
    this.find();
  },
  created() {},
};
</script>

<style scoped lang="less">
.icp-tokens__table--tokenInfo {
  display: flex;
  justify-content: end;
  .ittt-box {
    margin-left: 10px;
  }
}
.token-list {
  background-color: #f8f8f8;
  min-height: calc(100vh - 124px);
  &__top {
    padding: 20px 15px;
    &--select {
      background: #ffffff;
      border-radius: 5px;
      border: 1px solid #ededed;
      display: flex;
      justify-content: space-between;
      padding: 15px 10px;
    }
    &--position {
      //   position: relative;
      .tltp-select {
        // position: absolute;
        // left: 0;
        // top: 0;
        background-color: #ffffff;
        width: 100%;
        z-index: 1000;
        > div {
          padding: 10px;
        }
      }
    }
    &--title {
      margin: 8px 0 15px 0;
      font-size: 16px;
      font-weight: 600;
      line-height: 20px;
    }
    &--button {
      width: 100%;
      background-color: #633af9;
      border: 0;
      border-radius: 5px;
      margin-top: 15px;
    }
  }
  &__card {
    padding: 10px 15px;
    margin-bottom: 10px;
    background-color: #fff;
    .ittt-title {
      text-align: right;
    }
    .ittt-remark {
      color: #6e7398;
    }
  }
  &__nodata {
    padding-top: 60px;

    > div {
      color: rgba(0, 0, 0, 0.5);
      text-align: center;
      margin: 10px 0;
    }
  }
}
</style>
