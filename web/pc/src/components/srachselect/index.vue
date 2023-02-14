<template>
  <div class="container">
    <header>
      <h3>
        <span v-if="iSsamllType == 1">Delegation</span>
        <span v-if="iSsamllType == 2">Validation</span>
        <span v-if="iSsamllType == 4">Transactions</span>
        <span v-if="iSsamllType == 3">Gov</span>
        <span v-if="iSsamllType == 1 || iSsamllType == 2 || iSsamllType == 3"
          >Txs List {{ total }} Txs</span
        >
      </h3>
      <div class="reach">
        <el-select
          v-if="iSsamllType == 1"
          v-model="smallType"
          placeholder="please choose"
          size="medium"
          style="width: 160px; margin-right: 10px"
          @change="sreachparentFn"
        >
          <el-option label="All TxType" value></el-option>
          <el-option label="Delegate" value="delegator"></el-option>
          <el-option label="Undelegate" value="undelegate"></el-option>
          <el-option
            label="WithdrawDelegatorReward"
            value="withdrawdelegatorreward"
          ></el-option>
          <el-option
            label="SetWithdrawAddress"
            value="setwithdrawaddress"
          ></el-option>
          <el-option
            label="BeginRedelegate"
            value="beginredelegate"
          ></el-option>
        </el-select>
        <el-select
          v-else-if="iSsamllType == 2"
          v-model="smallType"
          placeholder="please choose"
          size="medium"
          style="width: 160px; margin-right: 10px"
          @change="sreachparentFn"
        >
          <el-option label="All TxType" value></el-option>
          <el-option
            label="CreateValidator"
            value="createvalidator"
          ></el-option>
          <el-option label="EditValidator" value="editvalidator"></el-option>
          <el-option label="UnJail" value="unjail"></el-option>
          <el-option
            label="WithdrawValidatorCommission"
            value="withdrawvalidatorcommission"
          ></el-option>
        </el-select>
        <el-select
          v-else-if="iSsamllType == 3"
          v-model="smallType"
          placeholder="please choose"
          size="medium"
          style="width: 160px; margin-right: 10px"
        >
          <el-option label="All TxType" value></el-option>
          <el-option label="Vote" value="vote"></el-option>
          <el-option label="Submitproposal" value="submitproposal"></el-option>
          <el-option label="Deposit" value="deposit"></el-option>
        </el-select>
        <el-select
          v-else-if="iSsamllType == 4"
          v-model="bigType"
          placeholder="please choose"
          size="medium"
          style="width: 160px; margin-right: 10px"
          @change="sreachparentFn"
        >
          <el-option label="All TxType" value></el-option>
          <el-option label="Transfer" value="transfer"></el-option>
          <el-option label="Staking" value="staking"></el-option>
          <el-option label="Distribution" value="reward"></el-option>
          <el-option label="Governance" value="vote"></el-option>
          <el-option label="Slashing" value="jail"></el-option>
          <el-option label="Nft" value="nft"></el-option>
          <el-option label="Ethereum Tx" value="ethtx"></el-option>
          <!-- <el-option label="IBC" value="ibc"></el-option> -->
          <!-- <el-option label="EthBridge" value="ethbridge"></el-option> -->

          <!-- <el-option label="Issue" value="issues"></el-option> -->
          <el-option label="Defi" value="defi"></el-option>
          <!-- <el-option label="Swap" value="ammswap"></el-option> -->
          <!-- <el-option label="Order" value="business"></el-option> -->
        </el-select>
        <el-select
          v-model="status"
          placeholder="please choose"
          size="medium"
          style="width: 140px; margin-right: 5px"
          @change="sreachparentFn"
        >
          <el-option label="All Status" value></el-option>
          <el-option label="Success" value="0"></el-option>
          <el-option label="Failed" value="1"></el-option>
        </el-select>
        <el-date-picker
          v-model="dataTime"
          type="daterange"
          size="medium"
          start-placeholder="Start Date"
          end-placeholder="End Date"
          @change="Timechange"
        ></el-date-picker>
        <el-tooltip
          class="item"
          effect="dark"
          placement="top"
          content="Date selection range from the first block to the latest block."
        >
          <i class="el-icon-question wenhao"></i>
        </el-tooltip>

        <el-button size="small" @click="sreachparentFn">
          <i class="el-icon-search"></i>
        </el-button>
        <el-button size="small" @click="resetFn">
          <i class="el-icon-refresh-right"></i>
        </el-button>
      </div>
    </header>
  </div>
</template>

<script>
import { getDateFn } from "@/utils/public";
export default {
  props: ["total", "iSsamllType"],
  data() {
    return {
      smallType: "",
      status: "",
      dataTime: "",
      bigType: "",
      startTime: "",
      endTime: "",
    };
  },
  created() {
    var srachData = JSON.parse(sessionStorage.getItem("srachData"));
    if (srachData) {
      this.smallType = srachData.smallType;
      this.status = srachData.status;
      this.dataTime = srachData.dataTime;
      this.bigType = srachData.bigType;
      this.startTime = srachData.startTime;
      this.endTime = srachData.endTime;
    }
    this.sreachparentFn();
  },
  methods: {
    Timechange(val) {
      if (val) {
        this.startTime = getDateFn(val[0]);
        this.endTime = getDateFn(val[1]);
      } else {
        this.startTime = "";
        this.endTime = "";
      }
    },
    resetFn() {
      this.smallType = "";
      this.status = "";
      this.dataTime = "";
      this.bigType = "";
      this.startTime = "";
      this.endTime = "";
      sessionStorage.setItem("srachData", null);
      setTimeout(() => {
        this.sreachparentFn();
      });
    },
    sreachparentFn() {
      this.$emit("srechclickFn", {
        startTime: this.startTime,
        endTime: this.endTime,
        smallType: this.smallType,
        status: this.status,
        bigType: this.bigType,
      });
      sessionStorage.setItem(
        "srachData",
        JSON.stringify({
          startTime: this.startTime,
          endTime: this.endTime,
          smallType: this.smallType,
          status: this.status,
          bigType: this.bigType,
        })
      );
    },
  },
  components: {},
};
</script>

<style scoped lang="less">
header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30px 0px;
}
.wenhao {
  font-size: 24px;
  color: #5841ac;
  margin-right: 5px;
}
.reach {
  display: flex;
  align-items: center;
  button {
    background: #633af9;
    border-radius: 5px;
    color: #fff;
    font-size: 14px;
  }
}
</style>
