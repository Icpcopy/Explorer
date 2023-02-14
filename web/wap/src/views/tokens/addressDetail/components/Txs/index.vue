<template>
  <div class="address-txs">
    <div class="address-txs__card">
      <div class="address-txs__card--title">Txs</div>
      <van-divider />
      <van-radio-group
        v-model="currentRadio"
        @change="onChangeRadio"
        direction="horizontal"
      >
        <van-radio name="chain">Chain Txs</van-radio>
        <van-radio name="contract">Evm Txs</van-radio>
      </van-radio-group>
    </div>
    <div class="infos-title">Transaction records</div>
    <van-tabs v-if="currentRadio == 'chain'" @click="TabonClick">
      <van-tab v-for="(item, index) in tbsData" :title="item" :key="index">
        <section v-if="ByBlockNumberData.length">
          <div class="list-box" v-for="(j, i) in ByBlockNumberData" :key="i">
            <div>
              <span>TXHash:</span>
              <div>
                <span class="colors" @click="$clickHshRouterFn(j)">
                  {{ j.txhash_jiequ }}
                </span>
                <span class="tiaoshu" v-if="j.event.length > 1"
                  >+{{ j.event.length }}</span
                >
              </div>
            </div>
            <div>
              <span>TXType:</span>
              <div>{{ j.event[0].type }}</div>
            </div>
            <div>
              <span>From:</span>

              <span v-if="j.event.length > 1">--</span>
              <div v-else>
                <div v-if="j.event[0].fromIsNull == 0" @click="clickloadingFn">
                  <span
                    class="colors"
                    @click="$headerRouterFn(j.event[0].from, j.event[0].from)"
                    >{{ j.event[0].from_jiequ }}</span
                  >
                </div>
                <div v-if="j.event[0].fromIsNull == 1">--</div>
                <div v-if="j.event[0].fromIsNull == 2">
                  <div
                    class="minimgbox colors"
                    @click="$userInfoRouterFn(j.event[0].from.operatorAddress)"
                  >
                    <img :src="j.event[0].from.icon" />
                    <span>{{ j.event[0].from.moniker }}</span>
                  </div>
                </div>
              </div>
            </div>
            <div>
              <span>To:</span>
              <span v-if="j.event.length > 1">--</span>
              <div v-else>
                <div v-if="j.event[0].toIsNull == 0" @click="clickloadingFn">
                  <span
                    class="colors"
                    @click="$headerRouterFn(j.event[0].to, true)"
                    >{{ j.event[0].to_jiequ }}</span
                  >
                </div>
                <div v-if="j.event[0].toIsNull == 1">--</div>
                <div v-if="j.event[0].toIsNull == 2">
                  <div
                    class="minimgbox colors"
                    @click="$userInfoRouterFn(j.event[0].to.operatorAddress)"
                  >
                    <img :src="j.event[0].to.icon" />
                    <span>{{ j.event[0].to.moniker }}</span>
                  </div>
                </div>
              </div>
            </div>
            <div>
              <span>Amount({{ j.event[0].coinName }}):</span>
              <span v-if="j.isMore" class="tiaoshu">More+</span>
              <div v-else-if="j.event[0].amount">
                {{
                  (j.event[0].amount && onFeixedNum(j.event[0].amount, 6)) || 0
                }}
              </div>
              <div v-else>--</div>
            </div>
            <div>
              <span>Fee({{ j.feeCoinName }}):</span>
              <div>{{ (j.fee && onFeixedNum(j.fee, 6)) || 0 }}</div>
            </div>

            <div>
              <span>Time:</span>
              <div class="huise">{{ j.timestamp }} UTC</div>
            </div>
          </div>
        </section>
        <!-- <div class="date-no" v-else>No Data</div> -->
        <div
          class="address-txs__card--nodata"
          v-if="ByBlockNumberData.length == 0"
        >
          No Data
        </div>
      </van-tab>
      <div v-if="ByBlockNumberData.length > 0">
        <Pagination
          :total="Number(total)"
          :pageSize="Number(pageSize)"
          :pageIndex="Number(pageIndex)"
          @currpageChange="currpageChange"
        >
        </Pagination>
      </div>
    </van-tabs>
    <div class="vloading" v-if="dataBool && currentRadio == 'chain'">
      <van-loading type="spinner" class="spinner" color="#0043FF" />
    </div>
    <div v-if="currentRadio == 'contract'">
      <van-tabs v-model="activeTabs" @change="onChangeTab">
        <van-tab title="Transactions" name="Transfers">
          <div
            class="address-txs__card--content"
            v-for="(item, key) in tableData"
            :key="key"
          >
            <list-card :listItem="TransfersItem" :defaultValue="item">
              <template #txHash>
                <router-link
                  :to="{ path: `/tokens/transactionDetail/${item.txHash}` }"
                  >{{ onFormatStr(item.txHash, 6) }}</router-link
                >
              </template>
              <template #blockNumber>
                <router-link
                  :to="{ path: `/BlockchainDetails/${item.blockNumber}` }"
                  >{{ item.blockNumber }}</router-link
                >
              </template>

              <template #amount>
                {{ (item.amount && onFeixedNum(item.amount, 6)) || 0 }} ict
              </template>
              <template #gasUsed>
                {{ (item.gasUsed && onFeixedNum(item.gasUsed, 6)) || 0 }} ict
              </template>
              <template #method>
                {{
                  item && item.method && item.method.length > 12
                    ? onFormatStr(item.method, 6)
                    : item.method
                }}
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
              <template #blockTime>
                {{
                  (item &&
                    item.blockTime &&
                    `${$dayjs
                      .utc(item.blockTime * 1000)
                      .format("YYYY-MM-DD HH:mm:ss")} UTC`) ||
                  ""
                }}</template
              >
            </list-card>
          </div>
        </van-tab>
        <van-tab title="Ict20 Txs" name="Ict20">
          <div
            class="address-txs__card--content"
            v-for="(item, key) in tableData"
            :key="key"
          >
            <list-card :listItem="defaultIct20Cloumn" :defaultValue="item">
              <template #hash>
                <router-link
                  :to="{ path: `/tokens/transactionDetail/${item.hash}` }"
                  >{{ onFormatStr(item.hash, 6) }}</router-link
                >
              </template>
              <template #amount>
                {{ (item && item.amount && onFeixedNum(item.amount, 6)) || 0 }} {{item.symbol}}
              </template>
              <template #tokenInfo>
                <router-link
                  class="icp-evmTransaction__link"
                  :to="{ path: `/tokens/detail/${item.contractAddress}` }"
                >
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
                  {{ (item && item.tokenName) || "--"
                  }}{{ (item && item.symbol && `(${item["symbol"]})`) || "" }}
                </router-link>
              </template>
              <template #event>
                {{
                  item && item.event && item.event.length > 18
                    ? onFormatStr(item.event, 6)
                    : item.event
                }}
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
        </van-tab>
        <van-tab title="Ict721 Txs" name="Ict721">
          <div
            class="address-txs__card--content"
            v-for="(item, key) in tableData"
            :key="key"
          >
            <list-card :listItem="defaultIct721Cloumn" :defaultValue="item">
              <template #hash>
                <router-link
                  :to="{ path: `/tokens/transactionDetail/${item.hash}` }"
                  >{{ onFormatStr(item.hash, 6) }}</router-link
                >
              </template>
              <template #amount>
                {{ (item.amount && onFeixedNum(item.amount, 6)) || 0 }}
              </template>

              <template #tokenInfo>
                <router-link
                  class="icp-evmTransaction__link"
                  :to="{ path: `/tokens/detail/${item.contractAddress}` }"
                >
                  {{ (item && item.tokenName) || "--"
                  }}{{ (item && item.symbol && `(${item["symbol"]})`) || "" }}
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

              <template #event>
                {{
                  item && item.event && item.event.length > 18
                    ? onFormatStr(item.event, 6)
                    : item.event
                }}
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
        </van-tab>
        <!-- <van-tab title="Inventory" name="Inventory">内容 3</van-tab> -->
      </van-tabs>
      <!-- <div
        v-for="(el, elIndex) in tableData"
        :key="elIndex"
        class="address-txs__card--content"
        :style="elIndex < tableData.length - 1 ? '' : 'border:0'"
      >
        <list-card :listItem="columns" :defaultValue="el">
          <template #txhash>
            <router-link
              :to="{ path: `/tokens/transactionDetail/${el.txhash}` }"
              >{{ onFormatStr(el.txhash, 6) }}</router-link
            >
          </template>
          <template #from>
            <router-link :to="{ path: `/Accountdetails/${el.from}` }">
         
              {{ onFormatStr(el.from, 6) }}
             
            </router-link></template
          >
          <template #to>
       
            <router-link :to="{ path: `/Accountdetails/${el.to}` }">
              {{ onFormatStr(el.to, 6) }}
            </router-link>
         
          </template>
          <template #amount>
            {{ (el.amount && onFeixedNum(el.amount, 6)) || 0 }}
            {{ el.symbol }}
          </template>
          <template #timestamp>
            {{
              `${(el && el.timestamp && $dayjs.utc(el.timestamp)).format(
                "YYYY-MM-DD HH:mm:ss"
              )} UTC` || ""
            }}
          </template>
        </list-card>
      </div> -->
      <div v-if="tableData.length > 0">
        <Pagination
          :total="Number(total)"
          :pageSize="Number(pageSize)"
          :pageIndex="Number(pageIndex)"
          @currpageChange="currpageChange"
        >
        </Pagination>
      </div>
      <div class="address-txs__card--nodata" v-if="tableData.length == 0">
        No Data
      </div>
    </div>
  </div>
</template>

<script>
import { getAllTypeCount, getTransactionByBlockNumber } from "@/api/block";
import { mixinCommon } from "@/mixin";
import { ListCard } from "../../../components";
import Pagination from "@/components/Pagination";
import { Tokens } from "@/api";
import { Notify } from "vant";
export default {
  mixins: [mixinCommon],
  props: {
    chainAddress: { type: String, default: "" },
    evmAddress: { type: String, default: "" },
  },
  components: { ListCard, Pagination },
  data() {
    const columns = [
      {
        prop: "txhash",
        label: "TXHash",
        slot: "txhash",
      },
      {
        prop: "type",
        label: "TXtype",
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
        label: "Amount",
        prop: "amount",
        slot: "amount",
      },
      {
        prop: "timestamp",
        label: "Time",
        slot: "timestamp",
      },
    ];
    const TransfersItem = [
      // { label: "TXHash", prop: "hash", slot: "hash" },
      // { label: "TXtype", prop: "event", slot: "event" },
      // { label: "From", prop: "from", slot: "from" },
      // { label: "To", prop: "to", slot: "to" },
      // { label: "Amount", prop: "amount", slot: "amount" },
      // { label: "timestamp", prop: "transferTime", slot: "transferTime" },
      { label: "TXHash", prop: "txHash", slot: "txHash" },
      { label: "TXtype", prop: "method", slot: "method" },
      { label: "Block", prop: "blockNumber", slot: "blockNumber" },
      { label: "From", prop: "from", slot: "from" },
      { label: "To", prop: "to", slot: "to" },
      { label: "Amount", prop: "amount", slot: "amount" },
      { label: "Fee", prop: "gasUsed", slot: "gasUsed" },
      { label: "timestamp", prop: "blockTime", slot: "blockTime" },
    ];
    const defaultIct20Cloumn = [
      { label: "TXHash", prop: "hash", slot: "hash" },
      { label: "TXtype", prop: "event", slot: "event" },
      // { label: "Block", prop: "block" },
      { label: "From", prop: "from", slot: "from" },
      { label: "To", prop: "to", slot: "to" },
      { label: "Amount", prop: "amount", slot: "amount" },
      { label: "Token", prop: "tokenInfo", slot: "tokenInfo" },
      { label: "timestamp", prop: "transferTime", slot: "transferTime" },
    ];
    const defaultIct721Cloumn = [
      { label: "TXHash", prop: "hash", slot: "hash" },
      { label: "TXtype", prop: "event", slot: "event" },
      // { label: "Block", prop: "block" },
      { label: "From", prop: "from", slot: "from" },
      { label: "To", prop: "to", slot: "to" },
      { label: "Token ID", prop: "nftId",slot:"nftId" },
      // { label: "Token", prop: "tokenInfo", slot: "tokenInfo" },
      { label: "timestamp", prop: "transferTime", slot: "transferTime" },
    ];
    return {
      columns,
      currentRadio: "chain",
      tbsData: [],
      bigType: "transfer",
      activeTabs: "Transfers",
      blockVal: "",
      ByBlockNumberData: [],
      pageIndex: 1,
      pageSize: 10,
      Maxpages_index: 0,
      dataBool: false,
      total: 0,
      tableData: [],
      TransfersItem,
      defaultIct20Cloumn,
      defaultIct721Cloumn,
      contractAddress: "",
    };
  },
  watch: {
    $route() {
      // console.log(from.name, "BlockchainDetails");
      this.pageIndex = 1;
      this.ByBlockNumberData = [];
      this.getAllTypeCountFn();
      this.getTransactionByBlockNumberFn();
      // if (from.name == "BlockchainDetails") {
      //   this.ByBlockNumberData = [];
      //   this.getAllTypeCountFn();
      //   this.getTransactionByBlockNumberFn();
      //   this.$scrrollBox((This) => {
      //     if (This.pageIndex < This.Maxpages_index) {
      //       This.pageIndex++;
      //       This.getTransactionByBlockNumberFn();
      //     }
      //   });
      // }
    },
  },
  mounted() {
    this.contractAddress = this.$route.params.address;
    this.ByBlockNumberData = [];
    this.getAllTypeCountFn();
    this.getTransactionByBlockNumberFn();
    // this.$scrrollBox((This) => {
    //   if (This.pageIndex < This.Maxpages_index) {
    //     This.pageIndex++;
    //     This.getTransactionByBlockNumberFn();
    //   }
    // });
  },
  methods: {
    //tab切换
    onChangeTab(e) {
      // console.log(e, "e===");
      this.activeTabs = e;
      // this.tableData = [];
      this.pageIndex = 1;
      this.pageSize = 10;
      this.tableData = [];
      switch (e) {
        case "Transfers":
          this.getEvmTransaction();
          break;
        case "Ict20":
          this.getTransferList("erc20");
          break;
        case "Ict721":
          this.getTransferList("erc721");
          break;
      }
    },
    //获取交易列表
    getEvmTransaction() {
      const obj = {
        nftId: this.nftId,
        address: this.evmAddress, // this.contractAddress,
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      };
      //查询交易
      Tokens.getEvmTransactionList(obj)
        .then((res) => {
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.tableData = result.records; // this.tableData.concat();
            this.total = result.total;
          }
        })
        .catch(() => {});
    },
    //获取合约交易列表
    getTransferList(type) {
      const obj = {
        nftId: this.nftId,
        contractType: type,
        // contractAddress: this.contractAddress,
        address: this.evmAddress,
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      };

      Tokens.getEvmTransferList(obj)
        .then((res) => {
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.tableData = result.records;
            // this.tableData = this.tableData.concat(result.records);
            this.total = result.total;
          }
        })
        .catch(() => {});
    },
    //单选
    onChangeRadio(e) {
      this.pageIndex = 1;
      this.ByBlockNumberData = [];
      this.tableData = [];
      this.total = 0;
      if (e == "contract") {
        // this.getContractList();
        switch (this.activeTabs) {
          case "Transfers":
            this.getEvmTransaction();
            break;
          case "Ict20":
            this.getTransferList("erc20");
            break;
          case "Ict721":
            this.getTransferList("erc721");
            break;
        }
      } else {
        // this.TabonClick()
        this.getTransactionByBlockNumberFn();
      }
    },
    //分页
    currpageChange(e) {
      console.log(e, "e===");
      this.pageIndex = e;
      if (this.currentRadio == "contract") {
        // this.getContractList();
        switch (this.activeTabs) {
          case "Transfers":
            this.getEvmTransaction();
            break;
          case "Ict20":
            this.getTransferList("erc20");
            break;
          case "Ict721":
            this.getTransferList("erc721");
            break;
        }
      } else {
        this.getTransactionByBlockNumberFn();
      }
    },
    //获取合约Txs列表
    getContractList() {
      let obj = {
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
        address: this.evmAddress,
      };
      Tokens.getEvmTransferList({
        ...obj,
      })
        .then((res) => {
          this.updateLoading = false;
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.tableData = result.records.map((el) => {
              return {
                txhash: el.hash,
                type: el.event,
                from: el.from,
                to: el.to,
                amount: el.amount,
                timestamp: el.transferTime * 1000,
                code: "",
                symbol: el.symbol,
              };
            });
            console.log(this.tableData, " this.tableData===");
            this.total = result.total;
          }
        })
        .catch((err) => {
          console.log(err);
          Notify({ type: "danger", message: err.errError });
          this.updateLoading = false;
        });
    },
    clickloadingFn() {
      this.dataBool = true;
      setTimeout(() => {
        this.dataBool = false;
      }, 1000);
    },
    TabonClick(val) {
      switch (val) {
        case 0:
          this.bigType = "transfer";
          break;
        case 1:
          this.bigType = "staking";
          break;
        case 2:
          this.bigType = "reward";
          break;
        case 3:
          this.bigType = "vote";
          break;
        case 4:
          this.bigType = "jail";
          break;
        case 5:
          this.bigType = "nft";
          break;
        case 6:
          this.bigType = "issues";
          break;
        case 7:
          this.bigType = "defi";
          break;
      }
      this.ByBlockNumberData = [];
      this.pageIndex = 1;
      this.getTransactionByBlockNumberFn();
    },
    getAllTypeCountFn() {
      getAllTypeCount({
        // blockHeight:
        //   this.isStateTxt == "height" ? this.$route.params.id : undefined,
        address: this.chainAddress, //  this.isStateTxt == "address" ? : undefined,
        // bigType: this.bigType,
      }).then((res) => {
        var data = res.result;
        this.tbsData = [
          "Transfer(" + data.transferCount + ")",
          "Staking(" + data.delegatorCount + ")",
          "Distribution(" + data.rewardCount + ")",
          "Governance(" + data.voteCount + ")",
          // "Business(" + data.businessCount + ")",
          "Slashing(" + data.jailCount + ")",
          "Nft(" + data.nftCount + ")",
          "Issues(" + data.issuesCount + ")",
          "Defi(" + data.defiCount + ")",
        ];
      });
    },
    getTransactionByBlockNumberFn() {
      getTransactionByBlockNumber({
        // blockHeight:
        //   this.isStateTxt == "height" ? this.$route.params.id : undefined,
        address: this.chainAddress, //this.isStateTxt == "address" ?: undefined,
        bigType: this.bigType,
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      }).then((res) => {
        const { result } = res;
        this.total = (result && result.total) || 0;
        res.result.records.map((item) => {
          item.timestamp = this.$getUTCTimeFn(item.timestamp);
          item.txhash_jiequ = this.$jiequStrFn(item.txhash);
          item.feeCoinName = item.feeCoinName.toUpperCase();
          item.event.map((children, key) => {
            if (children.from != null && children.from.icon == undefined) {
              children.from_jiequ = this.$jiequStrFn(children.from);
            }
            if (children.to != null && children.to.icon == undefined) {
              children.to_jiequ = this.$jiequStrFn(children.to);
            }
            children.coinName = children.coinName.toUpperCase();
          });
        });
        this.ByBlockNumberData = [...res.result.records];
        // this.ByBlockNumberData.push(...res.result.records);
        this.Maxpages_index = res.result.pages;
      });
    },
  },
};
</script>

<style scoped lang="less">
@import url("../../../../../css/index.less");
.address-txs {
  &__card {
    padding: 15px;
    &--title {
      font-size: 12px;
      font-weight: 600;
      color: #2a2a2a;
    }
    &--content {
      padding: 10px 15px;
      border-bottom: 1px solid #dedede;
    }
    &--nodata {
      padding: 100px 0;
      text-align: center;
      font-size: 18px;
      color: #dedede;
    }
  }
}
</style>
