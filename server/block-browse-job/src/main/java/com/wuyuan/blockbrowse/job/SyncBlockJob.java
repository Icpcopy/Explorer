package com.wuyuan.blockbrowse.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wuyuan.database.entity.FeeDestructionQuantity;
import com.wuyuan.database.entity.GlobalBlock;
import com.wuyuan.database.sevice.*;
import com.wuyuan.database.util.Collocation;
import com.wuyuan.database.util.ConfigUtil;
import io.cosmos.util.BalanceUtil;
import io.cosmos.util.CosmosUtil;
import io.cosmos.util.Sha256;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Component
@EnableScheduling
@Slf4j
public class SyncBlockJob {
    @Resource
    private BlockService blockService;
    @Resource
    private MongoSevice mongoSevice;

    @Resource
    private ConfigService configService;

    @Resource
    private AddressService addressService;

    @Resource
    private ValidatorDelegatorService validatorDelegatorService;

    @Resource
    private FeeDestructionQuantityService feeDestructionQuantityService;

	@Resource
    private NFTDealAmountService nftDealAmountService;
//    private String chainName;
//
//    private String chainPrefix;
//
//    private String coinName;

    private Long startNum;

    @Scheduled(fixedDelay = 1000)
    public void syncBlock() {
        log.info("start SyncBlockJob");
        log.info("chainName:{}",configService.getChainName());
        GlobalBlock globalBlock = blockService.getGlobal(null);
        long newBlockNum = CosmosUtil.getNewBlock(configService.getConfig(ConfigUtil.chainUrlKey));
        startNum = new BigDecimal(configService.getConfig(ConfigUtil.coinStartNumKey)).longValue();
        boolean isExist=false;
        if (globalBlock != null) {
            startNum = globalBlock.getBlockNum();
            isExist=true;
        }else{
            isExist=false;
            globalBlock =new GlobalBlock();
            globalBlock.setChainName(configService.getChainName());
            globalBlock.setBlockNum(startNum);
        }
        for (long i = startNum; i < newBlockNum; i++) {
            JSONObject b = CosmosUtil.getBlockByHeight(configService.getConfig(ConfigUtil.chainUrlKey),i);
            JSONArray txs = b.getJSONObject("block").getJSONObject("data").getJSONArray("txs");
            if(txs!=null && txs.size()>0){
                saveTransaction(txs,newBlockNum);
            }

            if (mongoSevice.getBlock(String.valueOf(i), configService.getChainName()+"_" + Collocation.collection_block) == 0) {
                mongoSevice.save(b, configService.getChainName()+"_" + Collocation.collection_block);
            }else {
            }

            if (i != 0){
                blockService.updateBlock(String.valueOf(i-1),b.getJSONObject("block").getJSONObject("last_commit").getJSONArray("signatures"));
            }
            globalBlock.setBlockNum(i);
            if(isExist){
                blockService.updateGlobal(null,globalBlock);
            }else {
                globalBlock=blockService.saveGlobal(null,globalBlock);
                isExist=true;
            }

            log.info("block"+i+"同步完成");
        }
    }

    public void saveTransaction(JSONArray txs,Long newBlockNum){
        List<String>  transactions = Collections.synchronizedList(txs.toJavaList(String.class));
        transactions.parallelStream().forEach( tx -> {
            String hash= Sha256.tx2Sha256(tx).toUpperCase();
            JSONObject transfer = CosmosUtil.getTxByhash(configService.getConfig(ConfigUtil.chainUrlKey)+"/"+configService.getChainPrefix(),hash);
            if(mongoSevice.getTransaction(hash,configService.getChainName()+"_"+ Collocation.collection_transaction) == 0){
                log.info("save tx");
                mongoSevice.save(transfer,configService.getChainName()+"_" + Collocation.collection_transaction);

                log.info("save address");
                saveAddress(transfer,newBlockNum);
            }

            long blockHeight = transfer.getJSONObject("tx_response").getLongValue("height");
            if (configService.getChainName().equals("gauss") && transfer.getJSONObject("tx_response").getLongValue("height") >= 3069220L) {
                String feeJson = transfer.getJSONObject("tx").getJSONObject("auth_info").getJSONObject("fee").getString("amount");
                BigDecimal amount = BigDecimal.ZERO;
                String denom = null;
                if(feeJson.startsWith("[")) {
                    JSONArray jsonArray = JSONArray.parseArray(feeJson);
                    amount = jsonArray.getJSONObject(0).getBigDecimal("amount");
                    denom = jsonArray.getJSONObject(0).getString("denom");
                }else {
                    JSONObject jsonObject = JSONObject.parseObject(feeJson);
                    amount = jsonObject.getBigDecimal("amount");
                    denom = jsonObject.getString("denom");
                }

                if (denom != null && denom.equals("ugauss")) {
                    amount = amount.divide(new BigDecimal(Math.pow(10,configService.getChaindecimal())));
                    FeeDestructionQuantity feeDestructionQuantity = feeDestructionQuantityService.getFeeDestructionQuantity();
                    if (feeDestructionQuantity != null && feeDestructionQuantity.getBlockNumber() <= blockHeight) {
                        feeDestructionQuantity.setAmount(feeDestructionQuantity.getAmount().add(amount));
                        feeDestructionQuantity.setBlockNumber(blockHeight);
                        feeDestructionQuantityService.saveFeeDestructionQuantity(feeDestructionQuantity);
                    }else {
                        feeDestructionQuantity = new FeeDestructionQuantity();
                        feeDestructionQuantity.setCoinName("gauss");
                        feeDestructionQuantity.setBlockNumber(blockHeight);
                        feeDestructionQuantity.setAmount(amount);
                        feeDestructionQuantityService.saveFeeDestructionQuantity(feeDestructionQuantity);
                    }
                }
            }
        });
    }

    public void saveAddress(JSONObject transfer,Long newBlockNum){
        JSONArray txs = transfer.getJSONObject("tx").getJSONObject("body").getJSONArray("messages");
        JSONArray logs = transfer.getJSONObject("tx_response").getJSONArray("logs");
        txs.stream().forEach(tx -> {
            if (tx instanceof JSONObject){
                String address = null;
                switch (((JSONObject) tx).getString("@type")){
                    case TransactionService.msgSend:
                    case TransactionService.CreatePermanentVestingAccount:
                    case TransactionService.CreatePeriodicVestingAccount:
                    case TransactionService.msgMintCoin:
                        address = ((JSONObject) tx).getString("from_address");
                        saveOrUpadte(address,newBlockNum,null);
                        address = ((JSONObject) tx).getString("to_address");
                        saveOrUpadte(address,newBlockNum,null);
                        break;

                    case TransactionService.msgTransfer:
                        address = ((JSONObject) tx).getString("sender");
                        saveOrUpadte(address,newBlockNum,null);
                        address = ((JSONObject) tx).getString("receiver");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgMulti:
                        JSONArray inputs = ((JSONObject) tx).getJSONArray("inputs");
                        if (inputs != null && inputs.size() > 0){
                            for (int i = 0; i < inputs.size(); i++) {
                                address = inputs.getJSONObject(i).getString("address");
                                saveOrUpadte(address,newBlockNum,null);
                            }
                        }
                        JSONArray outputs = ((JSONObject) tx).getJSONArray("outputs");
                        if (outputs != null && outputs.size() > 0){
                            for (int i = 0; i < outputs.size(); i++) {
                                address = outputs.getJSONObject(i).getString("address");
                                saveOrUpadte(address,newBlockNum,null);
                            }
                        }
                        break;
                    case TransactionService.msgDelegate:
                        address = ((JSONObject) tx).getString("delegator_address");
                        saveOrUpadte(address,newBlockNum,1);

                        Integer code = transfer.getJSONObject("tx_response").getInteger("code");
                        if (code == 0){
                            saveOrUpdateDelegator(address,(JSONObject) tx);
                        }
                        break;
                    case TransactionService.msgUnDelegate:
                        address = ((JSONObject) tx).getString("delegator_address");
                        saveOrUpadte(address,newBlockNum,1);
                        code = transfer.getJSONObject("tx_response").getInteger("code");
                        if (code == 0){
                            unDelegator(address,(JSONObject) tx);
                        }
                        break;
                    case TransactionService.msgBeginRedelegate:
                        address = ((JSONObject) tx).getString("delegator_address");
                        saveOrUpadte(address,newBlockNum,null);
                        code = transfer.getJSONObject("tx_response").getInteger("code");
                        if (code == 0){
                            updateDelegator(address);
                        }
                        break;
                    case TransactionService.msgCreateValidator:
                        address = ((JSONObject) tx).getString("delegator_address");
                        saveOrUpadte(address,newBlockNum,1);
                        code = transfer.getJSONObject("tx_response").getInteger("code");
                        if (code == 0){
                            saveOrUpdateDelegator(address,(JSONObject) tx);
                        }
                        break;
                    case TransactionService.msgDelegateReward:

                    case TransactionService.msgAgreeOrderPair:
                    case TransactionService.msgCreateDefi:
                    case TransactionService.msgDefiDelegate:
                    case TransactionService.msgWithdrawDefiDelegatorReward:
                    case TransactionService.msgDefiUndelegate:
                        address = ((JSONObject) tx).getString("delegator_address");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgWithdrawAddress:
                        address = ((JSONObject) tx).getString("delegator_address");
                        saveOrUpadte(address,newBlockNum,null);
                        address = ((JSONObject) tx).getString("withdraw_address");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgVote:
                        address = ((JSONObject) tx).getString("voter");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgDeposit:
                        address = ((JSONObject) tx).getString("depositor");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgSubmitProposal:
                        address = ((JSONObject) tx).getString("proposer");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgIssueToken:
                    case TransactionService.msgUnlockToken:
                    case TransactionService.msgEditToken:
                        address = ((JSONObject) tx).getString("owner");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgTransferTokenOwner:
                        address = ((JSONObject) tx).getString("old_owner");
                        saveOrUpadte(address,newBlockNum,null);
                        address = ((JSONObject) tx).getString("new_owner");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgMintToken:
                        address = ((JSONObject) tx).getString("owner");
                        saveOrUpadte(address,newBlockNum,null);
                        address = ((JSONObject) tx).getString("to");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgPlaceOrder:
                    case TransactionService.msgAddPledge:
                    case TransactionService.msgRedeemPledge:
                        address = ((JSONObject) tx).getString("owner_address");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgCreatePool:
                        address = ((JSONObject) tx).getString("owner_address");
                        saveOrUpadte(address,newBlockNum,null);
                        address = ((JSONObject) tx).getString("defi_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgCreatePool2:
                        address = ((JSONObject) tx).getString("pool_creator_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgCreateClient:
                    case TransactionService.msgUpdateClient:
                    case TransactionService.msgConnectionOpenInit:
                    case TransactionService.msgConnectionOpenConfirm:
                    case TransactionService.msgChannelOpenInit:
                    case TransactionService.msgTimeout:
                    case TransactionService.msgChannelOpenAck:
                    case TransactionService.msgConnectionOpenAck:
                    case TransactionService.msgChannelOpenTry:
                    case TransactionService.msgConnectionOpenTry:
                    case TransactionService.msgChannelOpenConfirm:
                    case TransactionService.msgAcknowledgement:
                        address = ((JSONObject) tx).getString("signer");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgRecvPacket:
                        address = ((JSONObject) tx).getString("signer");
                        saveOrUpadte(address,newBlockNum,null);
                        logs.stream().forEach(logEvent -> {
                            JSONArray events = JSON.parseObject(JSON.toJSONString(logEvent)).getJSONArray("events");
                            events.stream().forEach(event -> {
                                JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                                if (txEvent.containsKey("type") && txEvent.getString("type").equals("recv_packet")){
                                    JSONArray attributes = txEvent.getJSONArray("attributes");
                                    for (int i = 0; i < attributes.size(); i++) {
                                        JSONObject attribute = attributes.getJSONObject(i);
                                        String key = attribute.getString("key");
                                        if ("packet_data".equals(key)){
                                            String json = attribute.getString("value");
                                            if (StringUtils.isNotBlank(json)){
                                                JSONObject packetData = JSON.parseObject(json);
                                                log.info(packetData.getString("receiver") + "------跨链握手交易");
                                                saveOrUpadte(packetData.getString("receiver"),newBlockNum,null);
                                            }
                                        }
                                    }
                                }
                            });
                        });
                        break;
                    case TransactionService.msgBurnToken:
                        address = ((JSONObject) tx).getString("sender");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgBurnCoin:
                        address = ((JSONObject) tx).getString("address");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgRevokeOrder:
                        address = ((JSONObject) tx).getString("pool_address");
                        saveOrUpadte(address,newBlockNum,null);
                        address = ((JSONObject) tx).getString("owner_address");
                        saveOrUpadte(address,newBlockNum,null);
                        break;
                    case TransactionService.msgSwapWithinBatch:
                        address = ((JSONObject) tx).getString("swap_requester_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgDepositWithinBatch:
                        address = ((JSONObject) tx).getString("depositor_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgWithdrawWithinBatch:
                        address = ((JSONObject) tx).getString("withdrawer_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgSetOrchestratorAddress:
                    case TransactionService.msgValsetConfirm:
                    case TransactionService.msgValsetUpdatedClaim:
                    case TransactionService.msgConfirmBatch:
                    case TransactionService.msgWithdrawClaim:
                        address = ((JSONObject) tx).getString("orchestrator");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgDepositClaim:
                        address = ((JSONObject) tx).getString("orchestrator");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("cosmos_receiver");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgRequestBatch:
                    case TransactionService.msgSendToEth:
                    case TransactionService.msgSetBridgeCommission:
                    case TransactionService.msgStake:
                    case TransactionService.msgUnbond:
                    case TransactionService.msgWithdraw:
                    case TransactionService.msgFrozenNft:
                        address = ((JSONObject) tx).getString("sender");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgSubmitEthereumEvent:
                    case TransactionService.msgSubmitEthereumTxConfirmation:
                        address = ((JSONObject) tx).getString("signer");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgDelegateKeys:
                        address = ((JSONObject) tx).getString("orchestrator_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgCreateVestingAccount:
                        address = ((JSONObject) tx).getString("from_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("to_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgDestroyPool:
                    case TransactionService.msgFarmCreatePool:
                        address = ((JSONObject) tx).getString("creator");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgMintNft:
                    case TransactionService.msgTransferNft:
                    case TransactionService.msgMintBatch:
                        address = ((JSONObject) tx).getString("sender");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("recipient");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgCreateNftPool:
                        address = ((JSONObject) tx).getString("creator");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("pool_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("commission_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("value_added_tax_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgCreateOrder:
                    case TransactionService.msgBid:
                        address = ((JSONObject) tx).getString("sender");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("pool_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgUpdatePool:
                        address = ((JSONObject) tx).getString("creator");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("pool");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("commission_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("value_added_tax_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgDeleteOrder:
                    case TransactionService.msgAgreeOrder:
                        address = ((JSONObject) tx).getString("creator");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("pool_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;
                    case TransactionService.msgUpdateOrder:
                        address = ((JSONObject) tx).getString("pool_creator");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("pool_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                    case TransactionService.msgBidOrder:
                        address = ((JSONObject) tx).getString("sender");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        address = ((JSONObject) tx).getString("pool_address");
                        if (StringUtils.isNotBlank(address)){
                            saveOrUpadte(address,newBlockNum,null);
                        }
                        break;

                    default:
                        break;
                }
            }
        });
    }

    public synchronized void saveOrUpadte(String address,Long newBlockNum,Integer isDelegator){

        if (configService.getChainName().equals("ict") && address.startsWith("icplaza")){

        }else if ( !address.startsWith(configService.getChainName())){
            return;
        }

        if (StringUtils.isBlank(address) || address.length() > 46){
            return;
        }
        if (addressService.isExistAddress(address) > 0){
            if (Long.valueOf(addressService.getAddressBlockNumber(address)) <= newBlockNum){
                JSONObject addressjSON = null;
                if (isDelegator != null){
                    addressjSON = addressService.getAddressInfo(address,newBlockNum);

                }else {
                    JSONObject data = addressService.getAddressByAddress(address);
                    addressjSON = addressService.getAddressJson(data,address,newBlockNum);
                }
                if (addressjSON != null){
                    addressService.updateAddress(address,addressjSON);
                }

            }
        }else {
            JSONObject addressjSON = null;
            if (isDelegator != null){
                addressjSON = addressService.getAddressInfo(address,newBlockNum);

            }else {
                JSONObject data = addressService.getAddressByAddress(address);
                addressjSON = addressService.getAddressJson(data,address,newBlockNum);
            }
            JSONObject addressJSON = addressService.getAddressJson(addressjSON,address,newBlockNum);
            if (addressJSON != null){
                addressService.saveAddress(addressJSON);
            }
        }
    }

    public void saveOrUpdateDelegator(String address,JSONObject tx){
        JSONObject delegator = null;
        String validatorAddress = tx.getString("validator_address");
        JSONObject amount = tx.getJSONObject("amount");
        BigDecimal shares = amount.getBigDecimal("amount").setScale(2, RoundingMode.HALF_UP);
        if (validatorDelegatorService.isExist(address,validatorAddress) == 0){
            String recommanderAddress = tx.getString("recommander_address");
            delegator = new JSONObject();
            delegator.put("balance",amount);
            JSONObject delegation = new JSONObject();
            delegation.put("shares",shares.toPlainString());
            delegation.put("delegator_address",address);
            delegation.put("validator_address",validatorAddress);
            delegation.put("recommander_address",recommanderAddress);
            delegator.put("delegation",delegation);
            validatorDelegatorService.saveDelegator(delegator);
            return;
        }
        delegator = validatorDelegatorService.getDelegator(address,validatorAddress);
        shares = shares.add(delegator.getJSONObject("balance").getBigDecimal("amount"));
        amount.put("amount",shares);
        delegator.put("balance",amount);
        //更新
        validatorDelegatorService.saveDelegator(delegator);
    }

    public void updateDelegator(String address) {
        JSONArray delegator = BalanceUtil.getDelegatorTxByAddress(configService.getConfig(ConfigUtil.chainUrlKey)+"/"+configService.getChainPrefix(),address);
        if (delegator != null && delegator.size() > 0){
            validatorDelegatorService.deleteDelegatorByAddress(address);
            for (int i = 0; i < delegator.size(); i++) {
                JSONObject amount = delegator.getJSONObject(i);
                validatorDelegatorService.saveDelegator(amount);
            }
        }else if (validatorDelegatorService.isExistByAddress(address) != 0){
            validatorDelegatorService.deleteDelegatorByAddress(address);
        }
    }

    public void unDelegator(String address,JSONObject tx){
        String validatorAddress = tx.getString("validator_address");
        JSONObject amount = tx.getJSONObject("amount");
        BigDecimal undelegate = amount.getBigDecimal("amount");

        JSONObject delegator = validatorDelegatorService.getDelegator(address,validatorAddress);
        JSONObject balance = delegator.getJSONObject("balance");
        BigDecimal delegateAmount = balance.getBigDecimal("amount").subtract(undelegate).setScale(2,RoundingMode.HALF_UP);
        if(delegateAmount.compareTo(BigDecimal.ZERO) == 0){
            validatorDelegatorService.deleteDelegatorByAddress(address,validatorAddress);
        }else {
            balance.put("amount",delegateAmount);
            JSONObject delegation = delegator.getJSONObject("delegation");
            delegation.put("shares",delegateAmount.toPlainString());

            delegator.put("delegation",delegation);
            delegator.put("balance",balance);

            validatorDelegatorService.saveDelegator(delegator);
        }
    }






}
