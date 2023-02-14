<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>
        <li>
          <span>Name:</span>
          <div>
            {{data.name}}
          </div>
        </li>
        <li>
          <span>Initial Supply:</span>
          <div>
            {{data.initial_supply}}
          </div>
        </li>

        <li>
          <span>Total Supply:</span>
          <div>
            {{data.total_supply}}
          </div>
        </li>

        <li>
          <span>Smallest Unit:</span>
          <div>
            {{ data.smallest_unit ? data.smallest_unit : "--" }}
          </div>
        </li>

        <li>
          <span>Decimals:</span>
          <div>
            {{data.decimals}}
          </div>
        </li>

        <li>
          <span>Mintable:</span>
          <div>
            {{data.mintable}}
          </div>
        </li>

        <li>
          <span>Locked:</span>
          <div>
             {{ data.unlocked ? data.unlocked : "--" }}
          </div>
        </li>

        <li>
          <span>OWner:</span>
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
