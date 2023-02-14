<template>
  <div class="container">
    <!-- <div class="reset-box">
      <span>reset</span>
    </div>-->
    <div class="sreach-top">
      <div>
        <van-field
          v-model="AllTxtypeVal.value"
          readonly
          clickable
          name="picker"
          placeholder="All Txtype"
          @click="AllTxtypeVal.showPicker = true"
        />
        <van-popup v-model:show="AllTxtypeVal.showPicker" position="bottom">
          <van-picker
            :columns="AllTxtype"
            @confirm="AllTxtypeonConfirm"
            confirm-button-text="Confirm"
            cancel-button-text="Cancel"
            @cancel="AllTxtypeVal.showPicker = false"
          />
        </van-popup>
      </div>
      <div>
        <van-field
          v-model="AllStatusVal.value"
          readonly
          clickable
          name="picker"
          placeholder="All Status"
          @click="AllStatusVal.showPicker = true"
        />
        <van-popup v-model:show="AllStatusVal.showPicker" position="bottom">
          <van-picker
            :columns="AllStatus"
            @confirm="AllStatusonConfirm"
            @cancel="AllStatusVal.showPicker = false"
            confirm-button-text="Confirm"
            cancel-button-text="Cancel"
          />
        </van-popup>
      </div>
    </div>
    <van-cell title="Select date" :value="date" is-link @click="showFn" />
    <van-calendar
      v-model:show="show"
      color="#271c60"
      :min-date="minDate"
      :max-date="maxDate"
      :show-title="false"
      :show-subtitle="false"
      type="range"
      :formatter="formatter"
      confirm-disabled-text="Confirm"
      confirm-text="Confirm"
      @confirm="formatDateonConfirm"
    />
    <div class="sreach-button">
      <div class="sousuo">
        <van-button round type="primary" color="#633AF9" @click="clickParentFn">
          <img src="../../assets/sousuo.png" alt />
        </van-button>
      </div>
      <div class="chongzhi">
        <van-button round type="primary" color="#633AF9" @click="resetFn">
          <img src="../../assets/rest.png" alt />
        </van-button>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive } from 'vue';
import { ref } from 'vue';
import { getDateFn } from '@/utils/public';
export default {
  props: ["state"],
  setup (props, context) {
    const show = ref(false);
    const date = ref('');
    let start = "";
    let end = "";
    let typetext = "";
    let statetext = "";
    const AllTxtypeVal = reactive({
      value: '',
      showPicker: false,
    });
    let AllTxtype = [];
    if (props.state == 4) {
      AllTxtype = ['Transfer', 'Business', 'Staking', 'Distribution', 'Governance', 'Slashing'];
    } else if (props.state == 1) {
      AllTxtype = ['Delegate', 'Undelegate', 'WithdrawDelegatorReward', 'SetWithdrawAddress', 'BeginRedelegate'];
    } else if (props.state == 2) {
      AllTxtype = ['CreateValidator', 'EditValidator', 'UnJail', 'WithdrawValidatorCommission'];
    } else {
      AllTxtype = ['Vote', 'Submitproposal', 'Deposit'];
    }
    const AllTxtypeonConfirm = (value) => {
      switch (value) {
        case "Vote":
          typetext = "vote";
          break;
        case "Submitproposal":
          typetext = "submitproposal";
          break;
        case "Deposit":
          typetext = "deposit";
          break;
        case "CreateValidator":
          typetext = "createvalidator";
          break;
        case "EditValidator":
          typetext = "editvalidator";
          break;
        case "UnJail":
          typetext = "unjail";
          break;
        case 'WithdrawValidatorCommission':
          typetext = "withdrawvalidatorcommission";
          break;
        case "Delegate":
          typetext = "delegator";
          break;
        case "Undelegate":
          typetext = "undelegate";
          break;
        case "WithdrawDelegatorReward":
          typetext = "withdrawdelegatorreward";
          break;
        case 'SetWithdrawAddress':
          typetext = "setwithdrawaddress";
          break;
        case 'BeginRedelegate':
          typetext = "beginredelegate";
          break;
        case "Transfer":
          typetext = "transfer";
          break;
        case "Business":
          typetext = "business";
          break;
        case "Staking":
          typetext = "staking";
          break;
        case 'Distribution':
          typetext = "reward";
          break;
        case 'Governance':
          typetext = "vote";
          break;
        case "Slashing":
          typetext = "jail";
          break;
      }

      AllTxtypeVal.value = value;
      AllTxtypeVal.showPicker = false;
    };
    const AllStatus = ['Success', 'Failed'];
    const AllStatusVal = reactive({
      value: '',
      showPicker: false,
    });
    const AllStatusonConfirm = (value) => {
      switch (value) {
        case "Success":
          statetext = "0";
          break;
        case "Failed":
          statetext = "1";
          break;
      }
      AllStatusVal.value = value;
      AllStatusVal.showPicker = false;
    };
    const resetFn = () => {
      start = "";
      end = "";
      typetext = "";
      statetext = "";
      AllTxtypeVal.value = "";
      AllTxtypeVal.showPicker = false;
      AllStatusVal.value = "";
      AllStatusVal.showPicker = false;
      date.value = "";
      setTimeout(() => {
        context.emit("clickParentFn", {
          start,
          end,
          typetext,
          statetext,
        })
      });
    }
    const showFn = (value) => {
      show.value = true;
      setTimeout(() => {
        var calendarweekdays = document.getElementsByClassName("van-calendar__weekday");
        var month_title = document.getElementsByClassName("van-calendar__month-title");

        calendarweekdays[0].innerHTML = "SUN";
        calendarweekdays[1].innerHTML = "MON";
        calendarweekdays[2].innerHTML = "TUE";
        calendarweekdays[3].innerHTML = "WED";
        calendarweekdays[4].innerHTML = "THU";
        calendarweekdays[5].innerHTML = "FRI";
        calendarweekdays[6].innerHTML = "SAT";
        for (var i = 0; i < month_title.length; i++) {
          var date = month_title[i].innerHTML.split("年");
          var text = "";

          month_title[i].innerHTML = date[0];

        }
      })

    }
    const formatDate = (date) => `${date.getMonth() + 1}/${date.getDate()}`;
    const formatDateonConfirm = (values) => {
      const [start_date, end_date] = values;
      start = getDateFn(values[0]);
      end = getDateFn(values[1]);
      show.value = false;
      date.value = `${formatDate(start_date)} - ${formatDate(end_date)}`;
    };
    const formatter = (day) => {
      if (day.type === 'start') {
        day.bottomInfo = "Start"
      }
      if (day.type === 'end') {
        day.bottomInfo = "End"
      }
      return day;
    }
    const clickParentFn = () => {
      context.emit("clickParentFn", {
        start,
        end,
        typetext,
        statetext,
      })
    }

    return {
      formatter,
      AllTxtype,
      AllStatus,
      AllTxtypeVal,
      AllTxtypeonConfirm,
      AllStatusVal,
      AllStatusonConfirm,
      show,
      minDate: new Date(1974, 0, 1),
      maxDate: new Date(2025, 10, 1),
      showFn,
      date,
      formatDateonConfirm,
      clickParentFn,
      resetFn
    }
  },

  mounted () {

  }
}
</script>

<style scoped lang="less">
@import url("../../css/index");
.sreach-button {
  text-align: center;
  margin: 10 / @rem 0px;
  display: flex;
  justify-content: space-between;
  .sousuo {
    width: 80%;
  }
  button {
    width: 100%;
    border-radius: 5px;
  }
  img {
    width: 23 / @rem;
    height: 23 / @rem;
  }
}
.reset-box {
  text-align: right;
  line-height: 40 / @rem;
  font-size: 14 / @rem;
  color: #402ed8;
  text-decoration: underline;
}
.sreach-top {
  display: flex;
  justify-content: space-between;
  margin: 10 / @rem auto;
  & > div {
    width: 48%;
  }
}
.container {
  margin: 0px 10 / @rem;
}
</style>
