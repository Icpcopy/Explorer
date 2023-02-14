<template>
  <div class="container">
    <header>
      <div class="title-text">Latest Blocks</div>
    </header>
    <section v-if="BlockListData.length">
      <div class="section-box" v-for="(item, key) of BlockListData" :key="key">
        <div class="s-title-left">
          <span>Block：</span>
          <div class="maodian" @click="$blockChainInfosRouterFn(item.height)">{{ item.height }}</div>
        </div>
        <div class="s-title-left">
          <span>Age：</span>
          <div>{{ item.shijia }} ago</div>
        </div>
        <div class="s-title-left">
          <span>No.of Txs：</span>
          <div>{{ item.tx_num }}</div>
        </div>
        <div class="s-title-left">
          <span>Proposer：</span>
          <div class="minimgbox" @click="$userInfoRouterFn(item.operatorAddress)">
            <img :src="item.validator_coin" alt />
            <span>{{ item.validator }}</span>
          </div>
        </div>
      </div>
    </section>
    <div class="vloading" v-else>
      <van-loading type="spinner" color="#402ED8" />
    </div>
    <div class="list-load-end" v-if="Maxpages_index == pageIndex">
      <span>--no more--</span>
    </div>
  </div>
</template>

<script>
import { getBlockList } from '@/api/block';
import infiniteScroll from "vue-infinite-scroll"
export default {
  data () {
    return {
      BlockListData: [],
      dataLoadStop: false,
      dataLoadNomore: false,
      dataNomore: false,
      pageIndex: 1,
      pageSize: 30,
      Maxpages_index: 0,
    }
  },
  created () {
    this.BlockListData = [];
    this.getBlockListFn();
    this.$scrrollBox((This) => {
      if (This.pageIndex < This.Maxpages_index) {
        This.pageIndex++;
        This.getBlockListFn()
      }
    });
  },
  methods: {
    getBlockListFn () {
      this.dataLoadStop = true;
      getBlockList({
        pageIndex: this.pageIndex,
        pageSize: this.pageSize
      }).then(res => {
        this.BlockListData.push(...res.result.records);
        this.Maxpages_index = res.result.pages;
        if (this.BlockListData.length) {
          this.BlockListData.map((item, key) => {
            item.shijia = this.$TimeremainingFn(item.time_stamp);

          })
        }
      })
    }
  },
  directives: {
    infiniteScroll
  }
}
</script>

<style scoped lang="less">
@import url("../../css/index.less");
.section-box {
  background: #fff;
  border-top: 1px solid #ededed;
  border-bottom: 1px solid #ededed;
  padding: 5 / @rem 0px;
  margin-bottom: 15 / @rem;
  .s-title-left {
    display: flex;
    height: 40 / @rem;
    line-height: 40 / @rem;
    span {
      width: 100 / @rem;
      margin-left: 15 / @rem;
    }
  }
  .minimgbox {
    span {
      margin: 0px;
    }
  }
}
</style>
