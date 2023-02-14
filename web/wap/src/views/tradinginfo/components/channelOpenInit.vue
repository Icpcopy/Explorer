<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Signer:</span>

           <div>
            <span class="colors" @click="$headerRouterFn(data.signer)">
              {{
                $jiequStrFnMaxlength(data.signer)
              }}
            </span>
          </div>
          
        </li>

        <li>
          <span>Port ID:</span>
          <div>
            {{data.port_id}}
          </div>
        </li>

        <li>
          <span>Channel Id:</span>
          <div>
            {{data.channel_id}}
          </div>
        </li>

        <li>
          <span>State:</span>
          <div>
            {{data.state}}
          </div>
        </li>

        <li>
          <span>Ordering:</span>
          <div>
            {{data.ordering}}
          </div>
        </li>

        <li>
          <span>Version:</span>
          <div>
            {{data.version}}
          </div>
        </li>

        <li>
          <span>Connection Hops:</span>
          <div>
            {{data.connection_hops}}
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
