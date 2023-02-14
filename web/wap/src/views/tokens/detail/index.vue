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
        <span>{{ defaultValue && tagEunm[defaultValue.contractType] }}</span>
      </div>
      <div class="tokens-detail__card--extra">
        {{ (defaultValue && defaultValue.remark) || "" }}
      </div>
    </div>
    <div class="tokens-detail__card">
      <div class="tokens-detail__card--subtitle">Overview</div>
      <div>
        <list-card
          :listItem="
            defaultValue &&
            ercDetailListEunm[defaultValue.contractType].overviewList
          "
          :defaultValue="defaultValue"
        >
          <template #totalSupply>
            {{ onFeixedNum(defaultValue.totalSupply, 6) }}
          </template>
          <template #circulation>
            {{ onFeixedNum(defaultValue.circulation, 6) }}
          </template>
        </list-card>
      </div>
    </div>
    <div class="tokens-detail__card">
      <div class="tokens-detail__card--subtitle">Basic Info</div>
      <div>
        <list-card
          :listItem="
            defaultValue &&
            ercDetailListEunm[defaultValue.contractType].infoList
          "
          :defaultValue="defaultValue"
        >
          <template #contractAddress>
            <div>
              {{ onFormatStr(defaultValue.contractAddress, 6) }}
              <img
                v-if="defaultValue && defaultValue.contractAddress"
                class="tokens-detail__card--icon"
                @click="
                  onCopy(
                    defaultValue.contractAddress,
                    'tokens-detail__card--icon'
                  )
                "
                src="@/assets/icon/copy.png"
              />
            </div>
          </template>
          <template #owner>
            <router-link
              :to="{ path: `/Accountdetails/${defaultValue.owner}` }"
            >
              {{ onFormatStr(defaultValue.owner, 6) }}
            </router-link>
            <img
              v-if="defaultValue && defaultValue.owner"
              class="tokens-detail__card--icon"
              @click="onCopy(defaultValue.owner, 'tokens-detail__card--icon')"
              src="@/assets/icon/copy.png"
            />
          </template>
          <template #issuerTimestamp>
            <span style="color: rgba(0, 0, 0, 0.5)">
              {{
                defaultValue.issuerTimestamp &&
                $dayjs(defaultValue.issuerTimestamp * 1000).format(
                  "YYYY-MM-DD HH:mm:ss"
                )
              }}
              UTC
            </span>
          </template>
        </list-card>
      </div>
    </div>
    <div class="tokens-detail__card">
      <div class="tokens-detail__card--subtitle">More</div>
      <div>
        <list-card
          :listItem="
            defaultValue &&
            ercDetailListEunm[defaultValue.contractType].moreList
          "
          :defaultValue="defaultValue"
        >
          <template #officialAddress>
            <a
              v-if="defaultValue && defaultValue['officialAddress']"
              :href="(defaultValue && defaultValue['officialAddress']) || ''"
              target="_blank"
            >
              {{ (defaultValue && defaultValue["officialAddress"]) || "" }}
            </a>
            <div v-else>--</div>
          </template>
          <template #whitePaperAddress>
            <a
              v-if="defaultValue && defaultValue['whitePaperAddress']"
              :href="(defaultValue && defaultValue['whitePaperAddress']) || ''"
              target="_blank"
            >
              {{ (defaultValue && defaultValue["whitePaperAddress"]) || "" }}
            </a>
            <div v-else>--</div>
          </template>
          <template #linkList>
            <!-- {{ defaultValue }} -->
            <div v-if="defaultValue && defaultValue['linkList']">
              <a
                class="tokens-detail__card--linkimg"
                v-for="(li, idx) in defaultValue['linkList']"
                :key="idx"
                :href="li.linkAddress"
                target="_blank"
              >
                <img :src="li.linkLogo || ''" />
              </a>
            </div>
            <div v-else>--</div>
          </template>
        </list-card>
      </div>
    </div>
    <div class="tokens-detail__subtitle">Transaction records</div>
    <van-tabs v-model="activeTabs" @change="onChangeTab">
      <van-tab title="Transactions" name="Transfers">
        <div
          class="tokens-detail__listCard"
          v-for="(item, key) in transferTableData"
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
        <div
          class="tokens-detail__card--nodata"
          v-if="transferTableData.length == 0"
        >
          No Data
        </div>
      </van-tab>
      <van-tab title="Ict20 Txs" name="Ict20">
        <div
          class="tokens-detail__listCard"
          v-for="(item, key) in transferTableData"
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
              {{ (item && item.amount && onFeixedNum(item.amount, 6)) || 0 }}
              {{ item.symbol && item.symbol }}
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
        <div
          class="tokens-detail__card--nodata"
          v-if="transferTableData.length == 0"
        >
          No Data
        </div>
      </van-tab>
      <van-tab title="Ict721 Txs" name="Ict721">
        <div
          class="tokens-detail__listCard"
          v-for="(item, key) in transferTableData"
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
        <div
          class="tokens-detail__card--nodata"
          v-if="transferTableData.length == 0"
        >
          No Data
        </div>
      </van-tab>
      <van-tab title="Holdres" name="Holdres">
        <div class="tokens-detail__row">
          <span class="tokens-detail__row--label">Token Total Supply:</span>
          <span class="tokens-detail__row--value">
            {{
              (holdersValue &&
                holdersValue.totalSupply &&
                onFeixedNum(holdersValue.totalSupply, 6)) ||
              0
            }}</span
          >
        </div>
        <div class="tokens-detail__row">
          <span class="tokens-detail__row--label">Total Token Holders:</span>
          <span class="tokens-detail__row--value">
            {{
              (holdersValue &&
                holdersValue.holderCount &&
                onFeixedNum(holdersValue.holderCount, 6)) ||
              0
            }}</span
          >
        </div>

        <div class="tokens-detail__holderInfo">
          <van-divider :dashed="true" />
          <div class="tokens-detail__holderInfo--label">
            Asset Breakdown by Holders
          </div>
          <van-row style="display: flex">
            <van-col
              :span="12"
              class="tokens-detail__holderInfo--category"
              v-for="(item, key) in proportionEunm"
              :key="key"
            >
              <span
                class="ihdc-color"
                :style="{ 'background-color': item.color }"
              ></span>
              <span class="ihdc-title">{{ item.label }}</span>
              <span class="ihdc-value">{{ item.value }}</span>
            </van-col>
          </van-row>
          <div class="tokens-detail__holderInfo--progress">
            <span
              :style="{
                'background-color': proportionEunm[1].color,
                width: proportionEunm[1].value,
              }"
            ></span>
            <span
              :style="{
                'background-color': proportionEunm[2].color,
                width: proportionEunm[2].value,
              }"
            ></span>
            <span
              :style="{
                'background-color': proportionEunm[3].color,
                width: proportionEunm[3].value,
              }"
            ></span>
            <span
              :style="{
                'background-color': proportionEunm[4].color,
                width: proportionEunm[4].value,
              }"
            ></span>
            <span
              :style="{
                'background-color': proportionEunm[5].color,
                width: proportionEunm[5].value,
              }"
            ></span>
            <span
              :style="{
                'background-color': proportionEunm[6].color,
                width: proportionEunm[6].value,
              }"
            ></span>
          </div>
        </div>
        <div class="tokens-detail__holderRank">
          <span class="tokens-detail__holderRank--left"> Top 100 holders </span>
          <van-button
            :loading="rankUpdateDisabled"
            @click="onUpdateRank"
            icon="replay"
            type="primary"
            class="tokens-detail__holderRank--right"
          />
        </div>
        <div
          class="tokens-detail__listCard"
          v-for="(item, key) in holdersTableData"
          :key="key"
        >
          <list-card :listItem="holdresListCloumn" :defaultValue="item">
            <template #address>
              <router-link :to="{ path: `/Accountdetails/${item.address}` }">
                {{ onFormatStr(item.address, 6) }}</router-link
              >
            </template>
            <template #balance>
              {{ (item.balance && onFeixedNum(item.balance, 6)) || 0 }}
            </template>
            <template #percentage>
              {{
                (item &&
                  item.circulation &&
                  onFeixedNum((row["balance"] * 100) / item.circulation, 2)) ||
                "0"
              }}
              %
            </template>
          </list-card>
        </div>
      </van-tab>
      <van-tab
        title="Inventory"
        name="Inventory"
        v-if="defaultValue && defaultValue.contractType == 'erc721'"
      >
        <div v-if="nftList.length > 0">
          <div v-for="(item, key) in nftList" :key="key">
            <inventory-card :defaultValue="item"> </inventory-card>
          </div>
        </div>
        <div class="tokens-detail__card--nodata" v-if="nftList.length == 0">
          No Data
        </div>
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
    const erc20Obj = {
      overviewList: [
        { label: "--", value: "--", prop: "", labelSpan: 10, valueSpan: 14 },
        {
          label: "Total Supply",
          value: "",
          prop: "totalSupply",
          slot: "totalSupply",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Circulating Supply",
          value: "",
          prop: "circulation",
          slot: "circulation",
          labelSpan: 12,
          valueSpan: 12,
        },
        {
          label: "Total Market Value",
          value: "",
          prop: "",
          labelSpan: 12,
          valueSpan: 12,
        },
        {
          label: "Market Cap",
          value: "",
          prop: "",
          labelSpan: 10,
          valueSpan: 14,
        },
      ],
      infoList: [
        {
          label: "Contract",
          value: "",
          prop: "contractAddress",
          labelSpan: 10,
          valueSpan: 14,
          slot: "contractAddress",
        },
        {
          label: "Issuer",
          value: "",
          prop: "owner",
          labelSpan: 10,
          valueSpan: 14,
          slot: "owner",
        },
        {
          label: "Issuing Time",
          value: "",
          prop: "issuerTimestamp",
          labelSpan: 10,
          valueSpan: 14,
          slot: "issuerTimestamp",
        },
        {
          label: "Decimals",
          value: "",
          prop: "decimal",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Symbol",
          value: "",
          prop: "tokenSymbol",
          labelSpan: 10,
          valueSpan: 14,
        },
        // { label: "Minimum unit", value: "22", prop: "" },
      ],
      moreList: [
        {
          label: "Holders",
          value: "",
          prop: "addresstotal",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Transfers",
          value: "",
          prop: "transactiontotalCount",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Official Website",
          value: "",
          prop: "officialAddress",
          labelSpan: 10,
          valueSpan: 14,
          slot: "officialAddress",
        },
        {
          label: "White Paper",
          value: "",
          prop: "whitePaperAddress",
          labelSpan: 10,
          valueSpan: 14,
          slot: "whitePaperAddress",
        },
        {
          label: "Social Profiles",
          value: "",
          prop: "linkList",
          labelSpan: 10,
          valueSpan: 14,
          slot: "linkList",
        },
      ],
    };
    const erc721Obj = {
      overviewList: [
        {
          label: "Total Supply",
          value: "",
          prop: "totalSupply",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Holders",
          value: "",
          prop: "addresstotal",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Transfers",
          value: "",
          prop: "transactiontotalCount",
          labelSpan: 10,
          valueSpan: 14,
        },
      ],
      infoList: [
        {
          label: "Contract",
          value: "",
          prop: "contractAddress",
          slot: "contractAddress",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Issuer",
          value: "",
          prop: "owner",
          slot: "owner",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Issuing Time",
          value: "",
          prop: "issuerTimestamp",
          slot: "issuerTimestamp",
          labelSpan: 10,
          valueSpan: 14,
        },
      ],
      moreList: [
        {
          label: "Official Website",
          value: "",
          prop: "officialAddress",
          slot: "officialAddress",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "White Paper",
          value: "",
          prop: "whitePaperAddress",
          slot: "whitePaperAddress",
          labelSpan: 10,
          valueSpan: 14,
        },
        {
          label: "Social Profiles",
          value: "",
          prop: "linkList",
          slot: "linkList",
          labelSpan: 10,
          valueSpan: 14,
        },
      ],
    };

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
      { label: "Token ID", prop: "nftId", slot: "nftId" },
      // { label: "Token", prop: "tokenInfo", slot: "tokenInfo" },
      { label: "timestamp", prop: "transferTime", slot: "transferTime" },
    ];
    const proportionEunm = {
      1: { label: "NO.1~10:", value: "0%", color: "#3E91FF" },
      2: { label: "NO.11~50:", value: "0%", color: "#83C958" },
      3: { label: "NO.51~100:", value: "0%", color: "#7F5FE7" },
      4: { label: "NO.101~500:", value: "0%", color: "#F08856" },
      5: { label: "NO.501~1000:", value: "0%", color: "#F2D015" },
      6: { label: "NO.1000~:", value: "0%", color: "#BF5ECB" },
    };
    const holdresListCloumn = [
      { label: "#", prop: "serailNo" },
      { label: "Address", prop: "address", slot: "address" },
      { label: "Quantity", prop: "balance", slot: "balance" },
      { label: "Percentage", prop: "percentage", slot: "percentage" },
    ];
    this.onDebounceList = _.debounce(this.onDebounceList, 500);
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
      activeTabs: "Transfers",
      TransfersItem,
      proportionEunm,
      holdresListCloumn,
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
      nftList: [],
      rankUpdateDisabled: false,
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
    //刷新排名列表
    onUpdateRank() {
      this.pageNum = 1;
      this.pageSize = 10;
      this.getAssteDistribution();
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
          this.nftList = this.nftList.concat(result.records);
          this.total = result.total;
        }
        console.log(res, "res===");
      });
    },

    //tab切换
    onChangeTab(e) {
      // console.log(e, "e===");
      this.activeTabs = e;
      // this.tableData = [];
      this.pageNum = 1;
      this.pageSize = 10;
      this.transferTableData = [];
      this.holdersTableData = [];
      this.nftList = [];
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
        case "Holdres":
          this.getAssteDistribution();
          break;
        case "Inventory":
          this.getNftList();
          break;
      }
    },
    //获取交易列表
    getEvmTransaction() {
      console.log(this.contractAddress, " this.contractAddress====");
      if (this.contractAddress) {
        const obj = {
          nftId: this.nftId,
          address: this.contractAddress,
          pageIndex: this.pageNum,
          pageSize: this.pageSize,
        };
        //查询交易
        Tokens.getEvmTransactionList(obj)
          .then((res) => {
            if (res.code >= 200 && res.code < 300) {
              const { result } = res;
              this.transferTableData = this.transferTableData.concat(
                result.records
              );
              this.total = result.total;
            }
          })
          .catch(() => {});
      }
    },
    //获取合约交易列表
    getTransferList(type) {
      const obj = {
        nftId: this.nftId,
        contractType: type,
        contractAddress: this.contractAddress,
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
      };

      Tokens.getEvmTransferList(obj)
        .then((res) => {
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.transferTableData = this.transferTableData.concat(
              result.records
            );
            console.log(this.transferTableData, " this.transferTableData=====");
            this.total = result.total;
          }
        })
        .catch(() => {});
    },
    //获取持有者详情
    getAssteDistribution() {
      const obj = {
        contractAddress: this.contractAddress,
        pageIndex: this.pageNum,
        pageSize: this.pageSize,
      };
      this.rankUpdateDisabled = true;
      Tokens.getAssteDistribution(obj)
        .then((res) => {
          this.rankUpdateDisabled = false;
          if (res.code >= 200 && res.code < 300) {
            const { result } = res;
            this.holdersValue = result;
            result.assteDistribution.map((el, index) => {
              this.proportionEunm[index + 1].label = `NO.${el.startIndex} ~${
                el.endIndex || ""
              }`;
              this.proportionEunm[index + 1].value = ` ${
                (el.totalAmount &&
                  this.onFeixedNum(
                    (el.totalAmount / result.circulation) * 100,
                    2
                  )) ||
                0
              }%`;
              return el;
            });
            this.holdersTableData =
              result &&
              result.topList &&
              result.topList.map((el, index) => {
                return {
                  ...el,
                  serailNo: index + 1,
                };
              });

            // this.total = result.total;
          }
        })
        .catch(() => {
          this.rankUpdateDisabled = false;
        });
    },
    //详情
    getDetail() {
      const obj = { contractAddress: this.contractAddress };
      Tokens.getTokensDetail(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          this.defaultValue = res.result;
          // this.TransfersItem = this.TransfersItem.map((el) => {
          //   if (el.prop == "amount") {
          //     return {
          //       ...el,
          //       label: `Amount(${this.defaultValue.tokenSymbol})`,
          //     };
          //   } else {
          //     return el;
          //   }
          // });

          Tokens.getTokenList({
            listType: this.defaultValue.contractType == "erc721" ? 2 : 1,
          }).then((req) => {
            if (req.code >= 200 && req.code < 300) {
              const index = req.rows.findIndex((li) => {
                return this.contractAddress == li.contractsAddress;
              });
              if (index != -1) {
                this.defaultValue = {
                  ...this.defaultValue,
                  remark: req.rows[index].remark,
                  tokenLogo: req.rows[index].tokenLogo,
                  tokenName: req.rows[index].tokenName,
                  linkList: req.rows[index].linkList,
                  officialAddress: req.rows[index].officialAddress,
                  whitePaperAddress: req.rows[index].whitePaperAddress,
                };
              }
            }
          });
        }
      });
    },
    //防抖
    onDebounceList() {
      if (this.pageNum < Math.ceil(this.total / this.pageSize)) {
        this.pageNum = this.pageNum + 1;
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
          case "Holdres":
            this.getAssteDistribution();
            break;
          case "Inventory":
            this.getNftList();
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
  },
  mounted() {
    this.contractAddress = this.$route.params.id;
    this.type = this.$route.query.type;
    console.log(this.$route.params, "this.$route.params===");
    this.getDetail();
    // this.transferTableData = [];
    this.getEvmTransaction();

    // window.addEventListener("scroll", this.handleScroll);
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
      word-break: break-all;
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
