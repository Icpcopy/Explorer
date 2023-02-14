<template>
  <section class="card-content">
    <div class="pub-section-title">Native assets</div>
    <div class="list-box">
      <div>
        <span>Address:</span>
        <div>
          <span>{{ $jiequStrFnMaxlength(result.address) }}</span>
          <!-- <img class="fuzhi" src="../../../assets/fuzhi.png" @click="$copyFn('copy_hash')" alt /> -->
        </div>
      </div>
      <div>
        <span>Token:</span>
        <div>{{ result.coinName }}</div>
      </div>
      <div>
        <span>Total Amount:</span>
        <div>
          {{ (result.balance && onFeixedNum(result.balance, 6)) || 0 }}
          {{ result.coinName }}
        </div>
      </div>
      <div>
        <span>Available Balance:</span>
        <div>
          {{ (result.usedAmount && onFeixedNum(result.usedAmount, 6)) || 0 }}
          {{ result.coinName }}
        </div>
      </div>
    </div>
    <div class="pub-section-title">Interchain Assets</div>
    <!-- <div v-if="result.tokenBalances.length">
      <div class="list-box" v-for="(item, key) of result.tokenBalances" :key="key">
        <div>
          <span>Token:</span>
          <div>{{ item.tokenName }}</div>
        </div>
        <div>
          <span>Amount:</span>
          <div>{{ item.amount }} {{ item.tokenName }}</div>
        </div>
      </div>
    </div>

    <div class="center-content" v-else>-->
    <div class="list-box">
      <div>
        <span>Token:</span>
        <div>--</div>
      </div>
      <div>
        <span>Amount:</span>
        <div>--</div>
      </div>
    </div>
    <!-- </div> -->

    <div class="pub-section-title">Contractual Assets</div>
    <div v-if="result.tokenBalances.length">
      <div
        class="list-box"
        v-for="(item, key) of result.tokenBalances"
        :key="key"
      >
        <div>
          <span>Token:</span>
          <div>{{ $jiequStrgmmFn(item.tokenName) }}</div>
        </div>
        <div>
          <span>Amount:</span>
          <div>
            {{ $jiequStrgmmFn(item.amount) }}
            {{ $jiequStrgmmFn(item.tokenName) }}
          </div>
        </div>
      </div>
    </div>
    <div class="list-box" v-else>
      <div>
        <span>Token:</span>
        <div class="colors">--</div>
      </div>
      <div>
        <span>Amount:</span>
        <div>--</div>
      </div>
    </div>
  </section>
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
@import url("../../../css/index.less");
.card-content {
  padding-top: 20 / @rem;
}
</style>
