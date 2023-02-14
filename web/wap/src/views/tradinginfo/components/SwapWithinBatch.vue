<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>from:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.from)">
              {{
              $jiequStrFnMaxlength(data.from)
              }}
            </span>
          </div>
        </li>
        <li>
          <span>Offer Amount:</span>
          <div>
            <div>{{data.offerAmount}} {{data.offerCoin.toUpperCase()}}</div>
          </div>
        </li>
        <li>
          <span>Offer Amount:</span>
          <div>
            <div>{{data.feePoolAmount}} {{data.offerCoin.toUpperCase()}}</div>
          </div>
        </li>
        <li>
          <span>Buy Amount:</span>
          <div>{{$JiequXiaoshudianFn(data.buyAmount)}} {{data.buyCoin.toUpperCase()}}</div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  props: ["data"],
  data () {
    return {

    }
  },
  components: {

  }
}
</script>

<style scoped lang="less">
@import url("../../../css/index.less");
</style>
