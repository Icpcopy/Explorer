import { createRouter, createWebHistory } from "vue-router";

import Nft from "@/views/nft/index.vue";
const routes = [
  {
    path: "/",
    redirect: "index",
    component: () => import("@/views/index/index.vue"),
    meta: { title: "入口" },
    children: [
      {
        path: "index",
        component: () => import("@/views/home/index.vue"),
        name: "home",
        meta: { title: "首页" },
      },
      // {
      //   path: "Accountdetails/:address",
      //   component: () => import("@/views/tokens/addressDetail/index.vue"),
      //   name: "addressDetail",
      //   meta: { title: "addressDetail" },
      // },
      {
        path: "tokens",
        component: Nft,
        name: "Tokens",
        meta: { title: "Tokens" },
        redirect: "/tokens/index",
        children: [
          {
            path: "/tokens/index",
            component: () => import("@/views/tokens/list"),
            name: "TokensIndex",
            meta: { title: "TokensIndex" },
          },
          {
            path: "/tokens/txs",
            component: () => import("@/views/tokens/txsList"),
            name: "TokensTxs",
            meta: { title: "TokensTxs" },
          },
          {
            path: "/tokens/repeat-list",
            component: () => import("@/views/tokens/repeatList"),
            name: "TokensRepeatIndex",
            meta: { title: "TokensRepeatIndex" },
          },
          {
            path: "/tokens/repeat-txs",
            component: () => import("@/views/tokens/repeatTxsList"),
            name: "TokensRepeatTxs",
            meta: { title: "TokensRepeatTxs" },
          },
          {
            path: "/tokens/repeat-txs/txs/:id",
            component: () => import("@/views/tokens/repeatTxsList/nftDetail"),
            name: "TokensTxsNftDetail",
            meta: { title: "TokensTxsNftDetail" },
          },
          {
            path: "/tokens/detail/:id",
            component: () => import("@/views/tokens/list/detail"),
            name: "TokensDetail",
            meta: { title: "TokensDetail" },
          },
          {
            path: "/Accountdetails/:id",
            component: () => import("@/views/tokens/list/addressDetail"),
            name: "TokensAddressDetail",
            meta: { title: "TokensAddressDetail" },
          },
          // {
          //   path: "/tokens/addressdetail/:id",
          //   component: () => import("@/views/tokens/list/addressDetail"),
          //   name: "TokensAddressDetail",
          //   meta: { title: "TokensAddressDetail" },
          // },
          {
            path: "/tokens/transactionDetail/:id",
            component: () => import("@/views/tokens/txsList/transactionDetail"),
            name: "TransactionDetail",
            meta: { title: "TransactionDetail" },
          },
        ],
      },
      {
        path: "nft",
        component: Nft,
        name: "NFT",
        meta: { title: "NFT" },
        redirect: "/nft/index",
        children: [
          {
            path: "/nft/index",
            component: () => import("@/views/nft/nftIndex"),
            name: "NftIndex",
            meta: { title: "NftIndex" },
          },
          {
            path: "/nft/stats",
            component: () => import("@/views/nft/nftStats"),
            name: "NftStats",
            meta: { title: "NftStats" },
          },
          {
            path: "/nft/create-detail/:id",
            component: () => import("@/views/nft/detail/nftCreateDetail"),
            name: "NftCreateDetail",
            meta: { title: "NftCreateDetail" },
          },
          {
            path: "/nft/detail/:id",
            component: () => import("@/views/nft/detail/nftDetail"),
            name: "NftDetail",
            meta: { title: "NftDetail" },
          },
          {
            path: "/nft/transaction-detail/:id",
            component: () => import("@/views/nft/detail/transaction"),
            name: "NftDetailTransaction",
            meta: { title: "NftDetailTransaction" },
          },
        ],
      },
      {
        path: "srechpages",
        component: () => import("@/views/srechpages/index.vue"),
        name: "srechpages",
        meta: { title: "搜索组件页" },
      },

      {
        path: "blockchain",
        component: () => import("@/views/blockchain/index.vue"),
        name: "blockchain",
        meta: { title: "区块链" },
      },
      {
        path: "blockchainInfo/:name",
        component: () => import("@/views/blockchainInfo/index.vue"),
        name: "blockchainInfo",
        meta: { title: "区块链详情" },
      },
      {
        path: "deal",
        component: () => import("@/views/deal/index.vue"),
        name: "deal",
        meta: { title: "交易" },
      },
      {
        path: "dealInfo/:name",
        component: () => import("@/views/dealInfo/index.vue"),
        name: "dealInfo",
        meta: { title: "交易详情" },
      },

      {
        path: "identifier",
        component: () => import("@/views/identifier/index.vue"),
        name: "identifier",
        meta: { title: "验证人" },
      },
      {
        path: "Validatorinfo/:name",
        component: () => import("@/views/Validatorinfo/index.vue"),
        name: "Validatorinfo",
        meta: { title: "验证人详情" },
      },
      {
        path: "Sendtherecord",
        component: () => import("@/views/Sendtherecord/index.vue"),
        name: "Sendtherecord",
        meta: { title: "验证人委托人发送记录" },
      },
      {
        path: "Verifierverification",
        component: () => import("@/views/Verifierverification/index.vue"),
        name: "Verifierverification",
        meta: { title: "验证人验证人发送记录" },
      },
      {
        path: "Governanceproposals",
        component: () => import("@/views/Governanceproposals/index.vue"),
        name: "Governanceproposals",
        meta: { title: "治理提案" },
      },
      {
        path: "votegovernance",
        component: () => import("@/views/votegovernance/index.vue"),
        name: "votegovernance",
        meta: { title: "治理提案-投票" },
      },
      {
        path: "Proposedtransaction",
        component: () => import("@/views/Proposedtransaction/index.vue"),
        name: "Proposedtransaction",
        meta: { title: "提案交易" },
      },
      {
        path: "statistics",
        component: () => import("@/views/statistics/index.vue"),
        name: "statistics",
        meta: { title: "统计" },
      },

      {
        path: "statistical",
        component: () => import("@/views/gaussstatistical/index.vue"),
        name: "statistical",
        meta: { title: "ICT统计" },
      },
      {
        path: "Accountbalance/:name",
        component: () => import("@/views/Accountbalance/index.vue"),
        name: "Accountbalance",
        meta: { title: "用户详情-资产" },
      },
      {
        path: "Issuanceassets",
        component: () => import("@/views/Issuanceassets/index.vue"),
        name: "Issuanceassets",
        meta: { title: "用户详情-发行资产" },
      },
    ],
  },
  {
    path: "/pagesno",
    component: () => import("@/views/pagesundfind/pagesno.vue"),
    name: "pagesno",
    meta: { title: "暂无数据" },
  },
  {
    path: "/pageswh",
    component: () => import("@/views/pagesundfind/pageswh.vue"),
    name: "pageswh",
    meta: { title: "系统正在维护" },
  },
  {
    path: "/pages404",
    component: () => import("@/views/pagesundfind/pages404.vue"),
    name: "pages404",
    meta: { title: "404" },
  },
  {
    path: "/pagesnodata",
    component: () => import("@/views/pagesundfind/pagesnodatae.vue"),
    name: "pagesnodatae",
    meta: { title: "404" },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  mode: "hash",
  routes,
});

export default router;
