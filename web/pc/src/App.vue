<template>
  <div class="box">
    <keep-alive>
      <router-view></router-view>
    </keep-alive>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  watch: {
    $route() {}
  }
};
</script>
<style lang="less">
</style>
