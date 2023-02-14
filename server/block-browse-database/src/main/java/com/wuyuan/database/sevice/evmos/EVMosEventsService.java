package com.wuyuan.database.sevice.evmos;

import com.wuyuan.database.entity.EVMEvents;
import com.wuyuan.database.util.GetIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EVMosEventsService {
    @Autowired
    private MongoTemplate mongoTemplate;
    public static final String COLLECTUIB_NAME = "evm_event";
    public static Map<String, EVMEvents> eventMap=new HashMap<String,EVMEvents>();

    public static Map<String, EVMEvents> eventNameMap=new HashMap<String,EVMEvents>();
    public void save(EVMEvents evmEvents){
        evmEvents.setId(GetIdUtil.getId());
        this.mongoTemplate.save(evmEvents,COLLECTUIB_NAME);
    }
    public EVMEvents getEventsByCode(String code){
        Query query = new Query(Criteria.where("code").is(code));
        return this.mongoTemplate.findOne(query,EVMEvents.class,COLLECTUIB_NAME);
    }

    public EVMEvents getEventsByCode(String code,String contractType){
        Query query = new Query(Criteria.where("code").is(code));
        query.addCriteria(Criteria.where("contractType").is(contractType));
        return this.mongoTemplate.findOne(query,EVMEvents.class,COLLECTUIB_NAME);
    }
    public EVMEvents getEventsByName(String name){
        Query query = new Query(Criteria.where("name").is(name));
        return this.mongoTemplate.findOne(query,EVMEvents.class,COLLECTUIB_NAME);
    }
    public EVMEvents getEventByName(String name){
        EVMEvents evmEvents = eventNameMap.get(name);
        if(evmEvents==null){
            evmEvents=this.getEventsByName(name);
            if(evmEvents!=null){
                eventNameMap.put(name,evmEvents);
            }
        }
        return evmEvents;

    }
    public List<EVMEvents> getEventsAll(){
        return  this.mongoTemplate.findAll(EVMEvents.class,COLLECTUIB_NAME);
    }
//    public EVMEvents getEventByCode(String code){
//        EVMEvents evmEvents = eventMap.get(code);
//        if(evmEvents==null){
//            evmEvents=this.getEventsByCode(code);
//            if(evmEvents!=null){
//                eventMap.put(code,evmEvents);
//            }
//        }
//        return evmEvents;
//
//    }
    public EVMEvents getEventByCode(String code,String contractType){
        EVMEvents evmEvents = eventMap.get(code+contractType);
        if(evmEvents==null){
            evmEvents=this.getEventsByCode(code,contractType);
            if(evmEvents!=null){
                eventMap.put(code+contractType,evmEvents);
            }else {
                evmEvents=this.getEventsByCode(code);
            }
        }
        return evmEvents;

    }
}
