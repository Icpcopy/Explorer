<template>
  <div class="container">
    <div class="maxmin-title">
      <div>
        Proposals
        <span>Here is a list of governance proposals</span>
      </div>
    </div>
    <section v-if="getProposalIdListData.length">
      <div class="list-box" v-for="(j, i) in getProposalIdListData" :key="i">
        <div>
          <span>Proposal ID:</span>
          <div>{{ j.proposal_id }}</div>
        </div>
        <div>
          <span>Title:</span>
          <div
            v-if="j.content"
            class="colors hiddenbox-hash"
            @click="$DetailsproposalRouter(j.proposal_id)"
          >
            {{ j.content.title }}
          </div>
        </div>
        <div>
          <span>Type:</span>
          <div>{{ j.type }}</div>
        </div>
        <div>
          <span>Status:</span>
          <div
            v-if="j.status == 'PROPOSAL_STATUS_VOTING_PERIOD'"
            class="tx_yellow"
          >
            Voting period
          </div>
          <div
            v-else-if="j.status == 'PROPOSAL_STATUS_PASSED'"
            class="tx_grren"
          >
            Passed
          </div>
          <div
            v-else-if="j.status == 'PROPOSAL_STATUS_REJECTED'"
            class="hongse"
          >
            Rejected
          </div>
          <div v-else class="tx_pink">remove</div>
        </div>
        <div>
          <span>Submit Time:</span>
          <div>{{ j.submit_time }} UTC</div>
        </div>
        <div>
          <span>Voting Start Time:</span>
          <div>{{ j.voting_start_time }}UTC</div>
        </div>
        <div>
          <span>Total Deposit(ICT):</span>
          <div>
            {{
              (j.total_deposit[0] &&
                j.total_deposit[0].amount &&
                onFeixedNum(
                  j.total_deposit[0].amount / 10000000000000000000*10,
                  6
                )) ||
              0
            }}
          </div>
        </div>
      </div>
      <div class="list-load-end" v-if="Maxpages_index == pageIndex">
        <span>--no more--</span>
      </div>
    </section>
    <div class="date-no" v-else>No Data</div>
  </div>
</template>

<script>
import { getProposalIdList } from "@/api/proposal";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      getProposalIdListData: [],
      pageIndex: 1,
      pageSize: 30,
      Maxpages_index: 0,
    };
  },
  created() {
    this.getProposalIdListFn();
    this.getProposalIdListData = [];
    this.$scrrollBox((This) => {
      if (This.pageIndex < This.Maxpages_index) {
        This.pageIndex++;
        This.getProposalIdListFn();
      }
    });
  },
  methods: {
    getProposalIdListFn() {
      getProposalIdList().then((res) => {
        this.getProposalIdListData.push(...res.result.records);
        this.Maxpages_index = res.result.pages;
        this.getProposalIdListData.map((items) => {
          items.submit_time = this.$getUTCTimeFn(items.submit_time);
          items.voting_start_time = this.$getUTCTimeFn(items.voting_start_time);
        });
      });
    },
  },
  components: {},
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
.list-box {
  & > div > span {
    width: 150 / @rem;
  }
}
.maxmin-title {
  padding: 10 / @rem 0px;
}
</style>
