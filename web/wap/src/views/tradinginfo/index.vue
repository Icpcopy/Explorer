<template>
  <div class="container">
    <header>
      <div class="header-title">Transaction Details</div>
      <div class="fuzhi-box">
        <span>{{ $jiequStrFnMaxlength($route.params.id) }}</span>
        <textarea id="copy_hash" class="displaynone">{{
          $route.params.id
        }}</textarea>
        <img
          class="fuzhi"
          src="../../assets/fuzhi.png"
          @click="$copyFn('copy_hash')"
          alt
        />
      </div>
    </header>
    <div class="pub-section-title">Base Info</div>
    <div class="list-info-box">
      <ul>
        <li>
          <span>Block：</span>
          <div @click="$blockChainInfosRouterFn(actionByHashData.height)">
            {{ actionByHashData.height }}
          </div>
        </li>
        <li>
          <span>Status：</span>
          <div class="green" v-if="actionByHashData.code == 0">Success</div>
          <div class="error" v-else>Failed</div>
        </li>
        <li>
          <span>Timestamp：</span>
          <div class="huise">{{ actionByHashData.timestamp }} UTC</div>
        </li>
        <li>
          <span>Fee：</span>
          <div>
            {{
              (actionByHashData.fee && onFeixedNum(actionByHashData.fee, 6)) ||
              0
            }}
            <span v-if="actionByHashData">{{
              actionByHashData.feeCoinName
            }}</span>
          </div>
        </li>
        <li class="memo-box">
          <span>Memo：</span>
          <div>
            <span v-if="actionByHashData.memo">{{
              actionByHashData.memo
            }}</span>
            <span v-else>--</span>
          </div>
        </li>
        <li class="memo-box heightbox" v-if="isShangXiaBool">
          <span>Log:</span>
          <div class="memotext">
            <span v-if="actionByHashData.raw_log">{{
              actionByHashData.raw_log
            }}</span>
            <span v-else>--</span>
          </div>
        </li>
      </ul>
    </div>
    <div class="shengsuo-box">
      <div v-if="isShangXiaBool" @click="clickisShangXiaBoolFn(false)">
        <span>Click to see more</span>
        <img class="imgwidth" src="../../assets/shangjiantou.png" alt />
      </div>
      <div v-else @click="clickisShangXiaBoolFn(true)">
        <span>Put away</span>
        <img class="imgwidth" src="../../assets/xiajiant.png" alt />
      </div>
    </div>
    <section>
      <div class="pub-section-title">Transaction Message or Result</div>
      <div v-for="(j, i) in actionByHashData.event" :key="i">
        <Singleevent
          v-if="
            j.type == 'Transfer' ||
            j.type == 'Withdraw Delegator Reward' ||
            j.type == 'Send' ||
            j.type == 'Delegate' ||
            j.type == 'Undelegate'
          "
          :data="j"
        ></Singleevent>

        <AddPledge v-if="j.type == 'Add Pledge'" :data="j"></AddPledge>

        <AgreeOrderPair
          v-if="j.type == 'Agree Order Pair'"
          :data="j"
        ></AgreeOrderPair>

        <BeginRedelegate
          v-if="j.type == 'Begin Redelegate'"
          :data="j"
        ></BeginRedelegate>

        <BurnCoin v-if="j.type == 'Burn Coin'" :data="j"></BurnCoin>

        <BurnToken v-if="j.type == 'Burn Token'" :data="j"></BurnToken>

        <ChannelOpenAck
          v-if="j.type == 'Channel Open Ack'"
          :data="j"
        ></ChannelOpenAck>

        <ChannelOpenConfirm
          v-if="j.type == 'Channel Open Confirm'"
          :data="j"
        ></ChannelOpenConfirm>

        <ChannelOpenInit
          v-if="j.type == 'Channel Open Init'"
          :data="j"
        ></ChannelOpenInit>

        <ChannelOpenTry
          v-if="j.type == 'Channel Open Try'"
          :data="j"
        ></ChannelOpenTry>

        <ConnectionOpenAck
          v-if="j.type == 'Connection Open Ack'"
          :data="j"
        ></ConnectionOpenAck>

        <ConnectionOpenConfirm
          v-if="j.type == 'Connection Open Confirm'"
          :data="j"
        ></ConnectionOpenConfirm>

        <ConnectionOpenInit
          v-if="j.type == 'Connection Open Init'"
          :data="j"
        ></ConnectionOpenInit>

        <ConnectionOpenTry
          v-if="j.type == 'Connection Open Try'"
          :data="j"
        ></ConnectionOpenTry>

        <CreateClient v-if="j.type == 'Create Client'" :data="j"></CreateClient>

        <CreateDefi v-if="j.type == 'Create Defi'" :data="j"></CreateDefi>

        <CreatePool v-if="j.type == 'Create Pool'" :data="j"></CreatePool>

        <CreateValidator
          v-if="j.type == 'Create Validator'"
          :data="j"
        ></CreateValidator>

        <Deposit v-if="j.type == 'Deposit'" :data="j"></Deposit>

        <EditToken v-if="j.type == 'Edit Token'" :data="j"></EditToken>

        <EditValidator
          v-if="j.type == 'Edit Validator'"
          :data="j"
        ></EditValidator>

        <IssueToken v-if="j.type == 'Issue Token'" :data="j"></IssueToken>

        <LoanCoin v-if="j.type == 'Loan Coin'" :data="j"></LoanCoin>

        <MintCoin v-if="j.type == 'Mint Coin'" :data="j"></MintCoin>

        <MintToken v-if="j.type == 'Mint Token'" :data="j"></MintToken>

        <PlaceOrder v-if="j.type == 'Place Order'" :data="j"></PlaceOrder>

        <RecvPacket v-if="j.type == 'Recv Packet'" :data="j"></RecvPacket>

        <RedeemPledge v-if="j.type == 'Redeem Pledge'" :data="j"></RedeemPledge>

        <RevokeOrder v-if="j.type == 'Revoke Order'" :data="j"></RevokeOrder>

        <SetWithDrawAddress
          v-if="j.type == 'Set Withdraw Address'"
          :data="j"
        ></SetWithDrawAddress>

        <SubmitProposal
          v-if="j.type == 'Submit Proposal'"
          :data="j"
        ></SubmitProposal>

        <Timeout v-if="j.type == 'Timeout'" :data="j"></Timeout>

        <TransferTokenOwner
          v-if="j.type == 'Transfer Token Owner'"
          :data="j"
        ></TransferTokenOwner>

        <Unjail v-if="j.type == 'Unjail'" :data="j"></Unjail>

        <UnlockToken v-if="j.type == 'Unlock Token'" :data="j"></UnlockToken>

        <UpdateClient v-if="j.type == 'Update Client'" :data="j"></UpdateClient>

        <Vote v-if="j.type == 'Vote'" :data="j"></Vote>

        <WithdrawValidatorCommission
          v-if="j.type == 'Withdraw Validator Commission'"
          :data="j"
        ></WithdrawValidatorCommission>

        <WithdrawClaim
          v-if="j.type == 'Withdraw Claim'"
          :data="j"
        ></WithdrawClaim>
        <RequestBatch v-if="j.type == 'Request Batch'" :data="j"></RequestBatch>

        <ConfirmBatch v-if="j.type == 'Confirm Batch'" :data="j"></ConfirmBatch>

        <SendToEth v-if="j.type == 'Send To Eth'" :data="j"></SendToEth>

        <ValsetUpdatedClaim
          v-if="j.type == 'Valset Updated Claim'"
          :data="j"
        ></ValsetUpdatedClaim>

        <DepositClaim v-if="j.type == 'DepositClaim'" :data="j"></DepositClaim>

        <ValsetConfirm
          v-if="j.type == 'Valset Confirm'"
          :data="j"
        ></ValsetConfirm>

        <SetOrchestratorAddress
          v-if="j.type == 'Set Orchestrator Address'"
          :data="j"
        ></SetOrchestratorAddress>

        <WithdrawWithinBatch
          v-if="j.type == 'Withdraw Within Batch'"
          :data="j"
        ></WithdrawWithinBatch>

        <DepositWithinBatch
          v-if="j.type == 'Deposit Within Batch'"
          :data="j"
        ></DepositWithinBatch>

        <SwapWithinBatch
          v-if="j.type == 'Swap Within Batch'"
          :data="j"
        ></SwapWithinBatch>
        <DefiUndelegate
          v-if="j.type == 'Defi Undelegate'"
          :data="j"
        ></DefiUndelegate>
        <WithdrawDefiDelegatorReward
          v-if="j.type == 'Withdraw Defi Delegator Reward'"
          :data="j"
        ></WithdrawDefiDelegatorReward>
        <DefiDelegate v-if="j.type == 'Defi Delegate'" :data="j"></DefiDelegate>

        <Mintnft v-if="j.type == 'Mint Nft'" :data="j"></Mintnft>
      </div>
    </section>
  </div>
</template>

<script>
import WithdrawClaim from "./components/WithdrawClaim";
import RequestBatch from "./components/RequestBatch";
import ConfirmBatch from "./components/ConfirmBatch";
import SendToEth from "./components/SendToEth";

import ValsetUpdatedClaim from "./components/ValsetUpdatedClaim";
import DepositClaim from "./components/DepositClaim";
import SetOrchestratorAddress from "./components/SetOrchestratorAddress";

import WithdrawWithinBatch from "./components/WithdrawWithinBatch";
import DepositWithinBatch from "./components/DepositWithinBatch";
import SwapWithinBatch from "./components/SwapWithinBatch";
import DefiUndelegate from "./components/DefiUndelegate";
import WithdrawDefiDelegatorReward from "./components/WithdrawDefiDelegatorReward";
import DefiDelegate from "./components/DefiDelegate";

import AddPledge from "./components/addPledge";
import Singleevent from "./components/singleevent";
import AgreeOrderPair from "./components/agreeOrderPair";
import BeginRedelegate from "./components/beginRedelegate";
import BurnCoin from "./components/burnCoin";
import BurnToken from "./components/burnToken";
import ChannelOpenAck from "./components/channelOpenAck";
import ChannelOpenConfirm from "./components/channelOpenConfirm";
import ChannelOpenInit from "./components/channelOpenInit";
import ChannelOpenTry from "./components/channelOpenTry";
import ConnectionOpenAck from "./components/connectionOpenAck";
import ConnectionOpenConfirm from "./components/connectionOpenConfirm";
import ConnectionOpenInit from "./components/connectionOpenInit";
import ConnectionOpenTry from "./components/connectionOpenTry";
import ValsetConfirm from "./components/ValsetConfirm";
import CreateClient from "./components/createClient";
import CreateDefi from "./components/createDefi";
import CreatePool from "./components/createPool";
import CreateValidator from "./components/createValidator";
import Deposit from "./components/deposit";
import EditToken from "./components/editToken";
import EditValidator from "./components/editValidator";
import IssueToken from "./components/issueToken";
import LoanCoin from "./components/loanCoin";
import MintCoin from "./components/mintCoin";
import MintToken from "./components/mintToken";
import PlaceOrder from "./components/placeOrder";
import RecvPacket from "./components/recvPacket";
import RedeemPledge from "./components/redeemPledge";
import RevokeOrder from "./components/revokeOrder";
import SetWithDrawAddress from "./components/setWithDrawAddress";
import SubmitProposal from "./components/submitProposal";
import Timeout from "./components/timeout";
import TransferTokenOwner from "./components/transferTokenOwner";
import Unjail from "./components/unjail";
import UnlockToken from "./components/unlockToken";
import UpdateClient from "./components/updateClient";
import Vote from "./components/vote";
import Mintnft from "./components/mintnft";
import WithdrawValidatorCommission from "./components/withdrawValidatorCommission";

import { getTransactionByHash } from "@/api/deal";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      actionByHashData: { memo: "" },
      hashid: "",
      isShangXiaBool: false,
    };
  },
  created() {
    this.getTransactionByHashFn();
  },
  watch: {
    $route(from, to) {
      if (from.name == "tradinginfo") {
        this.getTransactionByHashFn();
      }
    },
  },
  mounted() {
    this.$scrrollBox((This) => {});
  },
  methods: {
    clickisShangXiaBoolFn(bool) {
      this.isShangXiaBool = bool;
    },
    copyFn(id) {
      var orderNum = document.getElementById(id).innerText;
      var oInput = document.createElement("input");
      oInput.value = orderNum;
      document.body.appendChild(oInput);
      oInput.select(); // 选择对象
      document.execCommand("Copy"); // 执行浏览器复制命令
      oInput.className = "oInput";
      oInput.style.display = "none";
      alert("复制成功");
    },
    getTransactionByHashFn() {
      getTransactionByHash({
        txHash: this.$route.params.id,
      }).then((res) => {
        this.actionByHashData = res.result;
        this.actionByHashData.timestamp = this.$getUTCTimeFn(
          this.actionByHashData.timestamp
        );
      });
    },
  },
  components: {
    Mintnft,
    WithdrawClaim,
    RequestBatch,
    ConfirmBatch,
    SendToEth,
    ValsetUpdatedClaim,
    DepositClaim,
    ValsetConfirm,
    SetOrchestratorAddress,
    WithdrawWithinBatch,
    DepositWithinBatch,
    SwapWithinBatch,
    DefiUndelegate,
    WithdrawDefiDelegatorReward,
    DefiDelegate,
    AddPledge,
    AgreeOrderPair,
    BeginRedelegate,
    BurnCoin,
    BurnToken,
    ChannelOpenAck,
    ChannelOpenConfirm,
    ChannelOpenInit,
    ChannelOpenTry,
    ConnectionOpenAck,
    ConnectionOpenConfirm,
    ConnectionOpenInit,
    ConnectionOpenTry,
    CreateClient,
    CreateDefi,
    CreatePool,
    CreateValidator,
    Deposit,
    EditToken,
    EditValidator,
    IssueToken,
    LoanCoin,
    MintCoin,
    MintToken,
    PlaceOrder,
    RecvPacket,
    RedeemPledge,
    RevokeOrder,
    SetWithDrawAddress,
    SubmitProposal,
    Timeout,
    TransferTokenOwner,
    Unjail,
    UnlockToken,
    UpdateClient,
    Vote,
    WithdrawValidatorCommission,
    Singleevent,
  },
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
header {
  background: #fff;
  padding: 20 / @rem 10 / @rem;
  font-size: 14 / @rem;
  .fuzhi-box {
    display: flex;
  }
  .fuzhi-box {
    display: flex;
    align-items: center;
    margin-top: 10 / @rem;
  }
  span {
    font-size: 12 / @rem;
    color: #696969;
  }
  img {
    margin-left: 20 / @rem;
  }
}
.heightbox {
  height: auto !important;
}
.header-title {
  font-size: 14 / @rem;
}
.memo-box > div span {
  word-wrap: break-word;
  display: inline-block;
  word-wrap: break-word;
  white-space: normal;
  word-break: break-all;
}
.memo-box {
  line-height: 20 / @rem !important;
  word-wrap: break-word;
  margin-right: 10 / @rem;
}
.memotext {
  width: 320 / @rem;
  margin-right: 10 / @rem;
}
.shengsuo-box {
  background: #eceefd;
  opacity: 1;
  border-radius: 5px;
  text-align: center;
  color: #592ec3;
  font-size: 18 / @rem !important;
  cursor: pointer;
  padding: 10 / @rem 0px;
  margin-bottom: 10px;

  div {
    width: 100%;
    img {
      position: relative;
      top: 5 / @rem;
      margin-left: 5px;
      vertical-align: center;
      width: 8 / @rem;
      height: 15 / @rem;
    }
  }
}
</style>
