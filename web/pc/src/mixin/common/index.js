import { mapActions, mapState } from "vuex";
import useClipboard from "vue-clipboard3";
export default {
  data() {
    return {
      name: "CommonMixin",
    };
  },
  computed: {
    ...mapState({
      ipfsAddress: (state) => state.common.ipfsAddress,
    }),
  },
  methods: {
    ...mapActions({
      getUrl: "common/getUrl",
    }),
    //跳转到ipfs浏览器
    toIpfs(url) {
      if (url) {
        if (this.ipfsAddress) {
          const nftUrl = `${this.ipfsAddress}/browser/ipfs?key=${url}`;
          window.open(nftUrl);
        } else {
          const urls = window.location.href.split(":");
          const ipfsUrl = `${urls[0]}:${urls[1]}:8020/browser/ipfs?key=${url}`;
          window.open(ipfsUrl);
        }
      } else {
        this.$message.info("暂无ipfs相关浏览器信息");
      }
    },

    //保留几位小数
    onFeixedNum(str, num) {
      const strList = String(str).split(".");
      if (strList.length > 1) {
        const firstNum = strList[0];
        const lastNum = strList[1];
        if (strList[1].length > num) {
          return `${firstNum}.${lastNum.substring(0, num)}`;
        } else {
          return `${firstNum}.${lastNum}`;
        }
      } else {
        return str;
      }
    },
    //复制文本
    onCopyText(msg) {
      const { toClipboard } = useClipboard();
      toClipboard(msg)
        .then(() => {
          this.$message({
            message: "copy successful",
            type: "success",
          });
        })
        .catch(() => {
          this.$message({
            message: "copy faild",
            type: "error",
          });
        });
    },
  },
  created() {},
  mounted() {},
};
