<template>
  <div class="container">
    <div class="list-info-box">
      <ul>

        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Pool Address:</span>          
          <div>
            <span class="colors" @click="$headerRouterFn(data.pool_address)">
              {{
                $jiequStrFnMaxlength(data.pool_address)
              }}
            </span>
          </div>
        </li>

        <li>
          <span>Owner Address:</span>         
          <div>
            <span
              class="colors"
              @click="$headerRouterFn(data.owner_address)"
              >
                {{
                  $jiequStrFnMaxlength(data.owner_address)
                }}
              </span
            >
          </div>
        </li>
        
        <li>
          <span>Tx Pair:</span>
          <div>
            {{data.tx_pair}}
          </div>
        </li>

        <li>
          <span>Order Id:</span>
          <div>
            {{data.order_id}}
          </div>
        </li>

        <li>
          <span>Is Left Order:</span>
          <div>
            {{data.is_left_order}}
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
