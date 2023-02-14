<template>
  <div class="container">
    <div class="top-box">
      <div class="left-box" @click="clickHomeFn">
        <div @click="bizhongBoolFn">
          <!-- <img v-if="chainName == 'gauss'" src="../../assets/log.png" alt />
          <img v-if="chainName == 'usdg'" src="../../assets/usdg.png" alt />
          <img v-if="chainName == 'themis'" src="../../assets/themis.png" alt /> -->
          <!-- <img v-if="chainName == 'igpc'" src="../../assets/igpc.png" alt /> -->
          <!-- <img v-if="chainName == 'fec'" src="../../assets/fec.png" alt />
          <img v-if="chainName == 'gpb'" src="../../assets/gpb.png" alt /> -->
          <img src="../../assets/icplazaLogo.png" alt />
          <!-- <span v-if="chainName == 'gpb' || chainName == 'themis'"
            >{{ chainName.toUpperCase() }} ICPLAZA 1.0.0</span
          > -->
          <!-- v-else -->
          <!-- {{ chainName.toUpperCase() }} -->
          <span> ICPLAZA </span>
          <!-- <van-icon v-if="bizhongBool" name="arrow-up" /> -->
          <!-- <van-icon v-else name="arrow-down" class="van_icon" /> -->
        </div>
        <!-- <div class="slect-box-bi" v-if="bizhongBool">
          <ul>
            <li @click="windClickGoFn(bizhongUrlsData.usdgUrl)">
              <img src="../../assets/usdg.png" alt />
              USDG
            </li>
            <li @click="windClickGoFn(bizhongUrlsData.fecUrl)">
              <img src="../../assets/fec.png" alt />
              FEC
            </li>
            <li @click="windClickGoFn(bizhongUrlsData.gaussUrl)">
              <img src="../../assets/log.png" alt />
              GAUSS
            </li>
            <li @click="windClickGoFn(bizhongUrlsData.gpbUrl)">
              <img src="../../assets/gpb.png" alt />
              GPB
            </li>
            <li @click="windClickGoFn(bizhongUrlsData.themisUrl)">
              <img src="../../assets/themis.png" alt />
              THEMIS
            </li>
          </ul>
          <div class="bghidenbox" @click="bizhongBool= false"></div>
        </div> -->
      </div>
      <div class="right-box">
        <div>
          <img src="../../assets/meun.png" alt @click="showPopupFn" />
        </div>
        <div class="right-select" v-if="showPopup">
          <div class="header-title" @click="ClickrouterFn('/blockchain')">
            Blockchain
          </div>
          <div>
            <div class="header-title" @click="tokensVisible = !tokensVisible">
              <span>Tokens</span>
              <van-icon v-if="!tokensVisible" name="arrow" />
              <van-icon v-else name="arrow-down" />
            </div>
            <ul v-if="tokensVisible">
              <li
                class="header-title__item"
                @click="
                  () => {
                    (tokensVisible = false),
                      ClickrouterFn('/tokens/erc20-list');
                  }
                "
              >
                <router-link :to="{ path: '/tokens/erc20-list' }"
                  >ICT20 Tokens</router-link
                >
              </li>
              <li
                class="header-title__item"
                @click="
                  () => {
                    (nftVisible = false), ClickrouterFn('/tokens/erc20-txs');
                  }
                "
              >
                <router-link :to="{ path: '/tokens/erc20-txs' }">
                  ICT20 Txs</router-link
                >
              </li>
              <li
                class="header-title__item"
                @click="
                  () => {
                    (tokensVisible = false),
                      ClickrouterFn('/tokens/erc721-list');
                  }
                "
              >
                <router-link :to="{ path: '/tokens/erc721-list' }"
                  >ICT721 Tokens</router-link
                >
              </li>
              <li
                class="header-title__item"
                @click="
                  () => {
                    (nftVisible = false), ClickrouterFn('/tokens/erc721-txs');
                  }
                "
              >
                <router-link :to="{ path: '/tokens/erc721-txs' }">
                  ICT721 Txs</router-link
                >
              </li>
            </ul>
          </div>
          <div>
            <div class="header-title" @click="nftVisible = !nftVisible">
              <span>NFT</span>
              <van-icon v-if="!nftVisible" name="arrow" />
              <van-icon v-else name="arrow-down" />
            </div>
            <ul v-if="nftVisible">
              <li
                class="header-title__item"
                @click="
                  () => {
                    (nftVisible = false), ClickrouterFn('/nft/nft-list');
                  }
                "
              >
                <router-link :to="{ path: '/nft/nft-list' }">NFT</router-link>
              </li>
              <li
                class="header-title__item"
                @click="
                  () => {
                    (nftVisible = false), ClickrouterFn('/nft/nft-stats');
                  }
                "
              >
                <router-link :to="{ path: '/nft/nft-stats' }">
                  NFT Stats</router-link
                >
              </li>
            </ul>
          </div>
          <div class="header-title" @click="ClickrouterFn('/deal')">
            Transactions
          </div>
          <div>
            <div class="header-title" @click="Staking_boolFn">
              <span>Staking</span>
              <van-icon v-if="!Staking_bool" name="arrow" />
              <van-icon v-else name="arrow-down" />
            </div>
            <ul v-if="Staking_bool">
              <li
                class="header-title__item"
                @click="ClickrouterFn('/identifier')"
              >
                Validators
              </li>
              <li
                class="header-title__item"
                @click="ClickrouterFn('/entrustrecord')"
              >
                Delegation Txs
              </li>
              <li
                class="header-title__item"
                @click="ClickrouterFn('/Verifierrecord')"
              >
                Validation Txs
              </li>
            </ul>
          </div>
          <div>
            <div class="header-title" @click="Gov_boolFn">
              <span>Gov</span>
              <van-icon v-if="!Govbool" name="arrow" />
              <van-icon v-else name="arrow-down" />
            </div>
            <ul v-if="Govbool">
              <li
                class="header-title__item"
                @click="ClickrouterFn('/Governanceproposals')"
              >
                Proposals
              </li>
              <li
                class="header-title__item"
                @click="ClickrouterFn('/Proposedtransaction')"
              >
                Gov Txs
              </li>
            </ul>
          </div>
          <div>
            <div class="header-title" @click="StatsboolFn">
              <span>Stats</span>
              <van-icon v-if="!Statsbool" name="arrow" />
              <van-icon v-else name="arrow-down" />
            </div>
            <ul v-if="Statsbool">
              <li
                class="header-title__item"
                @click="ClickrouterFn('/statistics')"
              >
                {{ (coinName && coinName.toUpperCase()) || "" }} Rich List
                <!--  -->
              </li>
              <li
                class="header-title__item"
                @click="ClickrouterFn('/statisticaldetails')"
              >
                {{ (coinName && coinName.toUpperCase()) || "" }} Stats
                <!-- .toUpperCase() -->
              </li>
            </ul>
          </div>
          <a
            style="color:#fff;text-decoration:none"
            href="https://browseevm.ic-plaza.org/"
            target="_blank"
          >
            <div class="header-title">
              Evm
            </div>
          </a>
          <div class="bghidenbox" @click="showPopup = false"></div>
        </div>
      </div>
    </div>
    <div class="bottom-box">
      <div class="srech-box">
        <input type="text" v-model="serchtext" />
        <img src="../../assets/sousuo.png" alt @click="srchTxtChangeFn" />
      </div>
    </div>
  </div>
</template>

<script>
import { getChainName } from "@/api/home";
export default {
  data() {
    return {
      value: "",
      showPopup: false,
      Staking_bool: false,
      Govbool: false,
      Statsbool: false,
      serchtext: "",
      bizhongBool: false,
      bizhongUrlsData: {},
      coinName: "",
      isSousuoBool: false,
      nftVisible: false,
      tokensVisible: false,
    };
  },
  created() {
    this.getChainNameFn();
  },
  watch: {
    $route(from, to) {
      if (!this.isSousuoBool) {
        if (
          from.name == "deal" ||
          from.name == "Accountdetails" ||
          from.name == "blockchain" ||
          from.name == "identifier" ||
          from.name == "entrustrecord" ||
          from.name == "Verifierrecord" ||
          from.name == "Governanceproposals" ||
          from.name == "Proposedtransaction" ||
          from.name == "statistics" ||
          from.name == "statisticaldetails"
        ) {
          this.parentSreachFn();
        }
      } else {
        if (
          from.name == "deal" ||
          from.name == "blockchain" ||
          from.name == "identifier" ||
          from.name == "entrustrecord" ||
          from.name == "Verifierrecord" ||
          from.name == "Governanceproposals" ||
          from.name == "Proposedtransaction" ||
          from.name == "statistics" ||
          from.name == "statisticaldetails"
        ) {
          this.parentSreachFn();
        }
      }
    },
  },
  methods: {
    parentSreachFn() {
      this.serchtext = "";
    },
    isSousuoBoolFn() {
      this.isSousuoBool = false;
    },
    headers_ico_Fn(chainName) {
      var link =
        document.querySelector("link[rel*='icon']") ||
        document.createElement("link");
      link.type = "image/x-icon";
      link.rel = "shortcut icon";
      var title = document.getElementsByTagName("title")[0];
      switch (chainName) {
        case "gauss":
          link.href = "/gauss.ico";
          title.innerText = "GAUSS";
          break;
        case "usdg":
          link.href = "/usdg.ico";
          title.innerText = "USDG";
          break;
        case "themis":
          link.href = "/themis.ico";
          title.innerText = "THEMIS";
          break;
        case "fec":
          link.href = "/fec.ico";
          title.innerText = "FEC";
          break;
        case "igpc":
          link.href = "/igpc.ico";
          title.innerText = "IGPC";
          break;
        case "gpb":
          link.href = "/gpb.ico";
          title.innerText = "GPB";
          break;
      }
      this.iSsrcBool = chainName;
      document.getElementsByTagName("head")[0].appendChild(link);
    },
    getChainNameFn() {
      getChainName().then((res) => {
        var urlTxt = "";
        this.bizhongUrlsData = res;
        this.coinName = res.coinName;
        this.headers_ico_Fn(res.chainName);
        switch (res.chainName) {
          case "gauss":
            urlTxt = res.gaussUrl;
            break;
          case "themis":
            urlTxt = res.themisUrl;
            break;
          case "usdg":
            urlTxt = res.usdgUrl;
            break;
          case "igpc":
            urlTxt = res.igpcUrl;
            break;
          case "fec":
            urlTxt = res.fecUrl;
            break;
          case "gpb":
            urlTxt = res.gpbUrl;
            break;
        }
        // if (window.location.host != urlTxt.split("//")[1]) {
        //   window.location.href = urlTxt;
        // }
      });
    },
    srchTxtChangeFn() {
      var reg1 = /^\d{1,32}$/;
      var reg2 = /^[0-9A-Z]+$/;
      const evmHashReg = /^[0][x][0-9A-Za-z]{64}$/;
      const evmAddressReg = /^[0][x][0-9A-Za-z]{40}$/;
      var reg3 = /cosmos[a-z0-9]+$/;
      var reg4 = /cosmosvaloper[[a-z0-9]+$/;

      var reg3_gauss = /gauss[a-z0-9]+$/;
      var reg4_gauss = /gaussvaloper[a-z0-9]+$/;

      var reg3_usdg = /usdg[a-z0-9]+$/;
      var reg4_usdg = /usdgvaloper[a-z0-9]+$/;

      var reg3_igpc = /igpc[a-z0-9]+$/;
      var reg4_igpc = /igpcvaloper[a-z0-9]+$/;

      var reg3_fec = /fec[a-z0-9]+$/;
      var reg4_fec = /fecvaloper[a-z0-9]+$/;

      var reg3_gpb = /gpb[a-z0-9]+$/;
      var reg4_gpb = /gpbvaloper[a-z0-9]+$/;

      var reg3_themis = /gpb[a-z0-9]+$/;
      var reg4_themis = /gpbvaloper[a-z0-9]+$/;

      var reg3_icplaza = /icplaza[a-z0-9]+$/;
      var reg4_icplaza = /icplazavaloper[a-z0-9]+$/;

      this.isSousuoBool = true;
      if (this.serchtext != null) {
        this.serchtext = this.serchtext.replace(/\s*/g, "");
        if (reg1.test(this.serchtext)) {
          this.routerSreachFn("/BlockchainDetails");
        } else if (reg2.test(this.serchtext)) {
          this.routerSreachFn("/tradinginfo");
        } else if (
          reg4.test(this.serchtext) ||
          reg4_gauss.test(this.serchtext) ||
          reg4_usdg.test(this.serchtext) ||
          reg4_igpc.test(this.serchtext) ||
          reg4_fec.test(this.serchtext) ||
          reg4_themis.test(this.serchtext) ||
          reg4_gpb.test(this.serchtext) ||
          reg4_icplaza.test(this.serchtext)
        ) {
          this.routerSreachFn("/Verifierdetails");
        } else if (
          reg3.test(this.serchtext) ||
          reg3_gauss.test(this.serchtext) ||
          reg3_usdg.test(this.serchtext) ||
          reg3_igpc.test(this.serchtext) ||
          reg3_fec.test(this.serchtext) ||
          reg3_gpb.test(this.serchtext) ||
          reg3_themis.test(this.serchtext) ||
          reg3_icplaza.test(this.serchtext)
        ) {
          this.routerSreachFn("/Accountdetails");
          // this.routerSreachFn("/tokens/addressDetail");
        } else if (evmHashReg.test(this.serchtext)) {
          //evmHash
          this.routerSreachFn("/tokens/transactionDetail"); //地址
        } else if (evmAddressReg.test(this.serchtext)) {
          //evmHash
          this.routerSreachFn("/Accountdetails");
          // this.routerSreachFn("/tokens/addressdetail"); //地址
        } else {
          this.$router.push({
            path: "/pagesno",
            query: { id: this.serchtext },
          });
        }
      }
    },
    windClickGoFn(val) {
      (this.bizhongBool = false), (window.location.href = val);
    },
    routerSreachFn(names, query) {
      this.$router.push({
        path: names + "/" + this.serchtext,
      });
    },
    bizhongBoolFn() {
      this.showPopup = false;
      this.bizhongBool = !this.bizhongBool;
    },
    clickHomeFn() {
      this.$router.push("/index");
    },
    ClickrouterFn(router_name) {
      this.$router.push(router_name);
      (this.showPopup = false),
        (this.Staking_bool = false),
        (this.Govbool = false),
        (this.Statsbool = false),
        (this.bizhongBool = false);
    },
    showPopupFn() {
      this.bizhongBool = false;
      this.showPopup = !this.showPopup;
    },
    Staking_boolFn() {
      this.Staking_bool = !this.Staking_bool;
    },
    Gov_boolFn() {
      this.Govbool = !this.Govbool;
    },
    StatsboolFn() {
      this.Statsbool = !this.Statsbool;
    },
  },
  components: {},
};
</script>

<style scoped lang="less">
@import url("../../css/index");
.container {
  background: @bg;
  color: #fff;
  //   background: #271c60;
}
.top-box {
  position: fixed;
  top: 0px;
  width: 100%;
  z-index: 10;
  display: flex;
  justify-content: space-between;
  height: 25 / @rem;
  line-height: 25 / @rem;
  align-items: center;
  padding: 5 / @rem 0px;
  background: #271c60;

  .left-box {
    margin-left: 10 / @rem;

    span {
      margin-left: 10 / @rem;
      font-size: 14 / @rem;
    }
    .slect-box-bi {
      width: 375 / @rem;
      position: absolute;
      top: 35 / @rem;
      left: 0 / @rem;

      padding: 10 / @rem 0px;
      li {
        height: 40 / @rem;
        display: flex;
        align-items: center;
        background: rgba(18, 9, 63, 0.9);
        img {
          margin-right: 5 / @rem;
          margin-left: 13 / @rem;
        }
      }
    }
  }
  .header-title {
    height: 30 / @rem;
    line-height: 30 / @rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-right: 10 / @rem;
    background: rgba(18, 9, 63, 1);
    &__item {
      padding-left: 20px;
      > * {
        color: #fff;
        text-decoration: none;
      }
    }
  }
  .right-box {
    margin-right: 10 / @rem;
    .right-select {
      position: absolute;
      top: 35 / @rem;
      left: 0px;
      z-index: 10;
      width: 375 / @rem;
      text-indent: 15 / @rem;
      font-size: 14 / @rem;
      ul {
        background: #423390;

        li {
          height: 40 / @rem;
          line-height: 40 / @rem;
        }
      }
    }
  }

  img {
    width: 25 / @rem;
    height: 25 / @rem;
  }
}
.bottom-box {
  padding-top: 40px;
  .srech-box {
    position: relative;
    text-align: center;
    width: 350 / @rem;
    height: 36 / @rem;
    margin: 10 / @rem auto;
    img {
      position: absolute;
      top: 8 / @rem;
      right: 10 / @rem;
      width: 21 / @rem;
      height: 21 / @rem;
    }
    input {
      width: 83%;
      height: 36 / @rem;
      border-radius: 36 / @rem;
      border: 1 / @rem solid #fff;
      text-indent: 10 / @rem;
      background: @bg;
      padding-right: 17%;
    }
  }
  .bottom-text {
    display: flex;
    margin-left: 25 / @rem;
    .left-text {
      margin-right: 10 / @rem;
    }
    div {
      font-size: 10 / @rem;
      margin-top: 5 / @rem;
    }
  }
  padding-bottom: 20 / @rem;
}
.bghidenbox {
  height: 800 / @rem;
  background: rgba(0, 0, 0, 0.4);
}
</style>
