package com.wuyuan.database.sevice;

import com.wuyuan.database.entity.Statistics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class StatisticsService {
    @Resource
    private MongoTemplate mongoTemplate;


    public void save(Statistics statistics) {
        mongoTemplate.save(statistics);
    }

    public Statistics getStatistics() {
        return mongoTemplate.findOne(new Query(), Statistics.class);
    }

    public void updateBlock(long block) {
        //withdrawReward
        Query query = new Query();
        Update update = new Update();
        update.set("block", block);
        mongoTemplate.updateFirst(query,update,Statistics.class);
    }
    public void updateTotalUnDelegator(BigDecimal totalUnDelegator) {
        //withdrawReward
        Query query = new Query();
        Update update = new Update();
        update.set("totalUnDelegator", totalUnDelegator);
        mongoTemplate.updateFirst(query,update,Statistics.class);
    }
    public void updateWithdrawReward(BigDecimal withdrawReward) {
        //withdrawReward
        Query query = new Query();
        Update update = new Update();
        update.set("withdrawReward", withdrawReward);
        mongoTemplate.updateFirst(query,update,Statistics.class);
    }
    public void updateWithdrawCommission(BigDecimal withdrawCommission) {
        //withdrawReward
        Query query = new Query();
        Update update = new Update();
        update.set("withdrawCommission", withdrawCommission);
        mongoTemplate.updateFirst(query,update,Statistics.class);
    }
}
