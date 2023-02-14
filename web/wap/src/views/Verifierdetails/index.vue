<template>
  <div class="container">
    <header class="infos-title">Validator Details</header>
    <section>
      <div class="section-top">
        <img :src="userInfoData.icon" alt />
        <div class="colors usernamelength">
          {{ userInfoData.description.moniker }}
        </div>
        <div class="huise huise-size">
          <span v-if="userInfoData.description.moniker == ''">
            {{ userInfoData.description.identity }}
          </span>
          <span v-else>{{ userInfoData.address }}</span>
        </div>
        <div class="huise">~ The validator has no more information ~</div>
        <button class="button" type="primary">
          <span v-if="status == 1">Active</span>
          <div v-else-if="status == 2">Candidate</div>
          <span v-else>Jailed</span>
        </button>
      </div>
      <div class="section-bottom list-box">
        <div>
          <span>Operator Address：</span>
          <div class="colors huise">
            <textarea id="copy1__address" class="displaynone">{{
              userInfoData.operator_address
            }}</textarea>
            <span>{{ userInfoData.operator_address_jiequ }}</span>
            <img
              src="../../assets/fuzhi.png"
              @click="$copyFn('copy1__address')"
              alt
            />
          </div>
        </div>
        <div>
          <span>Owner Address：</span>
          <div class="colors">
            <textarea id="copy1_selfDelegateAddress" class="displaynone">{{
              userInfoData.selfDelegateAddress
            }}</textarea>
            <span
              class="shubiao"
              @click="$headerRouterFn(userInfoData.selfDelegateAddress)"
              >{{ userInfoData.selfDelegateAddress_jiequ }}</span
            >
            <img
              src="../../assets/fuzhi.png"
              @click="$copyFn('copy1_selfDelegateAddress')"
              alt
            />
          </div>
        </div>
        <div>
          <span>Withdraw Address：</span>
          <div class="colors">
            <textarea id="copy1_DelegateAddress" class="displaynone">{{
              userInfoData.selfDelegateAddress
            }}</textarea>
            <span
              class="shubiao"
              @click="$headerRouterFn(userInfoData.selfDelegateAddress)"
              >{{ userInfoData.selfDelegateAddress_jiequ }}</span
            >
            <img
              src="../../assets/fuzhi.png"
              @click="$copyFn('copy1_DelegateAddress')"
              alt
            />
          </div>
        </div>
        <div>
          <span>Voting Power：</span>
          <div class="huise">
            {{ ((userInfoData.votingPower || 0) * 100).toFixed(2) }}%
          </div>
        </div>
        <div>
          <span>Uptime：</span>
          <div class="huise">
            {{ (userInfoData.uptime * 1 || 0).toFixed(2) }}%
          </div>
        </div>
        <div>
          <span>Missed Blocks:</span>
          <div class="huise">{{ userInfoData.miss_info || "--" }}</div>
        </div>
        <div v-if="status == 1">
          <span>Consensus Pubkey:</span>
          <div class="huise colors">
            <textarea id="copy4_publicKey" class="displaynone shubiao">{{
              userInfoData.publicKey
            }}</textarea>
            <span>{{ userInfoData.publicKey_jiequ }}</span>
            <img
              src="../../assets/fuzhi.png"
              @click="$copyFn('copy4_publicKey')"
              alt
            />
          </div>
        </div>
        <div v-if="status == 2 || status == 3">
          <span>Jailed Until:</span>
          <div class="huise">
            {{
              $getUTCTimeFn(
                userInfoData.val_signing_info.val_signing_info.jailed_until
              )
            }}
            UTC
          </div>
        </div>
      </div>
    </section>
    <section class="Commission">
      <header class="infos-title">Commission Info</header>
      <section class="Sandiancharts">
        <div class="container-box">
          <div>
            <div class="container-header">
              Commission Rate & Bonded Tokens Distribution
            </div>
            <div class="container-text">Bonded-Token({{ coinName }})</div>
          </div>
          <div id="container"></div>
          <div class="container-bottom">Commission Rate(%)</div>
        </div>
      </section>
      <CommissionInfo
        :userInfoData="userInfoData"
        :coinName="coinName"
      ></CommissionInfo>
    </section>
    <section>
      <header class="infos-title">Delegations</header>
      <Delegations></Delegations>
    </section>
    <section>
      <header class="infos-title">Unbonding Delegations</header>
      <UnDelegations></UnDelegations>
    </section>
    <section>
      <Transactionrecords :isStateTxt="'address'"></Transactionrecords>
    </section>
  </div>
</template>

<script>
import { getValidatorsByMoniker } from "@/api/identifier";
import CommissionInfo from "./components/CommissionInfo";
import Delegations from "./components/Delegations";
import UnDelegations from "./components/UnDelegations";
import Transactionrecords from "@/components/Transactionrecords";
import * as echarts from "echarts";
import { getCommissionByMoniker } from "@/api/identifier";

export default {
  data() {
    return {
      userInfoData: {
        description: {},
        commission: { commission_rates: {} },
        val_signing_info: { val_signing_info: {} },
      },
      status: "",
      datas: [],
      coinName: "",
      ChartsCommissionData: [],
    };
  },
  mounted() {
    this.getValidatorsByMonikerFn();
  },
  methods: {
    getCommissionByMonikerFn(operatorAddress) {
      getCommissionByMoniker({ operatorAddress }).then((res) => {
        this.ChartsCommissionData = res.result;
        var charttDtata = [];
        var data = [];
        res.result.map((item, key) => {
          charttDtata[key] = [
            item.commission.commission_rates.rate * 1,
            item.tokens / 10000000000000000000*10,
            57096869,
            item.description.moniker,
            "Other Validator",
          ];
        });
        var user_name = [JSON.parse(JSON.stringify(this.datas))];
        data = [user_name, charttDtata];
        this.$nextTick(() => {
          var chartDom = document.getElementById("container");
          var myChart = echarts.init(chartDom);
          var This = this;
          var option;

          option = {
            tooltip: {
              formatter: function (params) {
                return (
                  (params.data[0] * 100).toFixed(2) +
                  "%<br/>" +
                  params.data[3] +
                  "：" +
                  params.data[1].toFixed(2) +
                  This.userInfoData.coinName.toUpperCase()
                );
              },
            },
            legend: {
              right: "10%",
              top: "3%",
              data: [this.userInfoData.description.moniker, "Other Validator"],
            },
            grid: {
              left: "18%",
              top: "10%",
            },
            xAxis: {
              splitLine: {
                lineStyle: {
                  type: "dashed",
                },
              },
              axisLabel: {
                fontSize: 10,
              },
            },
            yAxis: {
              axisLabel: {
                fontSize: 10,
                formatter: function (params) {
                  return params / 10000000000000000000*10+ "M";
                },
              },
              splitLine: {
                lineStyle: {
                  type: "dashed",
                },
              },
              scale: true,
            },
            series: [
              {
                symbolSize: 20,
                name: this.userInfoData.description.moniker,
                data: data[0],
                type: "scatter",
                emphasis: {
                  focus: "series",
                  label: {
                    show: true,
                    formatter: function (param) {
                      return param.data[3];
                    },
                    position: "top",
                  },
                },
                itemStyle: {
                  color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                    {
                      offset: 0,
                      color: "#FF9601",
                    },
                    {
                      offset: 1,
                      color: "#FF9601",
                    },
                  ]),
                },
              },
              {
                symbolSize: 20,
                name: "Other Validator",
                data: data[1],
                type: "scatter",

                emphasis: {
                  focus: "series",
                  label: {
                    show: true,

                    position: "top",
                  },
                },
                itemStyle: {
                  color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                    {
                      offset: 0,
                      color: "#7F62EA",
                    },
                    {
                      offset: 1,
                      color: "#7F62EA",
                    },
                  ]),
                },
              },
            ],
          };
          option && myChart.setOption(option, true);
          chartDom.removeAttribute("_echarts_instance_");
        });
      });
    },
    getValidatorsByMonikerFn() {
      getValidatorsByMoniker({
        operatorAddress: this.$route.params.id,
      }).then((res) => {
        this.userInfoData = res.result;
        // console.log(res.result, "res.result===");
        this.coinName = this.userInfoData.coinName.toUpperCase();
        this.userInfoData.operator_address_jiequ = this.$jiequStrFn(
          this.userInfoData.operator_address
        );
        this.userInfoData.selfDelegateAddress_jiequ = this.$jiequStrFn(
          this.userInfoData.selfDelegateAddress
        );
        this.userInfoData.publicKey_jiequ = this.$jiequStrFn(
          this.userInfoData.publicKey
        );
        this.datas = [
          this.userInfoData.commission.commission_rates.rate * 1,
          this.userInfoData.tokens / 10000000000000000000*10,
          5096869,
          this.userInfoData.description.moniker,
          "Sidnac",
        ];

        this.getCommissionByMonikerFn(this.$route.params.id);
        if (!this.userInfoData.jailed) {
          if (this.userInfoData.status == "BOND_STATUS_BONDED") {
            this.status = 1;
          } else {
            this.status == 2;
          }
        } else {
          this.status = 3;
        }
      });
    },
  },
  components: {
    CommissionInfo,
    Delegations,
    UnDelegations,
    Transactionrecords,
  },
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
#container {
  width: 96%;
  height: 400 / @rem;
  margin-left: 10px;
  margin: 0px auto;
  margin-top: 45 / @rem;
}
.usernamelength {
  width: 300 / @rem;
  line-height: 35 / @rem;
  margin: 0px auto;
  overflow: hidden; //超出的文本隐藏
  text-overflow: ellipsis; //溢出用省略号显示
  white-space: nowrap; //溢出不换行
}
.Sandiancharts {
  .container-header {
    position: absolute;
    top: -30 / @rem;
    left: 20 / @rem;
  }
  .container-text {
    position: absolute;
    top: -5 / @rem;
    left: 20 / @rem;
    color: #6e7398;
    font-size: 10 / @rem;
  }
  .container-bottom {
    position: absolute;
    bottom: 20 / @rem;
    right: 60 / @rem;
    color: #6e7398;
  }
  .container-box {
    position: relative;
  }
}
.Commission {
  padding-bottom: 20 / @rem;
}
.section-top {
  text-align: center;
  background: #fff;
  padding-bottom: 20 / @rem;
  //   Commission Info
  button {
    color: #fff;
    width: 100 / @rem;
    height: 38 / @rem;
    background: #633af9;
    border-radius: 8 / @rem;
    font-size: 16 / @rem;
    margin-top: 10 / @rem;
  }
  img {
    width: 75 / @rem;
    height: 75 / @rem;
    margin: 15 / @rem 0px;
  }
  .colors {
    font-size: 18 / @rem;
    margin-bottom: 10 / @rem;
  }
  .huise {
    margin-bottom: 7 / @rem;
  }
}
.list-box > div span {
  text-indent: 0px;
}
.list-box {
  padding-left: 15 / @rem;
}
.section-bottom {
  .colors {
    display: flex;
    align-items: center;
  }
  margin-top: 0px;
  img {
    width: 13 / @rem;
    height: 14 / @rem;
  }
  & > div {
    span {
      width: 150 / @rem;
    }
  }
}
.huise-size {
  font-size: 12 / @rem;
}
</style>
