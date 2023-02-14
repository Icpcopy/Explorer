package com.wuyuan.database.sevice;

import com.wuyuan.database.entity.FeeDestructionQuantity;
import com.wuyuan.database.util.Collocation;
import cosmos.Block;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FeeDestructionQuantityService {
    @Resource
    private MongoTemplate mongoTemplate;

    private String collectionName;

    public void saveFeeDestructionQuantity(FeeDestructionQuantity  feeDestructionQuantity){
        mongoTemplate.save(feeDestructionQuantity,getCollectionName());
    }

    public FeeDestructionQuantity getFeeDestructionQuantity() {
        Query query = new Query();
        return mongoTemplate.findOne(query,FeeDestructionQuantity.class,getCollectionName());
    }

    public String getCollectionName(){
        if (collectionName == null){
            collectionName = Collocation.COLLECTION_FEE_DESTRUCTIONQUANTITY;
        }
        return collectionName;
    }
}
