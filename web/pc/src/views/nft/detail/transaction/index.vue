<template>
  <div class="nft-transactionDetail">
    <div class="nft-transactionDetail__tip">
      <div class="nft-transactionDetail__tip--text">
        <span class="nttt-text">
          Transaction Detail
        </span>
        <span class="nttt-hash">一连串hash</span>
        <span>
          <img src="@/assets/icons/copy.png" />
        </span>
      </div>
      <div class="nft-transactionDetail__tip--button">
        <el-button @click="goBack">return</el-button>
      </div>
    </div>
    <div class="nft-transactionDetail__baseInfo">
      <div class="nft-transactionDetail__baseInfo--text">Base Info</div>
      <nft-detail-card :infoObj="baseInfo" />
    </div>
    <div class="nft-transactionDetail__message">
      <div class="nft-transactionDetail__message--text">Messages</div>
      <div
        v-for="(item, index) in messageInfoList"
        :key="index"
        class="nft-transactionDetail__message--card"
      >
        <nft-detail-card :infoObj="item" />
      </div>
    </div>
  </div>
</template>

<script>
import { NftDetailCard } from "@/components";
export default {
  components: { NftDetailCard },
  name: "NftDetailTransaction",
  data() {
    return {
      baseInfo: {
        status: { name: "Status", value: "sdfaasfd" },
        timestamp: { name: "Timestamp", value: "asdasfasf" },
        fee: { name: "Fee", value: "sdjhaksjdhlkasd" },
        signer: { name: "Signer", value: "sadhgjhagsdaf" },
      },
      messageInfoList: [
        {
          type: { name: "Type", value: "sssss" },
          amount: { name: "Amount", value: "sssss" },
          form: { name: "Form", value: "sssss" },
          to: { name: "To", value: "sssss" },
        },
        {
          type: { name: "NFT Transfer", value: "sssss" },
          nftId: { name: "NFT Id", value: "sssss" },
          to: { name: "To", value: "sssss" },
          form: { name: "Form", value: "sssss" },
          url: { name: "Url", value: "sssss" },
        },
        {
          type: { name: "NFT Transfer", value: "sssss" },
          nftId: { name: "NFT Id", value: "sssss" },
          to: { name: "To", value: "sssss" },
          form: { name: "Form", value: "sssss" },
          url: { name: "Url", value: "sssss" },
        },
      ],
    };
  },
  methods: {
    //返回
    goBack() {
      window.history.back();
    },
  },
  mounted() {},
};
</script>
<style lang="less" scoped>
.nft-transactionDetail {
  padding-bottom: 30px;
  &__tip {
    display: flex;
    justify-content: space-between;
    padding: 27px 0 11px 0;
    &--text {
      .nttt-text {
        font-size: 24px;
        color: #2a2a2a;
        padding-right: 15px;
        border-right: 3px solid #b7b7b7;
        font-weight: 600;
      }
      .nttt-hash {
        padding-left: 15px;
        font-size: 14px;
        color: #696969;
      }
      * > img {
        width: 11px;
        height: 14px;
        margin-left: 15px;
        line-height: 24px;
        margin-top: 10px;
        cursor: pointer;
      }
    }
    &--button {
      > button {
        background-color: #633af9;
        color: #fff;
      }
    }
  }
  &__baseInfo {
    border-radius: 5px;
    overflow: hidden;
    margin-bottom: 25px;
    box-shadow: 0px 3px 8px rgba(56, 54, 54, 0.06);
    &--text {
      height: 52px;
      line-height: 52px;
      background: #5740a9;
      font-size: 18px;
      color: #ffffff;
      padding: 0 27px;
    }
  }
  &__message {
    border-radius: 5px;
    overflow: hidden;
    box-shadow: 0px 3px 8px rgba(56, 54, 54, 0.06);
    &--text {
      height: 52px;
      line-height: 52px;
      background: #5740a9;
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
