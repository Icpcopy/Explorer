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
          <div class="maodian">{{ $headerRouterFn(data.from) }}</div>
        </div>
        <div>
          <span class="titles">CoinName:</span>
          <div>{{ data.coinName }}</div>
        </div>
        <div>
          <span class="titles">contractAddress:</span>
          <div>{{ data.contractAddress }}</div>
        </div>

        <div>
          <span class="titles">BtchNonce:</span>
          <div>{{ data.batchNonce }}</div>
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
