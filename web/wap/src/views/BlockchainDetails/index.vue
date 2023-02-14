<template>
  <div class="container">
    <div class="vloading" v-if="clickBlockBool">
      <van-loading type="spinner" color="#402ED8" />
    </div>
    <div class="pub-details-title details-title">
      <span class="jiantou-details">
        <img
          src="../../assets/jiantouselect.png"
          v-if="blockheight >=1 "
          @click="clickHeightFn('last')"
        />
        <img src="../../assets/sanjiaoxing.png" v-else />
      </span>
      <span>Block #{{ blockNumberData.height }}</span>
      <span class="jiantou-details">
        <img
          @click="clickHeightFn('next')"
          src="../../assets/jiantouselect.png"
          class="jiantou-right"
          v-if="blockNumberData.height < Maxblock"
        />
        <img src="../../assets/sanjiaoxing.png" class="jiantou-right" v-else />
      </span>
    </div>
    <section>
      <div class="pub-section-title">Base Info</div>
      <div class="title-content">
        <div class="hash-box">
          <span>Block Hash：</span>
          <div>{{ blockNumberData.block_hash }}</div>
        </div>
        <div class="base-info">
          <span>Timestamp：</span>
          <div class="huise">{{ blockNumberData.time_stamp }} UTC</div>
        </div>
        <div class="base-info">
          <span>Proposer：</span>
          <div>
            <div class="minimgbox" @click="$userInfoRouterFn(blockNumberData.operatorAddress)">
              <img :src="blockNumberData.validator_coin" alt />
              <span>{{ blockNumberData.validator }}</span>
            </div>
          </div>
        </div>
        <div class="base-info">
          <span class="Transaction">No. of Transactions：</span>
          <div>{{ blockNumberData.tx_num }}</div>
        </div>
      </div>
    </section>

    <section>
      <Transactionrecords :isStateTxt="'height'"></Transactionrecords>
    </section>
  </div>
</template>

<script>
import { getBlockByBlockNumber } from '@/api/block';
import Transactionrecords from '@/components/Transactionrecords';
export default {
  data () {
    return {
      blcok_id: "",
      blockNumberData: [],
      clickBlockBool: false,
      Maxblock: null,
      blockheight: null,
    }
  },
  created () {
    this.blockheight = this.blcok_id = this.$route.params.id;

    this.getBlockByBlockNumberFn();
  },
  watch: {
    $route (from, to) {
      if (from.name == "BlockchainDetails") {
        this.getBlockByBlockNumberFn();
      }
    }
  },
  methods: {
    clickHeightFn (name) {
      if (name == "last") {
        this.$route.params.id--;
      } else {
        this.$route.params.id++;
      }
      if (this.$route.params.id > this.Maxblock) {
        this.$route.params.id = Maxblock;
      }
      this.$router.push("/BlockchainDetails/" + this.$route.params.id);
    },
    getBlockByBlockNumberFn () {
      this.clickBlockBool = true;
      getBlockByBlockNumber({ blockNumber: this.$route.params.id }).then(res => {
        this.clickBlockBool = false;
        if (res.result) {
          this.blockNumberData = res.result;
          this.Maxblock = (res.result.blockMax * 1) + 1;
          this.blockNumberData.time_stamp = this.$getUTCTimeFn(
            this.blockNumberData.time_stamp
          );
        } else {
          this.$router.push({
            path: "/pagesno",
            query: { name: this.$route.params.id }
          });
        }
      })
    }
  },
  components: {
    Transactionrecords
  }
}
</script>

<style scoped lang="less">
@import url("../../css/index.less");
.pub-details-title {
  line-height: 11 / @rem;
}
.jiantou-details {
  display: inline-block;

  img {
    width: 11 / @rem;
    height: 11 / @rem;
    &:first-child {
      margin-right: 15 / @rem;
    }
    &:last-child {
      margin-left: 15 / @rem;
    }
  }
  .jiantou-right {
    transform: rotate(-180deg);
  }
}
.title-content {
  background: #fff;
  padding: 10 / @rem 0px;
  font-size: 12 / @rem;
  .base-info {
    line-height: 50 / @rem;
  }
  & > div {
    display: flex;
    height: 50 / @rem;
    border-bottom: 1px solid #ededed;
    margin: 0px 5 / @rem;

    & > span {
      width: 80 / @rem;
      margin-left: 15 / @rem;
    }
  }
}
.hash-box {
  word-break: break-all;
  span {
    line-height: 50 / @rem;
  }
  div {
    width: 250 / @rem;
    line-height: 20 / @rem;
  }
}
.Transaction {
  width: 130 / @rem !important;
}
.details-title {
  display: flex;
  align-items: center;
}
</style>
