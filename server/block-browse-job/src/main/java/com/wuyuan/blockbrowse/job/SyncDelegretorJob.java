package com.wuyuan.blockbrowse.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.sevice.ConfigService;
import com.wuyuan.database.sevice.ValidatorDelegatorService;
import com.wuyuan.database.sevice.ValidatorService;
import com.wuyuan.database.util.ConfigUtil;
import io.cosmos.util.CosmosUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class SyncDelegretorJob {
    @Resource
    private ValidatorService validatorService;

    @Resource
    private ValidatorDelegatorService validatorDelegatorService;

    @Resource
    private ConfigService configService;

//    private String chainPrefix;


    @Scheduled(fixedDelay = 600000*6)
    public void getDelegator(){
        log.info("delegretor 启动");
        List<String> validatorsAddress = validatorService.getValidatorAddress();
        for (String operatorAddress:validatorsAddress){
            JSONObject unTx = CosmosUtil.getTotalUnDelegations(configService.getConfig(ConfigUtil.chainUrlKey) + "/"+configService.getChainPrefix(),operatorAddress,100000);
            JSONArray unDelegationResponses = unTx.getJSONArray("unbonding_responses");
            if (unDelegationResponses.size() != unTx.getJSONObject("pagination").getIntValue("total")){
                unDelegationResponses = CosmosUtil.getTotalUnDelegations(configService.getConfig(ConfigUtil.chainUrlKey) + "/"+configService.getChainPrefix(),operatorAddress,unTx.getJSONObject("pagination").getIntValue("total")).getJSONArray("delegation_responses");
            }
            validatorDelegatorService.deleteUnDelegator(operatorAddress);
            for (Object unDelegator:unDelegationResponses){
                if (unDelegator instanceof JSONObject){
                    JSONObject unTransfer = (JSONObject) unDelegator;
                    validatorDelegatorService.saveUnDelegator(unTransfer);
                }
            }
        }
        log.info("delegretort同步完成");
    }

//    public String getChainPrefix(){
//        if (chainPrefix == null){
//            chainPrefix = configService.getConfig(ConfigUtil.chainPrefixKey);
//        }
//        return chainPrefix;
//    }
}
