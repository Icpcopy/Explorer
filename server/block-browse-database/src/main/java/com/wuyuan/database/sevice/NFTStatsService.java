package com.wuyuan.database.sevice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.entity.NFTDealAmount;
import com.wuyuan.database.entity.NFTStats;
import com.wuyuan.database.util.Collocation;
import com.wuyuan.database.util.PageModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NFTStatsService {
    @Resource
    private MongoTemplate mongoTemplate;

    public void saveStats(NFTStats nftStats) {
        mongoTemplate.save(nftStats,getCollectionName());
    }

    public boolean isExists(String address) {
        Query query = new Query(Criteria.where("address").is(address));
        return mongoTemplate.exists(query,getCollectionName());
    }

    public NFTStats getStats(String address) {
        Query query = new Query(Criteria.where("address").is(address));
        return mongoTemplate.findOne(query,NFTStats.class,getCollectionName());
    }

    public void  deleteStats(String address) {
        Query query = new Query(Criteria.where("address").is(address));
        mongoTemplate.remove(query,getCollectionName());
    }

    public long getCount(){
        return mongoTemplate.getCollection(getCollectionName()).estimatedDocumentCount();
    }

    public String getCollectionName() {
        return Collocation.COLLECTION_NFT_STATS;
    }

    public PageModel<JSONObject> getNFTStatsList(Integer pageIndex,Integer pageSize,String cateId,Integer isNumSort,Integer isSort) {
        Query query = new Query();
        query.limit(pageSize);
        query.skip((pageIndex - 1) * pageSize);
        long count = mongoTemplate.getCollection(getCollectionName()).estimatedDocumentCount();
        String sortString = null;
        String category = null;
        if (StringUtils.isNotBlank(cateId)){
            switch (cateId) {
                case "001":
                case "1":
                    category = "Virtual merchandise";
                    if (isNumSort == null || isNumSort == 0) {
                        sortString = "TicketingQuantity";
                    }else {
                        sortString = "TicketingQuantityNum";
                    }

                    break;
                case "002":
                case "2":
                    category = "Metaverse";
                    if (isNumSort == null || isNumSort == 0) {
                        sortString = "CollectionQuantity";
                    }else {
                        sortString = "CollectionQuantityNum";
                    }
                    break;
                case "003":
                case "3":
                    category = "Game";
                    if (isNumSort == null || isNumSort == 0) {
                        sortString = "ArtQuantity";
                    }else {
                        sortString = "ArtQuantityNum";
                    }

                    break;
                case "004":
                case "4":
                    category = "Blind box";
                    if (isNumSort == null || isNumSort == 0) {
                        sortString = "LigthLuxuryQuantity";
                    }else {
                        sortString = "LigthLuxuryQuantityNum";
                    }
                    break;

                default:
                    return null;
            }
        } else {
            category = "All";
            if (isNumSort == null || isNumSort == 0) {
                sortString = "AllQuantity";
            }else {
                sortString = "AllQuantityNum";
            }
        }
        if (isSort == null || isSort == 0) {
            query.with(Sort.by(
                    Sort.Order.desc(sortString))
            );
        }else if (isSort == 1){
            query.with(Sort.by(
                    Sort.Order.asc(sortString))
            );
        }
        List<NFTStats> nftDealAmountList = mongoTemplate.find(query,NFTStats.class,getCollectionName());
        String finalSortString = sortString;
        String finalCategory = category;
        List<JSONObject> nftStatsList = nftDealAmountList.stream().map(nftStats -> {
            return getStats(nftStats, finalSortString, finalCategory);
        }).collect(Collectors.toList());
        PageModel<JSONObject> pageModel = new PageModel<>(nftStatsList,pageIndex,pageSize,count);
        return pageModel;
    }

    public JSONObject getStats(NFTStats nftStats,String sortString,String category) {
        JSONObject nft = JSON.parseObject(JSONObject.toJSONString(nftStats));
        JSONObject stats = new JSONObject();
        String qulity = null;
        if (sortString.endsWith("Num")){
            qulity = sortString.substring(0,sortString.length() - 3);
            stats.put("totalQuantity",nft.getString(toLowerCaseFirstOne(sortString)));
            stats.put("totalValue",nft.getString(toLowerCaseFirstOne(qulity)));
        }else {
            qulity = sortString + "Num";
            stats.put("totalQuantity",nft.getString(toLowerCaseFirstOne(qulity)));
            stats.put("totalValue",nft.getString(toLowerCaseFirstOne(sortString)));
        }
        stats.put("walletAddress",nftStats.getAddress());
        stats.put("category",category);
        return stats;
    }

    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0))){
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
