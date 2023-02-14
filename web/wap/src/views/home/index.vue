<template>
  <div class="container">
    <div>
      <div class="bottom-box">
        <div class="bottom-text">
          <div class="left-text">
            <div class="text-style">
              <div v-if="WebSocket_data[0].coinInfo">
                Price: ${{
                  (WebSocket_data[0].coinInfo.coinAmount * 1 || 0).toFixed(6)
                }}
              </div>

              <div v-else>Price: --</div>
            </div>
            <div class="text-style" v-if="WebSocket_data[0].coinInfo">
              Inflation:
              {{
                (WebSocket_data[0].coinInfo.Inflation * 100 || 0).toFixed(2)
              }}%
            </div>
            <div
              v-if="
                WebSocket_data[0].feeDestructionQuantity &&
                WebSocket_data[0].feeDestructionQuantity.coinName &&
                WebSocket_data[0].feeDestructionQuantity.coinName == 'gauss'
              "
            >
              Fee destruction quantity:{{
                (WebSocket_data[0] &&
                  WebSocket_data[0].feeDestructionQuantity &&
                  WebSocket_data[0].feeDestructionQuantity.amount &&
                  Number(
                    WebSocket_data[0].feeDestructionQuantity.amount
                  ).toFixed(6)) ||
                0
              }}
              {{ WebSocket_data[0].coinInfo.coinName }}
              <!-- .toUpperCase() -->
            </div>
          </div>
          <div>
            <div v-if="WebSocket_data[0].coinInfo">
              Market Cap: ${{
                (WebSocket_data[0].coinInfo.totalMarketValue * 1 || 0).toFixed(
                  2
                )
              }}
            </div>
            <div v-else>Market Cap: -- }}</div>
            <div v-if="WebSocket_data[0].coinInfo">
              Community Pool:{{
                (WebSocket_data[0].coinInfo.CommunityPool * 1 || 0).toFixed(2)
              }}
              {{ WebSocket_data[0].coinInfo.coinName }}
              <!-- .toUpperCase()  -->
            </div>

            <div v-else>Community Pool:--</div>
          </div>
        </div>
      </div>
    </div>
    <div class="box-top">
      <div class="img-icon-title">
        <img src="../../assets/icplazaLogo.png" alt />
        Block Height
      </div>
      <div class="home-text-box">
        <div class="left-home">
          <div
            class="user-img-box"
            @click="$userInfoRouterFn(WebSocket_data[0].operatorAddress)"
          >
            <img src="../../assets/icplazaLogo.png" alt />
            <!-- :src="WebSocket_data[0].validator_coin"  -->
            <span class="colors">{{ WebSocket_data[0].validator }}</span>
          </div>
        </div>
        <div class="right-home">
          <span
            class="colors fontsizemax"
            @click="$blockChainInfosRouterFn(WebSocket_data[0].height)"
            >{{ (WebSocket_data[0].height * 1 || 0).toFixed(0) }}</span
          >
        </div>
      </div>
    </div>
    <div class="box-center">
      <div>
        <div class="img-icon-title">
          <img src="../../assets/tuceng.png" alt />
          Transactions
        </div>
        <div class="section-text-box">
          <div class="colors fontsizemax" @click="clickRoutesFn('/deal')">
            {{ (WebSocket_data[0].totalTxNum * 1 || 0).toFixed(0) }}
          </div>
          <span class="huise">{{ WebSocket_data[0].time_stamp_time }} UTC</span>
        </div>
      </div>
      <div>
        <div class="img-icon-title">
          <img src="../../assets/user.png" alt />
          Active Validators
        </div>
        <div class="section-text-box">
          <div class="fontsizemax colors" @click="clickRoutesFn('/identifier')">
            {{ (WebSocket_data[0].activeValidators * 1 || 0).toFixed(0) }}
          </div>
          <span class="huise">
            out of
            {{ (WebSocket_data[0].totalValidators * 1 || 0).toFixed(0) }}
            validators
          </span>
        </div>
      </div>
      <div>
        <div class="img-icon-title Average-box">
          <div>
            <img src="../../assets/zu3.png" alt />
            Average Block Time ({{ selectTimeTxt }})
          </div>
          <div>
            <img
              class="right-img"
              @click="showPropFn"
              src="../../assets/zu.png"
              alt
            />
          </div>
        </div>
        <div class="section-text-box">
          <div class="fontsizemax" v-if="WebSocket_data[0].averageBlockTime">
            {{
              (
                WebSocket_data[0].averageBlockTime[isTimesData] * 1 || 0
              ).toFixed(2)
            }}
          </div>
          <span class="huise">seconds</span>
        </div>
      </div>

      <div>
        <div class="img-icon-title">
          <img src="../../assets/zu4.png" alt />
          Bonded Tokens
        </div>
        <div class="section-text-box">
          <div class="fontsizemax" v-if="WebSocket_data[0].coinInfo">
            {{
              (WebSocket_data[0].coinInfo.onlineVotePower * 100 || 0).toFixed(2)
            }}
            <span class="fontsizemin">%</span>
          </div>
          <span class="huise" v-if="WebSocket_data[0].coinInfo">
            {{
              (
                WebSocket_data[0].coinInfo.Bonded / 10000000000000000000*10 || 0
              ).toFixed(2)
            }}M /
            <span v-if="WebSocket_data[0].coinInfo">
              {{
                (
                  WebSocket_data[0].coinInfo.TotalSupply /
                    10000000000000000000*10 || 0
                ).toFixed(2)
              }}M
            </span>
          </span>
        </div>
      </div>
    </div>
    <div class="box-footer">
      <div class="home-footer-title">
        <div class="h-left img-icon-title">
          <img src="../../assets/mokuai.png" alt />
          <span>Blocks</span>
        </div>
        <div class="h-right" @click="routerHomeFn('/blockchain')">
          <a>View all</a>
        </div>
      </div>
      <div class="home-footer-content">
        <ul v-if="WebSocket_data.length > 1">
          <li v-for="(item, key) of WebSocket_data" :key="key">
            <div class="h-footer-left">
              <div
                class="colors"
                @click="$blockChainInfosRouterFn(item.height)"
              >
                {{ item.height }}
              </div>
              <span>Txn:{{ item.tx_num }}</span>
            </div>
            <div
              class="h-footer-center user-img-box"
              @click="$userInfoRouterFn(item.operatorAddress)"
            >
              <img :src="item.validator_coin" alt />
              <span class="colors">{{ item.validator }}</span>
            </div>
            <div class="h-footer-right huise">
              <span>{{ item.time_stamp_tihuan }}</span> ago
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="box-footer">
      <div class="home-footer-title">
        <div class="h-left img-icon-title">
          <img src="../../assets/tuceng.png" alt />
          <span>Transactions</span>
        </div>
        <div class="h-right" @click="routerHomeFn('/deal')">
          <a>View all</a>
        </div>
      </div>
      <div class="home-footer-content">
        <ul>
          <li v-for="(item, key) of text_InfoData" :key="key">
            <div class="left left_hash">
              <div @click="$clickHshRouterFn(item)">
                <span>TX#</span>
                <span class="hiddenbox-hash_home">{{ item.txhash }}</span>
              </div>
              <div class="tran-center">
                <p
                  class="tiaoshu maodian"
                  @click="$clickHshRouterFn(item)"
                  v-if="item.event.length > 1"
                >
                  More+
                </p>
                <p v-else>{{ item.event[0].type }}</p>
              </div>
              <div class="huise">23s ago</div>
            </div>
            <div>
              <div
                class="right-text-froms"
                v-if="
                  item.event[0].type == 'Send' ||
                  item.event[0].type == 'BeginDelegate' ||
                  item.event[0].type == 'Delegate' ||
                  item.event[0].type == 'Deposit' ||
                  item.event[0].type == 'Multi Send' ||
                  item.event[0].type == 'Submit Proposal' ||
                  item.event[0].type == 'Transfer' ||
                  item.event[0].type == 'Undelegate' ||
                  item.event[0].type == 'Vote' ||
                  item.event[0].type == 'Issue Token' ||
                  item.event[0].type == 'Unlock Token' ||
                  item.event[0].type == 'Transfer Token Owner' ||
                  item.event[0].type == 'Mint Token' ||
                  item.event[0].type == 'Create Pool' ||
                  item.event[0].type == 'Add Pledge' ||
                  item.event[0].type == 'Edit Token' ||
                  item.event[0].type == 'Place Order'
                "
              >
                <span>From</span>
                <div
                  class="hiddenbox-dz"
                  @click="$headerRouterFn(item.event[0].from)"
                >
                  {{ item.event[0].from }}
                </div>
              </div>
              <div
                class="right-text-froms"
                v-else-if="item.event[0].from == undefined"
              >
                <span>From</span>
                <div class="hiddenbox-dz">--</div>
              </div>

              <div v-else>
                <div
                  v-if="typeof item.event[0].from == 'string'"
                  class="right-text-froms"
                >
                  <span>From</span>
                  <div
                    class="hiddenbox-dz maodian"
                    @click="$headerRouterFn(item.event[0].from)"
                  >
                    {{ item.event[0].from }}
                  </div>
                </div>
                <div
                  v-else-if="item.event[0].from.moniker == null"
                  class="right-text-froms"
                >
                  <span>From</span>
                  <div @click="$headerRouterFn(item.event[0].operatorAddress)">
                    {{ item.event[0].from.operatorAddress }}
                  </div>
                </div>

                <div
                  v-else
                  @click="$userInfoRouterFn(item.event[0].from.operatorAddress)"
                  class="right-text-froms"
                >
                  <span>From</span>
                  <div class="home-user-name maodian hiddenbox-dz">
                    {{ item.event[0].from.moniker }}
                  </div>
                </div>
              </div>
              <div
                class="right-text-froms marginMin"
                v-if="
                  item.event[0].type == 'Send' ||
                  item.event[0].type == 'Transfer' ||
                  item.event[0].type == 'Withdraw Reward' ||
                  item.event[0].type == 'Multi Send' ||
                  item.event[0].type == 'Transfer Token Owner' ||
                  item.event[0].type == 'Mint Token'
                "
              >
                <span>To</span>
                <div
                  class="hiddenbox-dz oversp maodian"
                  @click="$headerRouterFn(item.event[0].to)"
                >
                  {{ item.event[0].to }}
                </div>
              </div>
              <div
                class="right-text-froms marginMin"
                v-else-if="item.event[0].to == undefined"
              >
                <span>To</span>
                <div class="home-user-name maodian">--</div>
              </div>
              <div v-else>
                <div
                  v-if="item.event[0].to.icon"
                  class="flex_div right-text-froms marginMin"
                  @click="$userInfoRouterFn(item.event[0].to.operatorAddress)"
                >
                  <span class="froms">To</span>
                  <div class="home-user-name hiddenbox-dz maodian">
                    {{ item.event[0].to.moniker }}
                  </div>
                </div>
                <div class="right-text-froms marginMin" v-else>
                  <span class="froms">To</span>
                  <div
                    class="hiddenbox-dz maodian"
                    @click="$headerRouterFn(item.event[0].to)"
                  >
                    {{ item.event[0].to }}
                  </div>
                </div>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <section>
      <van-action-sheet
        v-model:show="show"
        :actions="actions"
        cancel-text="Cancel"
        close-on-click-action
        @select="selectFn"
      />
    </section>
  </div>
</template>

<script>
import { ref } from "vue";
import { Toast } from "vant";
export default {
  setup() {
    const show = ref(false);
    const selectTimeTxt = ref("All");
    const isTimesData = ref("averageTime");
    let actions = [
      { name: "All Time" },
      { name: "Last Minute" },
      { name: "Last Hour" },
      { name: "Last Day" },
    ];
    const showPropFn = (val) => {
      show.value = true;
    };
    const selectFn = (item) => {
      switch (item.name) {
        case "All Time":
          selectTimeTxt.value = "All";
          isTimesData.value = "averageTime";
          break;
        case "Last Minute":
          selectTimeTxt.value = "1m";
          isTimesData.value = "minuteTime";

          break;
        case "Last Hour":
          selectTimeTxt.value = "1h";
          isTimesData.value = "hourTime";

          break;
        case "Last Day":
          selectTimeTxt.value = "1d";
          isTimesData.value = "dayTime";
          break;
      }
    };
    return {
      show,
      actions,
      selectTimeTxt,
      showPropFn,
      selectFn,
      isTimesData,
    };
  },
  data() {
    return {
      WebSocket_data: [
        {
          coinInfo: {
            chainName: "",
            coinAmount: 0,
            totalMarketValue: 0,
            Inflation: 0,
            CommunityPool: 0,
            onlineVotePower: 0,
            Bonded: 0,
            TotalSupply: 0,
          },
          event: [{}],
          averageBlockTime: {},
        },
      ],
      home_butts_val: true,
      text_InfoData: [],
      loading: undefined,
    };
  },
  created() {
    this.WebSocketFn();
  },
  methods: {
    routerHomeFn(name) {
      this.$router.push(name);
    },
    clickRoutesFn(names) {
      this.$router.push(names);
    },
    getSecondsFn(names) {
      switch (names) {
        case "averageTime":
          this.selectTimeTxt = "All";
          break;
        case "dayTime":
          this.selectTimeTxt = "1d";
          break;
        case "hourTime":
          this.selectTimeTxt = "1h";
          break;
        case "minuteTime":
          this.selectTimeTxt = "1m";
          break;
      }

      this.isTimesData = names;
    },
    WebSocketFn() {
      //   var ws_addr = sessionStorage.getItem("ws");
      //   if (ws_addr == null) {
      //     ws_addr = "ws://192.168.1.40:12345/ws";
      //   }
      //   var ws = new WebSocket(ws_addr);

      // var HTTP = "http";
      // HTTP = window.location.href.split(":")[0] == "https" ? "wss" : "ws";
      // var host = location.host;
      // var ws = new WebSocket(HTTP + "://" + host + "/ws"); //new WebSocket("ws://192.168.1.37:12345/ws"); //
      //
      // var ws = new WebSocket("wss://browse.ic-plaza.org/ws");
      var ws = new WebSocket("wss://browsemainnet.ic-plaza.org/ws"); //主网
      // var ws = new WebSocket("ws://192.168.2.13:7002/ws");
      // var ws = new WebSocket("wss://browsetest.ic-plaza.org/ws"); //测试网
      var pages_web_data = { page: "homepage" };
      var This = this;
      //监听是否连接成功
      ws.onopen = function () {
        ws.send(JSON.stringify(pages_web_data));
      };
      //接听服务器发回的信息并处理展示
      ws.onmessage = function (data) {
        if (JSON.parse(data.data).code == "-200") {
          return;
        }
        var WebSocket_data = eval(data.data);
        if (WebSocket_data.length) {
          if (WebSocket_data.length == 10) {
            // data
            WebSocket_data.map((item) => {
              item.time_stamp_tihuan = This.$TimeremainingFn(item.time_stamp);
              item.time_stamp_time = This.$getUTCTimeFn(item.time_stamp);
            });
            // tx
            WebSocket_data[0].tx.map((item) => {
              item.time_stamp_tihuan = This.$TimeremainingFn(item.timestamp);
              item.time_stamp_time = This.$getUTCTimeFn(item.timestamp);
            });
            This.WebSocket_data = WebSocket_data;
            This.text_InfoData = WebSocket_data[0].tx;
          } else {
            WebSocket_data[0].time_stamp_tihuan = This.$TimeremainingFn(
              WebSocket_data[0].time_stamp
            );
            WebSocket_data[0].time_stamp_time = This.$getUTCTimeFn(
              WebSocket_data[0].time_stamp
            );
            if (WebSocket_data[0].tx.length) {
              WebSocket_data[0].tx.map((item) => {
                item.time_stamp_time = This.$getUTCTimeFn(item.timestamp);
              });
            } else {
              console.log("WebSocket_data[0].tx.length空空如也");
            }

            This.text_InfoData.unshift(...WebSocket_data[0].tx);
            This.WebSocket_data.unshift(WebSocket_data[0]);
            This.WebSocket_data.map((item) => {
              item.time_stamp_tihuan = This.$TimeremainingFn(item.time_stamp);
              item.time_stamp_time = This.$getUTCTimeFn(item.time_stamp);
            });
            This.text_InfoData = This.text_InfoData.splice(0, 10);
            This.WebSocket_data = This.WebSocket_data.splice(0, 10);
            This.text_InfoData.map((item) => {
              item.time_stamp_tihuan = This.$TimeremainingFn(item.timestamp);
              item.time_stamp_time = This.$getUTCTimeFn(item.timestamp);
            });
          }
        }
      };
      //监听连接关闭事件
      ws.onclose = function () {
        //监听整个过程中websocket的状态
        console.log("ws连接状态11111：" + ws.readyState);
      };
      //监听并处理error事件
      ws.onerror = function (error) {
        console.log(error);
      };
    },
  },
  components: {},
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
.box-top {
  background: #fff;
  padding: 0px 5 / @rem;
  border-bottom: 1px solid #ededed;
  .home-text-box {
    display: flex;
    align-items: center;
    justify-content: space-around;
    padding: 10 / @rem 0px 15 / @rem 0px;
  }
}
.box-center {
  & > div {
    margin: 15 / @rem 5 / @rem 0px 5 / @rem;
    background: #fff;
    padding: 5 / @rem 0px 15 / @rem 0px;
    .section-text-box {
      text-align: center;
      padding: 15 / @rem 0px;
      .fontsizemax {
        margin-bottom: 15 / @rem;
      }
    }
  }
}
.box-footer {
  background: #fff;
  margin-top: 15 / @rem;
  .home-footer-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-right: 13 / @rem;
    border-bottom: 1px solid #ededed;
  }
  .home-footer-content {
    ul li {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin: 0px 16 / @rem;
      padding: 20 / @rem 0px;
      border-bottom: 1px solid #ededed;
      font-size: 14 / @rem;
      .right-text-froms {
        display: flex;
        align-items: center;
        span {
          width: 50 / @rem;
        }
      }
      &:last-child {
        border: none;
      }
      .h-footer-center {
        width: 100 / @rem;

        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        span {
          margin-top: 10px;
        }
      }
      .colors {
        margin-bottom: 10 / @rem;
      }
    }
    .tran-center {
      margin: 10 / @rem 0px;
    }
  }
}
.bottom-box {
  color: #fff;
  background: #271c60;
  .srech-box {
    position: relative;
    text-align: center;
    width: 350 / @rem;
    height: 36 / @rem;
    margin: 10 / @rem auto;
    img {
      position: absolute;
      top: 8 / @rem;
      right: 10 / @rem;
      width: 21 / @rem;
      height: 21 / @rem;
    }
    input {
      width: 100%;
      height: 36 / @rem;
      border-radius: 36 / @rem;
      border: 1 / @rem solid #fff;
      text-indent: 10 / @rem;
      background: @bg;
    }
  }
  .bottom-text {
    display: flex;
    margin-left: 25 / @rem;
    .left-text {
      margin-right: 10 / @rem;
    }
    div {
      font-size: 10 / @rem;
      margin-top: 5 / @rem;
    }
  }
  padding-bottom: 20 / @rem;
}
.user-img-box {
  span {
    font-size: 14 / @rem;
    font-weight: normal;
  }
}
.Average-box {
  display: flex;
  justify-content: space-between;
  .right-img {
    width: 5 / @rem;
    margin-right: 10 / @rem;
  }
}
</style>
