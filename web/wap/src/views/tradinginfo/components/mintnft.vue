<template>
  <div class="container">
    <div class="list-info-box">
      <ul>
        <li>
          <span>TxType:</span>
          <div>{{data.type}}</div>
        </li>

        <li>
          <span>From:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.from)">
              {{
              $jiequStrFnMaxlength(data.from)
              }}
            </span>
          </div>
        </li>

        <li>
          <span>To:</span>
          <div>
            <span class="colors" @click="$headerRouterFn(data.to)">
              {{
              $jiequStrFnMaxlength(data.to)
              }}
            </span>
          </div>
        </li>

        <li class="wasewrap">
          <span>Token Id:</span>
          <div>{{data.tokenId}}</div>
        </li>

        <li class="wasewrap">
          <span>Cate Id:</span>
          <div>{{data.cateId}}</div>
        </li>

        <li class="wasewrap">
          <span>Token Uri:</span>
          <div>{{data.tokenUri}}</div>
        </li>

        <li class="wasewrap">
          <span>Company Uri:</span>
          <div>{{data.companyUri}}</div>
        </li>

        <li>
          <span>Value Added Tax:</span>
          <div>{{data.valueAddedTax}}</div>
        </li>

        <li>
          <span>Copy Right Tax:</span>
          <div>{{data.copyrightTax}}</div>
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
</style>
