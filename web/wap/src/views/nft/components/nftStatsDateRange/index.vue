<template>
  <van-row class="nft-daterange">
    <van-col class="nft-daterange__box" @click="dateShow = true">
      {{ startTime }}
      <img class="nft-daterange__box--img" src="@/assets/icon/date.png" />
    </van-col>
    <van-col class="nft-daterange__line">—</van-col>
    <van-col class="nft-daterange__box" @click="dateShow = true">
      {{ endTime }}
      <img class="nft-daterange__box--img" src="@/assets/icon/date.png" />
    </van-col>
  </van-row>
  <van-calendar
    class="nft-daterange__calendar"
    title="日期选择"
    type="range"
    :min-date="minDate"
    :max-date="maxDate"
    :max-range="30"
    :default-date="[new Date(startTime), new Date(endTime)]"
    v-model:show="dateShow"
    @confirm="onConfirmTime"
  />
</template>

<script>
export default {
  components: {},
  props: {
    dateList: { type: Array, default: () => [] },
    dateDataList: { type: Array, default: () => [] },
  },
  name: "NftStatsDateRange",
  data() {
    const startTime = this.$dayjs()
      .subtract(7, "day")
      .format("YYYY-MM-DD");
    return {
      startTime: startTime,
      endTime: this.$dayjs().format("YYYY-MM-DD"),
      dateShow: false,
      date: "",
      show: false,
    };
  },
  computed: {
    minDate() {
      const date = this.$dayjs()
        .subtract(1, "year")
        .format("YYYY-MM-DD");
      return new Date(date);
    },

    maxDate() {
      return new Date();
    },
  },
  watch: {},
  methods: {
    //日历最大日期
    initMaxDate() {
      return new Date();
    },
    //初始时间
    onConfirmTime(e) {
      this.startTime = this.$dayjs(e[0]).format("YYYY-MM-DD");
      this.endTime = this.$dayjs(e[1]).format("YYYY-MM-DD");
      this.$emit("onChangeTime", this.startTime, this.endTime);
      this.dateShow = false;
    },
    //结束时间
    onConfirmEndTime(e) {
      this.endShow = false;
      console.log(e, "e====onConfirmEndTime");
    },
  },
  mounted() {
    this.$emit("onChangeTime", this.startTime, this.endTime);
  },
  created() {},
};
</script>
<style lang="less" scoped>
.nft-daterange {
  &__box {
    display: flex;
    width: 105px;
    height: 30px;
    padding: 8px 5px;
    background: #ffffff;
    border-radius: 5px;
    border: 1px solid #ededed;
    color: #b7b7b7;
    justify-content: space-between;
    &--img {
      width: 14px;
      height: 12px;
    }
  }
  &__line {
    margin: 0 10px;
    line-height: 30px;
    color: #b7b7b7;
  }
}
</style>
