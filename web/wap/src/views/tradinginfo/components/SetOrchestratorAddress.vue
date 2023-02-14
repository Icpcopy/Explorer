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
          <div
            class="zhuti colors minimgbox"
            v-if="data.from.icon"
            @click="$userInfoRouterFn(data.from.operatorAddress)"
          >
            <img class="user_icon" :src="data.from.icon" alt />
            <span>{{ data.from.moniker }}</span>
          </div>
          <div class="zhuti colors" @click="$headerRouterFn(data.from)" v-else>
            {{
            $jiequStrFnMaxlength(data.from)
            }}
          </div>
        </li>
        <li>
          <span>Orchestrator:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.orchestrator)">
              {{
              $jiequStrFnMaxlength(data.orchestrator)
              }}
            </span>
          </div>
        </li>
        <li>
          <span>EthAddress:</span>
          <div>{{data.ethAddress}}</div>
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
