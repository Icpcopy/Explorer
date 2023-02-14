import { time } from "echarts";

export function getUTCTimeFnThisFn(times) {
  if (!times) {
    return;
  }
  var now = new Date(times);
  var year = now.getUTCFullYear(); //取得4位数的年份
  var month = now.getUTCMonth() + 1; //取得日期中的月份，其中0表示1月，11表示12月
  var date = now.getUTCDate(); //返回日期月份中的天数（1到31）
  var hour = now.getUTCHours(); //返回日期中的小时数（0到23）
  var minute = now.getUTCMinutes(); //返回日期中的分钟数（0到59）
  var second = now.getUTCSeconds(); //返回日期中的秒数（0到59）
  if (month < 10) {
    month = "0" + month;
  }
  if (date < 10) {
    date = "0" + date;
  }
  if (hour < 10) {
    hour = "0" + hour;
  }
  if (minute < 10) {
    minute = "0" + minute;
  }
  if (second < 10) {
    second = "0" + second;
  }
  return (
    year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second
  );
}

export function getUTCTimeFn(times) {
  if (!times) {
    return;
  }
  var dangqianTime = null;
  var now = null;
  var u = navigator.userAgent;
  var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
  if (isiOS) {
    dangqianTime = new Date(getUTCTimeFnThisFn(new Date()).replace(/\-/g, "/"));
    now = new Date(getUTCTimeFnThisFn(new Date(times)).replace(/\-/g, "/"));
  } else {
    dangqianTime = new Date();
    now = new Date(times);
  }
  var year = now.getUTCFullYear(); //取得4位数的年份
  var month = now.getUTCMonth() + 1; //取得日期中的月份，其中0表示1月，11表示12月
  var date = now.getUTCDate(); //返回日期月份中的天数（1到31）
  var hour = now.getUTCHours(); //返回日期中的小时数（0到23）
  var minute = now.getUTCMinutes(); //返回日期中的分钟数（0到59）
  var second = now.getUTCSeconds(); //返回日期中的秒数（0到59）
  if (month < 10) {
    month = "0" + month;
  }
  if (date < 10) {
    date = "0" + date;
  }
  if (hour < 10) {
    hour = "0" + hour;
  }
  if (minute < 10) {
    minute = "0" + minute;
  }
  if (second < 10) {
    second = "0" + second;
  }
  return (
    year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second
  );
}

export function TimeremainingFn(times) {
  var dangqianTime = null;
  var nowTime = null;
  var u = navigator.userAgent;
  var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
  if (isiOS) {
    dangqianTime = new Date(
      getUTCTimeFnThisFn(new Date()).replace(/-/g, "/")
    ).getTime();
    nowTime = new Date(
      getUTCTimeFnThisFn(new Date(times)).replace(/-/g, "/")
    ).getTime();
  } else {
    dangqianTime = new Date(getUTCTimeFnThisFn(new Date())).getTime();
    nowTime = new Date(getUTCTimeFnThisFn(times)).getTime();
  }
  var end_Times = dangqianTime - nowTime;
  var date = Math.floor(end_Times / (1000 * 60 * 60 * 24));
  var hours = Math.floor((end_Times / (1000 * 60 * 60)) % 24);
  var minutes = Math.floor((end_Times / (1000 * 60)) % 60);
  var seconds = Math.floor((end_Times / 1000) % 60);
  var milliseconds = Math.floor(end_Times % 1000);
  if (date != 0) {
    return date + "day";
  }
  if (hours != 0) {
    return hours > 0 ? hours + "H" : 0 + "H";
  }
  if (minutes != 0) {
    return minutes > 0 ? minutes + "M" : 0 + "M";
  }
  if (seconds != 0) {
    return seconds > 0 ? seconds + "S" : 0 + "S";
  }
  if (milliseconds != 0) {
    return hours > 0 ? hours + "N" : 0 + "N";
  }
}

export function getDateFn(times) {
  var now = new Date(times);
  var year = now.getFullYear(); //取得4位数的年份
  var month = now.getMonth() + 1; //取得日期中的月份，其中0表示1月，11表示12月
  var date = now.getDate(); //返回日期月份中的天数（1到31）
  var hour = now.getHours(); //返回日期中的小时数（0到23）
  var minute = now.getMinutes(); //返回日期中的分钟数（0到59）
  var second = now.getSeconds(); //返回日期中的秒数（0到59）
  if (month < 10) {
    month = "0" + month;
  }
  if (date < 10) {
    date = "0" + date;
  }

  return year + "-" + month + "-" + date;
}
export function jiequStrFn(str) {
  if (str) {
    var start_str = str.slice(0, 9);
    var end_str = str.substr(str.length - 9, 9);
    return " " + start_str + "......" + end_str;
  } else {
    return "NULL";
  }
}

export function jiequStrgmmFn(str) {
  console.log(str, "str===");
  if (str) {
    if (str.slice(0, 4) == "gamm") {
      var start_str = str.slice(0, 9);
      var end_str = str.substr(str.length - 9, 9);
      return start_str + "......" + end_str;
    } else {
      return str;
    }
  } else {
    return "NULL";
  }
}

export function jiequStrFnMaxlength(str) {
  if (str) {
    if (str.length > 10) {
      var start_str = str.slice(0, 15);
      var end_str = str.substr(str.length - 15, 15);
      return start_str + "......" + end_str;
    } else {
      return str;
    }
  } else {
    return "NULL";
  }
}

export function timestamp(data) {
  var times = new Date(data).getTime();
  return times;
}

export function TimeRemaining(data) {
  var times = (new Date().getTime() - new Date(data).getTime()) / 1000;
  return times;
}
export function userInfoRouterFn(operatorAddress) {
  this.$router.push({
    path: "/Verifierdetails/" + operatorAddress,
  });
}
//区块高度列表
export function blockChainRouterFn() {
  this.$router.push({ path: "/blockchain" });
}
//区块高度详情
export function blockChainInfosRouterFn(name) {
  this.$router.push({ path: "/BlockchainDetails/" + name });
}
//交易hash详情
export function clickHshRouterFn(datas) {
  this.$router.push({ path: "/tradinginfo/" + datas.txhash });
}
export function headerRouterFn(addr, refresh) {
  var reg = /cosmosvaloper[[a-z0-9]+$/;
  var regs = /gaussvaloper[[a-z0-9]+$/;
  var regs_usdg = /usdgvaloper[[a-z0-9]+$/;
  var regs_igpc = /igpcvaloper[[a-z0-9]+$/;
  var regs_fec = /fecvaloper[[a-z0-9]+$/;
  var regs_fec = /fecvaloper[[a-z0-9]+$/;
  var regs_themis = /themisvaloper[[a-z0-9]+$/;
  var pathName = "";
  if (!refresh) {
    sessionStorage.setItem(addr, null);
  }

  if (
    reg.test(addr) ||
    regs.test(addr) ||
    regs_themis.test(addr) ||
    regs_usdg.test(addr) ||
    regs_igpc.test(addr) ||
    regs_fec.test(addr)
  ) {
    pathName = "/Verifierdetails/";
  } else {
    pathName = "/Accountdetails/";
    // pathName = "/tokens/addressDetail/";
  }

  this.$router.push({
    path: pathName + addr,
  });
}
export function copyFn(id) {
  var orderNum = document.getElementById(id).innerText;
  var oInput = document.createElement("input");
  oInput.value = orderNum;
  document.body.appendChild(oInput);
  oInput.select(); // 选择对象
  document.execCommand("Copy"); // 执行浏览器复制命令
  oInput.className = "oInput";
  oInput.style.display = "none";
  this.$toast({
    showClose: true,
    message: "copy success",
    type: "success",
  });
}
export function DetailsproposalRouter(addr) {
  this.$router.push({
    path: "/Detailsproposal/" + addr,
  });
}
export function scrrollBox(callback) {
  var box = document.getElementById("app");
  var This = this;
  function getScrollTop() {
    var scrollTop = 0,
      bodyScrollTop = 0,
      documentScrollTop = 0;
    if (document.body) {
      bodyScrollTop = document.body.scrollTop;
    }
    if (document.documentElement) {
      documentScrollTop = document.documentElement.scrollTop;
    }
    scrollTop =
      bodyScrollTop - documentScrollTop > 0 ? bodyScrollTop : documentScrollTop;
    return scrollTop;
  }

  function getScrollHeight() {
    var ScrollHeight = 0;
    ScrollHeight =
      document.documentElement.scrollHeight || document.body.scrollHeight;
    return ScrollHeight;
  }

  function getWindowHeight() {
    var windowHeight = 0;
    windowHeight = document.documentElement
      ? document.documentElement.clientHeight
      : window.innerHeight;
    return windowHeight;
  }
  window.onscroll = function() {
    if (getScrollTop() + getWindowHeight() >= getScrollHeight() / 2) {
      callback && callback(This);
    } else {
    }
  };
}

export function JiequXiaoshudianFn(str) {
  var str = str + "";
  var from = str.split(".")[1];
  var to = str.split(".")[0];

  if (to) {
    if (to.length > 8) {
      to = to.slice(0, 8);
    }
  }

  if (from) {
    if (from.length > 6) {
      from = from.slice(0, 6);
    }
  }

  // console.log(from);
  if (/[\.]/.test(str) && from == undefined) {
    return to + ".";
  } else if (from) {
    return to + "." + from;
  } else {
    return to;
  }
}
