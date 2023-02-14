<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Amount:</span>
          <div>{{data.amount}} {{data.coinName}}</div>
        </li>

        <li>
          <span>Validator:</span>
          <div class="maodian minimgbox" @click="$userInfoRouterFn(data.from.operatorAddress)">
            <div v-if="data.from.icon">
              <img class="user_icon" :src="data.from.icon" alt />
              <span>{{ data.from.moniker }}</span>
            </div>
            <div v-else>{{
                $jiequStrFnMaxlength(data.from.operatorAddress)
              }}</div>
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
</style>
