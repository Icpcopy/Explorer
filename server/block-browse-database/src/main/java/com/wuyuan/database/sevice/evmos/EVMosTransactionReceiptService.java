package com.wuyuan.database.sevice.evmos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;


@Service
public class EVMosTransactionReceiptService {

    @Autowired
    private MongoTemplate mongoTemplate;
    public static final String COLLECTUIB_NAME = "evm_transaction_receipt";

    public void save(TransactionReceipt receipt){
        this.mongoTemplate.save(receipt,COLLECTUIB_NAME);
    }
    public TransactionReceipt getTransactionReceipt(String hash){
        Query query = new Query(Criteria.where("transactionHash").is(hash));

        return this.mongoTemplate.findOne(query,TransactionReceipt.class,COLLECTUIB_NAME);
    }
    public List<TransactionReceipt> getTransactionsByBlockNumber(long blockNumber){
        Query query = new Query(Criteria.where("blockNumber").is("0x"+new BigInteger(String.valueOf(blockNumber)).toString(16)));
        return this.mongoTemplate.find(query,TransactionReceipt.class,COLLECTUIB_NAME);

    }
}
