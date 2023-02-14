import { createApp } from "vue";
import App from "./App.vue";
import store from "./store";
import "./css/public.css";
import "./css/index.css";
import "./css/vant.css";
import "vant/lib/index.css";
import router from "./router";
import * as dayjs from "dayjs";
import utc from "dayjs/plugin/utc";
dayjs.extend(utc);
import {
  jiequStrFnMaxlength,
  timestamp,
  TimeremainingFn,
  jiequStrgmmFn,
  JiequXiaoshudianFn,
  blockChainInfosRouterFn,
  getUTCTimeFn,
  jiequStrFn,
  headerRouterFn,
  DetailsproposalRouter,
  userInfoRouterFn,
  clickHshRouterFn,
  copyFn,
  scrrollBox,
} from "@/utils/public";
import {
  Icon,
  Search,
  Col,
  Row,
  Loading,
  Tab,
  Tabs,
  Form,
  Field,
  Picker,
  Popup,
  Cell,
  Calendar,
  Button,
  Progress,
  ActionSheet,
  Toast,
  Divider ,
  RadioGroup, Radio
} from "vant";
const app = createApp(App);
app
  .use(router)
  .use(store)
  .use(Loading)
  .use(Icon)
  .use(Col)
  .use(Row)
  .use(Form)
  .use(Calendar)
  .use(Button)
  .use(Toast)
  .use(Progress)
  .use(ActionSheet)
  .use(Picker)
  .use(Cell)
  .use(Popup)
  .use(Field)
  .use(Tab)
  .use(Tabs)
  .use(Search)
  .use(Divider)
  .use(Radio)
  .use(RadioGroup)
  .mount("#app");
app.config.globalProperties.$TimeremainingFn = TimeremainingFn;
app.config.globalProperties.$blockChainInfosRouterFn = blockChainInfosRouterFn;
app.config.globalProperties.$getUTCTimeFn = getUTCTimeFn;
app.config.globalProperties.$jiequStrFn = jiequStrFn;
app.config.globalProperties.$headerRouterFn = headerRouterFn;
app.config.globalProperties.$DetailsproposalRouter = DetailsproposalRouter;
app.config.globalProperties.$userInfoRouterFn = userInfoRouterFn;
app.config.globalProperties.$timestamp = timestamp;
app.config.globalProperties.$clickHshRouterFn = clickHshRouterFn;
app.config.globalProperties.$copyFn = copyFn;
app.config.globalProperties.$scrrollBox = scrrollBox;
app.config.globalProperties.$jiequStrFnMaxlength = jiequStrFnMaxlength;
app.config.globalProperties.$JiequXiaoshudianFn = JiequXiaoshudianFn;
app.config.globalProperties.$jiequStrgmmFn = jiequStrgmmFn;
app.config.globalProperties.$dayjs = dayjs;
