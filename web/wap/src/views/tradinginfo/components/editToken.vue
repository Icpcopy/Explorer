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
          <div>
            {{data.symbol}}
          </div>
        </li>

        <li>
          <span>Mintable:</span>
          <div>
            {{data.mintable}}
          </div>
        </li>

        <li>
          <span>Owner:</span>        
          <div class="colors" @click="$headerRouterFn(data.from)">
            {{ 
              $jiequStrFnMaxlength(data.from)
            }}
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
