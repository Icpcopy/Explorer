package com.wuyuan.database.sevice;

import com.wuyuan.database.entity.NFTDealAmount;
import com.wuyuan.database.util.Collocation;
import com.wuyuan.database.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@Service
@Slf4j
public class NFTDealAmountService {

    @Resource
    private MongoTemplate mongoTemplate;

    public void save(NFTDealAmount nftDealAmount) {
        mongoTemplate.save(nftDealAmount,getCollectionName());
    }

    public boolean isExist(String date) {
        Query query = new Query(Criteria.where("date").is(date));
        return mongoTemplate.exists(query,getCollectionName());
    }

    public NFTDealAmount getNFTDealAmount(String date) {
        Query query = new Query(Criteria.where("date").is(date));
        return mongoTemplate.findOne(query,NFTDealAmount.class,getCollectionName());
    }

    public List<NFTDealAmount> getDealList(String beginDate,String endDate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        try {
            long beginTime = DateFormatUtil.timeToSecond(beginDate);
            Criteria begin = Criteria.where("timestamp").gte(beginTime);
            if (StringUtils.isNotBlank(endDate)) {
                long endTime = DateFormatUtil.timeToSecond(endDate)+24*60*60*1000;
                Criteria end = Criteria.where("timestamp").lte(endTime);
                if (endTime < beginTime) {
                    begin = Criteria.where("timestamp").gte(endTime);
                    end = Criteria.where("timestamp").lte(beginTime);
                }
                criteria.andOperator(begin,end);
            }else {
                criteria.andOperator(begin);
            }
            query.addCriteria(criteria);
            return mongoTemplate.find(query,NFTDealAmount.class,getCollectionName());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCollectionName() {
        return Collocation.COLLECTION_NFT_DEAL_AMOUNT;
    }
}
