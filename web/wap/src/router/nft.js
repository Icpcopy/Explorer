const nftRoutes = [
  {
    path: "/",
    redirect: "/nft/nft-list",
    component: () => import("@/views/nft/index.vue"),
    name: "nft",
    meta: { title: "NFT" },
    children: [
      {
        path: "/nft/nft-list",
        component: () => import("@/views/nft/nftList/index.vue"),
        name: "nftList",
        meta: { title: "NFT" },
      },
      {
        path: "/nft/nft-list/detail/:id",
        component: () => import("@/views/nft/nftList/detail/index.vue"),
        name: "nftDeatil",
        meta: { title: "nftDeatil" },
      },
      {
        path: "/nft/nft-stats",
        component: () => import("@/views/nft/nftStats/index.vue"),
        name: "ndtStats",
        meta: { title: "NFT Stats" },
      },
    ],
  },
];

export default nftRoutes;
