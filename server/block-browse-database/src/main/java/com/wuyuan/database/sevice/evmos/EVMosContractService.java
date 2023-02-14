package com.wuyuan.database.sevice.evmos;

import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.entity.EVMosContract;
import com.wuyuan.database.util.GetIdUtil;
import com.wuyuan.database.util.PageModel;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;


@Service
public class EVMosContractService {
    @Autowired
    private MongoTemplate mongoTemplate;
    public static final String COLLECTUIB_NAME = "evm_contract";
    public static Map<String, EVMosContract> contractMap=new HashMap<String,EVMosContract>();
    public void save(EVMosContract contract){
        contract.setIds(GetIdUtil.getId());
        this.mongoTemplate.save(contract,COLLECTUIB_NAME);
    }
    public void updateTranctionCount(long id,long transactionCount){
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("transactionCount", transactionCount);
        this.mongoTemplate.updateFirst(query,update,COLLECTUIB_NAME);

    }
    public EVMosContract getEvmosContractByContractAddress(String contractAddress){
        Query query = new Query(Criteria.where("contractAddress").is(contractAddress));
        return this.mongoTemplate.findOne(query,EVMosContract.class,COLLECTUIB_NAME);
    }
    public synchronized void updateStats(long transactionCount,long addressTotal,long id){
        Query query = new Query(Criteria.where("_id").is(id));
        EVMosContract contract = this.mongoTemplate.findById(id, EVMosContract.class, COLLECTUIB_NAME);
        if(contract!=null){
            Update update = new Update();
            update.set("transactiontotalCount", contract.getTransactiontotalCount()+transactionCount);
            update.set("addresstotal",contract.getAddresstotal()+addressTotal);
            this.mongoTemplate.updateFirst(query,update,COLLECTUIB_NAME);
        }
    }

    public synchronized void updateTotalSupply(BigDecimal totalSupply, long id){
        Query query = new Query(Criteria.where("_id").is(id));
        EVMosContract contract = this.mongoTemplate.findById(id, EVMosContract.class, COLLECTUIB_NAME);
        if(contract!=null){
            Update update = new Update();
            update.set("totalSupply",totalSupply.toPlainString());
            this.mongoTemplate.updateFirst(query,update,COLLECTUIB_NAME);
        }
    }
    public synchronized void updateAbi(String abi, long id){
        Query query = new Query(Criteria.where("_id").is(id));
        EVMosContract contract = this.mongoTemplate.findById(id, EVMosContract.class, COLLECTUIB_NAME);
        if(contract!=null){
            Update update = new Update();
            update.set("abi",abi);
            this.mongoTemplate.updateFirst(query,update,COLLECTUIB_NAME);
        }
    }
    public EVMosContract getContract(String contractAddress){
        EVMosContract contract=contractMap.get(contractAddress);
        if(contract==null){
            contract=getEvmosContractByContractAddress(contractAddress);
            if(contract!=null){
                contractMap.put(contractAddress,contract);
            }
        }
        return contract;
    }
    public PageModel<EVMosContract> getContracts(int pageSize,int pageIndex,String param,int contractType ){
        Query query = new Query();
        Criteria criteria = Criteria.where("contractType").is(contractType);
        Document document = Collation.of("zh").toDocument();
        if(StringUtils.isNotBlank(param)){
            String reges=String.format("%s%s%s", "^.*", param, ".*$");//匹配规则
            Pattern pattern=Pattern.compile(reges, Pattern.CASE_INSENSITIVE);//正则匹配规则--pattern的常量
            criteria.orOperator(Criteria.where("contractAddress").regex(pattern),Criteria.where("tokenName").regex(pattern));
        }
        query.addCriteria(criteria);
        document.put("numericOrdering", true);
        long count=mongoTemplate.count(query,COLLECTUIB_NAME);
        query.skip((pageIndex - 1) * pageSize);
        query.limit(pageSize);
        query.collation(Collation.from(document));
        query.with(Sort.by(
                Sort.Order.desc("transactiontotalCount"),Sort.Order.desc("_id"))
        );

        List<EVMosContract> evMosContracts = mongoTemplate.find(query, EVMosContract.class, COLLECTUIB_NAME);
        PageModel<EVMosContract> pageModel = new PageModel<>(evMosContracts, pageIndex, pageSize, count);
        return pageModel;

    }
    public List<EVMosContract> getAll(){
            return this.mongoTemplate.findAll(EVMosContract.class,COLLECTUIB_NAME);
    }
    public List<EVMosContract> getTokensContracts(){
        Query query = new Query(Criteria.where("contractType").is(0));
        return this.mongoTemplate.find(query,EVMosContract.class,COLLECTUIB_NAME);
    }
}
