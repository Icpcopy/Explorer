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
            class="zhuti maodian minimgbox"
            v-if="data.from.icon"
            @click="$userInfoRouterFn(data.from.operatorAddress)"
          >
            <img class="user_icon" :src="data.from.icon" alt />
            <span>{{ data.from.moniker }}</span>
          </div>
          <div class="zhuti maodian" @click="$headerRouterFn(data.from)" v-else>
            {{
            $jiequStrFnMaxlength(data.from)
            }}
          </div>
        </li>
        <li>
          <span>Amount:</span>
          <div>{{data.amount}} {{data.coinName}}</div>
        </li>

        <li v-if="data.type == 'Delegate' || data.type == 'Undelegate'">
          <span class="Maxwidth">AutoClaimReward:</span>
          <div style="margin-left:25px">{{data.AutoClaimReward}}{{data.coinName}}</div>
        </li>

        <li>
          <span>To:</span>
          <div
            class="maodian minimgbox"
            v-if="data.to.icon"
            @click="$userInfoRouterFn(data.to.operatorAddress)"
          >
            <img class="user_icon" :src="data.to.icon" alt />
            <span>{{ data.to.moniker }}</span>
          </div>
          <div class="zhuti maodian" @click="$headerRouterFn(data.to)" v-else>
            {{
            $jiequStrFnMaxlength(data.to)
            }}
          </div>
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
.Maxwidth {
  display: inline-block;
  width: 130 / @rem !important;
}
</style>
