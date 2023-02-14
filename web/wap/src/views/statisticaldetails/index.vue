<template>
  <div class="container">
    <div class="tjxq-title">{{ data.coinName }} Token Stats</div>
    <div class="container-row">
      <div
        class="container-row__col"
        style="background: linear-gradient(180deg, #dcfaf4 0%, #f8fffe 100%)"
      >
        <div class="li-top" style="color: #2e8474">
          <img
            class="container-row__col--img"
            src="@/assets/icon/totalSupply.png"
          />Total Supply
        </div>
        <div class="li-bottom" style="color: #1fb795">
          {{
            (data && data.TotalSupply && onFeixedNum(data.TotalSupply, 6)) || 0
          }}
        </div>
        <div class="li-text" style="color: #1fb795">
          {{ data.coinName }}
        </div>
      </div>

      <div
        class="container-row__col"
        style="background: linear-gradient(180deg, #e6f1ff 0%, #f7fdff 100%)"
      >
        <div class="li-top" style="color: #1693c9">
          <img
            class="container-row__col--img"
            src="@/assets/icon/circulation.png"
          />Circulation
        </div>
        <div class="li-bottom" style="color: #17adeb">
          {{
            (data && data.Circulation && onFeixedNum(data.Circulation, 6)) || 0
          }}
        </div>
        <div class="li-text" style="color: #17adeb">
          {{ data.coinName }}
        </div>
      </div>

      <div
        class="container-row__col"
        style="background: linear-gradient(180deg, #e2e9ff 0%, #f5f8ff 100%)"
      >
        <div class="li-top" style="color: #1693c9">
          <img
            class="container-row__col--img"
            src="@/assets/icon/ecologicalPool.png"
          />Ecological pool
        </div>
        <div class="li-bottom" style="color: #17adeb">
          {{
            (data &&
              data.EcologicalProPool &&
              onFeixedNum(data.EcologicalProPool, 6)) ||
            0
          }}
        </div>
        <div class="li-text" style="color: #17adeb">
          {{ data.coinName }}
        </div>
      </div>

      <div
        class="container-row__col"
        style="background: linear-gradient(180deg, #e7e2ff 0%, #f7f5ff 100%)"
      >
        <div class="li-top" style="color: #391db7">
          <img
            class="container-row__col--img"
            src="@/assets/icon/locked.png"
          />Locked ICT
        </div>
        <div class="li-bottom" style="color: #5d3bf7">
          {{
            (data && data.LockedTotal && onFeixedNum(data.LockedTotal, 6)) || 0
          }}
        </div>
        <div class="li-text" style="color: #5d3bf7">
          {{ data.coinName }}
        </div>
        <van-row :gutter="20">
          <van-col :span="12">
            <div
              class="li-box"
              style="color: #391db7; background: rgba(228, 221, 255, 1)"
            >
              <div>24-phase locking</div>
              <div class="li-box__count">
                {{
                  (data && data.Locked24 && onFeixedNum(data.Locked24, 6)) || 0
                }}
                <span> {{ data.coinName }} </span>
              </div>
            </div>
          </van-col>
          <van-col :span="12">
            <div
              class="li-box"
              style="color: #391db7; background: rgba(228, 221, 255, 1)"
            >
              <div>36-phase lock-up</div>
              <div class="li-box__count">
                {{
                  (data && data.Locked36 && onFeixedNum(data.Locked36, 6)) || 0
                }}
                <span> {{ data.coinName }} </span>
              </div>
            </div>
          </van-col>
        </van-row>
      </div>
      <div
        class="container-row__col"
        style="background: linear-gradient(180deg, #ffe3fe 0%, #fff8ff 100%)"
      >
        <div class="li-top" style="color: #a319a3">
          <img
            class="container-row__col--img"
            src="@/assets/icon/redemptionProgre.png"
          />
          Redemption in progress
        </div>
        <div class="li-bottom" style="color: #bf2fbf">
          {{
            (data &&
              data.totalUnDelegator &&
              onFeixedNum(data.totalUnDelegator, 6)) ||
            0
          }}
        </div>
        <div class="li-text" style="color: #bf2fbf">
          {{ data.coinName }}
        </div>
      </div>
      <div
        class="container-row__col"
        style="background: linear-gradient(180deg, #fff5db 0%, #fdf9ef 100%)"
      >
        <div class="li-top" style="color: #dba308">
          <img class="container-row__col--img" src="@/assets/icon/bonded.png" />
          Bonded
        </div>
        <div class="li-bottom" style="color: #dba308">
          {{ (data.Bonded && onFeixedNum(data.Bonded, 6)) || 0 }}
        </div>
        <div class="li-text" style="color: #dba308">
          {{ data.coinName }}
        </div>
        <van-row :gutter="20">
          <van-col :span="12">
            <div class="li-box" style="color: #dba308; background: #fff2d3">
              <div>Non redeemable</div>
              <div class="li-box__count">
                {{
                  (data &&
                    data.BondedLocked &&
                    onFeixedNum(data.BondedLocked, 6)) ||
                  0
                }}
                <span> {{ data.coinName }} </span>
              </div>
            </div>
          </van-col>
          <van-col :span="12">
            <div class="li-box" style="color: #dba308; background: #fff2d3">
              <div>Redeemable</div>
              <div class="li-box__count">
                {{
                  (data &&
                    data.BondedNotLocked &&
                    onFeixedNum(data.BondedNotLocked, 6)) ||
                  0
                }}
                <span> {{ data.coinName }} </span>
              </div>
            </div>
          </van-col>
        </van-row>
      </div>

      <div
        class="container-row__col"
        style="background: linear-gradient(180deg, #fff4ef 0%, #fff9f6 100%)"
      >
        <div class="li-top" style="color: #eb7714">
          <img
            class="container-row__col--img"
            src="@/assets/icon/commission.png"
          />
          Output of POS pool
        </div>
        <div class="li-bottom" style="color: #eb7714">
          {{ (data && data.PosTotal && onFeixedNum(data.PosTotal, 6)) || 0 }}
        </div>
        <div class="li-text" style="color: #eb7714">
          {{ data.coinName }}
        </div>
        <van-row :gutter="20">
          <van-col :span="12">
            <div class="li-box" style="color: #eb7714; background: #ffe8de">
              <div>Withdrawal commission</div>
              <div class="li-box__count">
                {{
                  (data &&
                    data.withdrawCommission &&
                    onFeixedNum(data.withdrawCommission, 6)) ||
                  0
                }}
                <span> {{ data.coinName }} </span>
              </div>
            </div>
          </van-col>
          <van-col :span="12">
            <div class="li-box" style="color: #eb7714; background: #ffe8de">
              <div>Withdrawal distribution</div>
              <div class="li-box__count">
                {{
                  (data &&
                    data.withdrawReward &&
                    onFeixedNum(data.withdrawReward, 6)) ||
                  0
                }}
                <span> {{ data.coinName }} </span>
              </div>
            </div>
          </van-col>
          <van-col :span="24">
            <div class="li-box" style="color: #eb7714; background: #ffe8de">
              <div>Community Pool</div>
              <div class="li-box__count">
                {{
                  (data &&
                    data.CommunityPool &&
                    onFeixedNum(data.CommunityPool, 6)) ||
                  0
                }}
                <span> {{ data.coinName }} </span>
              </div>
            </div>
          </van-col>
          <van-col :span="24">
            <div class="li-box" style="color: #eb7714; background: #ffe8de">
              <div>Community Pool extraction</div>
              <div class="li-box__count">
                {{
                  (data &&
                    data.withdrawalCommunityPool &&
                    onFeixedNum(data.withdrawalCommunityPool, 6)) ||
                  0
                }}
                <span> {{ data.coinName }} </span>
              </div>
            </div>
          </van-col>
        </van-row>
      </div>
    </div>
    <!-- <header>
      <ul>
        <li>
          <div class="title">Total Supply</div>
          <div class="content">
            <p>
              {{ (data.TotalSupply && onFeixedNum(data.TotalSupply, 6)) || 0 }}
            </p>
            <span class="huise">{{ data.coinName }}</span>
          </div>
        </li>
        <li>
          <div class="title">Circulation</div>
          <div class="content">
            <p>
              {{ (data.Circulation && onFeixedNum(data.Circulation, 6)) || 0 }}
            </p>
            <span class="huise">{{ data.coinName }}</span>
          </div>
        </li>
        <li>
          <div class="title">Community Pool</div>
          <div class="content">
            <p>
              {{
                (data.CommunityPool && onFeixedNum(data.CommunityPool, 6)) || 0
              }}
            </p>
            <span class="huise">{{ data.coinName }}</span>
          </div>
        </li>
        <li>
          <div class="title">Bonded</div>
          <div class="content">
            <p>{{ (data.Bonded && onFeixedNum(data.Bonded, 6)) || 0 }}</p>
            <span class="huise">{{ data.coinName }}</span>
          </div>
        </li>
      </ul>
    </header> -->
    <section>
      <div class="tjxq-title">{{ data.coinName }} Token Distribution</div>
      <div id="main"></div>
      <div class="right">
        <ul>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor1"></span>
              <div>NO.1~5</div>
            </div>
            <div>
              <div class="left-text">
                {{ (tongji_data.topFive * 100).toFixed(2) }}%
              </div>
              <div class="center-text">
                {{
                  (tongji_data.topFiveCoin &&
                    onFeixedNum(tongji_data.topFiveCoin, 6)) ||
                  0
                }}
                {{ tongji_data.coinName }}
              </div>
              <div class="right-text">{{ tongji_data.topFiveNum }}</div>
            </div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor2"></span>
              <div>NO.6~10</div>
            </div>
            <div>
              <div class="left-text">
                {{ (tongji_data.topTen * 100).toFixed(2) }}%
              </div>
              <div class="center-text">
                {{
                  (tongji_data.topTenCoin &&
                    onFeixedNum(tongji_data.topTenCoin, 6)) ||
                  0
                }}{{ tongji_data.coinName }}
              </div>
              <div class="right-text">{{ tongji_data.topTenNum }}</div>
            </div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor3"></span>
              <div>NO.11~50</div>
            </div>
            <div>
              <div class="left-text">
                {{ (tongji_data.topFifty * 100).toFixed(2) }}%
              </div>
              <div class="center-text">
                {{
                  (tongji_data.topFiftyCoin &&
                    onFeixedNum(tongji_data.topFiftyCoin, 6)) ||
                  0
                }}
                {{ tongji_data.coinName }}
              </div>
              <div class="right-text">{{ tongji_data.topFiftyNum }}</div>
            </div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor4"></span>
              <div>NO.51~100</div>
            </div>
            <div>
              <div class="left-text">
                {{ (tongji_data.topHun * 100).toFixed(2) }}%
              </div>
              <div class="center-text">
                {{
                  (tongji_data.topHunCoin &&
                    onFeixedNum(tongji_data.topHunCoin, 6)) ||
                  0
                }}
                {{ tongji_data.coinName }}
              </div>
              <div class="right-text">{{ tongji_data.topHunNum }}</div>
            </div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor5"></span>
              <div>NO.101~500</div>
            </div>
            <div>
              <div class="left-text">
                {{ (tongji_data.topFiveHun * 100).toFixed(2) }}%
              </div>
              <div class="center-text">
                {{
                  (tongji_data.topFiveHunCoin &&
                    onFeixedNum(tongji_data.topFiveHunCoin, 6)) ||
                  0
                }}
                {{ tongji_data.coinName }}
              </div>
              <div class="right-text">{{ tongji_data.topFiveHunNum }}</div>
            </div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor6"></span>
              <div>NO.501~1000</div>
            </div>
            <div>
              <div class="left-text">
                {{ (tongji_data.topThousand * 100).toFixed(2) }}%
              </div>
              <div class="center-text">
                {{
                  (tongji_data.topThousandCoin &&
                    onFeixedNum(tongji_data.topThousandCoin, 6)) ||
                  0
                }}
                {{ tongji_data.coinName }}
              </div>
              <div class="right-text">{{ tongji_data.topThousandNum }}</div>
            </div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor7"></span>
              <div>NO.1001-</div>
            </div>
            <div>
              <div class="left-text">
                {{ (tongji_data.otherAddress * 100).toFixed(2) }}%
              </div>
              <div class="center-text">
                {{
                  (tongji_data.otherAddressCoin &&
                    onFeixedNum(tongji_data.otherAddressCoin, 6)) ||
                  0
                }}
                {{ tongji_data.coinName }}
              </div>
              <div class="right-text">{{ tongji_data.otherNum }}</div>
            </div>
          </li>
        </ul>
      </div>
    </section>
  </div>
</template>

<script>
import * as echarts from "echarts/core";
import { TooltipComponent, LegendComponent } from "echarts/components";
import { PieChart } from "echarts/charts";
import { CanvasRenderer } from "echarts/renderers";
import { getCoinInfo, getCoinRange } from "@/api/statistics";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      data: { TotalSupply: 0, CommunityPool: 0, Bonded: 0, Circulation: 0 },
      tongji_data: {},
    };
  },
  created() {
    this.getCoinInfoFn();
    this.getCoinRangeFn();
  },
  watch: {
    $route() {
      location.reload();
    },
  },
  methods: {
    getCoinRangeFn() {
      getCoinRange().then((res) => {
        var This = this;
        this.tongji_data = res.result;
        this.tongji_data.coinName = this.tongji_data.coinName.toUpperCase();
        echarts.use([
          TooltipComponent,
          LegendComponent,
          PieChart,
          CanvasRenderer,
        ]);
        var chartDom = document.getElementById("main");
        var myChart = echarts.init(chartDom);
        var option = {
          color: [
            "#3E91FF",
            "#83C958",
            "#7F5FE7",
            "#F08856",
            "#F2D015",
            "#BF5ECB",
            "#E22359",
          ],

          tooltip: {
            formatter: function (params) {
              console.log(params.data);
              return (
                params.data.name +
                ":<br/>" +
                params.data.value +
                This.tongji_data.coinName +
                "<br/>" +
                +params.data.percentage +
                "%<br/>" +
                "Pool Add. No:" +
                params.data.people
              );
            },
          },
          //   legend: {
          //     top: "5%",
          //     left: "center"
          //   },
          series: [
            {
              // name: "访问来源",
              type: "pie",
              radius: ["80%", "57%"],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: "center",
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: "20",

                  fontWeight: "bold",
                },
              },
              labelLine: {
                show: false,
              },
              data: [
                {
                  value: this.tongji_data.topFiveCoin,
                  name: "NO.1~5",
                  people: this.tongji_data.topFiveNum,
                  percentage: (this.tongji_data.topFive * 100).toFixed(2),
                },
                {
                  value: this.tongji_data.topTenCoin,
                  name: "NO.6~10",
                  people: this.tongji_data.topTenNum,
                  percentage: (this.tongji_data.topTen * 100).toFixed(2),
                },
                {
                  value: this.tongji_data.topFiftyCoin,
                  name: "NO.11~50",
                  people: this.tongji_data.topFiftyNum,
                  percentage: (this.tongji_data.topFifty * 100).toFixed(2),
                },
                {
                  value: this.tongji_data.topHunCoin,
                  name: "NO.51~100",
                  people: this.tongji_data.topHunNum,
                  percentage: (this.tongji_data.topHun * 100).toFixed(2),
                },
                {
                  value: this.tongji_data.topFiveHunCoin,
                  name: "NO.101~500",
                  people: this.tongji_data.topFiveHunNum,
                  percentage: (this.tongji_data.topFiveHun * 100).toFixed(2),
                },
                {
                  value: this.tongji_data.topThousandCoin,
                  name: "NO.501~1000",
                  people: this.tongji_data.topThousandNum,
                  percentage: (this.tongji_data.topThousand * 100).toFixed(2),
                },
                {
                  value: this.tongji_data.otherAddressCoin,
                  name: "NO.1001-",
                  people: this.tongji_data.otherNum,
                  percentage: (this.tongji_data.otherAddress * 100).toFixed(2),
                },
              ],
            },
          ],
        };
        option && myChart.setOption(option);
      });
    },
    getCoinInfoFn() {
      getCoinInfo().then((res) => {
        this.data = res.result;
        this.data.coinName = res.result.coinName.toUpperCase();
      });
    },
  },
  components: {},
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
#main {
  width: 255 / @rem;
  height: 255 / @rem;
  margin: 0px auto;
}
.container-row{
  padding:10px;
  &__col{
    margin-bottom:20px;
    padding:20px;
    text-align: center;
    .li-top{
        font-size: 18px;
        line-height:20px;
        font-weight: 600;
          > img{
            width:20px;
            height:20px;
            margin-right:10px;
          }
    }
    .li-bottom{
        font-size: 32px;
        font-weight: 600;
        margin-top:20px;
    }
    .li-text{
      font-size: 14px;
      margin-top:15px;
      width:100%;
    }
    .li-box{
      text-align: center;
      padding: 15px 0;
      border-radius: 10px;
      margin: 20px 0 0 0;
      font-size: 12px;
      &__count {
        font-size: 14px;
        font-weight: 600;
        margin-top: 10px;
        > span {
          font-weight: 400;
          font-size: 9px;
        }
      }
      
    }
  }
}
header {
  ul {
    li {
      background: #fff;
      text-align: center;
      margin: 0px 10 / @rem 15 / @rem 10 / @rem;
      .title {
        line-height: 40 / @rem;
        border-bottom: 1px solid #ededed;
        font-size: 18 / @rem;
      }
      .content {
        padding: 20 / @rem 0px;
        p {
          margin-bottom: 10 / @rem;
          font-size: 26 / @rem;
        }
      }
    }
  }
}

.right {
  padding-bottom: 30 / @rem;
  ul > li {
    margin: 15 / @rem;
    font-size: 12 / @rem;
    & > div:last-child {
      display: flex;
      line-height: 40 / @rem;
    }
    .left-text {
      margin-left: 29 / @rem;
      width: 50 / @rem;
    }
    .center-text {
      margin-left: 60 / @rem;
      flex:1
      // width: 180 / @rem;
    }
    span {
      display: inline-block;
      width: 17 / @rem;
      height: 17 / @rem;
    }
  }
  .left-text,
  .right-text {
    color: #9196c1;
  }

  .fk-box {
    display: flex;
    div {
      width: 80 / @rem;
    }
    span {
      margin-right: 15 / @rem;
    }
  }
  .fkcolor1 {
    background: #3e91ff;
  }
  .fkcolor2 {
    background: #83c958;
  }
  .fkcolor3 {
    background: #7f5fe7;
  }
  .fkcolor4 {
    background: #f08856;
  }
  .fkcolor5 {
    background: #f2d015;
  }
  .fkcolor6 {
    background: #bf5ecb;
  }
  .fkcolor7 {
    background: #e22359;
  }
}
</style>
