<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span class="Delegate-span">Delegate Address:</span>
          <div class="colors">
            <span @click="$headerRouterFn(data.from)">
              {{
              $jiequStrFnMaxlength(data.from)
              }}
            </span>
          </div>
        </li>

        <li>
          <span>Pool Address:</span>
          <div class="colors">
            <span
              @click="$headerRouterFn(data.pool_address)"
            >{{$jiequStrFnMaxlength(data.pool_address)}}</span>
          </div>
        </li>

        <li>
          <span>Tx Pair:</span>
          <div>{{data.tx_pair}}</div>
        </li>

        <li>
          <span>Left Order Id:</span>
          <div>{{data.left_order_id}}</div>
        </li>

        <li>
          <span>Right Order Id:</span>
          <div>{{data.right_order_id}}</div>
        </li>

        <li>
          <span>Price:</span>
          <div>{{data.price}} {{data.coinName}}</div>
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
// .Delegate-span {
//   display: inline-block;
//   width: 150 / @rem !important;
// }
</style>
