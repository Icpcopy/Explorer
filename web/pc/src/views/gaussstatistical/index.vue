<template>
  <div class="container-children-box">
    <h3 class="s-b-t">{{ coinName }} Token Stats</h3>
    <div class="container-content">
      <div class="container-content__content">
        <el-row :gutter="20" class="container-row" type="flex">
          <el-col :span="5">
            <div
              class="container-row__col"
              style="
                background: linear-gradient(180deg, #dcfaf4 0%, #f8fffe 100%);
                height: 100%;
              "
            >
              <div class="li-top" style="color: #2e8474">
                <img
                  class="container-row__col--img"
                  src="@/assets/icons/totalSupply.png"
                />Total Supply
              </div>
              <div class="li-bottom" style="color: #1fb795">
                {{
                  (header_data &&
                    header_data.TotalSupply &&
                    onFeixedNum(header_data.TotalSupply, 6)) ||
                  0
                }}
                <span> {{ header_data.coinName }} </span>
              </div>
            </div>
          </el-col>
          <el-col :span="5">
            <div
              class="container-row__col"
              style="
                background: linear-gradient(180deg, #e6f1ff 0%, #f7fdff 100%);
                height: 100%;
              "
            >
              <div class="li-top" style="color: #1693c9">
                <img
                  class="container-row__col--img"
                  src="@/assets/icons/circulation.png"
                />Circulation
              </div>
              <div class="li-bottom" style="color: #17adeb">
                {{
                  (header_data &&
                    header_data.Circulation &&
                    onFeixedNum(header_data.Circulation, 6)) ||
                  0
                }}
                <span> {{ header_data.coinName }} </span>
              </div>
            </div>
          </el-col>
          <el-col :span="5">
            <div
              class="container-row__col"
              style="
                background: linear-gradient(180deg, #e2e9ff 0%, #f5f8ff 100%);
                height: 100%;
              "
            >
              <div class="li-top" style="color: #2a4cbc">
                <img
                  class="container-row__col--img"
                  src="@/assets/icons/ecologicalPool.png"
                />Ecological pool
              </div>
              <div class="li-bottom" style="color: #144bff">
                {{
                  (header_data &&
                    header_data.EcologicalProPool &&
                    onFeixedNum(header_data.EcologicalProPool, 6)) ||
                  0
                }}

                <span> {{ header_data.coinName }} </span>
              </div>
            </div>
          </el-col>
          <el-col :span="9">
            <div
              class="container-row__col"
              style="
                background: linear-gradient(180deg, #e7e2ff 0%, #f7f5ff 100%);
                height: 100%;
              "
            >
              <div class="li-top" style="color: #391db7">
                <img
                  class="container-row__col--img"
                  src="@/assets/icons/locked.png"
                />Locked ICT
              </div>
              <div class="li-bottom" style="color: #5d3bf7">
                {{
                  (header_data &&
                    header_data.LockedTotal &&
                    onFeixedNum(header_data.LockedTotal, 6)) ||
                  0
                }}
                <span> {{ header_data.coinName }} </span>
              </div>

              <el-row :gutter="20">
                <el-col :span="12">
                  <div
                    class="li-box"
                    style="color: #391db7; background: #e4ddff"
                  >
                    <div>24-phase locking</div>
                    <div class="li-box__count">
                      {{
                        (header_data &&
                          header_data.Locked24 &&
                          onFeixedNum(header_data.Locked24, 6)) ||
                        0
                      }}
                      <div>{{ header_data.coinName }}</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div
                    class="li-box"
                    style="color: #391db7; background: #e4ddff"
                  >
                    <div>36-phase lock-up</div>
                    <div class="li-box__count">
                      {{
                        (header_data &&
                          header_data.Locked36 &&
                          onFeixedNum(header_data.Locked36, 6)) ||
                        0
                      }}
                      <div>{{ header_data.coinName }}</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
        <el-row
          style="margin-top: 60px"
          :gutter="20"
          class="container-row"
          type="flex"
        >
          <el-col :span="6">
            <div
              class="container-row__col"
              style="
                background: linear-gradient(180deg, #ffe3fe 0%, #fff8ff 100%);
                height: 100%;
              "
            >
              <div class="li-top" style="color: #a319a3">
                <img
                  class="container-row__col--img"
                  src="@/assets/icons/redemptionProgre.png"
                />
                Redemption in progress
              </div>
              <div class="li-bottom" style="color: #bf2fbf">
                {{
                  (header_data &&
                    header_data.totalUnDelegator &&
                    onFeixedNum(header_data.totalUnDelegator, 6)) ||
                  0
                }}
                <span> {{ header_data.coinName }} </span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div
              class="container-row__col"
              style="
                background: linear-gradient(180deg, #fff4ef 0%, #fdf7f4 100%);
                height: 100%;
              "
            >
              <div class="li-top" style="color: #dba308">
                <img
                  class="container-row__col--img"
                  src="@/assets/icons/bonded.png"
                />
                Bonded
              </div>
              <div class="li-bottom" style="color: #dba308">
                {{
                  (header_data &&
                    header_data.Bonded &&
                    onFeixedNum(header_data.Bonded, 6)) ||
                  0
                }}
                <span> {{ header_data.coinName }} </span>
              </div>

              <el-row :gutter="20">
                <el-col :span="12">
                  <div
                    class="li-box"
                    style="
                      color: #dba308;
                      background: #fff2d3;
                      min-height: 72px;
                    "
                  >
                    <div style="height: 24px">Non redeemable</div>
                    <div class="li-box__count">
                      {{
                        (header_data &&
                          header_data.BondedLocked &&
                          onFeixedNum(header_data.BondedLocked, 6)) ||
                        0
                      }}
                      <div>{{ header_data.coinName }}</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div
                    class="li-box"
                    style="
                      color: #dba308;
                      background: #fff2d3;
                      min-height: 72px;
                    "
                  >
                    <div style="height: 24px">Redeemable</div>
                    <div class="li-box__count">
                      {{
                        (header_data &&
                          header_data.BondedNotLocked &&
                          onFeixedNum(header_data.BondedNotLocked, 6)) ||
                        0
                      }}
                      <div>{{ header_data.coinName }}</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
          <el-col :span="12">
            <div
              class="container-row__col"
              style="
                background: linear-gradient(180deg, #fde8e8 0%, #fff4f4 100%);
                height: 100%;
              "
            >
              <div class="li-top" style="color: #c92c2c">
                <img
                  class="container-row__col--img"
                  src="@/assets/icons/commission.png"
                />
                Output of POS pool
              </div>
              <div class="li-bottom" style="color: #e84a4a">
                {{
                  (header_data &&
                    header_data.PosTotal &&
                    onFeixedNum(header_data.PosTotal, 6)) ||
                  0
                }}
                <span> {{ header_data.coinName }} </span>
              </div>

              <el-row :gutter="20" type="flex">
                <el-col :span="6">
                  <div
                    class="li-box"
                    style="
                      min-height: 72px;
                      font-size: 9px;
                      color: #e84a4a;
                      background: #ffe8de;
                    "
                  >
                    <div style="height: 24px">Withdrawal commission</div>
                    <div class="li-box__count">
                      {{
                        (header_data &&
                          header_data.withdrawCommission &&
                          onFeixedNum(header_data.withdrawCommission, 6)) ||
                        0
                      }}
                      <div>{{ header_data.coinName }}</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div
                    class="li-box"
                    style="
                      min-height: 72px;
                      font-size: 9px;
                      color: #e84a4a;
                      background: #ffe8de;
                    "
                  >
                    <div style="height: 24px">Withdrawal distribution</div>
                    <div class="li-box__count">
                      {{
                        (header_data &&
                          header_data.withdrawReward &&
                          onFeixedNum(header_data.withdrawReward, 6)) ||
                        0
                      }}
                      <div>{{ header_data.coinName }}</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div
                    class="li-box"
                    style="
                      min-height: 72px;
                      color: #e84a4a;
                      font-size: 9px;
                      background: #ffe8de;
                    "
                  >
                    <div style="height: 24px">Community Pool</div>
                    <div class="li-box__count">
                      {{
                        (header_data &&
                          header_data.CommunityPool &&
                          onFeixedNum(header_data.CommunityPool, 6)) ||
                        0
                      }}
                      <div>{{ header_data.coinName }}</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div
                    class="li-box"
                    style="
                      min-height: 72px;
                      font-size: 9px;
                      color: #e84a4a;
                      background: #ffe8de;
                    "
                  >
                    <div style="height: 24px">Community Pool extraction</div>
                    <div class="li-box__count">
                      {{
                        (header_data &&
                          header_data.withdrawalCommunityPool &&
                          onFeixedNum(
                            header_data.withdrawalCommunityPool,
                            6
                          )) ||
                        0
                      }}
                      <div>{{ header_data.coinName }}</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
          <!-- <el-col :span="6">
            <div
              class="container-row__col"
              style="
                background: linear-gradient(180deg, #ffe3fe 0%, #fff8ff 100%);
              "
            >
              <div class="li-top" style="color: #a319a3">
                <img
                  class="container-row__col--img"
                  src="@/assets/icons/redemption.png"
                />
                Redemption in progress
              </div>
              <div class="li-bottom" style="color: #bf2fbf">
                {{
                  (tongji_data &&
                    tongji_data.totalUnDelegator &&
                    onFeixedNum(tongji_data.totalUnDelegator, 6)) ||
                  0
                }}
                <span> {{ header_data.coinName }} </span>
              </div>
            </div>
          </el-col> -->
        </el-row>
      </div>
    </div>

    <!-- <header>
      <ul>
        <li>
          <div class="li-top">Total Supply</div>
          <div class="li-bottom">
            {{
              (header_data.TotalSupply &&
                onFeixedNum(header_data.TotalSupply, 6)) ||
              0
            }}
            {{ header_data.coinName }}
          </div>
        </li>
        <li>
          <div class="li-top">Circulation</div>
          <div class="li-bottom">
            {{
              (header_data.Circulation &&
                onFeixedNum(header_data.Circulation, 6)) ||
              0
            }}
            {{ header_data.coinName }}
          </div>
        </li>
        <li>
          <div class="li-top">Community Pool</div>
          <div class="li-bottom">
            {{
              (header_data.CommunityPool &&
                onFeixedNum(header_data.CommunityPool, 6)) ||
              0
            }}

            {{ header_data.coinName }}
          </div>
        </li>
        <li>
          <div class="li-top">Bonded</div>
          <div class="li-bottom">
            {{
              (header_data.Bonded && onFeixedNum(header_data.Bonded, 6)) || 0
            }}
            {{ header_data.coinName }}
          </div>
        </li>
      </ul>
    </header> -->
    <h3 class="s-b-t">{{ coinName }} Token Distribution</h3>
    <section>
      <div class="left">
        <div id="main"></div>
        <!-- <Piecharts :tongji_data="tongji_data"></Piecharts> -->
      </div>
      <div class="right">
        <ul>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor1"></span>
              <div>NO.1~5</div>
            </div>
            <div class="center-text">
              {{ (tongji_data.topFive * 100).toFixed(2) }}%
            </div>
            <div class="right-text">
              {{
                (tongji_data.topFiveCoin &&
                  onFeixedNum(tongji_data.topFiveCoin, 6)) ||
                0
              }}
              {{ tongji_data.coinName }}
            </div>
            <div class="right-number">{{ tongji_data.topFiveNum }}</div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor2"></span>
              <div>NO.6~10</div>
            </div>
            <div class="center-text">
              {{ (tongji_data.topTen * 100).toFixed(2) }}%
            </div>
            <div class="right-text">
              {{
                (tongji_data.topTenCoin &&
                  onFeixedNum(tongji_data.topTenCoin, 6)) ||
                0
              }}{{ tongji_data.coinName }}
            </div>
            <div class="right-number">{{ tongji_data.topTenNum }}</div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor3"></span>
              <div>NO.11~50</div>
            </div>
            <div class="center-text">
              {{ (tongji_data.topFifty * 100).toFixed(2) }}%
            </div>
            <div class="right-text">
              {{
                (tongji_data.topFiftyCoin &&
                  onFeixedNum(tongji_data.topFiftyCoin, 6)) ||
                0
              }}
              {{ tongji_data.coinName }}
            </div>
            <div class="right-number">{{ tongji_data.topFiftyNum }}</div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor4"></span>
              <div>NO.51~100</div>
            </div>
            <div class="center-text">
              {{ (tongji_data.topHun * 100).toFixed(2) }}%
            </div>
            <div class="right-text">
              {{
                (tongji_data.topHunCoin &&
                  onFeixedNum(tongji_data.topHunCoin, 6)) ||
                0
              }}
              {{ tongji_data.coinName }}
            </div>
            <div class="right-number">{{ tongji_data.topHunNum }}</div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor5"></span>
              <div>NO.101~500</div>
            </div>
            <div class="center-text">
              {{ (tongji_data.topFiveHun * 100).toFixed(2) }}%
            </div>
            <div class="right-text">
              {{
                (tongji_data.topFiveHunCoin &&
                  onFeixedNum(tongji_data.topFiveHunCoin, 6)) ||
                0
              }}
              {{ tongji_data.coinName }}
            </div>
            <div class="right-number">{{ tongji_data.topFiveHunNum }}</div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor6"></span>
              <div>NO.501~1000</div>
            </div>
            <div class="center-text">
              {{ (tongji_data.topThousand * 100).toFixed(2) }}%
            </div>
            <div class="right-text">
              {{
                (tongji_data.topThousandCoin &&
                  onFeixedNum(tongji_data.topThousandCoin, 6)) ||
                0
              }}
              {{ tongji_data.coinName }}
            </div>
            <div class="right-number">{{ tongji_data.topThousandNum }}</div>
          </li>
          <li>
            <div class="fk-box">
              <span class="fangkuai fkcolor7"></span>
              <div>NO.1001-</div>
            </div>
            <div class="center-text">
              {{ (tongji_data.otherAddress * 100).toFixed(2) }}%
            </div>
            <div class="right-text">
              {{
                (tongji_data.otherAddressCoin &&
                  onFeixedNum(tongji_data.otherAddressCoin, 6)) ||
                0
              }}
              {{ tongji_data.coinName }}
            </div>
            <div class="right-number">{{ tongji_data.otherNum }}</div>
          </li>
        </ul>
      </div>
    </section>
  </div>
</template>
<script>
// import Piecharts from "./conmponets/piecharts";
import * as echarts from "echarts/core";
import { TooltipComponent, LegendComponent } from "echarts/components";
import { PieChart } from "echarts/charts";
import { CanvasRenderer } from "echarts/renderers";
import { getCoinInfo, getCoinRange } from "@/api/tongji";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      header_data: [],
      tongji_data: [],
      coinName: "",
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
        this.$scrollHeightFn();
      });
    },
    getCoinInfoFn() {
      getCoinInfo().then((res) => {
        this.header_data = res.result;
        this.coinName = res.result.coinName.toUpperCase();
        this.header_data.coinName = this.header_data.coinName.toUpperCase();
      });
    },
  },
  // components: { Piecharts }
};
</script>

<style scoped lang="less">
.container-content {
  padding: 5px;
  &__content {
    background: #fff;
    padding: 0px 30px 30px 30px;
    border: 1px solid #eaebec;
    box-shadow: 0px 3px 7px 1px rgba(56, 54, 54, 0.06);
    border-radius: 5px;
  }
}
.container-row {
  margin-top: 30px;
  margin-bottom: 30px;
  background: #fff;
  &__col {
    padding: 30px 15px 0 15px;
    .li-top {
      font-size: 18px;
      line-height: 20px;
    }
    .li-right {
      font-size: 20px;
      font-weight: 600;
      // margin-top: 20px;
      > span {
        font-size: 14px;
        font-weight: 400;
        margin-left: 10px;
      }
    }
    .li-bottom {
      font-size: 20px;
      font-weight: 600;
      margin-top: 20px;
      > span {
        font-size: 14px;
        font-weight: 400;
        margin-left: 10px;
      }
    }
    .li-box {
      text-align: center;
      padding: 15px;
      border-radius: 10px;
      margin-top: 20px;
      font-size: 12px;
      &__count {
        font-size: 14px;
        font-weight: 600;
        margin-top: 10px;
        > div {
          margin-top:5px;
          font-weight: 400;
          font-size: 9px;
        }
      }
    }
    &--img {
      width: 20px;
      height: 20px;
      margin-right: 10px;
    }
  }
}
#main {
  width: 300px;
  height: 300px;
}
header {
  background: #fff;
  padding: 30px 0px;
  border: 1px solid #eaebec;
  box-shadow: 0px 3px 7px 1px rgba(56, 54, 54, 0.06);
  border-radius: 5px;
  ul {
    display: flex;
    justify-content: space-around;
    li {
      width: 260px;
      background: #ffffff;
      border: 1px solid #eaebec;
      border-radius: 5px;
      .li-top {
        line-height: 40px;
        text-align: center;
        background: #f9f9f9;
        border: 1px solid #eaebec;
      }
      .li-bottom {
        line-height: 70px;
        text-align: center;
        font-weight: bold;
        font-size: 14px;
      }
    }
  }
}
section {
  background: #fff;
  border: 1px solid #eaebec;
  box-shadow: 0px 3px 7px 1px rgba(56, 54, 54, 0.06);
  border-radius: 5px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 60px 0px 40px 0px;
  margin-bottom: 40px;
}
.left {
  margin: 0px 40px;
  margin-right: 60px;
}
.right {
  padding-right: 40px;
  .fk-box {
    width: 180px;
    display: flex;
    align-items: center;
  }
  .fangkuai {
    display: inline-block;
    width: 51px;
    height: 24px;
    border-radius: 5px;
    margin-right: 20px;
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
  li {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    span {
      display: inline-block;
    }
    .center-text {
      width: 80px;
      border-left: 3px solid #9196c1;
      padding: 0px 30px;
      margin: 0px 70px;
      color: #9196c1;
    }
    .right-text {
      // width: 200px;
      flex: 1;
      text-align: right;
    }
  }
}
.right-number {
  width: 100px;
  text-align: right;
  color: #9196c1;
}
</style>
