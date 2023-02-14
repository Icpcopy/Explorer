package com.wuyuan.database.sevice.evmos;

import com.wuyuan.database.entity.EVMosContract;
import com.wuyuan.database.entity.EVMosContranctStats;
import com.wuyuan.database.util.GetIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class EVMosContranctStatsService {
    @Autowired
    private MongoTemplate mongoTemplate;
    public static final String COLLECTUIB_NAME = "evm_contract_stats";

    public void save(EVMosContranctStats stats) {
        stats.setId(GetIdUtil.getId());
        this.mongoTemplate.save(stats, COLLECTUIB_NAME);
    }
    public void update(Long id, int transactionCount, BigDecimal amount, long endBlockNumber) {
        Query query = new Query(Criteria.where("_id").is(id));
        EVMosContranctStats stats = this.mongoTemplate.findById(id, EVMosContranctStats.class, COLLECTUIB_NAME);
        Update update = new Update();
        update.set("transactionCount", stats.getTransactionCount()+transactionCount);
        update.set("amountTotal", new BigDecimal(String.valueOf(stats.getAmountTotal())).add(amount).doubleValue());
        update.set("endBlockNumber", endBlockNumber);
        this.mongoTemplate.updateFirst(query, update, COLLECTUIB_NAME);

    }
    public EVMosContranctStats getByDayAncContractAddress(String day,String contractAddress){
        Query query = new Query(Criteria.where("day").is(day).andOperator(Criteria.where("contactAddress").is(contractAddress)));
        return mongoTemplate.findOne(query,EVMosContranctStats.class,COLLECTUIB_NAME);
    }

    public List<EVMosContranctStats> getByContractAddress(int count, String contractAddress){
        Query query = new Query(Criteria.where("contactAddress").is(contractAddress));
        query.limit(count);
        query.with(Sort.by(
                Sort.Order.desc("_id"))
        );
        return mongoTemplate.find(query,EVMosContranctStats.class,COLLECTUIB_NAME);
    }
}
