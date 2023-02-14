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
          <span>Data:</span>
          <div>
            {{data.data}}
          </div>
        </li>
        
        <li>
          <span>Sequence:</span>
          <div>
            {{data.sequence}}
          </div>
        </li>

        <li>
          <span>Source Port:</span>
          <div>
            {{data.source_port}}
          </div>
        </li>

        <li>
          <span>Source Channel:</span>
          <div>
            {{data.source_channel}}
          </div>
        </li>

        <li>
          <span>Destination Port:</span>
          <div>
            {{data.destination_port}}
          </div>
        </li>

        <li>
          <span>Timeout Timestamp:</span>
          <div>
            {{data.timeout_timestamp}}
          </div>
        </li>

        <li>
          <span>Destination Channel:</span>
          <div>
            {{data.destination_channel}}
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

        <li>
          <span>Proof Commitment:</span>
          <div>
            {{data.proof_commitment}}
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
