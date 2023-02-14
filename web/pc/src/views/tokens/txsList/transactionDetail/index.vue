<template>
  <div class="icp-transactionDetail">
    <div class="container">
      <header class="public-head">
        <h3>Transaction Details</h3>
        <p>
          <textarea id="copy1" class="displaynone"></textarea>
          {{ hash }}
        </p>
        <span class="copystyle" @click="onCopyText(hash)">
          <i class="el-icon-document-copy"></i>
        </span>
      </header>
      <section class="section-top">
        <div class="s-title">Base Info</div>
        <div class="content">
          <div>
            <span class="titles">Block:</span>
            <div
              class="zhuti blod maodian"
              @click="$blockChainInfosRouterFn(data.blockNumber)"
            >
              <!-- @click="$blockChainInfosRouterFn(data.height)" -->
              {{ data.blockNumber }}
            </div>
          </div>
          <div>
            <span class="titles">Status:</span>
            <div class="green" v-if="data.success">Success</div>
            <div class="error" v-else>Failed</div>
          </div>
          <div>
            <span class="titles">Timestamp:</span>
            <div class="huise">
              {{
                data.blockTime &&
                $dayjs.utc(data.blockTime * 1000).format("YYYY-MM-DD HH:mm:ss")
              }}
              UTC
            </div>
          </div>
          <!-- <div v-if="data && data.contractType == 'erc721'">
            <span class="titles">Transaction Action:</span>
            <div
              class="huise"
              v-for="(item, key) in data.contractTransfers"
              :key="key"
            >
              <span class="icp-transactionDetail__address--title">
                Transfer of
              </span>
              <span class="icp-transactionDetail__address">
                <router-link
                  :to="{
                    path: `/tokens/detail/${item.contractAddress}`,
                    query: {
                      type: 'erc721',
                    },
                  }"
                >
                  {{ item.tokenName || "--" }}
                </router-link>
              </span>
              <span class="icp-transactionDetail__address--title">From </span>
              <span class="icp-transactionDetail__address">
                <router-link
                  :to="{ path: `/tokens/addressdetail/${item.from}` }"
                >
                  {{
                    (item.from && `${item.from.substring(0, 11)}...`) || "--"
                  }}
                </router-link>
              </span>
              <span class="icp-transactionDetail__address--title">To</span>
              <span class="icp-transactionDetail__address">
                <router-link :to="{ path: `/tokens/addressdetail/${item.to}` }"
                  >{{ (item.to && `${item.to.substring(0, 11)}...`) || "--" }}
                </router-link>
              </span>
              <span class="icp-transactionDetail__address--title">
                Token ID</span
              >
              <span class="icp-transactionDetail__address">
                <router-link
                  :to="{
                    path: `/tokens/repeat-txs/txs-nft/${item.nftId}`,
                    query: {
                      contractAddress: item.contractAddress,
                    },
                  }"
                >
                  {{ item.nftId || "--" }}
                </router-link>
              </span>
            </div>
          </div> -->
          <div>
            <span class="titles">From:</span>
            <div class="icp-transactionDetail__address">
              <router-link :to="{ path: `/Accountdetails/${data.from}` }">
                {{ data.from }}
              </router-link>
              <span class="copystyle" @click="onCopyText(data.from)">
                <i class="el-icon-document-copy"></i>
              </span>
            </div>
          </div>
          <div>
            <span class="titles">Interacted With(To):</span>
            <div>
              <!-- class="icp-transactionDetail__address" -->
              <span class="icp-transactionDetail__address--title"
                >Contract
              </span>
              {{ data.to }}
              <span class="copystyle" @click="onCopyText(data.to)">
                <i class="el-icon-document-copy"></i>
              </span>
            </div>
          </div>
          <div>
            <span class="titles">Tokens Transferred:</span>
            <!-- class="huise" -->
            <div>
              <div v-for="(item, key) in data.contractTransfers" :key="key">
                <span class="icp-transactionDetail__address--title">From</span>
                <span class="icp-transactionDetail__address">
                  <router-link
                    :to="{ path: `/Accountdetails/${item.from}` }"
                  >
                    {{
                      (item.from && `${item.from.substring(0, 11)}...`) || "--"
                    }}
                  </router-link>
                </span>
                <span class="icp-transactionDetail__address--title">To</span>
                <span class="icp-transactionDetail__address">
                  <router-link
                    :to="{ path: `/Accountdetails/${item.to}` }"
                    >{{ (item.to && `${item.to.substring(0, 11)}...`) || "--" }}
                  </router-link>
                </span>

                <span
                  class="icp-transactionDetail__address--title"
                  v-if="data && data.contractType == 'erc721'"
                  >Token ID</span
                >
                <span
                  class="icp-transactionDetail__address"
                  v-if="data && data.contractType == 'erc721'"
                >
                  <router-link
                    :to="{
                      path: `/tokens/repeat-txs/txs/${item.nftId}`,
                      query: {
                        contractAddress: item.contractAddress,
                      },
                    }"
                    >{{ item.nftId || "" }}
                  </router-link>
                </span>
                <span
                  class="icp-transactionDetail__address--title"
                  v-if="data && data.contractType !== 'erc721'"
                  >For</span
                >
                <span
                  class="icp-transactionDetail__address--amount"
                  v-if="data && data.contractType !== 'erc721'"
                  >{{ (item.amount && onFeixedNum(item.amount, 6)) || 0 }}</span
                >

                <span
                  class="icp-transactionDetail__address"
                  v-if="data && data.contractType !== 'erc721'"
                  >{{ item.symbol }}</span
                >
              </div>
            </div>
          </div>
          <div>
            <span class="titles">Value:</span>
            <div>
              {{ (data.amount && onFeixedNum(data.amount, 6)) || 0 }} ICT
            </div>
          </div>
          <div>
            <span class="titles">Fee:</span>
            <div>
              {{ (data.gasUsed && onFeixedNum(data.gasUsed, 6)) || 0 }} ICT
            </div>
          </div>
          <div>
            <span class="titles">Gas Price:</span>
            <div>
              {{
                (data.transaction &&
                  data.transaction.gasPrice &&
                  onFormatNum(
                    data.transaction.gasPrice / 1000000000000000000
                  )) ||
                0
              }}ICT
            </div>
          </div>
          <div>
            <span class="titles">Gas Limit&Usege by Txn:</span>
            <div>
              {{ (data.transaction && data.transaction.gas) || 0 }} |
              {{ (data.receipt && data.receipt.gasUsed) || 0 }}
              <span
                >({{
                  data.receipt &&
                  data.receipt.gasUsed &&
                  (data.receipt.gasUsed / data.transaction.gas) * 100 &&
                  onFeixedNum(
                    (data.receipt.gasUsed / data.transaction.gas) * 100,
                    2
                  )
                }}%)</span
              >
            </div>
          </div>
          <div>
            <span class="titles">Input Data:</span>
            <div class="icp-transactionDetail__inputData">
              <div
                class="icp-transactionDetail__inputData--row"
                style="margin-bottom: 20px"
              >
                Function:
                {{ (data.inputData && data.inputData.function) || "--" }}
              </div>
              <div class="icp-transactionDetail__inputData--row">
                MethodID:
                {{ (data.inputData && data.inputData.methodId) || "--" }}
              </div>
              <div v-if="data.inputData && data.inputData.inputs">
                <div
                  class="icp-transactionDetail__inputData--row"
                  v-for="(item, key) in data.inputData.inputs"
                  :key="key"
                >
                  <span>[{{ key }}]: </span> {{ item }}
                </div>
              </div>
            </div>
          </div>

          <div v-if="isShangXiaBool">
            <span class="titles">Log:</span>
            <div class="logs-text">{{ data.raw_log ? data.raw_log : "" }}</div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import { Tokens } from "@/api";
// import { getUTCTimeFn } from "@/utils/public";
import { mixinCommon } from "@/mixin";
import { ConfigTransactionTypes } from "@/config";
// import { NftDetailCard } from "@/components";

export default {
  components: {},
  mixins: [mixinCommon],
  data() {
    return {
      ConfigTransactionTypes,
      transactionListInfo: [],
      Tradinghash_data: {
        event: [{ commission: {}, description: {}, from: {} }],
      },
      hash: "",
      data: "",
      type: "",
    };
  },

  watch: {
    $route() {
      this.hash = this.$route.params.id;
      this.type = this.$route.query.type;
      this.init();
    },
  },
  methods: {
    //解决科学计数法问题
    onFormatNum(num) {
      var m = num.toExponential().match(/\d(?:\.(\d*))?e([+-]\d+)/);
      console.log(m[0], "m0====", m[1], "m[1]=====", m[2], "m[2]========");
      const newNum = num.toFixed(Math.max(0, (m[1] || "").length - m[2]));
      return this.onFeixedNum(newNum, 6);
    },
    //详情
    init() {
      Tokens.getTransactionDetail({ hash: this.hash }).then((res) => {
        if (res.code >= 200 && res.code < 300) {
          this.data = res.result;
          // Tokens.getTokenList({ listType: 1 }).then((res) => {
          //   if (res.code >= 200 && res.code < 300) {
          //     console.log(res, "res===");
          //   }
          // });
        }
      });
    },
    //复制
    copyFn(copy_id) {
      var e = document.getElementById(copy_id);
      e.select();
      document.execCommand("Copy");
      this.$message({
        showClose: true,
        message: "Copied！",
        type: "success",
      });
    },
  },
  created() {
    // this.getTransactionByHashFn();
  },
  mounted() {
    this.hash = this.$route.params.id;
    console.log(this.$route, "this.$route==");
    this.type = this.$route.query.type;
    this.init();
  },
};
</script>

<style scoped lang="less">
.icp-transactionDetail {
  padding-bottom: 30px;
  &__address {
    color: #0045ff;
    margin-right: 20px;
    &--title {
      font-size: 14px;
      font-weight: bold;
      color: #2a2a2a;
      margin-right: 20px;
    }
    &--amount {
      font-size: 14px;
      font-weight: 400;
      color: #0370ef;
      margin-right: 20px;
    }
    > a {
      text-decoration: none;
      color: #0045ff;
    }
  }
  &__inputData {
    background: rgba(247, 249, 250, 0.39);
    border: 1px solid #e5e8eb;
    opacity: 1;
    border-radius: 2px;
    padding: 20px;
    margin: 20px 0;
    color: rgba(0, 0, 0, 0.4);
    &--row {
      line-height: 20px;
    }
  }
  &__message {
    border-radius: 5px;
    overflow: hidden;
    box-shadow: 0px 3px 8px rgba(56, 54, 54, 0.06);
    margin: 20px 0;
    &--title {
      height: 52px;
      line-height: 52px;
      background: #3a5ddb;
      font-size: 18px;
      color: #ffffff;
      padding: 0 27px;
    }
    &--card {
      border-bottom: 6px solid #eaebec;
    }
    &--card:last-child {
      border: 0;
    }
  }
}
</style>
