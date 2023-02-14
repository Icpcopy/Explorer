<template>
  <div class="list-card">
    <van-row class="list-card__row" v-for="(item, key) in listItem" :key="key">
      <van-col class="list-card__row--label"> {{ item.label }}: </van-col>
      <van-col class="list-card__row--value">
        <router-link v-if="item.link" :to="{ path: item.link }">
          {{ (item && item.tokenId) || "--" }}
        </router-link>
        <span
          v-if="!item.link && !item.slot"
          :style="{ color: item.color || '' }"
        >
          {{
            (defaultValue &&
              defaultValue[item.prop] &&
              defaultValue[item.prop]) ||
            "--"
          }}
        </span>
        <img
          class="list-card__row--img1"
          v-if="item.copy"
          src="../../../../assets/fuzhi.png"
          @click="onCopy(item.owner, 1)"
        />
        <slot v-if="item.slot" :name="item.slot"></slot>
        <!-- {{
            (defaultValue &&
              defaultValue[item.prop] &&
              defaultValue[item.prop]) ||
            (defaultValue && isNaN(defaultValue[item.prop]))
              ? ""
              : 0
          }} -->
      </van-col>
    </van-row>

    <!-- <van-row class="list-card__row">
      <van-col span="6" class="list-card__row--label">Time:</van-col>
      <van-col span="18" class="list-card__row--value time">
        {{
          `${(item && item.timestamp && $dayjs.utc(item.timestamp)).format(
            "YYYY-MM-DD HH:mm:ss"
          )} UTC` || ""
        }}
      </van-col>
    </van-row> -->
  </div>
</template>

<script>
import { mixinCommon } from "@/mixin";
import Clipboard from "clipboard";
import { Toast } from "vant";
export default {
  components: {},
  mixins: [mixinCommon],
  props: {
    defaultValue: { type: Object, default: () => {} },
    listItem: { type: Array, default: () => [] },
  },
  data() {
    return {
      clipboard: null,
    };
  },
  watch: {
    defaultValue: {
      handler() {
        // console.log(this.defaultValue, "defaultValue====");
      },
    },
  },
  methods: {
    onCopy(text, index) {
      this.clipboard = new Clipboard(`.list-card__row--img${index}`, {
        // 点击copy按钮，直接通过text直接返回复印的内容
        text: function () {
          return text;
        },
      });
      this.clipboard.on("success", (e) => {
        // this.$message.success(e.text + " 已复制到剪贴板！");
        Toast.success("copy successfully");
        this.clipboard.destroy();
      });

      this.clipboard.on("error", function (e) {
        console.log(e);
      });
    },
  },
  mounted() {
    // console.log(this.defaultValue, "defaultValue===");
  },
  created() {},
};
</script>

<style scoped lang="less">
.list-card {
  &__row {
    display: flex;
    justify-content: space-between;
    padding: 5px 0;
    &--label {
      font-size: 12px;
      line-height: 30px;
      font-weight: 600;
    }
    &--value {
      word-break: break-all;
      line-height: 30px;
      > a {
        color: #561bf8;
      }
    }
    &--img0 {
      width: 16px;
      height: 17px;
      margin: 4px 0 0 10px;
    }
    &--img1 {
      width: 16px;
      height: 17px;
      margin: 4px 0 0 10px;
    }
    &--img2 {
      width: 16px;
      height: 17px;
      margin: 4px 0 0 10px;
    }
    .time {
      color: #9196c1;
    }
  }
}
</style>
