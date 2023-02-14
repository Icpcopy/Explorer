<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>From:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.from)">
              {{
              $jiequStrFnMaxlength(data.from)
              }}
            </span>
          </div>
        </li>
        <li>
          <span>PoolId:</span>
          <div>{{data.poolId}}</div>
        </li>
        <li>
          <span>LeftAmount:</span>
          <div>{{data.leftAmount}}{{data.leftCoin.toUpperCase()}}</div>
        </li>
        <li>
          <span>RightAmount:</span>
          <div>{{data.rightAmount}}{{data.rightCoin.toUpperCase()}}</div>
        </li>
        <li>
          <span>TxPairName:</span>
          <div>{{data.txPairName.toUpperCase()}}</div>
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
