<template>
  <div class="container">
    <section class="section-top" style="margin-top: 30px">
      <div class="s-title">Transaction Message or Result</div>
      <div class="content displaywrap">
        <div>
          <span class="titles">TxType:</span>
          <div>{{ data.type }}</div>
        </div>
        <div>
          <span class="titles">From:</span>
          <div class="maodian" @click="$headerRouterFn(data.from)">
            {{ data.from }}
          </div>
        </div>
        <div>
          <span class="titles">Amount:</span>
          <div>
            {{ (data.amount && onFeixedNum(data.amount, 6)) || 0 }}
            {{ $JiequXiaoshudianFn(data.coinName) }}
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  props: ["data"],
  data() {
    return {};
  },
  components: {},
};
</script>

<style scoped lang="less">
</style>
