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
          <span>State:</span>
          <div>
            {{data.state}}
          </div>
        </li>

        <li>
          <span>Version:</span>
          <div>
            {{data.version}}
          </div>
        </li>

        <li>
          <span>Ordering:</span>
          <div>
            {{data.ordering}}
          </div>
        </li>

        <li>
          <span>Channel Id:</span>
          <div>
            {{data.channel_id}}
          </div>
        </li>

        <li>
          <span>Connection Hops:</span>
          <div>
            {{data.connection_hops}}
          </div>
        </li>

        <li>
          <span>Port Id:</span>
          <div>
            {{data.port_id}}
          </div>
        </li>

        <li>
          <span>Previous Channel Id:</span>
          <div>
            {{data.previous_channel_id}}
          </div>
        </li>

        <li>
          <span>Proof Init:</span>
          <div>
            {{data.proof_init}}
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

        <li>
          <span>Counterparty Version:</span>
          <div>
            {{data.counterparty_version}}
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
