<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span class="li-textcolors">Operator Address:</span>
          <div class="colors" @click="$userInfoRouterFn(data.from.operatorAddress)">
            <div v-if="data.from.icon" class="minimgbox">
              <img class="user_icon" :src="data.from.icon" alt />
              <span>{{ data.from.moniker }}</span>
            </div>
            <div v-else>{{ data.from.operatorAddress }}
              {{ 
                $jiequStrFnMaxlength(data.from.operatorAddress)
              }}
            </div>
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
          <span class="li-textcolors">Min Self Delegation:</span>
          <div>{{ data.min_self_delegation ? data.min_self_delegation : "--" }}</div>
        </li>

        <li>
          <span class="li-textcolors">Commission Rate:</span>
          <div>{{data.commission_rate * 100}}%</div>
        </li>

        <li>
          <span>Website:</span>
          <div>{{ data.description.website ? data.description.website : "--" }}</div>
        </li>

        <li>
          <span>Details:</span>
          <div>{{ data.description.details ? data.description.details : "--" }}</div>
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
</style>
