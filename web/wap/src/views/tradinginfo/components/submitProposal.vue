<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>Proposer:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.from)">
              {{
              $jiequStrFnMaxlength(data.from)
              }}
            </span>
          </div>
        </li>

        <li>
          <span>Title:</span>
          <div>{{data.title}}</div>
        </li>

        <li>
          <span>Initial Deposit:</span>
          <div>{{data.initial_deposit}} {{data.coinName}}</div>
        </li>

        <li>
          <span>Description:</span>
          <div class="description">{{data.description}}</div>
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
.description {
  margin-left: 5 / @rem;
  word-break: break-al;
}
</style>
