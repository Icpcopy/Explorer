<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Validator Src Address:</span>
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
          <span>Validator Dst Address:</span>
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
          <span>Amount:</span>
          <div>{{data.amount}} {{data.coinName}}</div>
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
