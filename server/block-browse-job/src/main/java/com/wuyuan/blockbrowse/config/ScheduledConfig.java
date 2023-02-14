package com.wuyuan.blockbrowse.config;

import com.wuyuan.database.entity.EVMosContract;
import com.wuyuan.database.sevice.ConfigService;
import com.wuyuan.database.sevice.evmos.EVMosContractService;
import com.wuyuan.database.sevice.evmos.EVMosEventsService;
import com.wuyuan.database.sevice.evmos.EVMosService;
import com.wuyuan.database.util.AbiUtil;
import com.wuyuan.database.util.ContractTypeEnum;
import com.wuyuan.database.util.ERC20Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class ScheduledConfig {
    @Autowired
    private ConfigService configService;
    @Autowired
    private EVMosEventsService evMosEventsService;
    @Autowired
    private EVMosContractService evMosContractService;
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        return taskScheduler;
    }
    @PostConstruct
    public void setConfig(){
        AbiUtil.analysisAbi(ERC20Util.ERC20_ABI,evMosEventsService, ContractTypeEnum.erc20.getName());
        AbiUtil.analysisAbi(ERC20Util.ERC721_ABI,evMosEventsService,ContractTypeEnum.erc721.getName());
        List<EVMosContract> all = evMosContractService.getAll();
        for (int i = 0; i < all.size(); i++) {
            if(StringUtils.isBlank(all.get(i).getAbi())){
                continue;
            }
            AbiUtil.analysisAbi(all.get(i).getAbi(),evMosEventsService,"other");
        }
    }
}
