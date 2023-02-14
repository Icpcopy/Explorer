package com.wuyuan.database.sevice.evmos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;

@Service
public class EVMosBlockService {
    @Autowired
    private MongoTemplate mongoTemplate;
    public static final String COLLECTUIB_NAME = "evm_block";

    public void save(EthBlock.Block block){
        this.mongoTemplate.save(block,COLLECTUIB_NAME);
    }
    public EthBlock.Block getBlock(BigInteger number){
        Query query = new Query(Criteria.where("number").is("0x"+number.toString(16)));
        return this.mongoTemplate.findOne(query,EthBlock.Block.class,COLLECTUIB_NAME);
    }

}
