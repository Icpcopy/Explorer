<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Moniker:</span>
          <div class="colors">{{data.moniker}}</div>
        </li>

        <li>
          <span>Delegator Address:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.from)">
              {{
                $jiequStrFnMaxlength(data.from)
              }}
            </span>
          </div>
        </li>

        <li>
          <span>Defi Address:</span>
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
          <span>Min Self Delegation:</span>
          <div>{{data.min_self_delegation}}</div>
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
