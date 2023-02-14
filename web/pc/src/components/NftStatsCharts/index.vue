<template>
  <div class="nft-eahcrts">
    <div ref="charts" class="nft-eahcrts__chartBox"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  components: {},
  props: {
    dateList: { type: Array, default: () => [] },
    dateDataList: { type: Array, default: () => [] },
  },
  name: "NftStatsCharts",
  data() {
    return {};
  },
  watch: {
    dateDataList: {
      handler() {
        this.init();
      },
    },
  },
  methods: {
    init() {
      var chartDom = this.$refs.charts;
      var myChart = echarts.init(chartDom);
      var option;
      option = {
        title: {
          subtext: "Units：ict",
        },
        tooltip: {
          trigger: "axis",
        },
        legend: {
          data: ["Daily transactions"],
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        toolbox: {
          feature: {
            // saveAsImage: {},
          },
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: this.dateList,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: "Daily transactions",
            type: "line",
            stack: "Total",
            smooth: true,
            data: this.dateDataList,
          },
        ],
      };
      myChart.setOption(option);
    },
  },
  mounted() {
    this.init();
  },
};
</script>
<style lang="less" scoped>
.nft-eahcrts {
  width: 100%;
  height: 100%;
  &__chartBox {
    width: 100%;
    height: 100%;
  }
}
</style>
