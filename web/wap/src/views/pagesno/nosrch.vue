<template>
  <div class="container">
    <div class="imgs">
      <img src="../../assets/nodate.png" alt />
      <h3>Search not found</h3>
      <p>
        Oops! The search string you entered was:
        <span class="yellows">{{ $route.query.id }}</span> Sorry! This is an
        invalid search string.
      </p>
      <div class="button-box" @click="backRouterHome">Bake Home</div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {

    }
  },
  methods: {
    backRouterHome () {
      this.$router.push("/index");
    }
  },
  components: {

  }
}
</script>

<style scoped lang="less">
@import url("../../css/index.less");
.yellows {
  color: #ffa610;
}
.container {
  text-align: center;
  .imgs {
    position: absolute;
    top: 15%;
    left: 50%;
    margin-left: -150 / @rem;
    width: 300 / @rem;
    color: #5941ac;
    img {
      width: 290 / @rem;
    }
    h3 {
      font-size: 18 / @rem;
      line-height: 30 / @rem;
    }
    p {
      font-size: 14 / @rem;
      line-height: 25 / @rem;
    }
  }
}
.button-box {
  width: 100%;
  height: 40 / @rem;
  line-height: 40 / @rem;
  text-align: center;
  background: #633af9;
  border-radius: 5 / @rem;
  font-size: 14 / @rem;
  color: #fff;
  margin-top: 20 / @rem;
}
</style>
