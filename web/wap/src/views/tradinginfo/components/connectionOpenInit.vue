<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
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

        <li>
          <span>Connection ID:</span>
          <div>
            {{data.connection_id}}
          </div>
        </li>

        <li>
          <span>Counterparty Client ID:</span>
          <div>
            {{data.counterparty_client_id}}
          </div>
        </li>

        <li>
          <span>Client ID:</span>
          <div>
            {{data.client_id}}
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
