<template>
  <div class="nft-detailCard">
    <div v-for="(item, key) in infoObj" :key="key" class="nft-detailCard__row">
      <el-row v-if="item.name !== 'coinName'">
        <el-col class="nft-detailCard__row--label" :span="5"
          >{{ item.name }}:</el-col
        >
        <el-col :span="18">
          <router-link
            v-if="item.link && item.link !== 'ipfs'"
            :to="{ path: `${item.link}${item.value}` }"
          >
            {{ item.value || "--" }}
          </router-link>
          <router-link
            v-if="item.link && item.link == 'ipfs'"
            :to="{ path: `` }"
            @click="toIpfs(item.value)"
          >
            {{ item.value || "--" }}
          </router-link>
          <span
            v-if="!item.link"
            :class="
              item.name == 'type'
                ? 'nft-detailCard__row--typevalue'
                : 'nft-detailCard__row--value'
            "
          >
            {{ item.value || "--" }}
            <span>
              {{
              item.name == "amount"
                ? (infoObj["coinName"] &&
                    infoObj["coinName"].value &&
                    onFeixedNum(infoObj["coinName"].value, 6)) ||
                  ""
                : ""
            }}
            </span>
          </span>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
export default {
  props: {
    infoObj: { type: Object, default: () => {} },
  },
  mixins: [mixinCommon],
  components: {},
  name: "NftStatsCharts",
  data() {
    return {};
  },
  methods: {},
  mounted() {},
};
</script>
<style lang="less" scoped>
.nft-detailCard {
  width: 100%;
  background: #fff;
  * a {
    color: #633af9;
    text-decoration: none;
  }
  &__row {
    line-height: 56px;
    border-bottom: 1px solid #eaebec;
    padding: 0 97px;
    &--label {
      font-size: 18px;
      color: #2a2a2a;
      font-weight: 600;
      word-break: break-all;
    }
    &--typevalue {
      color: #ff9000;
      word-break: break-all;
      // font-size: 18px;
    }
    &--value {
      word-break: break-all;
      // font-size: 18px;
      // > a {
      //   font-size: 18px;
      // }
    }
  }
  &__row:last-child {
    border: 0;
  }
}
</style>
