<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Voter:</span>
          
          <div class="colors" @click="$headerRouterFn(data.from)">
              {{
                $jiequStrFnMaxlength(data.from)
              }}
          </div>
        </li>

        <li>
          <span>Proposal ID:</span>
          <div>
            {{data.proposal_id}}
          </div>
        </li>

        <li>
          <span>Option:</span>
          <div>
            {{data.option}}
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
