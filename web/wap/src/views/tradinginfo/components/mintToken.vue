<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Symbol:</span>
          <div>{{data.symbol}}</div>
        </li>

        <li>
          <span>Amount:</span>
          <div>{{data.amount}}</div>
        </li>

        <li>
          <span>To:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.to)">
              {{
                $jiequStrFnMaxlength(data.to)
              }}
            </span>
          </div>
        </li>

        <li>
          <span>Owner:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.from)">
              {{
                $jiequStrFnMaxlength(data.from)
              }}
            </span>
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
