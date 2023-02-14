package com.wuyuan.blockbrowse.job;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.entity.GlobalBlock;
import com.wuyuan.database.entity.Statistics;
import com.wuyuan.database.sevice.*;
import com.wuyuan.database.util.AmountUtil;
import com.wuyuan.database.util.ConfigUtil;
import io.cosmos.util.CosmosUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableScheduling
public class StatisticsTransation {
    @Resource
    private StatisticsService statisticsService;
    @Resource
    private BlockService blockService;
    @Resource
    private TransactionService transactionService;
    @Resource
    private ValidatorDelegatorService validatorDelegatorService;
    @Scheduled(fixedDelay = 1000L)
    public void statistics() {
        Statistics statistics = statisticsService.getStatistics();
        if (statistics == null) {
            statistics = new Statistics();
            statistics.setBlock(1);
            statistics.setWithdrawCommission(BigDecimal.ZERO);
            statistics.setWithdrawReward(BigDecimal.ZERO);
            statisticsService.save(statistics);
        }
        GlobalBlock global = blockService.getGlobal(null);
        if (statistics.getBlock() >= global.getBlockNum()) {
            return;
        }
        long end = global.getBlockNum();
        if (end > statistics.getBlock() + 1000) {
            end = statistics.getBlock() + 1000;
        }
        for (long i = statistics.getBlock() + 1; i < end; i++) {
            compute(i);
        }


    }
    @Scheduled(fixedDelay = 1000*60)
    public void statisticsUnDelegetor(){
        List<JSONObject> unDelegatorsAll = validatorDelegatorService.getUnDelegatorsAll();
        BigInteger totalUn=BigInteger.ZERO;
        for (JSONObject json:unDelegatorsAll){
            JSONArray entries = json.getJSONArray("entries");
            for (int i = 0; i < entries.size(); i++) {
                totalUn=totalUn.add(entries.getJSONObject(i).getBigInteger("balance"));
            }
        }
        Statistics statistics = statisticsService.getStatistics();
        if (statistics == null) {
           return;
        }
        BigDecimal unTotalBalance=BigDecimal.valueOf(totalUn.doubleValue()).divide(BigDecimal.valueOf(Math.pow(10,ConfigUtil.ChainDecimal)),ConfigUtil.ChainDecimal,BigDecimal.ROUND_HALF_UP);
        statisticsService.updateTotalUnDelegator(unTotalBalance);
        log.info("unTotalBalance:"+unTotalBalance);

    }

    @Transactional(rollbackFor = Exception.class)
    public void compute(long i) {
        computeWithdrawReward(i);
        computeWithdrawCommission(i);
        computeUnDelegatorWithdrawReward(i);
        statisticsService.updateBlock(i);
    }

    private void computeUnDelegatorWithdrawReward(long block) {
        List<JSONObject> transactions = transactionService.getTransactionsByBlock(block, TransactionService.msgUnDelegate);


        List<BigInteger> list = getTotalReword(transactions, "transfer");
        for (BigInteger total : list) {
            if (total == null) {
                continue;
            }
            Statistics statistics = statisticsService.getStatistics();

            BigDecimal reward = new BigDecimal(total).divide(new BigDecimal(Math.pow(10, 18)), 18, BigDecimal.ROUND_HALF_UP);
            statisticsService.updateWithdrawReward(statistics.getWithdrawReward().add(reward));
        }
    }
    private void computeWithdrawReward(long block) {
        List<JSONObject> transactions = transactionService.getTransactionsByBlock(block, TransactionService.msgDelegateReward);


        List<BigInteger> list = getTotalReword(transactions, "withdraw_rewards");
        for (BigInteger total : list) {
            if (total == null) {
                continue;
            }
            Statistics statistics = statisticsService.getStatistics();
            BigDecimal reward = new BigDecimal(total).divide(new BigDecimal(Math.pow(10, 18)), 18, BigDecimal.ROUND_HALF_UP);
            statisticsService.updateWithdrawReward(statistics.getWithdrawReward().add(reward));
        }
    }

    private void computeWithdrawCommission(long block) {
        List<JSONObject> transactions = transactionService.getTransactionsByBlock(block, TransactionService.msgWithdrawCommission);

        List<BigInteger> list = getTotalReword(transactions, "withdraw_commission");
        for (BigInteger total : list) {
            if (total == null||total.compareTo(BigInteger.ZERO)==0) {
                continue;
            }
            Statistics statistics = statisticsService.getStatistics();
            BigDecimal commond = new BigDecimal(total).divide(new BigDecimal(Math.pow(10, 18)), 18, BigDecimal.ROUND_HALF_UP);
            statisticsService.updateWithdrawCommission(statistics.getWithdrawCommission().add(commond));
        }
    }

    @Resource
    private ConfigService configService;
    private BigInteger getUnDelegatorAmount(JSONObject json){
        JSONArray jsonArray = json.getJSONObject("tx").getJSONObject("body").getJSONArray("messages");
        BigInteger totalUnDelegator=BigInteger.ZERO;
        for (int i = 0; i < jsonArray.size() ; i++) {
            JSONObject message=jsonArray.getJSONObject(i);
            if(!TransactionService.msgUnDelegate.equalsIgnoreCase(message.getString("@type"))){
                continue;
            }
            JSONObject amount = message.getJSONObject("amount");
            String coinName = configService.getCoinName();
            if(coinName.equalsIgnoreCase(amount.getString("denom"))){
                totalUnDelegator=totalUnDelegator.add(amount.getBigInteger("amount"));
            }
        }
        return totalUnDelegator;

    }
    private List<BigInteger> getTotalReword(List<JSONObject> transactions, String eventsType) {
        List<BigInteger> collect = transactions.stream().map(jsonObject -> {
            if (jsonObject.getJSONObject("tx_response").getInteger("code") != 0) {
                return null;
            }
            List<BigInteger> totalReward = jsonObject.getJSONObject("tx_response").getJSONArray("logs").stream().map(json -> {


                System.out.println(json);
                List<BigInteger> totalList = JSONObject.parseObject(JSONObject.toJSONString(json)).getJSONArray("events").stream().map(events -> {
                    JSONObject event = JSONObject.parseObject(JSONObject.toJSONString(events));
                    if (eventsType.equalsIgnoreCase(event.getString("type"))) {
                        JSONArray attributes = event.getJSONArray("attributes");
                        BigInteger total = BigInteger.ZERO;
                        for (int i = 0; i < attributes.size(); i++) {
                            JSONObject attribute = attributes.getJSONObject(i);
                            if ("amount".equalsIgnoreCase(attribute.getString("key")) ||
                                    "recommanders_rewards".equalsIgnoreCase(attribute.getString("key"))) {
                                AmountUtil value = AmountUtil.initAmount(attribute.getString("value"), BigDecimal.ONE, 0);
                                total = total.add(value.getAmount().toBigInteger());
                            }
                        }
                        return total;
                    }
                    return null;
                }).collect(Collectors.toList());
                BigInteger total = BigInteger.ZERO;
                for (BigInteger in : totalList) {
                    if (in != null) {
                        total= total.add(in);
                    }
                }
                return total;
            }).collect(Collectors.toList());
            BigInteger total = BigInteger.ZERO;
            for (BigInteger in : totalReward) {
                if (in != null) {
                    total= total.add(in);
                }
            }
            BigInteger unDelegatorAmount = getUnDelegatorAmount(jsonObject);
            return total.subtract(unDelegatorAmount);
        }).collect(Collectors.toList());

        return collect;
    }
//    private List<BigInteger> getTotalReword(List<JSONObject> transactions, String eventsType) {
//        List<BigInteger> collect =new ArrayList<>();
//        for (JSONObject jsonObject:transactions){
//            if (jsonObject.getJSONObject("tx_response").getInteger("code") != 0) {
//                return collect;
//            }
//            BigInteger total = BigInteger.ZERO;
//            for(Object json:jsonObject.getJSONObject("tx_response").getJSONArray("logs")){
//
//                for (Object events:JSONObject.parseObject(JSONObject.toJSONString(json)).getJSONArray("events")){
//                    JSONObject event = JSONObject.parseObject(JSONObject.toJSONString(events));
//                    if (eventsType.equalsIgnoreCase(event.getString("type"))) {
//                        JSONArray attributes = event.getJSONArray("attributes");
//                        for (int i = 0; i < attributes.size(); i++) {
//                            JSONObject attribute = attributes.getJSONObject(i);
//                            if ("amount".equalsIgnoreCase(attribute.getString("key")) ||
//                                    "recommanders_rewards".equalsIgnoreCase(attribute.getString("key"))) {
//                                AmountUtil value = AmountUtil.initAmount(attribute.getString("value"), BigDecimal.ONE, 0);
//                                total = total.add(value.getAmount().toBigInteger());
//                            }
//                        }
//                    }
//                }
//            }
//            BigInteger unDelegatorAmount = getUnDelegatorAmount(jsonObject);
//            total.subtract(unDelegatorAmount);
//            collect.add(total);
//        }
//
//
//        return collect;
//    }
}
