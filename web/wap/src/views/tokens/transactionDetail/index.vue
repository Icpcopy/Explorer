<template>
  <div class="icp-transactionDetail">
    <div class="icp-transactionDetail__title">
      <div class="icp-transactionDetail__title--title">Transaction Details</div>
      <div class="icp-transactionDetail__title--hash">
        {{ hash }}
        <img
          v-if="hash"
          class="icp-transactionDetail__content--img"
          @click="onCopy(hash, 'icp-transactionDetail__content--img')"
          src="@/assets/icon/copy.png"
        />
      </div>
    </div>

    <div class="icp-transactionDetail__subtitle">Base Info</div>
    <div class="icp-transactionDetail__content">
      <van-row>
        <van-col :span="6" class="icp-transactionDetail__content--label"
          >Block:</van-col
        >
        <van-col :span="18">
          <router-link :to="{ path: '/BlockchainDetails/' + data.blockNumber }"
            >{{ data.blockNumber }}
          </router-link></van-col
        >
      </van-row>
      <van-divider />
      <van-row>
        <van-col :span="6" class="icp-transactionDetail__content--label"
          >Status:</van-col
        >
        <van-col :span="18">
          <div class="green" v-if="data.success">Success</div>
          <div class="error" v-else>Failed</div>
        </van-col>
      </van-row>
      <van-divider />
      <van-row>
        <van-col :span="6" class="icp-transactionDetail__content--label"
          >Timestamp:</van-col
        >
        <van-col :span="18">
          {{
            data.blockTime &&
            $dayjs.utc(data.blockTime * 1000).format("YYYY-MM-DD HH:mm:ss")
          }}
          UTC
        </van-col>
      </van-row>
      <van-divider />
      <van-row>
        <van-col :span="6" class="icp-transactionDetail__content--label"
          >From:</van-col
        >
        <van-col :span="18">
          <router-link :to="{ path: `/Accountdetails/${data.from}` }">
            {{ (data.from && onFormatStr(data.from, 7)) || "" }}
          </router-link>
          <img
            v-if="data && data.from"
            class="icp-transactionDetail__content--img"
            @click="onCopy(data.from, 'icp-transactionDetail__content--img')"
            src="@/assets/icon/copy.png"
          />
        </van-col>
      </van-row>
      <van-divider />
      <van-row>
        <van-col :span="6" class="icp-transactionDetail__content--label"
          >Interacted With(To):</van-col
        >
        <van-col :span="18">
          <div>
            <!-- class="icp-transactionDetail__address" -->
            <span class="icp-transactionDetail__address--title">Contract </span>
            {{ (data.to && onFormatStr(data.to, 7)) || "" }}
            <img
              v-if="data && data.to"
              class="icp-transactionDetail__content--img"
              @click="onCopy(data.to, 'icp-transactionDetail__content--img')"
              src="@/assets/icon/copy.png"
            />
          </div>
          <!-- <router-link :to="{ path: `/tokens/addressdetail/${data.from}` }">
        
          </router-link> -->
        </van-col>
      </van-row>
      <van-divider />
      <van-row>
        <van-col class="icp-transactionDetail__content--label"
          >Tokens Transferred:</van-col
        >
        <van-col :span="24">
          <div
            class="icp-transactionDetail__content--card"
            v-for="(item, key) in data.contractTransfers"
            :key="key"
          >
            <van-row>
              <van-col span="6">From</van-col>
              <van-col span="18">
                <router-link
                  :to="{ path: `/Accountdetails/${item.from}` }"
                >
                  {{ (item.from && onFormatStr(item.from, 7)) || "--" }}
                </router-link>
              </van-col>
            </van-row>
            <van-row>
              <van-col span="6">To</van-col>
              <van-col span="18">
                <router-link :to="{ path: `/Accountdetails/${item.to}` }">
                  {{ (item.to && onFormatStr(item.to, 7)) || "--" }}
                </router-link>
              </van-col>
            </van-row>

            <van-row v-if="data && data.contractType == 'erc721'">
              <van-col span="6">Token ID</van-col>
              <van-col span="18">
                <span class="icp-transactionDetail__address">
                  <router-link
                    :to="{
                      path: `/tokens/nft-detail/${item.nftId}`,
                      query: {
                        contractAddress: item.contractAddress,
                      },
                    }"
                    >{{ item.nftId || "" }}
                  </router-link>
                </span>
              </van-col>
            </van-row>

            <van-row v-if="data && data.contractType !== 'erc721'">
              <van-col span="6">For</van-col>
              <van-col span="18">
                <span class="icp-transactionDetail__address">
                  {{ (item.amount && onFeixedNum(item.amount, 6)) || 0 }}
                  {{ item.symbol }}
                </span>
              </van-col>
            </van-row>
          </div>
        </van-col>
      </van-row>
      <van-divider />
      <van-row>
        <van-col :span="6" class="icp-transactionDetail__content--label"
          >Value:</van-col
        >
        <van-col :span="18">
          {{ (data.amount && onFeixedNum(data.amount, 6)) || 0 }} ICT
        </van-col>
      </van-row>
      <van-divider />
      <van-row>
        <van-col :span="6" class="icp-transactionDetail__content--label"
          >Fee:</van-col
        >
        <van-col :span="18">
          {{ (data.gasUsed && onFeixedNum(data.gasUsed, 6)) || 0 }}
         ICT</van-col
        >
      </van-row>
      <van-divider />
      <van-row>
        <van-col :span="6" class="icp-transactionDetail__content--label"
          >Gas Price:</van-col
        >
        <van-col :span="18">
          {{
            (data.transaction &&
              data.transaction.gasPrice &&
              onFormatNum(data.transaction.gasPrice / 1000000000000000000)) ||
            0
          }}ICT
        </van-col>
      </van-row>
      <van-divider />
      <van-row>
        <van-col :span="6" class="icp-transactionDetail__content--label"
          >Gas Limit&Usege by Txn:</van-col
        >
        <van-col :span="18">
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
        </van-col>
      </van-row>
      <van-divider />
      <van-row>
        <van-col span="24" class="icp-transactionDetail__content--label"
          >Input Data:</van-col
        >
        <van-col span="24">
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
        </van-col>
      </van-row>
    </div>
  </div>
</template>

<script>
// import { Tokens } from "@/api";
// import { getUTCTimeFn } from "@/utils/public";
import { mixinCommon } from "@/mixin";
// import { ConfigTransactionTypes } from "@/config";
// import { NftDetailCard } from "@/components";
import { Tokens } from "@/api";

export default {
  components: {},
  mixins: [mixinCommon],
  data() {
    return {
      //   ConfigTransactionTypes,
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
      this.hash = this.$route.params.hash;
      this.init();
      // console.log(this.$route.path, "path====");
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
  },
  created() {
    // this.getTransactionByHashFn();
  },
  mounted() {
    window.scrollTo(0, 0);
    this.hash = this.$route.params.hash;
    this.init();
  },
};
</script>

<style scoped lang="less">
.icp-transactionDetail {
  &__title {
    padding: 30px 15px 15px 15px;
    &--title {
      color: #2a2a2a;
      font-size: 14px;
      font-weight: 600;
      margin-bottom: 10px;
    }
    &--hash {
      word-break: break-all;
      font-size: 12px;
      color: #696969;
      line-height: 12px;
    }
  }
  &__subtitle {
    background: #eeeeee;
    font-size: 14px;
    color: #2a2a2a;
    padding: 10px 15px;
  }
  &__content {
    padding: 15px;
    &--label {
      font-size: 12px;
      font-weight: 600;
      color: #2a2a2a;
    }
    &--img {
      margin-left: 10px;
    }
    &--card {
      background: #fcfcfc;
      border-radius: 5px;
      border: 1px solid #ededed;
      padding: 10px 15px;
      margin-top: 10px;
      .van-row {
        padding: 10px 0;
      }
    }
  }
  &__inputData {
    background: rgba(247, 249, 250, 0.39);
    border: 1px solid #e5e8eb;
    opacity: 1;
    border-radius: 5px;
    padding: 20px;
    margin: 20px 0;
    color: rgba(0, 0, 0, 0.4);
    &--row {
      line-height: 20px;
      word-break: break-all;
    }
  }
  &__address{
    word-break: break-all;
  }
}
</style>
