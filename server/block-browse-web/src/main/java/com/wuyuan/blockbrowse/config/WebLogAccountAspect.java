package com.wuyuan.blockbrowse.config;

import cn.hutool.http.useragent.UserAgent;
import com.alibaba.fastjson.JSONObject;
import com.wuyuan.blockbrowse.utils.ApiResult;
import com.wuyuan.database.sevice.ConfigService;
import com.wuyuan.database.util.PageModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author admin
 */
@Aspect
@Component
public class WebLogAccountAspect {

    @Resource
    private ConfigService configService;
    @Pointcut("execution(public * com.wuyuan.blockbrowse.controller..*.*(..))")//切入点描述 这个是controller包的切入点
    public void controllerLog() {
    }//签名，可以理解成这个切入点的一个名称


    @Around("controllerLog()") //在切入点的方法run之前要干的
    public Object logBeforeController(ProceedingJoinPoint joinPoint) {
        Object proceed=null;
        try {
             proceed = joinPoint.proceed();
             if(proceed instanceof ApiResult){
                 ApiResult result = (ApiResult) proceed;
                 if(result.getResult() instanceof PageModel){
                     ((PageModel)result.getResult()).setCoinName(configService.getCoinName1());
                     ((PageModel)result.getResult()).setChainName(configService.getChainName());
                 }


             }
            //如果需要重新设置参数，则调用proceed的有参方法，否则直接调用无参方法即可
        } catch (Throwable throwable) {
            System.out.println("异常");
            throwable.printStackTrace();
            return  proceed;
        }
        return proceed;
    }
}