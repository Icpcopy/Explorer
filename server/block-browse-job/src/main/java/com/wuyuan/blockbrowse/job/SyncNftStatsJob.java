package com.wuyuan.blockbrowse.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.entity.GlobalBlock;
import com.wuyuan.database.entity.NFT;
import com.wuyuan.database.entity.NFTDealAmount;
import com.wuyuan.database.sevice.*;
import com.wuyuan.database.util.AmountUtil;
import com.wuyuan.database.util.ConfigUtil;
import com.wuyuan.database.util.DateFormatUtil;
import io.cosmos.util.CosmosUtil;
import io.cosmos.util.Sha256;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class SyncNftStatsJob {

    @Resource
    private ConfigService configService;

    @Resource
    private NFTService nftService;

    @Resource
    private BlockService blockService;

    @Resource
    private NFTDealAmountService nftDealAmountService;

    @Resource
    private TokenService tokenService;

    @Scheduled(fixedDelay = 1000*3)
    public void syncNFTStats() {
        log.info("start SyncNFTJob");
        GlobalBlock globalBlock = blockService.getGlobal(null);
        if (globalBlock == null){
            return;
        }
        long old = 1;
        long newBlock =globalBlock.getBlockNum();
        if (globalBlock.getNftNum() != null) {
            old = globalBlock.getNftNum();
        }
        if (old != 1){
            old += 1;
        }
        String gaussUrl = configService.getConfig(ConfigUtil.chainUrlKey);
        for (long i = old; i <= newBlock; i++) {
            log.info("nft当前区块" + i);
            log.info("nft最新区块" + newBlock);
            JSONObject b = CosmosUtil.getBlockByHeight(gaussUrl,i);
            JSONArray txs = b.getJSONObject("block").getJSONObject("data").getJSONArray("txs");
            syncNftTransfer(i,b.getJSONObject("block").getJSONObject("header"));
            if(txs!=null && txs.size()>0){
                saveTransaction(txs,gaussUrl);
            }



            globalBlock.setNftNum(i);
            blockService.updateGlobal(1,globalBlock);
        }
        log.info("SyncNFTJob end");
    }

    public void saveTransaction(JSONArray txs,String url){
        List<String> transactions = txs.toJavaList(String.class);
        List<String> transactionList = Collections.synchronizedList(transactions);
        transactionList.parallelStream().forEach(txString ->{
            String hash= Sha256.tx2Sha256(txString).toUpperCase();
            JSONObject transfer = CosmosUtil.getTxByhash(url+"/icplaza",hash);
            if (transfer != null){
                JSONObject txResponse = transfer.getJSONObject("tx_response");
                JSONObject tx = transfer.getJSONObject("tx");
                JSONArray messages = tx.getJSONObject("body").getJSONArray("messages");
                int code = txResponse.getIntValue("code");
                messages.toJavaList(JSONObject.class).stream().forEach(message -> {
                    String type = message.getString("@type");
                    switch (type) {
                        case TransactionService.msgMintNft:
                            if (code == 0){
                                long timestamp = txResponse.getTimestamp("timestamp").getTime();
                                saveNFT(message,timestamp,hash);
                            }
                            nftService.getNftStats(message.getString("recipient"));
                            break;
                        case TransactionService.msgMintBatch:
                            if (code == 0){
                                long timestamp = txResponse.getTimestamp("timestamp").getTime();
                                JSONArray items = message.getJSONArray("items");
                                String creator = message.getString("recipient");
                                items.toJavaList(JSONObject.class).stream().forEach(item -> {
                                    item.put("recipient", creator);
                                    saveNFT(item,timestamp,hash);
                                    nftService.getNftStats(creator);
                                });
                            }
                            break;
                        case TransactionService.msgTransferNft:

                            String tokenId = message.getString("token_id");
                            NFT nft = nftService.getNFT(tokenId);
                            if(nft != null) {
                                long timestamp = txResponse.getTimestamp("timestamp").getTime();
                                nft.setTimestamp(timestamp);
                                if (code == 0){
                                    nft.setOwner(message.getString("recipient"));
                                }
                                updateNft(nft);
                            }
                            nftService.getNftStats(message.getString("recipient"));
                            break;
                        case TransactionService.msgCreateOrder:
                        case TransactionService.msgFrozenNft:
                        case TransactionService.msgDeleteOrder:
                        case TransactionService.msgAgreeOrder:
                        case TransactionService.msgUpdateOrder:

                            tokenId = message.getString("token_id");
                            nft = nftService.getNFT(tokenId);
                            if(nft != null) {
                                long timestamp = txResponse.getTimestamp("timestamp").getTime();
                                nft.setTimestamp(timestamp);
                                updateNft(nft);
                            }
                            break;
                        case TransactionService.msgBid:
                        case TransactionService.msgBidOrder:
                            tokenId = message.getString("token_id");
                            nft = nftService.getNFT(tokenId);
                            if(nft != null) {
                                long timestamp = txResponse.getTimestamp("timestamp").getTime();
                                nft.setTimestamp(timestamp);
                                if (code == 0){
                                    JSONObject price = message.getJSONObject("price");
                                    String demon = price.getString("denom");
                                    BigDecimal amount = price.getBigDecimal("amount");
                                    JSONObject token = tokenService.getTokenByUnit(demon);
                                    if (token != null) {
                                        demon = token.getString("symbol");
                                        amount = amount.divide(new BigDecimal(Math.pow(10,token.getIntValue("decimals"))));
                                    }else {
                                        demon = demon.substring(1);
                                        amount = amount.divide(new BigDecimal(Math.pow(10,configService.getChaindecimal())));
                                    }
//                                    saveNFTDealAmount(BigDecimal.ZERO,timestamp);
                                    nft.setNowValue(amount);
                                    nft.setValueSymbol(demon);
                                }
                                updateNft(nft);
                            }
                            nftService.getNftStats(message.getString("sender"));
                            break;
                        default:
                            break;
                    }
                });
            }
        });
    }

    public void saveNFT(JSONObject nftJson,Long timestamp,String hash){
        String tokenId = nftJson.getString("token_id");
        if ( !nftService.isExist(tokenId)) {
            NFT nft = new NFT();
            nft.setCreator(nftJson.getString("recipient"));
            nft.setOwner(nftJson.getString("recipient"));
            nft.setTokenId(tokenId);
            nft.setCateId(nftJson.getString("cate_id"));
//            nft.setPlatform(nftJson.getBoolean("is_platform"));
//            nft.setTokenStatus(nftJson.getInteger("token_status"));
            nft.setCopyrightTax(nftJson.getString("copyright_tax"));
            nft.setValueAddedTax(nftJson.getString("value_added_tax"));
            nft.setCompanyUri(nftJson.getString("company_uri"));
            nft.setTokenUri(nftJson.getString("token_uri"));
            nft.setName(nftJson.getString("name"));
            if (timestamp != null) {
                nft.setTimestamp(timestamp);
            }
            nft.setMintHash(hash);
            nft.setNowValue(BigDecimal.ZERO);
            nftService.saveNft(nft);
        }

    }

    public void syncNftTransfer(long blockNumber,JSONObject header){
        String url = configService.getConfig(ConfigUtil.chainSocketUrl);
        if(StringUtils.isBlank(url)){
            return;
        }
        List<JSONObject> blockResults = CosmosUtil.getBlockResultes(url, blockNumber);
        String dateTime = header.getString("time");
        long blocktime=System.currentTimeMillis();
        if (dateTime.length() > 23) {
            JSONObject time=new JSONObject();
            time.put("time", dateTime.substring(0, 22) + "Z");
            blocktime=time.getDate("time").getTime();
        }
//        System.out.println(blockResults.get(blockResults.size()-1));
        for (JSONObject json : blockResults) {
            String type = json.getString("type");
            JSONArray attributes = json.getJSONArray("attributes");
            String amount = "";
            String tokenId = "";
            String from=null;
            String to=null;
//            System.out.println(json);
            if(!"nft_transfer".equalsIgnoreCase(type)&&
                    !"order_close".equalsIgnoreCase(type)){
                continue;
            }
            System.out.println(type);
            BigDecimal amountOf=BigDecimal.ZERO;
            for (int i = 0; i < attributes.size(); i++) {
                JSONObject attribute=attributes.getJSONObject(i);
                if("from".equalsIgnoreCase(attribute.getString("key"))){
                    from=attribute.getString("value");
                }else if("to".equalsIgnoreCase(attribute.getString("key"))){
                    to=attribute.getString("value");
                }else if("price".equalsIgnoreCase(attribute.getString("key"))){
                    amount=attribute.getString("value");
                }else if("token_id".equalsIgnoreCase(attribute.getString("key"))){
                    tokenId=attribute.getString("value");
                }
            }
            if("order_close".equalsIgnoreCase(type)){
                System.out.println("json:"+json);
                System.out.println("amount:"+amount);
                AmountUtil amountUtil = AmountUtil.initAmount(amount, new BigDecimal(Math.pow(10,configService.getChaindecimal())), configService.getChaindecimal());
                saveNFTDealAmount(amountUtil.getAmount(),blocktime);

            }else  if("nft_transfer".equalsIgnoreCase(type)){
                NFT nft = nftService.getNFT(tokenId);
                if(nft!=null){
                    nft.setOwner(to);
                    nft.setTimestamp(blocktime);
                    updateNft(nft);
                    nftService.getNftStats(from);
                    nftService.getNftStats(to);
                }
            }

        }
    }


    public void updateNft(NFT nft) {
        nftService.saveNft(nft);
    }

    public void saveNFTDealAmount(BigDecimal amount, long timeStamp) {
        NFTDealAmount nftDealAmount = null;
        String date = DateFormatUtil.timeToUTCFormat(timeStamp);
        if (nftDealAmountService.isExist(date)) {
            nftDealAmount = nftDealAmountService.getNFTDealAmount(date);
            nftDealAmount.setDealAmount(nftDealAmount.getDealAmount().add(amount));
        }else {
            nftDealAmount = new NFTDealAmount();
            nftDealAmount.setDate(date);
            nftDealAmount.setTimestamp(timeStamp);
            nftDealAmount.setDealAmount(amount);
        }
        nftDealAmountService.save(nftDealAmount);
    }
    @Scheduled(fixedDelay = 1000*60)
    public void syncNFTDealAmount(){
        long timeStamp = System.currentTimeMillis();
        String date = DateFormatUtil.timeToUTCFormat(timeStamp);
        if (nftDealAmountService.isExist(date)) {
            return;
        }else {
            saveNFTDealAmount(BigDecimal.ZERO,timeStamp);
        }
    }

    public static void main(String[] args) {
        String url = "http://192.168.3.23:2657/";
        List<JSONObject> blockResults = CosmosUtil.getBlockResultes(url, 48260L);
            System.out.println(JSONArray.toJSONString(blockResults));
        BigDecimal amountOf=BigDecimal.ZERO;
        for (JSONObject json : blockResults) {
            String type = json.getString("type");
            JSONArray attributes = json.getJSONArray("attributes");
            String amount = "";
            String tokenId = "";
            String from=null;
            String to=null;
            if(!"nft_transfer".equalsIgnoreCase(type)&&
                    !"order_close".equalsIgnoreCase(type)){
                continue;
            }
            for (int i = 0; i < attributes.size(); i++) {
                JSONObject attribute=attributes.getJSONObject(i);
                if("from".equalsIgnoreCase(attribute.getString("key"))){
                    from=attribute.getString("value");
                }else if("to".equalsIgnoreCase(attribute.getString("key"))){
                    to=attribute.getString("value");
                }else if("price".equalsIgnoreCase(attribute.getString("key"))){
                    amount=attribute.getString("value");
                }else if("token_id".equalsIgnoreCase(attribute.getString("key"))){
                    tokenId=attribute.getString("value");
                }
            }
//            if("order_close".equalsIgnoreCase(type)){
//                System.out.println(amount);
//                AmountUtil amountUtil = AmountUtil.initAmount(amount, new BigDecimal(1000000L), 6);
//                System.out.println(amountUtil.getAmount());
//
//            }else if("token_transfer".equalsIgnoreCase(type)){
//                System.out.println(amount);
//                AmountUtil amountUtil = AmountUtil.initAmount(amount, new BigDecimal(1000000L), 6);
//                amountOf= amountOf.add(amountUtil.getAmount());
//            }
        }
    }
}
