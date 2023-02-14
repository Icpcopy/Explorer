<template>
  <div class="icp-listCard">
    <div></div>
    <div>
      <el-row
        v-for="(item, index) in ListItem"
        :key="index"
        class="icp-listCard__list"
      >
        <el-col class="icp-listCard__list--left" :span="item.labelSpan || 13">{{
          item.label
        }}</el-col>
        <el-col class="icp-listCard__list--right" :span="item.valueSpan || 11">
          <span v-if="item.prop == 'totalSupply' || item.prop == 'circulation'">
            {{
              (defaultValue &&
                defaultValue[item.prop] &&
                onFeixedNum(defaultValue[item.prop], 6)) ||
              "--"
            }}</span
          >
          <span
            v-if="item.prop == 'issuerTimestamp' || item.prop == 'mintTime'"
          >
            {{
              `${
                defaultValue &&
                defaultValue[item.prop] &&
                $dayjs
                  .utc(defaultValue[item.prop] * 1000)
                  .format("YYYY-MM-DD HH:mm:ss")
              } UTC` || "--"
            }}</span
          >

          <span
            v-if="
              item.prop == 'addresstotal' ||
              item.prop == 'transactiontotalCount'
            "
          >
            {{ (defaultValue && defaultValue[item.prop]) || "--" }}
          </span>
          <a
            class="icp-listCard__list--link"
            v-if="
              item.prop == 'officialAddress' || item.prop == 'whitePaperAddress'
            "
            :href="(defaultValue && defaultValue[item.prop]) || ''"
            target="_blank"
          >
            {{ (defaultValue && defaultValue[item.prop]) || "--" }}
          </a>
          <div
            v-if="
              item.prop == 'linkList' && defaultValue && defaultValue[item.prop]
            "
          >
            <a
              class="icp-listCard__list--linkimg"
              v-for="(li, idx) in defaultValue[item.prop]"
              :key="idx"
              :href="li.linkAddress"
              target="_blank"
            >
              <img :src="li.linkLogo || ''" />
            </a>
            <span v-if="defaultValue[item.prop].length == 0">--</span>
          </div>

          <span
            v-if="
              (item.prop == 'owner' &&
                defaultValue &&
                defaultValue[item.prop]) ||
              (item.prop == 'issuer' && defaultValue && defaultValue[item.prop])
            "
          >
            <router-link
              class="icp-listCard__list--link"
              :to="{ path: `/Accountdetails/${defaultValue[item.prop]}` }"
            >
              {{
                (defaultValue &&
                  defaultValue[item.prop] &&
                  defaultValue[item.prop].substring(0, 9)) ||
                ""
              }}
              ...{{
                (defaultValue &&
                  defaultValue[item.prop] &&
                  defaultValue[item.prop].substring(
                    defaultValue[item.prop].length - 6,
                    defaultValue[item.prop].length
                  )) ||
                ""
              }}
            </router-link>
            <img
              class="icp-listCard__list--img"
              @click="onCopyText(defaultValue[item.prop])"
              src="@/assets/icons/copy.png"
            />
          </span>
          <span
            v-if="
              (item.prop == 'contractAddress' &&
                defaultValue &&
                defaultValue[item.prop]) ||
              (item.prop == 'contentAddress' &&
                defaultValue &&
                defaultValue[item.prop])
            "
          >
            {{
              (defaultValue &&
                defaultValue[item.prop] &&
                defaultValue[item.prop].substring(0, 9)) ||
              ""
            }}
            ...{{
              (defaultValue &&
                defaultValue[item.prop] &&
                defaultValue[item.prop].substring(
                  defaultValue[item.prop].length - 6,
                  defaultValue[item.prop].length
                )) ||
              ""
            }}

            <img
              class="icp-listCard__list--img"
              @click="onCopyText(defaultValue[item.prop])"
              src="@/assets/icons/copy.png"
            />
          </span>

          <span
            v-if="
              item.prop !== 'totalSupply' &&
              item.prop !== 'circulation' &&
              item.prop !== 'issuerTimestamp' &&
              item.prop !== 'addresstotal' &&
              item.prop !== 'transactiontotalCount' &&
              item.prop !== 'officialAddress' &&
              item.prop !== 'whitePaperAddress' &&
              item.prop !== 'linkList' &&
              item.prop !== 'contractAddress' &&
              item.prop !== 'owner' &&
              item.prop !== 'mintTime' &&
              item.prop !== 'contentAddress' &&
              item.prop !== 'issuer'
            "
          >
            {{ (defaultValue && defaultValue[item.prop]) || "--" }}
          </span>
        </el-col>
      </el-row>
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
.icp-listCard {
  &__list {
    padding: 20px 24px;
    border-bottom: 1px solid #eaebec;

    color: #2a2a2a;
    &--left {
      // font-weight: 600;
      font-size: 14px;
    }
    &--right {
      font-size: 12px;
      word-break: break-all;
    }
    &--img {
      width: 13px;
      margin: 0 0 0 10px;
      cursor: pointer;
    }
    &--link {
      text-decoration: none;
      color: #5b1cf6;
    }
    &--linkimg {
      width: 30px;
      height: 30px;
      display: flex;
      justify-content: center;
      align-items: center;
      float: left;
      border-radius: 50%;
      overflow: hidden;
      margin: 0 10px 10px 0;
      border: 1px dotted #dedede;
      > img {
        max-width: 100%;
      }
    }
  }
}
.icp-listCard__list:last-child {
  border: 0;
}
</style>
