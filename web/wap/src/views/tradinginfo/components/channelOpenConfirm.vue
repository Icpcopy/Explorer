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
          <span>Channel Id:</span>
          <div>
            {{data.channel_id}}
          </div>
        </li>

        <li>
          <span>Port Id:</span>
          <div>
            {{data.port_id}}
          </div>
        </li>

        <li>
          <span>Proof Ack:</span>
          <div>
            {{data.proof_ack}}
          </div>
        </li>

        <li>
          <span>Revision Height:</span>
          <div>
            {{data.revision_height}}
          </div>
        </li>

        <li>
          <span>Revision Number:</span>
          <div>
            {{data.revision_number}}
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
