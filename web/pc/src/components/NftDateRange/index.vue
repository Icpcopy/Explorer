<template>
  <div class="nft-dateRange">
    <el-date-picker
      v-model="startTime"
      @change="onChangeStartTime"
      :disabledDate="onDisableStartDate"
      type="date"
      size="small"
      placeholder="start time"
      style="width:150px;"
    >
    </el-date-picker>
    <span style="margin:0 10px;">-</span>
    <el-date-picker
      style="width:150px;"
      v-model="endTime"
      @change="onChangeEndTime"
      type="date"
      size="small"
      placeholder="end time"
      :disabledDate="onDisableDate"
    >
    </el-date-picker>
  </div>
</template>

<script>
import dayjs from "dayjs";
export default {
  props: {},
  components: {},
  name: "NftDatePickerRange",
  data() {
    return {
      startTime: "",
      endTime: "",
    };
  },
  methods: {
    //初始化时间段
    init() {
      this.startTime = this.$dayjs().subtract(7, "day");
      this.endTime = this.$dayjs();
      this.$emit("onChangeTime", this.startTime, this.endTime);
    },
    //修改初始时间
    onChangeStartTime() {
      this.endTime = "";
    },
    //修改结束时间
    onChangeEndTime() {
      this.$emit("onChangeTime", this.startTime, this.endTime);
    },
    //禁止开始时间点击
    onDisableStartDate(e) {
      const endTime = this.$dayjs(e).format("YYYY-MM-DD");
      const startTime = this.$dayjs().format("YYYY-MM-DD");
      const subtractTime = dayjs(startTime).diff(dayjs(endTime), "day");
      if (subtractTime < 7) {
        return true;
      } else {
        return false;
      }
    },
    //是否禁止点击时间
    onDisableDate(e) {
      const startTime = this.$dayjs(e).format("YYYY-MM-DD");
      if (!this.startTime) {
        const endTime = this.$dayjs().format("YYYY-MM-DD");
        const subtractTime = dayjs(startTime).diff(dayjs(endTime), "day");
        if (subtractTime > 0) {
          return true;
        } else {
          return false;
        }
      } else {
        const endTime = this.$dayjs(this.startTime).format("YYYY-MM-DD");
        const subtractTime = this.$dayjs(startTime).diff(
          this.$dayjs(endTime),
          "day"
        );
        if (subtractTime < 7 || subtractTime > 30) {
          return true;
        } else {
          if (
            this.$dayjs(startTime).diff(
              this.$dayjs(this.$dayjs().format("YYYY-MM-DD")),
              "day"
            ) > 0
          ) {
            return true;
          } else {
            return false;
          }
        }
      }
    },
  },
  mounted() {
    this.init();
  },
};
</script>
<style lang="less" scoped>
.nft-dateRange {
}
</style>
