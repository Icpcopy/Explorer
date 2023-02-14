const port = process.env.port || process.env.npm_config_port || 9527; // dev port

const path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir);
}

module.exports = {
  lintOnSave: false,
  publicPath: "/",
  outputDir: "dist",
  assetsDir: "static",
  lintOnSave: process.env.NODE_ENV === "development",
  productionSourceMap: false,

  devServer: {
    port: port,
    open: true,
    overlay: {
      warnings: false,
      errors: true,
    },
    proxy: {
      ["/api/token"]: {
        target: `http://192.168.3.25:31059`, //"http://192.168.1.38:8080", // //`http://192.168.2.13:7002`,// `http://192.168.2.13:80`,  "https://browsemainnet.ic-plaza "https://browsemainnet.ic-plaza.org/", //.org/", //
        changeOrigin: true,
        pathRewrite: {
          ["^" + "/api/token"]: "",
        },
      },
      ["/api/api"]: {
        target: `http://192.168.2.13:7002`,
        changeOrigin: true,
        pathRewrite: {
          ["^" + "/api/api"]: "/api",
        },
      },
    },
  },

  configureWebpack: {
    resolve: {
      alias: {
        "@": resolve("src"),
      },
    },
  },

  pluginOptions: {
    i18n: {
      locale: "en",
      fallbackLocale: "en",
      localeDir: "locales",
      enableLegacy: false,
      runtimeOnly: false,
      compositionOnly: false,
      fullInstall: true,
    },
  },
};
