import { mapActions, mapState } from "vuex";
import Clipboard from "clipboard";
import { Toast } from "vant";
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
        console.log(this.ipfsAddress, "this.ipfsAddress===");
        if (this.ipfsAddress) {
          const nftUrl = `${this.ipfsAddress}/browser/ipfs?key=${url}`;
          window.open(nftUrl);
        } else {
          const urls = window.location.href.split(":");
          const ipfsUrl = `${urls[0]}:${urls[1]}:8017/browser/ipfs?key=${url}`;
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
    //格式化较长数据
    onFormatStr(str, num) {
      return (
        (str &&
          `${str.substring(0, num || 7)}...${str.substring(
            str.length - num || 7,
            str.length
          )}`) ||
        ""
      );
    },
    //复制
    onCopy(text, className) {
      this.clipboard = new Clipboard(`.${className}`, {
        // 点击copy按钮，直接通过text直接返回复印的内容
        text: function() {
          return text;
        },
      });
      this.clipboard.on("success", (e) => {
        // this.$message.success(e.text + " 已复制到剪贴板！");
        console.log(e);
        Toast.success("copy successfully");
        this.clipboard.destroy();
      });

      this.clipboard.on("error", function(e) {
        console.log(e);
      });
    },
  },
  created() {},
  mounted() {
    // if (!this.ipfsAddress) {
    //   this.getUrl({ configKey: "ipfs_address_h5" });
    // }
    window.addEventListener("scroll", this.handleScroll, true);
  },
};
