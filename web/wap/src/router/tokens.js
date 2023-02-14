const tokensRoutes = [
  {
    path: "/",
    redirect: "/tokens/erc20-list",
    component: () => import("@/views/tokens/index.vue"),
    name: "tokens",
    meta: { title: "Tokens" },
    children: [
      {
        path: "/tokens/erc20-list",
        component: () => import("@/views/tokens/erc20List/index.vue"),
        name: "tokensErc20List",
        meta: { title: "tokensErc20List" },
      },
      {
        path: "/tokens/erc20-txs",
        component: () => import("@/views/tokens/erc20Txs/index.vue"),
        name: "tokensErc20Txs",
        meta: { title: "tokensErc20Txs" },
      },
      {
        path: "/tokens/erc721-list",
        component: () => import("@/views/tokens/erc721List/index.vue"),
        name: "tokensErc721List",
        meta: { title: "tokensErc721List" },
      },
      {
        path: "/tokens/erc721-txs",
        component: () => import("@/views/tokens/erc721Txs/index.vue"),
        name: "tokensErc721Txs",
        meta: { title: "tokensErc721Txs" },
      },
      {
        path: "/tokens/detail/:id",
        component: () => import("@/views/tokens/detail/index.vue"),
        name: "tokensDeatil",
        meta: { title: "tokensDeatil" },
      },
      {
        path: "/tokens/transactionDetail/:hash",
        component: () => import("@/views/tokens/transactionDetail/index.vue"),
        name: "transactionDetail",
        meta: { title: "transactionDetail" },
      },
      // {
      //   path: "/tokens/addressDetail/:address",
      //   component: () => import("@/views/tokens/addressDetail/index.vue"),
      //   name: "addressDetail",
      //   meta: { title: "addressDetail" },
      // },
      {
        path: "/tokens/nft-detail/:id",
        component: () => import("@/views/tokens/nftDetail/index.vue"),
        name: "tokensNftDetail",
        meta: { title: "tokensNftDetail" },
      },
    ],
  },
];

export default tokensRoutes;
