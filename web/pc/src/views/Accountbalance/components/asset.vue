<template>
  <div class="container">
    <section class="public-tbs-accoun-top">
      <div class="s-title">Native Asset</div>
      <ul>
        <li>
          <span class="accoun-t">Address:</span>
          <div>
            <textarea id="copy1" class="displaynone">{{
              result.address
            }}</textarea>
            {{ result.address }}
          </div>
          <span class="copystyle" @click="$copyFn('copy1')">
            <i class="el-icon-document-copy"></i>
          </span>
        </li>
        <li>
          <span class="accoun-t">Token:</span>
          {{ result.coinName }}
        </li>
        <li>
          <span class="accoun-t">Total Amount:</span>
          <div>
            {{ (result.balance && onFeixedNum(result.balance, 6)) || 0 }}
            {{ result.coinName }}
          </div>
        </li>
        <li>
          <span class="accoun-t">Available Balance:</span>
          <div>
            {{ (result.usedAmount && onFeixedNum(result.usedAmount, 6)) || 0 }}
            {{ result.coinName }}
          </div>
        </li>
      </ul>
    </section>
    <section class="public-tbs-center borders-center">
      <div class="s-title">Interchain Assets</div>
      <div v-if="result && result.transferBalances.length">
        <div
          class="center-content"
          v-for="(item, key) of result.transferBalances"
          :key="key"
        >
          <div class="left">
            <span>Token：</span>
            <div>{{ item.coinName }}</div>
          </div>
          <div class="right">
            <span>Total Amount:</span>
            <div>
              {{ (item.amount && onFeixedNum(item.amount, 6)) || 0 }}
              {{ item.coinName }}
            </div>
          </div>
        </div>
      </div>
      <div class="center-content" v-else>
        <div class="left">
          <span>Token：</span>
          <div>--</div>
        </div>
        <div class="right">
          <span>Total Amount:</span>
          <div>--</div>
        </div>
      </div>
    </section>
    <section class="public-tbs-center borders-center">
      <div class="s-title">Contractual Assets</div>
      <div v-if="result.tokenBalances.length">
        <div
          class="center-content"
          v-for="(item, key) of result.tokenBalances"
          :key="key"
        >
          <div class="left">
            <span>Token：</span>
            <div>{{ $jiequStrFnMaxlength(item.tokenName) }}</div>
          </div>
          <div class="right">
            <span>Total Amount:</span>
            <div>
              {{ (item.amount && onFeixedNum(item.amount, 6)) || 0 }}
              {{ $jiequStrFnMaxlength(item.tokenName) }}
            </div>
          </div>
        </div>
      </div>

      <div class="center-content" v-else>
        <div class="left">
          <span>Token：</span>
          <div>--</div>
        </div>
        <div class="right">
          <span>Total Amount:</span>
          <div>--</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  props: ["result"],
  data() {
    return {};
  },
  components: {},
};
</script>

<style scoped lang="less">
section {
  margin-top: 25px;
  background: #fff;
  border: 1px solid #eaebec;
  box-shadow: 0px 3px 7px 1px rgba(56, 54, 54, 0.06);
  border-radius: 5px;
  padding-bottom: 20px 0px;
}
.borders-center .center-content {
  border-bottom: 3px solid #eaebec;
}
.borders-center .center-content:last-child {
  border: none;
}
// .right {
//   span {
//     display: inline-block;
//     width: 200px !important;
//     border: 1px solid #000;
//   }
// }
</style>
