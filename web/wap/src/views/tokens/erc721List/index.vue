<template>
  <div class="nft-list">
    <div class="nft-list__top">
      <div class="nft-list__top--title">
        Token Tracker <span>( Total ICT-721 Token:{{ total }})</span>
      </div>
      <van-field
        class="nft-list__top--input"
        v-model="param"
        placeholder="Search for Token Name or Address"
        clearable
        @clear="
          () => {
            param = '';
          }
        "
      />
      <van-button
        class="nft-list__top--button"
        icon="search"
        type="primary"
        :loading="searchLoading"
        @click="onSearch"
      />
    </div>
    <div class="nft-list__card" v-for="(item, key) in tableData" :key="key">
      <list-card :listItem="columns" :defaultValue="item">
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
      </list-card>
    </div>
    <div v-if="tableData.length <= 0" class="nft-list__nodata">
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
        prop: "serailNo",
        label: "#",
      },
      {
        prop: "tokenInfo",
        label: "Token",
        slot: "tokenInfo",
      },
      {
        prop: "transactiontotalCount",
        label: "Transfer(Total)",
        // sorter: true,
      },
      {
        prop: "addresstotal",
        label: "Holders",
        sorter: true,
      },
    ];
    return {
      columns,
      param: "",
      total: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
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
    find() {
      const obj = {
        param: this.param,
        contractType: 2,
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
      };
      this.searchLoading = true;
      Tokens.getTokenContractsList(obj)
        .then((req) => {
          if (req.code >= 200 && req.code < 300) {
            const { result } = req;
            this.tableData = this.tableData.concat(result.records);
            this.total = result.total;
            Tokens.getTokenList({ listType: 2 })
              .then((res) => {
                this.searchLoading = false;
                if (res.code >= 200 && res.code < 300) {
                  this.tableData = this.tableData.map((el, index) => {
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
                            ? Number(
                                `${
                                  !((this.pageNum - 1) * this.pageSize)
                                    ? index + 1
                                    : Number(
                                        (this.pageNum - 1) * this.pageSize
                                      ) + Number(index + 1)
                                }`
                              )
                            : Number(`${this.pageNum * (this.pageSize / 10)}0`),
                      };
                    } else {
                      return {
                        ...el,
                        serailNo:
                          index < this.pageSize - 1
                            ? Number(
                                `${
                                  !((this.pageNum - 1) * this.pageSize)
                                    ? index + 1
                                    : Number(
                                        (this.pageNum - 1) * this.pageSize
                                      ) + Number(index + 1)
                                }`
                              )
                            : Number(`${this.pageNum * (this.pageSize / 10)}0`),
                      };
                    }
                  });
                }
              })
              .catch(() => {
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
    this.onDebounceList = _.debounce(this.onDebounceList, 1000);
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
.nft-list {
  background-color: #f8f8f8;
  min-height: calc(100vh - 124px);
  &__top {
    padding: 20px 15px;
    &--title {
      margin: 8px 0 15px 0;
      font-size: 14px;
      font-weight: 600;
      line-height: 20px;
      > span {
        font-size: 10px;
        font-weight: 400;
      }
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
