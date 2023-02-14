<template>
  <div class="icp-tokensAddress">
    <div class="icp-tokensAddress__card">
      <div class="icp-tokensAddress__card--title">Address</div>
      <el-row class="icp-tokensAddress__card--line">
        <el-col :span="4">Chain address:</el-col>
        <el-col :span="9">{{ defaultValue.address || "--" }}</el-col>
        <el-col :span="4" class="itcl-img" :offset="2">
          <!-- v-clipboard:copy="defaultValue.address || ''"
          v-clipboard:success="onCopy" v-clipboard:error="onCopyError" -->
          <img
            @click="onCopyText(defaultValue.address)"
            src="@/assets/icons/copy.png"
        /></el-col>
      </el-row>
      <el-row class="icp-tokensAddress__card--line">
        <el-col :span="4">Evm address:</el-col>
        <el-col :span="9">{{ defaultValue.evmAddress || "--" }}</el-col>
        <el-col :span="4" class="itcl-img" :offset="2">
          <img
            @click="onCopyText(defaultValue.evmAddress)"
            src="@/assets/icons/copy.png"
        /></el-col>
      </el-row>
      <el-divider></el-divider>
      <el-row>
        <el-col :span="4" class="icp-tokensAddress__card--col">
          <img src="@/assets/icplazaLogo.png" />
        </el-col>
        <el-col :span="8">
          <el-row class="itcc-leftrow">
            <el-col class="itcc-leftrow__label" :span="8">Token:</el-col>
            <el-col class="itcc-leftrow__value" :span="16">
              {{ defaultValue.coinName || "--" }}</el-col
            >
          </el-row>
          <el-row class="itcc-leftrow">
            <el-col class="itcc-leftrow__label" :span="8">Total Amount:</el-col>
            <el-col class="itcc-leftrow__value" :span="16">
              {{
                (defaultValue.balance &&
                  onFeixedNum(defaultValue.balance, 6)) ||
                0
              }}
              {{ defaultValue.coinName }}</el-col
            >
          </el-row>
          <el-row class="itcc-leftrow">
            <el-col class="itcc-leftrow__label" :span="8"
              >Available Balance:</el-col
            >
            <el-col class="itcc-leftrow__value" :span="16">
              {{
                (defaultValue.usedAmount &&
                  onFeixedNum(defaultValue.usedAmount, 6)) ||
                0
              }}
              {{ defaultValue.coinName }}</el-col
            >
          </el-row>
          <el-row class="itcc-leftrow">
            <el-col class="itcc-leftrow__label" :span="8"
              >Locked Balance:</el-col
            >
            <el-col class="itcc-leftrow__value" :span="16">
              {{
                (defaultValue.balance &&onFeixedNum(defaultValue.balance, 6)-
                  onFeixedNum(defaultValue.usedAmount, 6)) ||
                0
              }}
              {{ defaultValue.coinName }}</el-col
            >
          </el-row>
        </el-col>
        <el-col :span="12">
          <el-row class="itcc-rightrow">
            <el-col :span="6" class="itcc-rightrow__label">Delegated:</el-col>
            <el-col class="itcc-rightrow__value" :span="16">
              {{
                (defaultValue.delegator_balance &&
                  onFeixedNum(defaultValue.delegator_balance, 6)) ||
                0
              }}
              {{ defaultValue.coinName }}
            </el-col>
          </el-row>
          <el-row class="itcc-rightrow">
            <el-col :span="6" class="itcc-rightrow__label">Unbonding:</el-col>
            <el-col class="itcc-rightrow__value" :span="16">
              {{
                (defaultValue.unBonding_balance &&
                  onFeixedNum(defaultValue.unBonding_balance, 6)) ||
                0
              }}
              {{ defaultValue.coinName }}
            </el-col>
          </el-row>
          <el-row class="itcc-rightrow">
            <el-col :span="6" class="itcc-rightrow__label">Rewards:</el-col>
            <el-col class="itcc-rightrow__value" :span="16">
              {{
                (defaultValue.rewards &&
                  onFeixedNum(defaultValue.rewards, 6)) ||
                0
              }}
              {{ defaultValue.coinName }}
            </el-col>
          </el-row>
          <el-row class="itcc-rightrow">
            <el-col :span="6" class="itcc-rightrow__label">Commission:</el-col>
            <el-col class="itcc-rightrow__value" :span="16">--</el-col>
          </el-row>
        </el-col>
      </el-row>
      <el-divider></el-divider>
      <div class="icp-tokensAddress__card--assets">
        <el-radio-group
          v-model="currentAssets"
          @change="onChangeAssets"
          style="margin-bottom: 20px"
        >
          <el-radio-button label="evm">Evm contract assets</el-radio-button>
          <el-radio-button label="chain">Chain contract assets</el-radio-button>
        </el-radio-group>
        <div
          class="icp-tokensAddress__card--tab"
          v-if="currentAssets !== 'chain'"
        >
          <span
            :style="
              defaultType == 'erc20'
                ? 'border-bottom: 2px solid #5841ab;color:#5841AC;'
                : ''
            "
            @click="onChangeType('erc20')"
            >ICT-20</span
          >
          <span
            :style="
              defaultType == 'erc721'
                ? 'border-bottom: 2px solid #5841ab;color:#5841AC;'
                : ''
            "
            @click="onChangeType('erc721')"
            >ICT-721</span
          >
        </div>
        <div class="itca-token" v-if="tokenList.length > 0">
          <el-row v-for="(item, key) of tokenList" :key="key">
            <el-col :span="11" :offset="1">
              <el-col :span="8" class="itca-token__label"
                >{{
                  currentAssets == "chain"
                    ? tokenEumn["erc20"].label
                    : tokenEumn[defaultType].label
                }}:</el-col
              >
              <el-col :span="8" class="itca-token__value">
                <span v-if="defaultType == 'erc20' || currentAssets == 'chain'">
                  {{ item.tokenName || item.symbol || "--" }}
                </span>
                <span v-if="defaultType == 'erc721'">
                  {{ item.contentAddress || "--" }}
                </span>
              </el-col>
            </el-col>
            <el-col :span="12">
              <el-col :span="8" class="itca-token__label"
                >{{
                  currentAssets == "chain"
                    ? tokenEumn["erc20"].label
                    : tokenEumn[defaultType].valueLabel
                }}:</el-col
              >
              <el-col :span="8" class="itca-token__value">
                <span v-if="defaultType == 'erc721'"
                  >{{ item.nftId || "--" }}
                </span>
                <span v-if="defaultType == 'erc20' || currentAssets == 'chain'">
                  {{
                    (item.amount && onFeixedNum(item.amount, 6)) ||
                    (item.balance && onFeixedNum(item.balance, 6)) ||
                    0
                  }}
                  {{ item.tokenName }}
                </span></el-col
              >
            </el-col>
            <el-divider v-if="key < tokenList.length - 1"></el-divider>
          </el-row>
        </div>
        <div
          class="itca-token"
          v-if="tokenList.length == 0"
          style="display: flex; justify-content: center; color: #909399"
        >
          No data
        </div>
      </div>
    </div>
    <el-row :gutter="20">
      <el-col :span="12" class="icp-tokensAddress__card--table">
        <div class="itct-title">Delegations</div>
        <el-table
          v-loading="loading"
          ref="multipleTable"
          :data="delegationTableData"
          tooltip-effect="dark"
          stripe
          header-cell-class-name="icp-tokensAddress__table--header"
        >
          <el-table-column
            align="center"
            v-for="(item, key) in delegationColumns"
            :key="key"
            :fixed="item.fixed"
            :prop="item.prop"
            :label="item.label"
            :width="item.width"
          >
            <template #default="{ row }">
              <div
                class="maodian"
                v-if="item.prop == 'address'"
                @click="$userInfoRouterFn(row.moniker.operatorAddress)"
              >
                <!-- @click="$userInfoRouterFn(row.moniker.operatorAddress)" -->
                <img class="user_icon" :src="row.moniker.icon" />
                <span>{{ (row.delegation && row.moniker.moniker) || "" }}</span>
              </div>
              <span v-if="item.prop == 'amount'">
                {{
                  (row.balance &&
                    row.balance.amount &&
                    onFeixedNum(row.balance.amount, 6)) ||
                  0
                }}
              </span>
              <span v-if="item.prop == 'shares'">
                {{
                  (row.delegation &&
                    row.delegation.shares &&
                    onFeixedNum(row.delegation.shares, 6)) ||
                  0
                }}</span
              >
            </template>
          </el-table-column>
        </el-table>
        <Pagination
          :total="delegationTotal"
          :pageSize="unbondingpageSize"
          :pageIndex="delegationPageNum"
          @currpageChange="handleDelegationChange"
        ></Pagination>
      </el-col>
      <el-col :span="12" class="icp-tokensAddress__card--table">
        <div class="itct-title">Unbonding Delegations</div>
        <el-table
          v-loading="loading"
          ref="multipleTable"
          :data="unbondingTableData"
          tooltip-effect="dark"
          stripe
          header-cell-class-name="icp-tokensAddress__table--header"
        >
          <el-table-column
            align="center"
            v-for="(item, key) in unbondingColumns"
            :key="key"
            :fixed="item.fixed"
            :prop="item.prop"
            :label="item.label"
            :width="item.width"
          >
            <template #default="{ row }">
              <div
                class="maodian"
                v-if="item.prop == 'address'"
                @click="$userInfoRouterFn(row.validator_address)"
              >
                <img class="user_icon" :src="row.moniker.icon" />
                <span>{{ row.moniker.moniker }}</span>
              </div>
              <span v-if="item.prop == 'amount'">
                {{ (row.balance && onFeixedNum(row.balance, 6)) || 0 }}
              </span>
              <span v-if="item.prop == 'time'">
                {{
                  (row.endtime &&
                    $dayjs.utc(row.endtime).format("YYYY-MM-DD HH:mm:ss")) ||
                  ""
                }}
                UTC</span
              >
            </template>
          </el-table-column>
        </el-table>
        <Pagination
          :total="total"
          :pageSize="unbondingpageSize"
          :pageIndex="unbondingPageNum"
          @currpageChange="handleCurrentChange"
        ></Pagination>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { Tokens } from "@/api";
import { mixinCommon } from "@/mixin";
import { getAddressInfo } from "@/api/tongji";
import { getAddressDelegateInfo } from "@/api/deal";
import Pagination from "@/components/Pagination";

export default {
  components: { Pagination }, //FormSearch
  mixins: [mixinCommon],
  name: "NftIndex",
  data() {
    const delegationColumns = [
      {
        prop: "address",
        label: "Address",
      },
      {
        prop: "amount",
        label: "Amount(ICT)",
      },
      {
        prop: "shares",
        label: "Shares",
        sorter: true,
      },
    ];
    const unbondingColumns = [
      {
        prop: "address",
        label: "Address",
      },
      {
        prop: "amount",
        label: "Amount(ICT)",
      },
      {
        prop: "time",
        label: "End Time",
        sorter: true,
      },
    ];

    return {
      inputValue: "",
      delegationColumns,
      unbondingColumns,
      tableData: [{ id: 1 }],
      loading: false,
      delegationPageNum: 1,
      delegationpageSize: 10,
      delegationTableData: [],
      delegationTotal: 0,
      unbondingPageNum: 1,
      unbondingpageSize: 10,
      unbondingTableData: [],
      unbondingTotal: 0,
      total: 0,
      address: "",
      modelEunm: {
        Token: { label: "Transfer", value: "" },
        Delegate: { label: "Delegate", value: "" },
        TotalAmount: { label: "Distribution", value: "" },
        Governance: { label: "Governance", value: "" },
        Siashing: { label: "Siashing", value: "" },
        NFT: { label: "NFT", value: "" },
        Defi: { label: "Defi", value: "" },
      },
      defaultValue: "",
      currentAssets: "evm",
      tokenList: [],
      chainAddress: "",
      evmAddress: "",
      defaultType: "erc20",
      tokenEumn: {
        erc20: {
          label: "Token",
          prop: "symbol",
          valueLabel: "Total Amount",
          valueProp: "balance",
        },
        erc721: {
          label: "Contract",
          prop: "symbol",
          valueLabel: "Token ID",
          valueProp: "balance",
        },
      },
    };
  },
  watch: {
    chainAddress: {
      handler() {
        this.initDelegationList();
        this.initUnbondingList();
      },
    },
  },
  methods: {
    //切换erc类型
    onChangeType(type) {
      this.defaultType = type;
      if (type == "erc20") {
        this.getEvmAddressInfo();
      } else if (type == "erc721") {
        this.getEvmNftAssetsList();
      }
    },
    //资产详情delegation 列表
    initDelegationList() {
      const obj = {
        type: "delegator",
        address: this.chainAddress,
        pageIndex: this.delegationPageNum,
        pageSize: this.delegationpageSize,
      };
      getAddressDelegateInfo(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.delegationTableData = result.records;
          this.delegationTotal = result.total;
        }
      });
    },
    //资产详情unbonding 列表
    initUnbondingList() {
      const obj = {
        type: "undelegator",
        address: this.chainAddress,
        pageIndex: this.unbondingPageNum,
        pageSize: this.unbondingpageSize,
      };
      getAddressDelegateInfo(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.unbondingTableData = result.records;
          this.unbondingTotal = result.total;
        }
      });
    },

    //资产详情
    initAssetsInfo() {
      const obj = {
        address: this.address,
      };
      getAddressInfo(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.defaultValue = result;
          this.tokenList = result.tokenBalances;
          this.chainAddress = result.address;
          this.evmAddress = result.evmAddress;
          this.$emit("onGetAddress", {
            chainAddress: this.chainAddress,
            evmAddress: this.evmAddress,
          });
          if (this.currentAssets == "evm") {
            this.onChangeAssets();
          }
          //   this.unbondingTotal = result.total;
        }
      });
    },

    handleDelegationChange(e) {
      this.delegationPageNum = e;
      this.initDelegationList();
    },
    handleCurrentChange(e) {
      this.pageNum = e;
      this.initUnbondingList();
    },
    //列表
    find() {
      this.initAssetsInfo();
    },
    //查询evmtoken地址信息
    getEvmAddressInfo() {
      const obj = { address: this.evmAddress };
      Tokens.getAddressInfo(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.tokenList = result.tokens;
          //   this.unbondingTotal = result.total;
        }
      });
    },

    //查询erc721evm合约资产列表
    getEvmNftAssetsList() {
      const obj = { address: this.evmAddress };
      Tokens.getEvmNftAssetsList(obj).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          const { result } = res;
          this.tokenList = result;
          //   this.unbondingTotal = result.total;
        }
      });
    },

    //tab切换展示token列表
    onChangeAssets(e) {
      if (e == "chain") {
        this.initAssetsInfo();
      } else {
        if (this.defaultType == "erc20") {
          this.getEvmAddressInfo();
        } else if (this.defaultType == "erc721") {
          this.getEvmNftAssetsList();
        }
        // this.getEvmAddressInfo();
      }
    },
  },
  mounted() {
    this.address = this.$route.params.id;
    this.find();
  },
};
</script>
<style scoped lang="less">
.icp-tokensAddress {
  &__table {
    /deep/ &--header {
      color: #fff;
      font-weight: 400;
      background-color: #5740a9;
    }
  }
  &__card {
    background: rgba(255, 255, 255, 0.39);
    border: 1px solid #eaebec;
    box-shadow: 0px 3px 8px rgba(56, 54, 54, 0.06);
    border-radius: 5px;
    padding: 24px 0;
    &--tab {
      > span {
        color: #afafaf;
        display: inline-block;
        padding: 15px;
        cursor: pointer;
        // border-bottom: 2px solid #5841ab;
      }
      margin-bottom: 10px;
    }
    &--title {
      padding: 0 35px 10px 35px;
      font-size: 18px;
      font-weight: bold;
      //   line-height: 72px;
      color: #2a2a2a;
    }
    &--line {
      padding: 0 35px;
      font-size: 16px;
      line-height: 40px;
      .itcl-img {
        display: flex;
        align-items: center;
        > img {
          cursor: pointer;
        }
      }
    }
    &--col {
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .itcc-leftrow {
      padding: 12px 0;
      font-size: 14px;
      color: #2a2a2a;
      &__label {
        font-weight: bold;
      }
      &__value {
      }
    }
    .itcc-rightrow {
      padding: 12px 0;
      font-size: 14px;
      color: #2a2a2a;
      &__label {
        font-weight: bold;
      }
      &__value {
      }
    }
    &--assets {
      padding: 0 20px;
      .itca-token {
        border: 1px solid #eaebec;
        padding: 20px 0;
        font-size: 16px;
        color: #2a2a2a;
        &__label {
          font-weight: bold;
          margin-right: 20px;
        }
        &__value {
        }
      }
    }
    &--table {
      .itct-title {
        margin: 34px 0 20px 0;
        font-size: 24px;
        font-weight: bold;
        color: #2a2a2a;
      }
    }
  }
}
</style>

