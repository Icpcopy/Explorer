<template>
  <section class="card-content">
    <section v-if="recordsData.length">
      <div v-for="(item, key) in recordsData" :key="key">
        <div class="pub-section-title">Issue Asset {{ key + 1 }}</div>
        <div class="list-box">
          <div>
            <span>Token:</span>
            <div>{{ item.event[0].symbol }}</div>
          </div>

          <div>
            <span>Initial supply:</span>
            <div>{{ item.event[0].initial_supply }}</div>
          </div>
          <div>
            <span>Total supply:</span>
            <div>{{ item.event[0].total_supply }}</div>
          </div>
          <div>
            <span>Smallest Unit:</span>
            <div>{{ item.event[0].smallest_unit }}</div>
          </div>
          <div>
            <span>Decimal:</span>
            <div>{{ item.event[0].decimals }}</div>
          </div>
        </div>
      </div>
    </section>
    <div class="date-no" v-else>
      <img src="../../../assets/wuzican.png" />
      <div>No Issued Token Data</div>
    </div>
  </section>
</template>

<script>
import { getByBlockNumber } from "@/api/deal";
export default {
  data () {
    return {
      recordsData: [{ event: [{}] }]
    }
  },
  created () {
    this.getByBlockNumberFn();
  },
  methods: {
    getByBlockNumberFn () {
      getByBlockNumber({
        address: this.$route.params.id,
        smallType: "issuetoken"
      }).then(res => {
        this.recordsData = res.result.records;
      });
    }
  },
  components: {

  }
}
</script>

<style scoped lang="less">
@import url("../../../css/index.less");
.card-content {
  padding-top: 20 / @rem;
}
.date-no {
  img {
    padding-top: 30 / @rem;
  }
  color: #5841ac;
  line-height: 20 / @rem !important;
}
</style>
