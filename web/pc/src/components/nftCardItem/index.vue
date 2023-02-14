<template>
  <div class="nftCardItem">
    <div class="nftCardItem-title">
      <img class="nftCardItem-title__icon" :src="icon" />
      <span class="nftCardItem-title__text">{{ title }}</span>
    </div>
    <div class="nftCardItem-count">
      <router-link v-if="path" :to="{ path: path }">{{ count }} </router-link>
      <span v-else>{{ count }}</span>
    </div>
    <div class="nftCardItem-description">{{ description }}</div>
  </div>
</template>

<script>
export default {
  props: {
    icon: { type: String, default: "" },
    title: { type: String, default: "" },
    count: { type: Number, default: 12345 },
    path: { type: String, default: "" },
    description: { type: String, default: "" },
  },
  components: {},
  data() {
    return {};
  },
  methods: {},
};
</script>

<style scoped lang="less">
.nftCardItem {
  border-radius: 5px;
  border: 1px solid #dedede;
  padding: 10px;
  &-title {
    overflow: hidden;
    &__icon {
      float: left;
    }
    &__text {
      margin-left: 10px;
      float: left;
      word-break: break-all;
      line-height: 17px;
    }
  }
  &-count {
    text-align: center;
    // color: #5b1cf6;
    font-size: 24px;
    margin: 24px 0;
    > a {
      color: #5b1cf6;
      text-decoration: none;
    }
  }
  &-description {
    text-align: center;
    color: #9196c1;
    font-size: 14;
    height: 14px;
    // line-height: 14px;
  }
}
</style>
