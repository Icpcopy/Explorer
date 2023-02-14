import { createRouter, createWebHistory } from "vue-router";
import nftRoutes from "./nft";
import tokensRoutes from "./tokens";
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
      {
        path: "deal",
        component: () => import("@/views/deal/index.vue"),
        name: "deal",
        meta: { title: "交易" },
      },
      {
        path: "blockchain",
        component: () => import("@/views/blockchain/index.vue"),
        name: "blockchain",
        meta: { title: "区块链" },
      },
      {
        path: "BlockchainDetails/:id",
        component: () => import("@/views/BlockchainDetails/index.vue"),
        name: "BlockchainDetails",
        meta: { title: "区块链详情" },
      },
      {
        path: "identifier",
        component: () => import("@/views/identifier/index.vue"),
        name: "identifier",
        meta: { title: "验证人" },
      },
      {
        path: "entrustrecord",
        component: () => import("@/views/entrustrecord/index.vue"),
        name: "entrustrecord",
        meta: { title: "委托人发送记录" },
      },
      {
        path: "Verifierrecord",
        component: () => import("@/views/Verifierrecord/index.vue"),
        name: "Verifierrecord",
        meta: { title: "验证人发送记录" },
      },
      {
        path: "Governanceproposals",
        component: () => import("@/views/Governanceproposals/index.vue"),
        name: "Governanceproposals",
        meta: { title: "治理提案" },
      },
      {
        path: "Proposedtransaction",
        component: () => import("@/views/Proposedtransaction/index.vue"),
        name: "Proposedtransaction",
        meta: { title: "提案交易" },
      },

      {
        path: "Detailsproposal/:id",
        component: () => import("@/views/Detailsproposal/index.vue"),
        name: "Detailsproposal",
        meta: { title: "提案详情" },
      },
      {
        path: "statistics",
        component: () => import("@/views/statistics/index.vue"),
        name: "statistics",
        meta: { title: "统计" },
      },
      {
        path: "statisticaldetails",
        component: () => import("@/views/statisticaldetails/index.vue"),
        name: "statisticaldetails",
        meta: { title: "统计详情" },
      },
      // {
      //   path: "Accountdetails/:id",
      //   component: () => import("@/views/Accountdetails/index.vue"),
      //   name: "Accountdetails",
      //   meta: { title: "账户详情" },
      // },
      {
        path: "Accountdetails/:address",
        component: () => import("@/views/tokens/addressDetail/index.vue"),
        name: "addressDetail",
        meta: { title: "addressDetail" },
      },
      {
        path: "Verifierdetails/:id",
        component: () => import("@/views/Verifierdetails/index.vue"),
        name: "Verifierdetails",
        meta: { title: "验证人详情" },
      },
      {
        path: "tradinginfo/:id",
        component: () => import("@/views/tradinginfo/index.vue"),
        name: "tradinginfo",
        meta: { title: "交易详情" },
      },
    ],
  },
  {
    path: "/pagesno",
    component: () => import("@/views/pagesno/nosrch.vue"),
    meta: { title: "入口" },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  mode: "hash",
  routes: [...routes, ...nftRoutes, ...tokensRoutes],
});

export default router;
