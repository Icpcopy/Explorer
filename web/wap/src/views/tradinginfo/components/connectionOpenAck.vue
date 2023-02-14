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
          <span>Connection Id:</span>
          <div>
            {{data.connection_id}}
          </div>
        </li>

        <li>
          <span>Counterparty Connection Id:</span>
          <div>
            {{data.counterparty_connection_id}}
          </div>
        </li>

        <li>
          <span>Version:</span>
          <div>
            {{data.version}}
          </div>
        </li>

        <li>
          <span>Proof Client:</span>
          <div>
            {{data.proof_client}}
          </div>
        </li>

        <li>
          <span>Proof Consensus:</span>
          <div>
            {{data.proof_consensus}}
          </div>
        </li>

        <li>
          <span>Proof Try:</span>
          <div>
            {{data.proof_try}}
          </div>
        </li>
        
        <li>
          <span>AiTeType:</span>
          <div>
            {{data.aiTeType}}
          </div>
        </li>

        <li>
          <span>Chain Id:</span>
          <div>
            {{data.chain_id}}
          </div>
        </li>

        <li>
          <span>Proof Specs:</span>
          <div>
            {{data.proof_specs}}
          </div>
        </li>

        <li>
          <span>Numerator:</span>
          <div>
            {{data.numerator}}
          </div>
        </li>

        <li>
          <span>Denominator:</span>
          <div>
            {{data.denominator}}
          </div>
        </li>

        <li>
          <span>Upgrade Path:</span>
          <div>
            {{data.upgrade_path}}
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
          <span>Max Clock Drift:</span>
          <div>
            {{data.max_clock_drift}}
          </div>
        </li>

        <li>
          <span>Trusting Period:</span>
          <div>
            {{data.trusting_period}}
          </div>
        </li>

        <li>
          <span>Unbonding Period:</span>
          <div>
            {{data.unbonding_period}}
          </div>
        </li>

        <li>
          <span>Allow Update After Expiry:</span>
          <div>
            {{data.allow_update_after_expiry}}
          </div>
        </li>

        <li>
          <span>Allow Update After Misbehaviour:</span>
          <div>
            {{data.allow_update_after_misbehaviour}}
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
