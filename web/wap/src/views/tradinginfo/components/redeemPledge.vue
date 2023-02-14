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
          <div>
            <span class="colors" @click="$headerRouterFn(data.from)">
              {{
                $jiequStrFnMaxlength(data.signer)
              }}</span
            >
          </div>
        </li>
        
        <li>
          <span>Pledge:</span>
          <div>
            {{data.pledge}} {{data.coinName}}
          </div>
        </li>

      </ul>
    </div>
  </div>
</template>

<script>
export default {
  props:["data"],
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
