<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Client ID:</span>
          <div>
            {{data.client_id}}
          </div>
        </li>

        <li>
          <span>Client Type:</span>
          <div>
            {{data.client_type}}
          </div>
        </li>

        <li>
          <span>Chain ID:</span>
          <div>
            {{data.chain_id}}
          </div>
        </li>

        <li>
          <span>Trusting Period:</span>
          <div>
            {{data.trusting_period}}
          </div>
        </li>

        <li>
          <span>Unboding Period:</span>
          <div>
            {{data.unbonding_period}}
          </div>
        </li>

        <li>
          <span>Signer:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.signer)">
              {{
                $jiequStrFnMaxlength(data.signer)
              }}
            </span>
          </div>
        </li>

      </ul>
    </div>
  </div>
</template>

<script>
export default {
  props:["data"],
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
