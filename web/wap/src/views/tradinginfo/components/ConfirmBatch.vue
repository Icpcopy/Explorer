<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Orchestrator:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.orchestrator)">
              {{
              $jiequStrFnMaxlength(data.orchestrator)
              }}
            </span>
          </div>
        </li>

        <li>
          <span>From:</span>
          <div>{{ $jiequStrFnMaxlength(data.from)}}</div>
        </li>
        <li>
          <span>ContractAddress:</span>
          <div>{{ data.contractAddress}}</div>
        </li>
        <li>
          <span>Nonce:</span>
          <div>{{ data.nonce}}</div>
        </li>
        <li>
          <span>CoinName:</span>
          <div>{{data.coinName}}</div>
        </li>
        <li>
          <span>Signature:</span>
          <div>{{data.signature}}</div>
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
