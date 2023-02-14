<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Owner Address:</span>
          <div class="colors" @click="$headerRouterFn(data.from)">
            {{
            $jiequStrFnMaxlength(data.from)
            }}
          </div>
        </li>
        <li v-if="data.defi_address == undefined">
          <span>Pool Type Id:</span>
          <div>
            {{
            data.pool_type_id
            }}
          </div>
        </li>
        <li v-if="data.defi_address">
          <span>Defi Address:</span>
          <div
            class="zhuti colors minimgbox"
            v-if="data.defi_address.icon"
            @click="$userInfoRouterFn(data.defi_address.operatorAddress)"
          >
            <img class="user_icon" :src="data.defi_address.icon" alt />
            <span>{{ data.defi_address.moniker }}</span>
          </div>
          <div class="zhuti colors" @click="$headerRouterFn(data.defi_address)" v-else>
            {{
            $jiequStrFnMaxlength(data.defi_address)
            }}
          </div>
        </li>

        <li>
          <span>Pledge:</span>
          <div>{{data.pledge}} {{data.coinName}}</div>
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
