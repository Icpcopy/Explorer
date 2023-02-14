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
              {{ data.signer }}
            </span>
          </div>
        </li>

        <li>
          <span>Port ID:</span>
          <div>
            {{data.port_id}}
          </div>
        </li>

        <li>
          <span>Channel Id:</span>
          <div>
            {{data.channel_id}}
          </div>
        </li>

        <li>
          <span>Counterparty Channel Id:</span>
          <div>
            {{data.counterparty_channel_id}}
          </div>
        </li>

        <li>
          <span>Counterparty Version:</span>
          <div>
            {{data.counterparty_version}}
          </div>
        </li>

        <li>
          <span>Proof Try:</span>
          <div>
            {{data.proof_try}}
          </div>
        </li>

        <li>
          <span>Revision Number:</span>
          <div>
            {{data.revision_number}}
          </div>
        </li>
        
        <li>
          <span>Revision Height:</span>
          <div>
            {{data.revision_height}}
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
