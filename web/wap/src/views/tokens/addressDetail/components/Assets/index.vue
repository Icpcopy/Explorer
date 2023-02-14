<template>
  <div class="address-assets">
    <div class="address-assets__title">Address</div>
    <div class="address-assets__address">
      <van-row>
        <van-col :span="8" class="address-assets__address--label"
          >Chain address:</van-col
        >
        <van-col :span="16">
          {{
            (defaultValue &&
              defaultValue.address &&
              onFormatStr(defaultValue.address, 9)) ||
            "--"
          }}
          <img
            v-if="defaultValue && defaultValue.address"
            class="address-assets__address--img"
            @click="
              onCopy(defaultValue.address, 'address-assets__address--img')
            "
            src="@/assets/icon/copy.png"
          />
        </van-col>
      </van-row>
      <van-divider />
      <van-row>
        <van-col :span="8" class="address-assets__address--label"
          >Evm address:</van-col
        >
        <van-col :span="16">
          {{
            (defaultValue &&
              defaultValue.evmAddress &&
              onFormatStr(defaultValue.evmAddress, 9)) ||
            "--"
          }}
          <img
            v-if="defaultValue && defaultValue.evmAddress"
            class="address-assets__address--img"
            @click="
              onCopy(defaultValue.evmAddress, 'address-assets__address--img')
            "
            src="@/assets/icon/copy.png"
        /></van-col>
      </van-row>
    </div>
    <div class="address-assets__info">
      <list-card :listItem="tokenCloumns" :defaultValue="defaultValue">
        <template #balance>{{
          (defaultValue &&
            defaultValue.balance &&
            onFeixedNum(defaultValue.balance, 6)) ||
          0
        }}</template>
        <template #usedAmount>{{
          (defaultValue &&
            defaultValue.usedAmount &&
            onFeixedNum(defaultValue.usedAmount, 6)) ||
          0
        }}</template>
        <template #lockedBalance>
          {{
            (defaultValue &&
              defaultValue.balance &&
              onFeixedNum(defaultValue.balance - defaultValue.usedAmount, 6)) ||
            0
          }}
        </template>
      </list-card>
      <van-divider />
      <list-card
        :listItem="delegatedCloumns"
        :defaultValue="defaultValue"
      ></list-card>
    </div>
    <van-row class="address-assets__tab">
      <van-col
        span="12"
        :class="`address-assets__tab--col ${
          activedTab == 'evm' ? 'is-actived' : ''
        }`"
        @click="onChangeTab('evm')"
        >EVM contract assets</van-col
      >
      <van-col
        span="12"
        :class="`address-assets__tab--col ${
          activedTab == 'chain' ? 'is-actived' : ''
        }`"
        @click="onChangeTab('chain')"
        >Chain contract assets</van-col
      >
    </van-row>
    <van-row class="address-assets__subtab" v-if="activedTab !== 'chain'">
      <van-col
        span="12"
        :class="`address-assets__subtab--col ${
          defaultType == 'erc20' ? 'is-actived' : ''
        }`"
        @click="onChangeSubTab('erc20')"
        >ICT-20</van-col
      >
      <van-col
        span="12"
        :class="`address-assets__subtab--col ${
          defaultType == 'erc721' ? 'is-actived' : ''
        }`"
        @click="onChangeSubTab('erc721')"
        >ICT-721</van-col
      >
    </van-row>
    <div class="address-assets__tokenList">
      <div
        class="address-assets__tokenList--row"
        v-for="(item, key) of tokenList"
        :key="key"
        :style="tokenList.length - 1 == key ? 'border-bottom:0' : ''"
      >
        <van-row class="address-assets__tokenList--line">
          <van-col span="6" class="address-assets__tokenList--label"
            >{{
              defaultType == "chain"
                ? tokenEumn["erc20"].label
                : tokenEumn[defaultType].label
            }}:</van-col
          >
          <van-col span="16">
            <span v-if="defaultType == 'erc20' || activedTab == 'chain'">
              {{ item.tokenName || item.symbol || "--" }}
            </span>
            <span v-if="defaultType == 'erc721'">
              {{ item.contentAddress || "--" }}
            </span>
          </van-col>
        </van-row>
        <!-- <van-col span="24"> -->
        <van-divider />
        <!-- </van-col> -->

        <van-row span="24" class="address-assets__tokenList--line">
          <van-col span="6" class="address-assets__tokenList--label">
            {{
              activedTab == "chain"
                ? tokenEumn["erc20"].valueLabel
                : tokenEumn[defaultType].valueLabel
            }}:</van-col
          >
          <van-col span="16">
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
            </span>
          </van-col>
        </van-row>
      </div>
      <div
        class="address-assets__tokenList--nodata"
        v-if="tokenList.length == 0"
      >
        NO Data
      </div>
    </div>
    <div class="address-assets__title" style="font-weight: 600">
      Delegations
    </div>
    <div
      class="address-assets__card"
      v-for="(item, key) in delegationTableData"
      :key="key"
    >
      <list-card :listItem="DelegationsCloumns" :defaultValue="item">
        <template #address>
          {{
            (item.moniker &&
              item.moniker.operatorAddress &&
              onFormatStr(item.moniker.operatorAddress, 9)) ||
            ""
          }}
        </template>
        <template #amount>
          {{
            (item.balance &&
              item.balance.amount &&
              onFeixedNum(item.balance.amount, 6)) ||
            0
          }}
        </template>
        <template #shares>
          {{
            (item.delegation &&
              item.delegation.shares &&
              onFeixedNum(item.delegation.shares, 6)) ||
            0
          }}
        </template>
      </list-card>
    </div>
    <div class="address-assets__nodata" v-if="delegationTableData.length == 0">
      NO Data
    </div>
    <Pagination
      :total="Number(delegationsTotal)"
      :pageSize="Number(delegationsPageSize)"
      :pageIndex="Number(delegationsPageNum)"
      @currpageChange="delegationsCurrpageChange"
    ></Pagination>
    <div class="address-assets__title" style="font-weight: 600">
      Unbonding Delegations
    </div>
    <div
      class="address-assets__card"
      v-for="(item, key) in unbondingTableData"
      :key="key"
    >
      <list-card :listItem="UnbondingCloumns" :defaultValue="item">
        <template #address>
          {{
            (item.validator_address &&
              onFormatStr(item.validator_address, 9)) ||
            ""
          }}
        </template>
        <template #amount>
          {{ (item.balance && onFeixedNum(item.balance, 6)) || 0 }}
        </template>
        <template #endTime>
          {{
            (item.endtime &&
              $dayjs(item.endtime).format("YYYY-MM-DD HH:mm:ss")) ||
            ""
          }}
          UTC
        </template>
      </list-card>
    </div>
    <div class="address-assets__nodata" v-if="unbondingTableData.length == 0">
      NO Data
    </div>

    <Pagination
      :total="Number(unbondingTotal)"
      :pageSize="Number(unbondingPageSize)"
      :pageIndex="Number(unbondingPageNum)"
      @currpageChange="unbondingCurrpageChange"
    ></Pagination>
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
import { ListCard } from "../../../components";
import Pagination from "@/components/Pagination";
// import Clipboard from "clipboard";
// import { Toast } from "vant";
export default {
  components: { ListCard, Pagination },
  mixins: [mixinCommon],
  props: {
    defaultValue: { type: Object, default: () => {} },
    tokenList: { type: Array, default: () => [] },
    delegationTableData: { type: Array, default: () => [] },
    unbondingTableData: { type: Array, default: () => [] },
    activedTab: { type: String, default: "" },
    defaultType: { type: String, default: "" },
    delegationsTotal: { type: Number, default: 0 },
    delegationsPageNum: { type: Number, default: 0 },
    delegationsPageSize: { type: Number, default: 0 },
    unbondingTotal: { type: Number, default: 0 },
    unbondingPageNum: { type: Number, default: 0 },
    unbondingPageSize: { type: Number, default: 0 },
  },
  data() {
    const tokenCloumns = [
      {
        prop: "coinName",
        label: "Token",
        init: "ICT",
      },
      {
        prop: "balance",
        label: "Total Amount",
        slot: "balance",
        init: "ICT",
      },
      {
        prop: "usedAmount",
        label: "Available Balance",
        slot: "usedAmount",
        init: "ICT",
      },
      {
        prop: "serailNo",
        label: "Locked Balance",
        slot: "lockedBalance",
        init: "ICT",
      },
    ];
    const delegatedCloumns = [
      {
        prop: "delegator_balance",
        label: "Delegated",
        init: "ICT",
      },
      {
        prop: "unBonding_balance",
        label: "Unbonding",
        init: "ICT",
      },
      {
        prop: "rewards",
        label: "Rewards",
        init: "ICT",
      },
      {
        prop: "serailNo",
        label: "Commission",
        init: "ICT",
      },
    ];

    const DelegationsCloumns = [
      {
        prop: "address",
        label: "Address",
        slot: "address",
      },
      {
        prop: "amount",
        label: "Amount(ICT)",
        slot: "amount",
      },
      {
        prop: "shares",
        label: "Shares",
        slot: "shares",
      },
    ];
    const UnbondingCloumns = [
      {
        prop: "address",
        label: "Address",
        slot: "address",
      },
      {
        prop: "amount",
        label: "Amount(ICT)",
        slot: "amount",
      },
      {
        prop: "endTime",
        label: "End Time",
        slot: "endTime",
      },
    ];
    return {
      tokenCloumns,
      delegatedCloumns,
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
      DelegationsCloumns,
      UnbondingCloumns,
      // delegationTotal: 0,
      // delegationPageNum:1,
    };
  },
  watch: {
    // delegationsTotal: {
    //   handler() {
    //     this.delegationTotal = this.delegationsTotal;
    //   },
    // },
  },

  methods: {
    //资产切换
    onChangeTab(tab) {
      this.$emit("onChangeAssets", tab);
    },
    //erc切换
    onChangeSubTab(tab) {
      this.$emit("onChangeType", tab);
    },
    //delegations分页
    delegationsCurrpageChange(val) {
      this.$emit("delegationsCurrpageChange", val);
    },
    //unbonding分页
    unbondingCurrpageChange(val) {
      this.$emit("unbondingCurrpageChange", val);
    },
  },
  mounted() {},
  created() {},
};
</script>

<style scoped lang="less">
.address-assets {
  background: #f8f8f8;

  &__title {
    padding: 10px 15px;
    font-size: 14px;
    font-weight: 600;
  }
  &__address {
    background: #fff;
    margin-bottom: 10px;
    padding: 15px;
    font-size: 12px;
    &--label {
      font-weight: 600;
    }
    &--img {
      margin-left: 10px;
    }
  }
  &__info {
    padding: 10px 15px;
    margin-bottom: 20px;
    background: #fff;
    .van-divider::after,
    .van-divider::before {
      border-width: 4px;
    }
  }
  &__tab {
    &--col {
      padding: 13px 0;
      text-align: center;
      background: #eeeeee;
      color: #2a2a2a;
      font-size: 14px;
    }
    .is-actived {
      background: #5841ac;
      color: #fff;
    }
  }
  &__subtab {
    &--col {
      padding: 13px 0;
      text-align: center;
      // background: #eeeeee;
      color: #a4a2a2;
      font-size: 12px;
      border-bottom: 2px solid #eeeeee;
    }
    .is-actived {
      color: #5841ac;
      border-bottom: 2px solid #5841ac;
    }
  }
  &__tokenList {
    padding: 0 15px;
    font-size: 12px;
    background: #fff;
    &--row {
      padding: 15px 0;
      border-bottom: 3px solid #ededed;
    }
    &--line {
    }
    &--label {
      font-weight: 600;
    }
    &--nodata {
      text-align: center;
      padding: 30px 0;
      color: #ddd;
    }
  }
  &__card {
    background: #fff;
    padding: 0 15px;
    margin-bottom: 20px;
  }
  &__nodata {
    text-align: center;
    padding: 30px 0;
    color: #ddd;
    background: #fff;
    margin-bottom: 20px;
  }
}
</style>
