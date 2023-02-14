<template>
  <div class="icp-inventoryCard">
    <div class="icp-inventoryCard__img">
      <div>
        <img  src="@/assets/icons/nft.png" />
      </div>
    </div>
    <div class="icp-inventoryCard__row">
      <span class="icp-inventoryCard__row--label">Token ID:</span>
      <span class="icp-inventoryCard__row--value">
        <router-link
          v-if="!nftId"
          class="icp-evmTransaction__link"
          :to="{
            path: `/tokens/repeat-txs/txs/${
              defaultValue && defaultValue.nftId
            }`,
            query: {
              contractAddress: defaultValue && defaultValue.contentAddress,
            },
          }"
        >
          {{ (defaultValue && defaultValue.nftId) || "--" }}
        </router-link>
        <span v-else> {{ (defaultValue && defaultValue.nftId) || "--" }}</span>
      </span>
    </div>
    <div class="icp-inventoryCard__row">
      <span class="icp-inventoryCard__row--label">Owner:</span>
      <span class="icp-inventoryCard__row--value">
        <!-- {{ (defaultValue && defaultValue.owner) || "--" }} -->
        <router-link
          :to="{
            path: `/Accountdetails/${
              (defaultValue && defaultValue.owner) || ''
            }`,
          }"
        >
          {{
            (defaultValue &&
              defaultValue.owner &&
              defaultValue.owner.substring(0, 5)) ||
            ""
          }}
          ...{{
            (defaultValue &&
              defaultValue.owner &&
              defaultValue.owner.substring(
                defaultValue.owner.length - 6,
                defaultValue.owner.length
              )) ||
            ""
          }}
        </router-link>
      </span>
    </div>
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
export default {
  components: {},
  name: "ListCard",
  mixins: [mixinCommon],
  props: {
    ListItem: {
      type: Array,
      default: () => [],
    },
    title: { type: String, default: "" },
    defaultValue: { type: Object, default: () => {} },
    nftId: { type: String, default: "" },
  },
  data() {
    return {};
  },
  watch: {},
  methods: {},
  mounted() {},
};
</script>
<style lang="less" scoped>
.icp-inventoryCard {
  background: rgba(255, 255, 255, 0.39);
  border: 1px solid #eaebec;
  box-shadow: 0px 3px 8px rgba(56, 54, 54, 0.06);
  padding: 20px 17px;
  &__img {
    display: flex;
    justify-content: center;
    align-items: center;

    > div {
      width: 78px;
      height: 78px;
      > img {
        width: 100%;
      }
    }
    //  overflow: hidden;
  }
  &__row {
    font-size: 14px;
    line-height: 20px;
    text-align: left;
    margin-top: 20px;
    &--label {
      color: #2a2a2a;
      font-weight: 600;
    }
    &--value {
      //   color: #5b1cf6;
      > a {
        text-decoration: none;
        color: #5b1cf6;
      }
    }
  }
}
</style>
