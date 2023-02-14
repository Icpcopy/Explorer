<template>
  <VHeader ref="childheader"></VHeader>
  <router-view />
</template>

<script>
import VHeader from "@/components/header";
export default {
  components: { VHeader },
  data() {
    return {};
  },
  methods: {},
  mounted() {},
  created() {},
};
</script>

<style scoped lang="less"></style>
