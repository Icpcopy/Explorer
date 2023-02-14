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
          <div class="colors">
            <span @click="$headerRouterFn(data.from)">
              {{
                $jiequStrFnMaxlength(data.from)
              }}
            </span>
          </div>
        </li>
        <li>
          <span>Amount:</span>
          <div>{{data.amount}} {{data.coinName}}</div>
        </li>
        <li>
          <span>Validator Src Address:</span>
          <div
            class="zhuti colors minimgbox"
            v-if="data.validator_src_address.icon"
            @click="
              $userInfoRouterFn(data.validator_src_address.operatorAddress)
            "
          >
            <img class="user_icon" :src="data.validator_src_address.icon" alt />
            <span>{{ data.validator_src_address.moniker }}</span>
          </div>
          <div
            class="zhuti colors"
            @click="$headerRouterFn(data.validator_src_address)"
            v-else
          >
              {{
                $jiequStrFnMaxlength(data.validator_src_address)
              }}
          </div>
        </li>

        <li>
          <span>Validator Dst Address:</span>
          <div>
            <span class="titles">Validator Dst Address:</span>
            <div
              class="zhuti colors minimgbox"
              v-if="data.validator_dst_address.icon"
              @click="
              $userInfoRouterFn(data.validator_dst_address.operatorAddress)
            "
            >
              <img class="user_icon" :src="data.validator_dst_address.icon" alt />
              <span>{{ data.validator_dst_address.moniker }}</span>
            </div>
            <div
              class="zhuti colors"
              @click="$headerRouterFn(data.validator_dst_address)"
              v-else
            >
             {{
                $jiequStrFnMaxlength(data.validator_dst_address)
              }}
            </div>
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
