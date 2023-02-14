package com.wuyuan.database.sevice.evmos;

import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.entity.EVMEvents;
import com.wuyuan.database.util.ContractTypeEnum;
import com.wuyuan.database.util.ERC20Util;
import com.wuyuan.database.util.PageModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.utils.Numeric;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.util.List;

@Service
public class EVMosLogsService {
    @Autowired
    private MongoTemplate mongoTemplate;
    public static final String COLLECTUIB_NAME = "evm_logs";

    @Autowired
    private EVMosEventsService evMosEventsService;
    @Autowired
    private EVMosContractService evMosContractService;

    public void save(JSONObject  log){
        this.mongoTemplate.save(log,COLLECTUIB_NAME);
    }
    public Log getLogByHashAndIndex(String hash, BigInteger index){
        Query query = new Query(Criteria.where("transactionHash").is(hash).andOperator(Criteria.where("logIndex").is(index.longValue())));
        return this.mongoTemplate.findOne(query,Log.class,COLLECTUIB_NAME);
    }
    public List<Log> get20And721LogByHash(String hash){
        Query query = new Query(Criteria.where("transactionHash").is(hash));
        query.addCriteria(Criteria.where("contractType").in(ContractTypeEnum.erc721.getType(),ContractTypeEnum.erc20.getType()));
        return this.mongoTemplate.find(query,Log.class,COLLECTUIB_NAME);
    }
    public PageModel<Log> queryErc20And721Logs(String hash, String address, Integer isOutputOnly,
                                    String transactionType, String blockNumber, int pageIndex,
                                    int pageSize, String contractAddress,
                                    String contractType,String nftId){
        if(StringUtils.isNotBlank(address)){
            if(address.startsWith("0x")){
                address="0x000000000000000000000000"+address.substring(2);
            }else{
                address="0x000000000000000000000000"+address;
            }
        }

        Criteria cr = new Criteria();
        if(StringUtils.isNotBlank(address)) {
            if (isOutputOnly == null) {
                cr.orOperator(Criteria.where("topics.2").is(address.trim().toLowerCase().toLowerCase()), Criteria.where("topics.1").is(address.trim().toLowerCase()));
            } else if (isOutputOnly.equals(0)) {
                Criteria criteria = Criteria.where("topics.1").is(address.trim().toLowerCase());
                cr.andOperator(criteria);
            } else if (isOutputOnly.equals(1)) {
                Criteria criteria = Criteria.where("topics.2").is(address.trim().toLowerCase());
                cr.andOperator(criteria);
            } else {
                cr.orOperator(Criteria.where("topics.2").is(address.trim().toLowerCase().toLowerCase()), Criteria.where("topics.1").is(address.trim().toLowerCase()));
            }
        }

        Query query=new Query(cr);
        if(StringUtils.isNotBlank(contractType)){
            query.addCriteria(Criteria.where("contractType").is(ContractTypeEnum.valueOf(contractType).getType()));
        }else{
            query.addCriteria(Criteria.where("contractType").in(ContractTypeEnum.erc721.getType(),ContractTypeEnum.erc20.getType()));
        }
        if(StringUtils.isNotBlank(nftId)){
            String hex = Numeric.toHexString(new BigInteger(nftId).toByteArray());
            byte[] bytes1 = DatatypeConverter.parseHexBinary(hex.substring(2));
            byte[] bs=new byte[32];
            System.arraycopy(bytes1,0,bs,bs.length-bytes1.length,bytes1.length);
            Bytes32 bytes32 = new Bytes32(bs);
            query.addCriteria(Criteria.where("topics.3").is(Numeric.toHexString(bytes32.getValue())));
        }
        if(StringUtils.isNotBlank(contractAddress)){
            query.addCriteria(Criteria.where("address").is(contractAddress));
        }
        String transactionCode="";
        if(StringUtils.isNotBlank(transactionType)){
            EVMEvents eventByName = evMosEventsService.getEventByName(transactionType);
            transactionCode=eventByName.getCode();
            query.addCriteria(Criteria.where("topics.0").is(transactionCode));
        }

        if(StringUtils.isNotBlank(hash)){
            query.addCriteria(Criteria.where("transactionHash").is(hash));
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
        List<Log> list=mongoTemplate.find(query, Log.class,COLLECTUIB_NAME);
        PageModel<Log> pageModel=new PageModel<Log>(list,pageIndex,pageSize,count);
        return pageModel;
    }
}
