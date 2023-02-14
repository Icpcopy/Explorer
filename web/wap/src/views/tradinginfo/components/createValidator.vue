<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>minimgbox:</span>
          <div
            class="zhuti colors minimgbox"
            v-if="data.to.icon"
            @click="$userInfoRouterFn(data.to.operatorAddress)"
          >
            <img class="user_icon" :src="data.to.icon" alt />
            <span>{{ data.to.moniker }}</span>
          </div>
          <div class="zhuti colors" @click="$headerRouterFn(data.to)" v-else>
            {{
            $jiequStrFnMaxlength(data.to)
            }}
          </div>
        </li>

        <li>
          <span>Moniker:</span>
          <div>{{ data.from.moniker ? data.from.moniker : "--" }}</div>
        </li>

        <li>
          <span>Identity:</span>
          <div>{{ data.from.identity ? data.from.identity : "--" }}</div>
        </li>

        <li>
          <span>Self-Bonded:</span>
          <div>{{data.selfDelegateAmount}}</div>
        </li>

        <li>
          <span>Min Self Delegation:</span>
          <div>{{data.min_self_delegation}}</div>
        </li>

        <li>
          <span>Owner Address:</span>
          <div
            class="colors"
            @click="$headerRouterFn(data.from)"
          >{{ $jiequStrFnMaxlength(data.from) }}</div>
        </li>

        <li>
          <span>Consensus Pubkey:</span>
          <div
            class="colors"
            @click="$headerRouterFn(data.consensus_pubkey)"
          >{{$jiequStrFnMaxlength(data.consensus_pubkey)}}</div>
        </li>

        <li>
          <span>Commission Rate:</span>
          <div>{{data.commission.rate?(data.commission.rate * 100).toFixed() + "%":"--"}}</div>
        </li>

        <li>
          <span>Website:</span>
          <div>{{data.description.website?data.description.website : "--"}}</div>
        </li>

        <li>
          <span>Details:</span>
          <div class="details-box">{{data.description.details ? data.description.details : "--"}}</div>
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
li span {
  display: inline-block;
  width: 130 / @rem !important;
}
.details-box {
  width: 220 / @rem;
  height: auto !important;
  word-wrap: break-word;
  word-break: break-all;
  line-height: 25 / @rem;
}
.list-info-box {
  padding-bottom: 60px !important;
}
</style>
