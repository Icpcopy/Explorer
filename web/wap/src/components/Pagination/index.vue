<template>
  <div class="container">
    <div class="pages-zujian">
      <div class="frist shubiao" @click="handleSizeChange('frist')">
        <van-icon name="arrow-left" /><van-icon name="arrow-left" />
      </div>
      <div class="left_button noclick">
        <van-icon
          name="arrow-left"
          class="el-icon-arrow-left shubiao"
          @click="handleSizeChange('subtract')"
        />
      </div>
      <div class="center-text">
        <span>{{ pageIndex }}</span>
        <span>/</span>
        <span>{{
          (total && pageSize && Math.ceil(total / pageSize)) || 0
        }}</span>
      </div>
      <div>
        <van-icon
          name="arrow"
          class="right_button el-icon-arrow-right colors"
          @click="handleSizeChange('add')"
        />
      </div>

      <div class="last shubiao" @click="handleSizeChange('last')">
        <van-icon name="arrow" /><van-icon name="arrow" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["pageIndex", "pageSize", "total"], // pageIndex 当前页  pageSize每页多少条 total总条数
  data() {
    return {
      pages_size_num: 1,
      maxPage: undefined,
    };
  },
  computed: {},
  created() {
    var left_button = document.getElementsByClassName("left_button")[0];
  },
  mounted() {},
  methods: {
    handleSizeChange(val) {
      var left_button = document.getElementsByClassName("left_button")[0];
      var right_button = document.getElementsByClassName("right_button")[0];
      console.log(right_button);
      this.pages_size_num = this.pageIndex;
      switch (val) {
        case "frist":
          this.$emit("currpageChange", 1);
          break;
        case "add":
          if (this.pages_size_num < Math.ceil(this.total / this.pageSize)) {
            this.pages_size_num++;
            this.$emit("currpageChange", this.pages_size_num);
          } else {
            console.log("进来不");
            return;
          }

          // if (this.pages_size_num == Math.ceil(this.total / this.pageSize)) {
          //   // right_button.classList.add("noclick");
          // }
          // if (this.pages_size_num >= Math.ceil(this.total / this.pageSize)) {
          //   this.pages_size_num = Math.ceil(this.total / this.pageSize);
          // } else {
          //   console.log("执行了");
          //   // left_button.classList.remove("noclick");
          //   // right_button.classList.remove("noclick");
          // }
          break;
        case "subtract":
          if (this.pages_size_num > 1) {
            this.pages_size_num--;
            this.$emit("currpageChange", this.pages_size_num);
            // left_button.classList.add("noclick");
          }
          // if (this.pages_size_num <= 1) {
          //   this.pages_size_num = 1;
          // } else {
          //   // left_button.classList.remove("noclick");
          //   // right_button.classList.remove("noclick");
          // }
          break;
        case "last":
          this.pages_size_num = Math.ceil(this.total / this.pageSize);
          this.$emit("currpageChange", this.pages_size_num);
          break;
      }
      // console.log(this.pages_size_num);
    },
  },
  components: {},
};
</script>

<style scoped lang="less">
@import url("../../css/index");
.container {
  width: 100%;
  color: #2a2a2a;
  text-align: center;
  padding-bottom: 20 / @rem;
}
.pages-zujian {
  display: flex;
  justify-content: space-around;
  margin-right: 15 / @rem;
  div {
    width: 37 / @rem;
    height: 29 / @rem;
    line-height: 29 / @rem;
    border: 1px solid #ededed;
    text-align: center;
  }
  .center-text {
    width: auto;
    padding: 0px 20 / @rem;
  }
}
</style>
