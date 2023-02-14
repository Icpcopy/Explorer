package com.wuyuan.database.sevice.evmos;

import com.wuyuan.database.util.PageModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;
import java.util.List;

@Service
public class EVMosTransactionService {
    @Autowired
    private MongoTemplate mongoTemplate;
    public static final String COLLECTUIB_NAME = "evm_transaction";

    public void save(Transaction transaction){
        this.mongoTemplate.save(transaction,COLLECTUIB_NAME);
    }
    public Transaction getTransaction(String hash){
        Query query = new Query(Criteria.where("hash").is(hash));
        return this.mongoTemplate.findOne(query,Transaction.class,COLLECTUIB_NAME);
    }
    public PageModel<Transaction> queryTransaction(String hash, String address, Integer isOutputOnly, String blockNumber, int pageIndex, int pageSize){
        Criteria cr = new Criteria();

        if(StringUtils.isNotBlank(address)){
            if(isOutputOnly==null){
                cr.orOperator(Criteria.where("to").is(address.trim().toLowerCase().toLowerCase()),Criteria.where("from").is(address.trim().toLowerCase()));
            }else if(isOutputOnly.equals(0)){
                Criteria criteria =  Criteria.where("from").is(address.trim().toLowerCase());
                cr.andOperator(criteria);
            }else if(isOutputOnly.equals(1)){
                Criteria criteria =  Criteria.where("to").is(address.trim().toLowerCase());
                cr.andOperator(criteria);
            }else {
                cr.orOperator(Criteria.where("to").is(address.trim().toLowerCase().toLowerCase()),Criteria.where("from").is(address.trim().toLowerCase()));
            }
        }
        Query query=new Query(cr);
        if(StringUtils.isNotBlank(hash)){
            query.addCriteria(Criteria.where("hash").is(hash));
        }
        if(StringUtils.isNotBlank(blockNumber)){
            query.addCriteria(Criteria.where("blockNumber").is("0x"+new BigInteger(blockNumber).toString(16)));
        }

        long count=mongoTemplate.count(query,COLLECTUIB_NAME);
        query.skip((pageIndex - 1) * pageSize);
        query.limit(pageSize);
        query.with(Sort.by(
                Sort.Order.desc("_id"))
        );
        List<Transaction>  list=mongoTemplate.find(query, Transaction.class,COLLECTUIB_NAME);
        PageModel<Transaction> pageModel=new PageModel<Transaction>(list,pageIndex,pageSize,count);
        return pageModel;
    }
}
