package com.wuyuan.database.sevice;

import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.util.Collocation;
import com.wuyuan.database.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class ConfigService {

    @Resource
    private MongoTemplate mongoTemplate;

    private String collocationName;

    private String chainName;

    private String chainPrefix;

    private String coinName;

    private String bech32Prefix;

    private static Integer chaindecimal;
    private static Integer evmosDecimal;

    public  Integer getChaindecimal() {
        if(chaindecimal==null){
            chaindecimal=getChainDecimalByKey();
        }
        return chaindecimal;
    }

    public Integer getEvmosDecimal(){
        if(evmosDecimal==null){
            evmosDecimal=getEvmosDecimalByKey();
        }
        return evmosDecimal;
    }

    private Integer getChainDecimalByKey(){
        String chain_decimal = this.getConfig("chain_decimal");
        if(chain_decimal==null){
            return 6;
        }
        return new BigDecimal(chain_decimal).intValue();
    }

    private Integer getEvmosDecimalByKey(){
        String evmos_decimal = this.getConfig("evmos_decimal");
        if(evmos_decimal==null){
            return 6;
        }
        return new BigDecimal(evmos_decimal).intValue();
    }


    public void saveConfig(JSONObject configJson){
        mongoTemplate.save(configJson,getCollection());
    }

    public synchronized Long isExistConfig(String key){
        Query query = new Query(Criteria.where("key").is(key));
        return mongoTemplate.count(query.limit(1),getCollection());
    }

    public String getConfig(String key){
        Query query = new Query(Criteria.where("key").is(key));
        query.fields().exclude("_id");
        JSONObject config = mongoTemplate.findOne(query,JSONObject.class,getCollection());
        if (config == null){
            return  null;
        }
        return config.getString("value");
    }

    public void updateConfig(String key,JSONObject configJson){
        Query query = new Query(Criteria.where("key").is(key));
        Update update = new Update();
        update.set("value",configJson.getString("value"));
        mongoTemplate.updateFirst(query,update,getCollection());
    }

    public String getCollection(){
        if (collocationName == null){
            collocationName = Collocation.collection_config;
        }
        return collocationName;
    }

    public String getChainName(){
        if (chainName == null){
            chainName = getConfig(ConfigUtil.chainNameKey);
        }
        return chainName;
    }


    public String getChainPrefix(){
        if (chainPrefix == null){
            chainPrefix = getConfig(ConfigUtil.chainPrefixKey);
        }
        return chainPrefix;
    }

    public String getCoinName(){
        if (coinName == null){
            coinName = getConfig(ConfigUtil.coinNameKey);
        }
        return coinName;
    }
    public String getCoinName1(){
        String coinName1=getCoinName();
        if(coinName1.startsWith("u")&&!"usdg".equalsIgnoreCase(coinName1)){
            return coinName1.substring(1);
        }
        return coinName1;
    }

    public String getBech32Prefix(){
        if (bech32Prefix == null){
            bech32Prefix = getConfig(ConfigUtil.bech32PrefixKey);
        }
        return bech32Prefix;
    }

}
