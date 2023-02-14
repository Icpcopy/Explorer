package com.wuyuan.database.sevice.evmos;

import com.mongodb.BasicDBObject;
import com.wuyuan.database.entity.EvmAddress;
import com.wuyuan.database.util.GetIdUtil;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class EVMosAddressService {
    @Autowired
    private MongoTemplate mongoTemplate;
    public static final String COLLECTUIB_NAME = "evm_address";


    public void save(EvmAddress address) {
        address.setId(GetIdUtil.getId());
        this.mongoTemplate.save(address, COLLECTUIB_NAME);
    }
    public EvmAddress getAddressByAddressAndContractAddress(String address,String contractAddress){
        Query query = new Query(Criteria.where("contactAddress").is(contractAddress).andOperator(Criteria.where("address").is(address)));
        return this.mongoTemplate.findOne(query,EvmAddress.class,COLLECTUIB_NAME);
    }

    public void update(EvmAddress address){
        Query query = new Query(Criteria.where("_id").is(address.getId()));
        Update update = new Update();
        update.set("balance",address.getBalance());
        update.set("blockNumber",address.getBlockNumber());
        this.mongoTemplate.updateFirst(query,update,COLLECTUIB_NAME);
    }
    public List<EvmAddress> getAddressList(String address){
        Query query = new Query(Criteria.where("address").is(address));
        return this.mongoTemplate.find(query,EvmAddress.class,COLLECTUIB_NAME);
    }
    public List<EvmAddress> getAddressesByContact(String contact,int pageIndex,int pageSize){
        Query query = new Query(Criteria.where("contactAddress").is(contact));
        Document document = Collation.of("zh").toDocument();
        document.put("numericOrdering", true);
        query.skip((pageIndex - 1) * pageSize);
        query.limit(pageSize);
        query.collation(Collation.from(document));
        query.with(Sort.by(
                Sort.Order.desc("balance"))
        );
        return mongoTemplate.find(query,EvmAddress.class,COLLECTUIB_NAME);
    }
    public long getAddressesCountByContact(String contact){
        Query query = new Query(Criteria.where("contactAddress").is(contact));
        return mongoTemplate.count(query,COLLECTUIB_NAME);
    }
    public BigDecimal getTotalAmountByContact(String contact){
        Criteria criteria = Criteria.where("contactAddress").is(contact);
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.match(criteria));//查询条件
        operations.add(Aggregation.group().sum(ConvertOperators.ToDecimal.toDecimal("$balance")).as("sum"));//字段字符串转换double
        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<BasicDBObject> aggregateResult = mongoTemplate.aggregate(aggregation, COLLECTUIB_NAME, BasicDBObject.class);
        List<BasicDBObject> results = aggregateResult.getMappedResults();
        if (results != null && results.size() > 0) {
            return new BigDecimal(results.get(0).getString("sum"));
        } else {
            return BigDecimal.ZERO;
        }
    }
}
