<template>
  <div class="container">
    <section class="section-top" style="margin-top:30px">
      <div class="s-title">Transaction Message or Result</div>
      <div class="content displaywrap">
        <div>
          <span class="titles">TxType:</span>
          <div>{{ data.type }}</div>
        </div>
        <div>
          <span class="titles">From:</span>
          <div class="maodian" @click="$headerRouterFn(data.from)">{{ data.from }}</div>
        </div>
        <div>
          <span class="titles">OfferAmount:</span>
          <div>{{ data.offerAmount }} {{data.offerCoin.toUpperCase()}}</div>
        </div>
        <div>
          <span class="titles">FeePoolAmount:</span>
          <div>{{ data.feePoolAmount }}{{data.offerCoin.toUpperCase()}}</div>
        </div>
        <div>
          <span class="titles">BuyAmount:</span>
          <div>{{ data.buyAmount }}{{data.buyCoin.toUpperCase()}}</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  props: ["data"],
  data() {
    return {};
  },
  components: {}
};
</script>

<style scoped lang="less">
</style>
