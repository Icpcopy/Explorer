<template>
   <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>

export default {
  name: 'App',

}
</script>

<style lang="less" scoped>
@import url("./css/index.less");
#app{
width: 375/@rem;
font-size: 12/@rem;
}
</style>
