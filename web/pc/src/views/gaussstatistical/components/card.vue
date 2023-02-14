<template>
  <div>
    <div id="main"></div>
  </div>
</template>

<script>
// import * as echarts from "echarts";
import * as echarts from "echarts/core";
import { TooltipComponent, LegendComponent } from "echarts/components";
import { PieChart } from "echarts/charts";
import { CanvasRenderer } from "echarts/renderers";
export default {
  props: ["tongji_data"],
  data() {
    return {};
  },
  mounted() {
   
  },
  components: {}
};
</script>

<style scoped lang="less">
#main {
  width: 300px;
  height: 300px;
}
</style>
