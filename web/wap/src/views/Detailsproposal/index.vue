<template>
  <div class="container">
    <div class="pub-details-title">
      <span>Proposals</span>
    </div>
    <section>
      <div class="pub-section-title">Proposal Details</div>
      <div class="list-box list-header-box">
        <div>
          <span>Proposal ID:</span>
          <div>{{ ProposalIdData.proposal_id }}</div>
        </div>
        <div>
          <span>Proposer：</span>
          <div
            class="maodian"
            @click="$headerRouterFn(ProposalIdData.proposer)"
            v-if="ProposalIdData.proposer"
          >
            {{ $jiequStrFnMaxlength(ProposalIdData.proposer) }}
          </div>
          <div v-else>--</div>
        </div>
        <div>
          <span>Submit Hash:</span>
          <div
            class="maodian"
            @click="$clickHshRouterFn(ProposalIdData)"
            v-if="ProposalIdData.txhash"
          >
            {{ $jiequStrFnMaxlength(ProposalIdData.txhash) }}
          </div>
          <div v-else>--</div>
        </div>

        <div class="titles-wrap">
          <span>Title：</span>
          <div>{{ ProposalIdData.content.title }}</div>
        </div>
        <div>
          <span>Proposal Type：</span>
          <div>{{ ProposalIdData.type }}</div>
        </div>
        <div>
          <span>Proposal Status:</span>

          <div>
            <span
              v-if="ProposalIdData.status == 'PROPOSAL_STATUS_VOTING_PERIOD'"
              class="tx_yellow"
            >
              Voting period
              <img class="imgew" src="../../assets/tpzhing.png" alt />
            </span>
            <span
              v-else-if="ProposalIdData.status == 'PROPOSAL_STATUS_PASSED'"
              class="tx_grren"
            >
              <img class="imgew" src="../../assets/tpsuccess.png" alt />Passed
            </span>
            <span
              v-else-if="ProposalIdData.status == 'PROPOSAL_STATUS_REJECTED'"
              class="hongse"
            >
              <img class="imgew" src="../../assets/tperror.png" alt />Rejected
            </span>
            <span v-else class="tx_pink">remove</span>
          </div>
        </div>
      </div>
      <div class="list-box list-header-box">
        <div>
          <span>Deposit:</span>
          <div>
            {{
              (ProposalIdData.total_deposit[0].amount &&
                onFeixedNum(
                  ProposalIdData.total_deposit[0].amount / 10000000000000000000*10,
                  6
                )) ||
              0
            }}
            {{ ProposalIdData.coinName }}
          </div>
        </div>
        <div>
          <span>Submit Time:</span>
          <div class="huise">{{ ProposalIdData.submit_time }} UTC</div>
        </div>
        <div>
          <span>Deposit End Time:</span>
          <div class="huise">{{ ProposalIdData.deposit_end_time }} UTC</div>
        </div>
        <div>
          <span>Voting Start Time:</span>
          <div class="huise">{{ ProposalIdData.voting_start_time }} UTC</div>
        </div>
        <div>
          <span>End Voting Time:</span>
          <div class="huise">{{ ProposalIdData.voting_end_time }} UTC</div>
        </div>
      </div>
      <div class="description">
        <div>Description:</div>
        <span>{{ ProposalIdData.content.description }}</span>
      </div>
      <div class="pub-section-title">
        Tally Result —
        <span v-if="ProposalIdData.status == 'PROPOSAL_STATUS_VOTING_PERIOD'"
          >Voting period</span
        >
        <span v-else-if="ProposalIdData.status == 'PROPOSAL_STATUS_PASSED'"
          >Pass</span
        >
        <span v-else-if="ProposalIdData.status == 'PROPOSAL_STATUS_REJECTED'"
          >Reject</span
        >
        <span v-else>remove</span>
      </div>
      <div class="section">
        <div class="section-top">
          <div class="description-content">
            <div class="propsal-left">
              <div class="cuilv_color">
                Total :
                <span>{{
                  (ProposalIdData.totalVote &&
                    onFeixedNum(ProposalIdData.totalVote, 6)) ||
                  0
                }}</span>
                <span>{{ ProposalIdData.coinName }}</span>
              </div>
              <div
                v-if="ProposalIdData.status == 'PROPOSAL_STATUS_VOTING_PERIOD'"
              >
                <div class="current-tur">
                  Current Turnout : {{ ProposalIdData.CurrentTurnout * 100 }}%
                </div>
                <div class="huise">
                  {{ ProposalIdData.mTotalVote }} M of
                  {{ ProposalIdData.mBondedTokens }} M has voted
                </div>
              </div>
              <div class="propsal-right">Quorum : 40.00%</div>
            </div>
          </div>
          <div class="jindutiao">
            <ul>
              <li>
                <div class="proposaltitle">
                  <span>YES:</span>
                  <span>{{ final_tally_result.yes }}</span>
                </div>
                <van-progress
                  :pivot-text="`${ProposalIdData.yesRate}%`"
                  color="#0DE3AC"
                  :percentage="ProposalIdData.yesRate"
                  stroke-width="10"
                />
              </li>
              <li>
                <div class="proposaltitle">
                  <span>NO:</span>
                  <span>{{ final_tally_result.no }}</span>
                </div>
                <van-progress
                  :pivot-text="`${ProposalIdData.noRate}%`"
                  color="#EA5D5D"
                  :percentage="ProposalIdData.noRate"
                  stroke-width="10"
                />
              </li>
              <li>
                <div class="proposaltitle">
                  <span>Abstain:</span>
                  <span>{{ final_tally_result.abstain }}</span>
                </div>
                <van-progress
                  :pivot-text="`${ProposalIdData.abstainRate}%`"
                  color="#3097F7"
                  :percentage="ProposalIdData.abstainRate"
                  stroke-width="10"
                />
              </li>
              <li>
                <div class="proposaltitle">
                  <span>NoWithVeto:</span>
                  <span>{{ final_tally_result.no_with_veto }}</span>
                </div>
                <van-progress
                  :pivot-text="`${ProposalIdData.noWithVoteRate}%`"
                  color="#FABA1E"
                  :percentage="ProposalIdData.noWithVoteRate"
                  stroke-width="10"
                />
              </li>
            </ul>
          </div>
        </div>
        <div class="section-bottom">
          <div class="selectblock">
            <ul>
              <li
                v-for="(j, i) in selectData"
                :key="i"
                :class="`${acitve == i ? 'selectcolor' : ''}`"
                @click="selectacitveFn(j, i)"
              >
                <span></span>
                <div>{{ j }}</div>
              </li>
            </ul>
            <div class="ul-renshu">Number of votes: 41,666</div>
          </div>
          <div v-if="ByBlockNumber.length">
            <div class="list-ul-box" v-for="(j, i) in ByBlockNumber" :key="i">
              <ul>
                <li>
                  <span>Voter：</span>
                  <div>
                    <span v-if="j.length > 1 || !j.event[0].from">--</span>
                    <span
                      @click="$headerRouterFn(j.event[0].from)"
                      v-if="j.event[0].moniker == null"
                      class="maodian"
                      >{{ j.form_jiequ }}</span
                    >
                    <div
                      v-else
                      class="maodian minimgbox"
                      @click="$userInfoRouterFn(j.event[0].from)"
                    >
                      <img class="moniker_img" :src="j.event[0].moniker.icon" />
                      <span>{{ j.event[0].moniker.moniker }}</span>
                    </div>
                  </div>
                </li>
              </ul>
              <li>
                <span>Vpte Option：</span>
                <div>YES</div>
              </li>
              <li>
                <span>Block：</span>
                <div class="colors">5290449</div>
              </li>
              <li>
                <span>Time:</span>
                <div class="huise">2021-04-05 13:44:00 UTC</div>
              </li>
            </div>
          </div>

          <div class="date-no" v-else>No Data</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { getProposalByProposalId } from "@/api/proposal";
import { getTransactionByBlockNumber } from "@/api/block";
import { mixinCommon } from "@/mixin";
export default {
  mixins: [mixinCommon],
  data() {
    return {
      ProposalIdData: {
        content: {},
        final_tally_result: {},
        total_deposit: [[]],
      },
      final_tally_result: {},
      acitve: 0,
      voteType: "yes",
      ByBlockNumber: [],
      selectData: ["Yes", "NO", "Abstain", "NoWithVeto"],
    };
  },
  created() {
    this.getProposalByProposalIdFn();
    this.getTransactionByBlockNumberFn();
  },
  methods: {
    selectacitveFn(j, i) {
      this.acitve = i;
      this.voteType = j;
      this.getTransactionByBlockNumberFn();
    },
    getProposalByProposalIdFn() {
      getProposalByProposalId({ proposalId: this.$route.params.id }).then(
        (res) => {
          this.ProposalIdData = res.result;
          this.final_tally_result = this.ProposalIdData.final_tally_result;
          this.ProposalIdData.coinName =
            this.ProposalIdData.coinName.toUpperCase();
          this.ProposalIdData.voting_start_time = this.$getUTCTimeFn(
            res.result.voting_start_time
          );
          this.ProposalIdData.voting_end_time = this.$getUTCTimeFn(
            res.result.voting_end_time
          );
          this.ProposalIdData.deposit_end_time = this.$getUTCTimeFn(
            res.result.deposit_end_time
          );
          this.ProposalIdData.submit_time = this.$getUTCTimeFn(
            res.result.submit_time
          );
          this.ProposalIdData.abstainRate =
            this.ProposalIdData.abstainRate * 100;
          this.ProposalIdData.noRate = this.ProposalIdData.noRate * 100;
          this.ProposalIdData.yesRate = this.ProposalIdData.yesRate * 100;
          this.ProposalIdData.noWithVoteRate =
            this.ProposalIdData.noWithVoteRate * 100;
        }
      );
    },
    getTransactionByBlockNumberFn() {
      getTransactionByBlockNumber({
        proposalId: this.$route.params.id,
        voteType: this.voteType,
        bigType: "vote",
      }).then((res) => {
        if (res.reuslt) {
          this.ByBlockNumber = res.reuslt.records;
        }
      });
    },
  },
  components: {},
};
</script>

<style scoped lang="less">
@import url("../../css/index.less");
.list-header-box {
  margin-top: 0px;
  margin-bottom: 15 / @rem;
  & > div {
    padding: 10 / @rem 0px;
    border-bottom: 1px solid #ededed;
    &:last-child {
      border-bottom: none;
    }
  }
}
.selectcolor {
  color: #402ed8;
}
.selectblock {
  padding: 20 / @rem 0px 10 / @rem 0px;
  ul {
    display: flex;

    margin-left: 15 / @rem;
    li {
      display: flex;
      margin-right: 30 / @rem;
    }
  }
  li > span {
    display: inline-block;
    width: 11 / @rem;
    height: 11 / @rem;
    margin-right: 5 / @rem;
  }
  li:nth-child(1) > span {
    background: #0de3ac;
  }
  li:nth-child(2) > span {
    background: #eb3112;
  }
  li:nth-child(3) > span {
    background: #3194f6;
  }
  li:nth-child(4) > span {
    background: #faba1e;
  }
}
.description-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.description {
  background: #fff;
  div {
    line-height: 40 / @rem;
    margin: 0px 10 / @rem;
    font-size: 14 / @rem;
    border-bottom: 1px solid #ededed;
  }
  span {
    display: inline-block;
    line-height: 19 / @rem;
    font-size: 12 / @rem;
    margin: 20 / @rem 20 / @rem;
    color: #9196c1;
  }
}
.current-tur {
  margin: 15 / @rem 0px;
}
.description-content {
  padding: 15 / @rem;
}
.section-top {
  background: #fff;
  font-size: 12 / @rem;
  margin-bottom: 15 / @rem;
  border-bottom: 1px solid #ededed;
  .huise {
    font-size: 8 / @rem;
  }
}
.proposaltitle {
  line-height: 30 / @rem;
  span:last-child {
    margin-left: 20 / @rem;
    font-size: 8 / @rem;
    color: #2a2a2a;
  }
}
.jindutiao {
  margin: 15 / @rem;
  li {
    margin-bottom: 15 / @rem;
  }
}
.propsal-right {
  margin-top: 5 / @rem;
}
.imgew {
  width: 19 / @rem;
  height: 19 / @rem;
  margin-right: 5px;
  position: relative;
  top: 5px;
}
.ul-renshu {
  padding-top: 10px;
  text-indent: 20px;
  font-size: 14px;
  border-top: 1px solid #ededed;
}
.selectblock {
  background: #fff;
  border: 1px solid #ededed;
  ul {
    padding-bottom: 10px;
  }
}
</style>
