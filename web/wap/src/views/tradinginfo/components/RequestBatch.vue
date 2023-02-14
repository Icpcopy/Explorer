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
          <span>CoinName:</span>
          <div>{{ data.coinName}}</div>
        </li>
        <li>
          <span>ContractAddress:</span>
          <div>{{ data.contractAddress}}</div>
        </li>
        <li>
          <span>BtchNonce:</span>
          <div>{{data.batchNonce}}</div>
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
