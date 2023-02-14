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
          <span>To:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.to)">
              {{
              $jiequStrFnMaxlength(data.to)
              }}
            </span>
          </div>
        </li>
        <li>
          <span>EthBlockHeight:</span>
          <div>{{data.ethBlockHeight}}</div>
        </li>
        <li>
          <span>ContractAddress:</span>
          <div>{{data.contractAddress}}</div>
        </li>
        <li>
          <span>EventNonce:</span>
          <div>{{data.eventNonce}}</div>
        </li>
        <li>
          <span>Amount:</span>
          <div>{{ data.amount }}{{data.coinName}}</div>
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
