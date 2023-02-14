package com.wuyuan.database.sevice.evmos;

import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.entity.EVMNft;
import com.wuyuan.database.util.PageModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class EVMNftService {
    @Autowired
    private MongoTemplate mongoTemplate;
    public static final String COLLECTUIB_NAME = "evm_nft";

    public void save(EVMNft nft){
        this.mongoTemplate.save(nft,COLLECTUIB_NAME);
    }
    public void updateTranctionCount(String id,long transactionCount){
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("transactionCount", transactionCount);
        this.mongoTemplate.updateFirst(query,update,COLLECTUIB_NAME);

    }
    public void updateOwner(String id,String owner){
        Query query = new Query(Criteria.where("hash").is(id));
        Update update = new Update();
        update.set("owner", owner);
        this.mongoTemplate.updateFirst(query,update,COLLECTUIB_NAME);

    }
    public EVMNft getNftByIdAndContract(String  nftId,String contractAddress){
        Query query = new Query(Criteria.where("nftId").is(nftId).andOperator(Criteria.where("contentAddress").is(contractAddress)));
        return this.mongoTemplate.findOne(query,EVMNft.class,COLLECTUIB_NAME);
    }
    public PageModel<EVMNft> getNftByContract(String contractAddress, int pageIndex, int pageSize){
        Query query = new Query(Criteria.where("contentAddress").is(contractAddress));
        long count=this.mongoTemplate.count(query,COLLECTUIB_NAME);
        query.skip((pageIndex - 1) * pageSize);
        query.limit(pageSize);
        query.with(Sort.by(
                Sort.Order.desc("transactionCount"),Sort.Order.desc("_id"))
        );

        List<EVMNft> evmNfts = this.mongoTemplate.find(query, EVMNft.class, COLLECTUIB_NAME);

        return new PageModel<EVMNft>(evmNfts,pageIndex,pageSize,count);
    }
    public long getHolders(String contractAddress){
        List<AggregationOperation> operations = new ArrayList<>();
        //根据用户id in查询
        operations.add(Aggregation.match(Criteria.where("contentAddress").in(contractAddress)));
        //然后分组
        operations.add(Aggregation.group("owner").count().as("count"));
        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<Object> aggregateData = mongoTemplate.aggregate(aggregation, COLLECTUIB_NAME, Object.class);
        List<Object> mappedResults = aggregateData.getMappedResults();
        if(mappedResults!=null){
            return mappedResults.size();
        }
        return 0;
    }
    public List<EVMNft> getNftByOwnerAndContract(String  owner,String contractAddress){
        Criteria criteria = Criteria.where("owner").is(owner);
        if(StringUtils.isNotBlank(contractAddress)){
            criteria.andOperator(Criteria.where("contentAddress").in(contractAddress));
        }
        Query query = new Query(criteria);
        return this.mongoTemplate.find(query,EVMNft.class,COLLECTUIB_NAME);
    }
}
