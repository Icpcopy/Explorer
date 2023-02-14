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
          <span>Client ID:</span>
          <div>{{data.client_id}}</div>
        </li>

        <li>
          <span>Height:</span>
          <div>{{data.height}}</div>
        </li>

        <li>
          <span>App:</span>
          <div>{{data.app}}</div>
        </li>

        <li class="wasewrap">
          <span>App Hash:</span>
          <div>{{data.app_hash}}</div>
        </li>

        <li>
          <span>Chain Id:</span>
          <div>{{data.chain_id}}</div>
        </li>

        <li class="wasewrap">
          <span>Data Hash:</span>
          <div>{{data.data_hash}}</div>
        </li>

        <li class="wasewrap">
          <span>Evidence Hash:</span>
          <div>{{data.evidence_hash}}</div>
        </li>

        <li class="wasewrap">
          <span>Hash:</span>
          <div>{{data.hash}}</div>
        </li>

        <li>
          <span>Total:</span>
          <div>{{data.total}}</div>
        </li>

        <li class="wasewrap">
          <span>Consensus Hash:</span>
          <div>{{data.consensus_hash}}</div>
        </li>

        <li class="wasewrap">
          <span>Validators Hash:</span>
          <div>{{data.validators_hash}}</div>
        </li>

        <li class="wasewrap">
          <span>Last Commit Hash:</span>
          <div>{{data.last_commit_hash}}</div>
        </li>

        <li class="wasewrap">
          <span>Proposer Address:</span>
          <div>{{data.proposer_address}}</div>
        </li>

        <li class="wasewrap">
          <span>Last Results Hash:</span>
          <div>{{data.last_results_hash}}</div>
        </li>

        <li class="wasewrap">
          <span>Next Validators Hash:</span>
          <div>{{data.next_validators_hash}}</div>
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
