import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementPlus from "element-plus";
import * as dayjs from "dayjs";
import utc from "dayjs/plugin/utc";
dayjs.extend(utc);
import "element-plus/lib/theme-chalk/index.css";
import "./css/index.css";
import "./css/elment.css";

import {
  scrollHeightFn,
  getUTCTimeFn,
  jiequStrFnMaxlength,
  JiequXiaoshudianFn,
  jiequStrFnmax,
  userInfoRouterFn,
  clickHshRouterFn,
  headerRouterFn,
  copyFn,
  blockChainInfosRouterFn,
} from "@/utils/public";
// import i18n from './i18n/index.js'
// const app = createApp(App).use(i18n);

const app = createApp(App);

// app.use(i18n);
app.use(ElementPlus);
app.config.globalProperties.$dayjs = dayjs;

app
  .use(store)
  .use(router)
  .mount("#app");
app.config.globalProperties.$userInfoRouterFn = userInfoRouterFn;
app.config.globalProperties.$clickHshRouterFn = clickHshRouterFn;
app.config.globalProperties.$headerRouterFn = headerRouterFn;
app.config.globalProperties.$blockChainInfosRouterFn = blockChainInfosRouterFn;
app.config.globalProperties.$copyFn = copyFn;
app.config.globalProperties.$getUTCTimeFn = getUTCTimeFn;
app.config.globalProperties.$scrollHeightFn = scrollHeightFn;
app.config.globalProperties.$jiequStrFnmax = jiequStrFnmax;
app.config.globalProperties.$JiequXiaoshudianFn = JiequXiaoshudianFn;
app.config.globalProperties.$jiequStrFnMaxlength = jiequStrFnMaxlength;
