<template>
  <div class="container">
    <header>
      <div class="title-text">Validator List 100 Active</div>
    </header>
    <section>
      <van-tabs type="card" @click="changes">
        <van-tab :title="j" v-for="(j, i) in titleTxt" :key="i">
          <div v-if="data.length">
            <ul class="tabs-box" v-for="(item, index) in data" :key="index">
              <li>
                <span>#:</span>
                <div>{{ index + 1 }}</div>
              </li>
              <li>
                <span>Moniker:</span>
                <div class>
                  <div
                    v-if="item.description"
                    class="user-name shubiao"
                    @click="$userInfoRouterFn(item.operator_address)"
                  >
                    <div class="minimgbox" v-if="!item.description.moniker">
                      <img :src="item.icon" />
                      <span class="address-style">{{ item.address }}</span>
                    </div>
                    <div class="minimgbox user-name" v-else>
                      <img :src="item.icon" />
                      <span
                        class="address-style"
                        @click="$userInfoRouterFn(item.operator_address)"
                      >{{ item.description.moniker }}</span>
                    </div>
                  </div>
                </div>
              </li>
              <li>
                <span>Commission:</span>
                <div v-if="item.commission">
                  {{
                  (item.commission.commission_rates.rate * 100).toFixed(2)
                  }}%
                </div>
              </li>
              <li>
                <span>Bonded-Tokens:</span>
                <div>{{ (item.tokens / 10000000000000000000*10).toFixed(2) }}</div>
              </li>
              <li v-if="active_name == 'BOND_STATUS_BONDED'">
                <span>Voting-Power:</span>
                <div>{{ (item.votingPower * 100).toFixed(2) }}%</div>
              </li>
              <li v-if="active_name == 'BOND_STATUS_BONDED'">
                <span>Uptime:</span>
                <div>{{ item.uptime || 0 }}%</div>
              </li>
              <li>
                <span>Self-Bonded:</span>
                <div>{{ parseFloat(item.selfDelegateAmount).toFixed(2) }}</div>
              </li>
              <li
                v-if="
              active_name == 'BOND_STATUS_UNBONDED' ||
                active_name == 'BOND_STATUS_BONDED'
            "
              >
                <span>Delegators:</span>
                <div>{{ item.totalDelegations }}</div>
              </li>
              <li
                v-if="
              active_name == 'BOND_STATUS_UNBONDED' || active_name == 'JAILED'
            "
              >
                <span>Unbonding_Height:</span>
                <div>{{ item.unbonding_height }}</div>
              </li>
            </ul>
          </div>
          <div class="date-no" v-else>No Data</div>
        </van-tab>
      </van-tabs>
    </section>
    <div class="list-load-end" v-if="Maxpages_index == pageIndex">
      <span>--no more--</span>
    </div>
  </div>
</template>

<script>
import { getValidatorsByStatus } from '@/api/identifier';
export default {
  data () {
    return {
      titleTxt: ["Active", "Candidate", "Jailed"],
      data: [],
      active_name: "BOND_STATUS_BONDED",
      pageIndex: 1,
      pageSize: 5,
      Maxpages_index: 0,
    }
  },
  created () {
    this.getValidatorsByStatusFn();
    this.data = [];
    var vue_This = this;
    this.$scrrollBox((This) => {
      if (This.pageIndex < This.Maxpages_index) {
        This.pageIndex++;
        This.getValidatorsByStatusFn()
      }
    });
  },
  methods: {
    changes (val) {
      if (val == 0) {
        this.active_name = "BOND_STATUS_BONDED";
      } else if (val == 1) {
        this.active_name = "BOND_STATUS_UNBONDED";
      } else {
        this.active_name = "JAILED";
      }
      this.pageIndex = 1;
      this.data = [];
      this.getValidatorsByStatusFn();
    },
    getValidatorsByStatusFn () {
      getValidatorsByStatus({
        pramsName: "tokens",
        status: this.active_name,
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      }).then(res => {
        this.Maxpages_index = res.result.pages;
        this.data.push(...res.result.records);
      })
    }
  },
  components: {

  }
}
</script>

<style scoped lang="less">
@import url("../../css/index.less");
.tabs-box {
  padding-left: 15 / @rem;
  li span {
    margin-left: 0px;
  }
}
</style>
