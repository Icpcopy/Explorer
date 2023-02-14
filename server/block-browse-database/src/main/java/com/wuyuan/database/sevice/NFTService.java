package com.wuyuan.database.sevice;

import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.entity.NFT;
import com.wuyuan.database.entity.NFTStats;
import com.wuyuan.database.util.Collocation;
import com.wuyuan.database.util.PageModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
@Slf4j
public class NFTService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private NFTStatsService nftStatsService;


    public void saveNft(NFT nft){
        mongoTemplate.save(nft,getCollectionName());
    }

//    public void UpdateNft(NFT nft) {
//        Query query = new Query(Criteria.where("symbol").is(symbol));
//        JSONObject tokens = mongoTemplate.findOne(query,JSONObject.class,getCollectionName());
//        if (tokens != null){
//            query = new Query(Criteria.where("_id").is(tokens.getString("_id")));
//            Update update = new Update();
//            update.set("mintable",token.getBoolean("mintable"));
//            update.set("owner",token.getString("owner"));
//            update.set("initial_supply",token.getString("initial_supply"));
//            return mongoTemplate.updateFirst(query,update,getCollectionName());
//        }
//        return null;
//    }

    public NFT getNFT(String  tokenId) {
        Query query = new Query(Criteria.where("tokenId").is(tokenId));
        return mongoTemplate.findOne(query,NFT.class,getCollectionName());
    }

    public long getNFTCount() {
        return mongoTemplate.getCollection(getCollectionName()).estimatedDocumentCount();
    }

    public PageModel<NFT> getNFTList(Integer pageSize,Integer pageIndex,String address) {
        Query query = new Query();
        long count = 0;
        if (StringUtils.isNotBlank(address)) {
            query.addCriteria(Criteria.where("owner").is(address));
            count = mongoTemplate.count(query,getCollectionName());
        }else {
            count = getNFTCount();
        }
        query.limit(pageSize);
        query.skip((pageIndex - 1) * pageSize);
        query.with(Sort.by(
                Sort.Order.desc("timestamp"))
        );
        List<NFT> nftList = mongoTemplate.find(query,NFT.class,getCollectionName());
        PageModel<NFT> pageModel =new PageModel<>(nftList,pageIndex,pageSize,count);
        return pageModel;
    }

    public boolean isExist(String tokenId) {
        Query query = new Query(Criteria.where("tokenId").is(tokenId));
        return mongoTemplate.exists(query,getCollectionName());
    }

    public boolean isExistByAddress(String address) {
        Query query = new Query(Criteria.where("owner").is(address));
        return mongoTemplate.exists(query,getCollectionName());
    }

    public void getNftStats(String address) {
        //判定nft列表中是否有这个地址
        if (!isExistByAddress(address)) {
            //判定nftstats列表中是否有这个地址
            if (nftStatsService.isExists(address)) {
                nftStatsService.deleteStats(address);
            }
            return;
        }
        Aggregation ownerAgg = newAggregation(
                Aggregation.match(
                        Criteria.where("owner").is(address)
                ),
                Aggregation.group("cateId").count().as("nftNum").
                        sum(ConvertOperators.ToDecimal.toDecimal("$nowValue")).
                        as("num")
        );
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(ownerAgg,getCollectionName(),JSONObject.class);

        NFTStats nftStats = null;

        if (nftStatsService.isExists(address)) {
            nftStats = nftStatsService.getStats(address);
        }else {
            nftStats = new NFTStats();
            nftStats.setAddress(address);
        }

        AtomicReference<BigDecimal> ticketing = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> lightLuxury = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> art = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> collection = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> all = new AtomicReference<>(BigDecimal.ZERO);

        AtomicReference<BigDecimal> ticketingNum = new AtomicReference<>(BigDecimal.ZERO);;
        AtomicReference<BigDecimal> lightLuxuryNum = new AtomicReference<>(BigDecimal.ZERO);;
        AtomicReference<BigDecimal> artNum = new AtomicReference<>(BigDecimal.ZERO);;
        AtomicReference<BigDecimal> collectionNum = new AtomicReference<>(BigDecimal.ZERO);;
        AtomicReference<BigDecimal> allNum = new AtomicReference<>(BigDecimal.ZERO);;

        if (results.getMappedResults() != null && results.getMappedResults().size() != 0){
            List<JSONObject> list = results.getMappedResults();
            list.stream().forEach(nft -> {
                String cateId = nft.getString("_id");
                switch (cateId) {
                    case "1":
                    case "001":
                        BigDecimal amount = nft.getBigDecimal("num");
                        BigDecimal num = nft.getBigDecimal("nftNum");
                        ticketing.set(ticketing.get().add(amount));
                        all.set(all.get().add(amount));
                        ticketingNum.set(ticketingNum.get().add(num));
                        allNum.set(allNum.get().add(num));
                        break;
                    case "2":
                    case "002":
                        amount = nft.getBigDecimal("num");
                        num = nft.getBigDecimal("nftNum");
                        collection.set(collection.get().add(amount));
                        all.set(all.get().add(amount));
                        collectionNum.set(collectionNum.get().add(num));
                        allNum.set(allNum.get().add(num));
                        break;
                    case "3":
                    case "003":
                        amount = nft.getBigDecimal("num");
                        num = nft.getBigDecimal("nftNum");
                        art.set(art.get().add(amount));
                        all.set(all.get().add(amount));
                        artNum.set(artNum.get().add(num));
                        allNum.set(allNum.get().add(num));
                        break;
                    case "4":
                    case "004":
                        amount = nft.getBigDecimal("num");
                        num = nft.getBigDecimal("nftNum");
                        lightLuxury.set(lightLuxury.get().add(amount));
                        all.set(all.get().add(amount));
                        lightLuxuryNum.set(lightLuxuryNum.get().add(num));
                        allNum.set(allNum.get().add(num));
                        break;
                    default:
                        log.info("未知的cateId: " + cateId);
                }
            });
        }
        nftStats.setAllQuantity(all.get().doubleValue());
        nftStats.setArtQuantity(art.get().doubleValue());
        nftStats.setCollectionQuantity(collection.get().doubleValue());
        nftStats.setLigthLuxuryQuantity(lightLuxury.get().doubleValue());
        nftStats.setTicketingQuantity(ticketing.get().doubleValue());

        nftStats.setAllQuantityNum(allNum.get().longValue());
        nftStats.setArtQuantityNum(artNum.get().longValue());
        nftStats.setTicketingQuantityNum(ticketingNum.get().longValue());
        nftStats.setCollectionQuantityNum(collectionNum.get().longValue());
        nftStats.setLigthLuxuryQuantityNum(lightLuxuryNum.get().longValue());

        nftStatsService.saveStats(nftStats);
    }

    public String getCollectionName(){
        return Collocation.COLLECTION_NFT;
    }
}
