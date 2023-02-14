<template>
  <div class="container-box">
    <VHeader ref="childheader"></VHeader>
    <router-view @getisSousuoFn="getisSousuoFn"></router-view>
  </div>
</template>

<script>
import VHeader from '@/components/header';
import { Button } from 'vant';
export default {
  data () {
    return {

    }
  },
  methods: {
    getisSousuoFn () {
      this.$refs.childheader.isSousuoBoolFn();
    }
  },
  components: {
    VHeader,
    Button

  }
}
</script>

<style scoped lang="less">
.container-box {
  background: #f8f8f8;
}
</style>
