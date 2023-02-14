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
          <span>EventNonce:</span>
          <div>{{ data.eventNonce}}</div>
        </li>
        <li>
          <span>EthBlockHeight:</span>
          <div>{{ data.ethBlockHeight}}</div>
        </li>
        <li>
          <span>ContractAddress:</span>
          <div>{{data.contractAddress}}</div>
        </li>
        <li>
          <span>BathNonce:</span>
          <div>{{data.bathNonce}}</div>
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
