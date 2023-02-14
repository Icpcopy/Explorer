<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span class="li-textcolors">Pool Address:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.pool_address)">
              {{
              $jiequStrFnMaxlength(data.pool_address)
              }}
            </span>
          </div>
        </li>

        <li>
          <span class="li-textcolors">Owner Address:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.from)">
              {{
              $jiequStrFnMaxlength(data.from)
              }}
            </span>
          </div>
        </li>

        <li>
          <span>My Asset:</span>
          <div>{{data.my_asset}}</div>
        </li>

        <li>
          <span>Price:</span>
          <div>{{data.price}}</div>
        </li>

        <li>
          <span>Expect Asset:</span>
          <div>{{data.expect_asset}}</div>
        </li>

        <li>
          <span>Order Id:</span>
          <div>{{data.order_id}}</div>
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
.li-textcolors {
  display: inline-block;
  width: 120 / @rem !important;
}
</style>
