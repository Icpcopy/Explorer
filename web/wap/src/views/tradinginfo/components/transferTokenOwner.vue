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
          <span>Old Owner:</span>

           <div>
            <span class="colors" @click="$headerRouterFn(data.from)">{{
              data.from
            }}</span>
          </div>
        </li>

        <li>
          <span>New Owner:</span>
           <div>
            <span class="colors" @click="$headerRouterFn(data.to)">{{
              data.to
            }}</span>
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
