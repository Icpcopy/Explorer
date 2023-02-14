package com.wuyuan.database.sevice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.wuyuan.database.sevice.evmos.EVMosTransactionService;
import com.wuyuan.database.util.*;
import io.cosmos.util.AddressUtil;
import io.cosmos.util.AmountUtil;
import io.cosmos.util.CosmosUtil;
import io.cosmos.util.PubkeyUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private Web3j web3j;

    @Resource
    private ConfigService configService;

    @Resource
    private ContractService contractService;

    private String collectionName;

    @Resource
    private ValidatorService validatorService;

    @Resource
    private VoteService voteService;

    @Resource
    private TokenService tokenService;
    @Autowired
    private EVMosTransactionService evMosTransactionService;

    public static final String msgSend = "/cosmos.bank.v1beta1.MsgSend";

    public static final String msgMulti = "/cosmos.bank.v1beta1.MsgMultiSend";
    public static final String msgDelegate = "/cosmos.staking.v1beta1.MsgDelegate";
    public static final String msgUnDelegate = "/cosmos.staking.v1beta1.MsgUndelegate";
    public static final String msgBeginRedelegate = "/cosmos.staking.v1beta1.MsgBeginRedelegate";
    public static final String msgDelegateReward = "/cosmos.distribution.v1beta1.MsgWithdrawDelegatorReward";
    public static final String msgWithdrawAddress = "/cosmos.distribution.v1beta1.MsgSetWithdrawAddress";
    public static final String msgWithdrawCommission = "/cosmos.distribution.v1beta1.MsgWithdrawValidatorCommission";
    public static final String msgVote = "/cosmos.gov.v1beta1.MsgVote";
    public static final String msgDeposit = "/cosmos.gov.v1beta1.MsgDeposit";
    public static final String msgEditValidator = "/cosmos.staking.v1beta1.MsgEditValidator";
    public static final String msgCreateValidator = "/cosmos.staking.v1beta1.MsgCreateValidator";
    public static final String msgSubmitProposal = "/cosmos.gov.v1beta1.MsgSubmitProposal";
    public static final String msgIssueToken = "/gauss.token.MsgIssueToken";
    public static final String msgUnlockToken = "/gauss.token.MsgUnlockToken";
    public static final String msgTransferTokenOwner = "/gauss.token.MsgTransferTokenOwner";
    public static final String msgMintToken = "/gauss.token.MsgMintToken";
    public static final String msgEditToken = "/gauss.token.MsgEditToken";
    public static final String msgUnJail = "/cosmos.slashing.v1beta1.MsgUnjail";

    public static final String msgPlaceOrder = "/gauss.orderbook.MsgPlaceOrder";
    public static final String msgAgreeOrderPair = "/gauss.orderbook.MsgAgreeOrderPair";
    public static final String msgAddPledge = "/gauss.orderbook.MsgAddPledge";
    public static final String msgCreatePool = "/gauss.orderbook.MsgCreatePool";
    public static final String msgRedeemPledge = "/gauss.orderbook.MsgRedeemPledge";
    public static final String msgRevokeOrder = "/gauss.orderbook.MsgRevokeOrder";

    public static final String msgCreatePool2 = "/tendermint.liquidity.v1beta1.MsgCreatePool";

    public static final String msgTransfer = "/ibc.applications.transfer.v1.MsgTransfer";
    public static final String msgCreateClient = "/ibc.core.client.v1.MsgCreateClient";
    public static final String msgUpdateClient = "/ibc.core.client.v1.MsgUpdateClient";
    public static final String msgConnectionOpenInit = "/ibc.core.connection.v1.MsgConnectionOpenInit";
    public static final String msgConnectionOpenConfirm = "/ibc.core.connection.v1.MsgConnectionOpenConfirm";
    public static final String msgChannelOpenInit = "/ibc.core.channel.v1.MsgChannelOpenInit";
    public static final String msgTimeout = "/ibc.core.channel.v1.MsgTimeout";
    public static final String msgChannelOpenAck = "/ibc.core.channel.v1.MsgChannelOpenAck";
    public static final String msgConnectionOpenAck = "/ibc.core.connection.v1.MsgConnectionOpenAck";
    public static final String msgChannelOpenTry = "/ibc.core.channel.v1.MsgChannelOpenTry";
    public static final String msgConnectionOpenTry = "/ibc.core.connection.v1.MsgConnectionOpenTry";
    public static final String msgRecvPacket = "/ibc.core.channel.v1.MsgRecvPacket";
    public static final String msgChannelOpenConfirm = "/ibc.core.channel.v1.MsgChannelOpenConfirm";
    public static final String msgAcknowledgement = "/ibc.core.channel.v1.MsgAcknowledgement";


    public static final String msgBurnToken = "/gauss.token.MsgBurnToken";
    public static final String msgBurnCoin = "/cosmos.bank.v1beta1.MsgBurnCoin";
    public static final String msgMintCoin = "/cosmos.bank.v1beta1.MsgMintCoin";
    public static final String msgLoanCoin = "/cosmos.bank.v1beta1.MsgLoanCoin";


    public static final String msgCreateDefi = "/gauss.defi.MsgCreateDefi";
    public static final String msgDefiDelegate = "/gauss.defi.MsgDefiDelegate";
    public static final String msgWithdrawDefiDelegatorReward = "/gauss.defi.MsgWithdrawDefiDelegatorReward";
    public static final String msgDefiUndelegate = "/gauss.defi.MsgDefiUndelegate";

    public static final String msgSwapWithinBatch = "/tendermint.liquidity.v1beta1.MsgSwapWithinBatch";
    public static final String msgDepositWithinBatch = "/tendermint.liquidity.v1beta1.MsgDepositWithinBatch";
    public static final String msgWithdrawWithinBatch = "/tendermint.liquidity.v1beta1.MsgWithdrawWithinBatch";

    public static final String msgSetOrchestratorAddress = "/gravity.v1.MsgSetOrchestratorAddress";
    public static final String msgValsetConfirm = "/gravity.v1.MsgValsetConfirm";
    public static final String msgDepositClaim = "/gravity.v1.MsgDepositClaim";
    public static final String msgValsetUpdatedClaim = "/gravity.v1.MsgValsetUpdatedClaim";
    public static final String msgConfirmBatch = "/gravity.v1.MsgConfirmBatch";
    public static final String msgRequestBatch = "/gravity.v1.MsgRequestBatch";
    public static final String msgSendToEth = "/gravity.v1.MsgSendToEthereum";
    public static final String msgWithdrawClaim = "/gravity.v1.MsgWithdrawClaim";

    public static final String msgSubmitEthereumEvent = "/gravity.v1.MsgSubmitEthereumEvent";
    public static final String msgSubmitEthereumTxConfirmation = "/gravity.v1.MsgSubmitEthereumTxConfirmation";
    public static final String msgSetBridgeCommission = "/gravity.v1.MsgSetBridgeCommission";
    public static final String msgDelegateKeys = "/gravity.v1.MsgDelegateKeys";
    public static final String msgCreateVestingAccount = "/cosmos.vesting.v1beta1.MsgCreateVestingAccount";
    public static final String msgStake = "/gauss.farm.MsgStake";
    public static final String msgWithdraw = "/gauss.farm.MsgWithdraw";
    public static final String msgDestroyPool = "/gauss.farm.MsgDestroyPool";
    public static final String msgFarmCreatePool = "/gauss.farm.MsgCreatePool";
    public static final String msgUnbond = "/gauss.farm.MsgUnbond";
    public static final String CreatePeriodicVestingAccount = "/cosmos.vesting.v1beta1.MsgCreatePeriodicVestingAccount";
    public static final String CreatePermanentVestingAccount = "/cosmos.vesting.v1beta1.MsgCreatePermanentVestingAccount";


    public static final String msgMintNft = "/gauss.nft.v1.MsgMintNft";
    public static final String msgCreateNftPool = "/gauss.nft.v1.MsgCreatePool";
    public static final String msgCreateOrder = "/gauss.nft.v1.MsgCreateOrder";
    public static final String msgUpdatePool = "/gauss.nft.v1.MsgUpdatePool";
    public static final String msgBid = "/gauss.nft.v1.MsgBid";
    public static final String msgMintBatch = "/gauss.nft.v1.MsgMintBatch";
    public static final String msgFrozenNft = "/gauss.nft.v1.MsgFrozenNft";
    public static final String msgTransferNft = "/gauss.nft.v1.MsgTransferNft";
    public static final String msgDeleteOrder = "/gauss.nft.v1.MsgDeleteOrder";
    public static final String msgAgreeOrder = "/gauss.nft.v1.MsgAgreeOrder";
    public static final String msgUpdateOrder = "/gauss.nft.v1.MsgUpdateOrder";
    public static final String msgBidOrder = "/gauss.auction.v1.MsgBidOrder";

    public static final String msgEthereumTx = "/ethermint.evm.v1.MsgEthereumTx";

    public JSONObject getTransactionByHash(String txhash) {
        Query query = new Query(Criteria.where("tx_response.txhash").is(txhash));
        JSONObject tx = mongoTemplate.findOne(query, JSONObject.class, getCollectionName());
        if (tx != null) {
            return getTx(tx);
        } else {
            return tx;
        }
    }


    public List<JSONObject> getTxList() {
        Query query = new Query();
//        Document document = Collation.of("zh").toDocument();
//        document.put("numericOrdering",true);
//        query.collation(Collation.from(document));
//        long count = mongoTemplate.count(query,getCollectionName());
        query.skip((1 - 1) * 10);
        query.limit(10);
        query.with(Sort.by(
                Sort.Order.desc("_id"))
        );
        List<JSONObject> transferList = mongoTemplate.find(query, JSONObject.class, getCollectionName());
        transferList = transferList.stream().map(tx -> {
            JSONObject txJson = getTx(tx);
            return txJson;
        }).collect(Collectors.toList());
        return transferList;
    }

    //isAccording 是否根据指定高度查出他以下的所有交易的分页（0或者null 否， 1 是）
    //proposalId 提案的id
    public PageModel<JSONObject> getTransaction(String blockHeight, String bigType, String smallType, String address, String proposalId, String voteType, Integer status, Integer pageIndex, Integer pageSize, String startTime, String endTime, Integer isAccording) throws Exception {
        Criteria criteria = new Criteria();
        Query query = new Query();
        if (address != null) {
            address = address.trim();
        }
        if (StringUtils.isNotBlank(bigType) && StringUtils.isBlank(smallType)) {
            getByType(criteria, bigType, address);
        }
        if (StringUtils.isNotBlank(smallType)) {
            switch (smallType.trim().toLowerCase()) {
                case "send":
                    Criteria send = Criteria.where("tx.body.messages.@type").is(msgSend);
                    if (StringUtils.isNotBlank(address)) {
                        send.orOperator(Criteria.where("tx.body.messages.from_address").is(address), Criteria.where("tx.body.messages.to_address").is(address));
                    }
                    criteria.andOperator(send);
                    break;
                case "transfer":
                    Criteria transfer = Criteria.where("tx.body.messages.@type").is(msgTransfer);
                    if (StringUtils.isNotBlank(address)) {
                        transfer.orOperator(Criteria.where("tx.body.messages.sender").is(address), Criteria.where("tx.body.messages.receiver").is(address));
                    }
                    criteria.andOperator(transfer);
                    break;
                case "multisend":
                    Criteria multi = Criteria.where("tx.body.messages.@type").is(msgMulti);
                    if (StringUtils.isNotBlank(address)) {
                        multi.orOperator(Criteria.where("tx.body.messages.inputs.address").is(address), Criteria.where("tx.body.messages.outputs.address").is(address));
                    }
                    criteria.andOperator(multi);
                    break;
                case "delegate":
                    Criteria delegator = Criteria.where("tx.body.messages.@type").is(msgDelegate);
                    if (StringUtils.isNotBlank(address)) {
                        delegator.orOperator(Criteria.where("tx.body.messages.delegator_address").is(address), Criteria.where("tx.body.messages.validator_address").is(address));
                    }
                    criteria.andOperator(delegator);
                    break;
                case "undelegate":
                    Criteria unDelegator = Criteria.where("tx.body.messages.@type").is(msgUnDelegate);
                    if (StringUtils.isNotBlank(address)) {
                        unDelegator.orOperator(Criteria.where("tx.body.messages.delegator_address").is(address), Criteria.where("tx.body.messages.validator_address").is(address));
                    }
                    criteria.andOperator(unDelegator);
                    break;
                case "beginredelegate":
                    Criteria beginRedelegate = Criteria.where("tx.body.messages.@type").is(msgBeginRedelegate);
                    if (StringUtils.isNotBlank(address)) {
                        beginRedelegate.orOperator(Criteria.where("tx.body.messages.validator_src_address").is(address), Criteria.where("tx.body.messages.validator_dst_address").is(address), Criteria.where("tx.body.messages.delegator_address").is(address));
                    }
                    criteria.andOperator(beginRedelegate);
                    break;
                case "withdrawdelegatorreward":
                    Criteria withdrawDelegatorReward = Criteria.where("tx.body.messages.@type").is(msgDelegateReward);
                    if (StringUtils.isNotBlank(address)) {
                        withdrawDelegatorReward.orOperator(Criteria.where("tx.body.messages.delegator_address").is(address), Criteria.where("tx.body.messages.validator_address").is(address));
                    }
                    criteria.andOperator(withdrawDelegatorReward);
                    break;
                case "setwithdrawaddress":
                    Criteria setWithdrawAddress = Criteria.where("tx.body.messages.@type").is(msgWithdrawAddress);
                    if (StringUtils.isNotBlank(address)) {
                        setWithdrawAddress.orOperator(Criteria.where("tx.body.messages.delegator_address").is(address), Criteria.where("tx.body.messages.validator_address").is(address));
                    }
                    criteria.andOperator(setWithdrawAddress);
                    break;
                case "validatorcommission":
                    Criteria withdrawValidatorCommission = Criteria.where("tx.body.messages.@type").is(msgWithdrawCommission);
                    if (StringUtils.isNotBlank(address)) {
                        withdrawValidatorCommission.orOperator(Criteria.where("tx.body.messages.delegator_address").is(address), Criteria.where("tx.body.messages.validator_address").is(address));
                    }
                    criteria.andOperator(withdrawValidatorCommission);
                    break;
                case "vote":
                    Criteria voter = Criteria.where("tx.body.messages.@type").is(msgVote);
                    if (StringUtils.isNotBlank(address)) {
                        voter.andOperator(Criteria.where("tx.body.messages.voter").is(address));
                    }
                    if (StringUtils.isNotBlank(proposalId)) {
                        query.addCriteria(Criteria.where("tx.body.messages.proposal_id").is(proposalId));
                    }
                    if (StringUtils.isNotBlank(voteType)) {
                        switch (voteType.trim().toLowerCase()) {
                            case "yes":
                                query.addCriteria(Criteria.where("tx.body.messages.option").is("VOTE_OPTION_YES"));
                                break;
                            case "no":
                                query.addCriteria(Criteria.where("tx.body.messages.option").is("VOTE_OPTION_NO"));
                                break;
                            case "nowithveto":
                                query.addCriteria(Criteria.where("tx.body.messages.option").is("VOTE_OPTION_NO_WITH_VETO"));
                                break;
                            case "abstain":
                                query.addCriteria(Criteria.where("tx.body.messages.option").is("VOTE_OPTION_ABSTAIN"));
                                break;
                        }
                    }
                    criteria.andOperator(voter);
                    break;
                case "deposit":
                    Criteria depositor = Criteria.where("tx.body.messages.@type").is(msgDeposit);
                    if (StringUtils.isNotBlank(address)) {
                        depositor.andOperator(Criteria.where("tx.body.messages.depositor").is(address));
                    }
                    criteria.andOperator(depositor);
                    break;
                case "editvalidator":
                    Criteria editValidator = Criteria.where("tx.body.messages.@type").is(msgEditValidator);
                    if (StringUtils.isNotBlank(address)) {
                        editValidator.andOperator(Criteria.where("tx.body.messages.validator_address").is(address));
                    }
                    criteria.andOperator(editValidator);
                    break;
                case "createvalidator":
                    Criteria createValidator = Criteria.where("tx.body.messages.@type").is(msgCreateValidator);
                    if (StringUtils.isNotBlank(address)) {
                        createValidator.andOperator(Criteria.where("tx.body.messages.validator_address").is(address));
                    }
                    criteria.andOperator(createValidator);
                    break;
                case "issuetoken":
                    Criteria issuetoken = Criteria.where("tx.body.messages.@type").is(msgIssueToken);
                    if (StringUtils.isNotBlank(address)) {
                        issuetoken.andOperator(Criteria.where("tx.body.messages.owner").is(address));
                    }
                    criteria.andOperator(issuetoken);
                    break;
                case "unlocktoken":
                    Criteria unlocktoken = Criteria.where("tx.body.messages.@type").is(msgUnlockToken);
                    if (StringUtils.isNotBlank(address)) {
                        unlocktoken.andOperator(Criteria.where("tx.body.messages.owner").is(address));
                    }
                    criteria.andOperator(unlocktoken);
                    break;
                case "jail":
                    Criteria unJail = Criteria.where("tx.body.messages.@type").is(msgUnJail);
                    if (StringUtils.isNotBlank(address)) {
                        unJail.andOperator(Criteria.where("tx.body.messages.validator_addr").is(address));
                    }
                    criteria.andOperator(unJail);
                    break;
                case "submitproposal":
                    Criteria submit = Criteria.where("tx.body.messages.@type").is(msgSubmitProposal);

                    if (StringUtils.isNotBlank(proposalId)) {
                        query.addCriteria(Criteria.where("tx_response.logs.events.attributes.value").is(proposalId));
                    }

                    if (StringUtils.isNotBlank(address)) {
                        submit.andOperator(Criteria.where("tx.body.messages.proposer").is(address));
                    }
                    criteria.andOperator(submit);
                    break;
                case "transfertokenowner":
                    Criteria transfertokenowner = Criteria.where("tx.body.messages.@type").is(msgTransferTokenOwner);
                    if (StringUtils.isNotBlank(address)) {
                        transfertokenowner.orOperator(Criteria.where("tx.body.messages.old_owner").is(address), Criteria.where("tx.body.messages.new_owner").is(address));
                    }
                    criteria.andOperator(transfertokenowner);
                    break;
                case "createpool":
                    Criteria criteria1 = new Criteria();
                    Criteria createPool = Criteria.where("tx.body.messages.@type").is(msgCreatePool);
                    Criteria createPool2 = Criteria.where("tx.body.messages.@type").is(msgCreatePool2);
                    Criteria createPool3 = Criteria.where("tx.body.messages.@type").is(msgCreateNftPool);
                    if (StringUtils.isNotBlank(address)) {
                        createPool.orOperator(Criteria.where("tx.body.messages.owner_address").is(address), Criteria.where("tx.body.messages.defi_address").is(address));
                        createPool2.andOperator(Criteria.where("tx.body.messages.pool_creator_address").is(address));
                        createPool3.orOperator(Criteria.where("tx.body.messages.creator").is(address), Criteria.where("tx.body.messages.pool_address").is(address),
                                Criteria.where("tx.body.messages.commission_address").is(address), Criteria.where("tx.body.messages.value_added_tax_address").is(address));
                    }
                    criteria1.orOperator(createPool, createPool2, createPool3);
                    criteria.andOperator(criteria1);
                    break;
                case "addpledge":
                    Criteria addpledge = Criteria.where("tx.body.messages.@type").is(msgAddPledge);
                    if (StringUtils.isNotBlank(address)) {
                        addpledge.andOperator(Criteria.where("tx.body.messages.owner_address").is(address));
                    }
                    criteria.andOperator(addpledge);
                    break;
                case "minttoken":
                    Criteria minttoken = Criteria.where("tx.body.messages.@type").is(msgMintToken);
                    if (StringUtils.isNotBlank(address)) {
                        minttoken.orOperator(Criteria.where("tx.body.messages.to").is(address), Criteria.where("tx.body.messages.owner").is(address));
                    }
                    criteria.andOperator(minttoken);
                    break;
                case "edittoken":
                    Criteria edittoken = Criteria.where("tx.body.messages.@type").is(msgEditToken);
                    if (StringUtils.isNotBlank(address)) {
                        edittoken.andOperator(Criteria.where("tx.body.messages.owner").is(address));
                    }
                    criteria.andOperator(edittoken);
                    break;
                case "placeorder":
                    Criteria placeorder = Criteria.where("tx.body.messages.@type").is(msgPlaceOrder);
                    if (StringUtils.isNotBlank(address)) {
                        placeorder.orOperator(Criteria.where("tx.body.messages.pool_address").is(address), Criteria.where("tx.body.messages.owner_address").is(address));
                    }
                    criteria.andOperator(placeorder);
                    break;
                case "agreeorderpair":
                    Criteria agreeorderpair = Criteria.where("tx.body.messages.@type").is(msgAgreeOrderPair);
                    if (StringUtils.isNotBlank(address)) {
                        agreeorderpair.orOperator(Criteria.where("tx.body.messages.pool_address").is(address), Criteria.where("tx.body.messages.delegate_address").is(address));
                    }
                    criteria.andOperator(agreeorderpair);
                    break;
                case "createclient":
                    Criteria createClient = Criteria.where("tx.body.messages.@type").is(msgCreateClient);
                    if (StringUtils.isNotBlank(address)) {
                        createClient.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(createClient);
                    break;
                case "updateclient":
                    Criteria updateClient = Criteria.where("tx.body.messages.@type").is(msgUpdateClient);
                    if (StringUtils.isNotBlank(address)) {
                        updateClient.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(updateClient);
                    break;
                case "connectionopeninit":
                    Criteria connectionOpenInit = Criteria.where("tx.body.messages.@type").is(msgConnectionOpenInit);
                    if (StringUtils.isNotBlank(address)) {
                        connectionOpenInit.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(connectionOpenInit);
                    break;
                case "connectionopenconfirm":
                    Criteria connectionopenconfirm = Criteria.where("tx.body.messages.@type").is(msgConnectionOpenConfirm);
                    if (StringUtils.isNotBlank(address)) {
                        connectionopenconfirm.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(connectionopenconfirm);
                    break;
                case "channelopeninit":
                    Criteria channelopeninit = Criteria.where("tx.body.messages.@type").is(msgChannelOpenInit);
                    if (StringUtils.isNotBlank(address)) {
                        channelopeninit.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(channelopeninit);
                    break;
                case "timeout":
                    Criteria timeout = Criteria.where("tx.body.messages.@type").is(msgTimeout);
                    if (StringUtils.isNotBlank(address)) {
                        timeout.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(timeout);
                    break;
                case "channelopenack":
                    Criteria channelopenack = Criteria.where("tx.body.messages.@type").is(msgChannelOpenAck);
                    if (StringUtils.isNotBlank(address)) {
                        channelopenack.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(channelopenack);
                    break;
                case "connectionopenack":
                    Criteria connectionopenack = Criteria.where("tx.body.messages.@type").is(msgConnectionOpenAck);
                    if (StringUtils.isNotBlank(address)) {
                        connectionopenack.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(connectionopenack);
                    break;
                case "connectionopentry":
                    Criteria connectionopentry = Criteria.where("tx.body.messages.@type").is(msgConnectionOpenTry);
                    if (StringUtils.isNotBlank(address)) {
                        connectionopentry.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(connectionopentry);
                    break;
                case "recvpacket":
                    Criteria recvpacket = Criteria.where("tx.body.messages.@type").is(msgRecvPacket);
                    if (StringUtils.isNotBlank(address)) {
                        recvpacket.orOperator(Criteria.where("tx.body.messages.signer").is(address), Criteria.where("tx_response.logs.events.attributes.value").is(address));
                    }
                    criteria.andOperator(recvpacket);
                    break;
                case "channelopentry":
                    Criteria channelopentry = Criteria.where("tx.body.messages.@type").is(msgChannelOpenTry);
                    if (StringUtils.isNotBlank(address)) {
                        channelopentry.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(channelopentry);
                    break;
                case "redeempledge":
                    Criteria redeempledge = Criteria.where("tx.body.messages.@type").is(msgRedeemPledge);
                    if (StringUtils.isNotBlank(address)) {
                        redeempledge.andOperator(Criteria.where("tx.body.messages.owner_address"));
                    }
                    criteria.andOperator(redeempledge);
                    break;
                case "createdefi":
                    Criteria createDefi = Criteria.where("tx.body.messages.@type").is(msgCreateDefi);
                    if (StringUtils.isNotBlank(address)) {
                        createDefi.orOperator(Criteria.where("tx.body.messages.delegator_address").is(address), Criteria.where("tx.body.messages.defi_address").is(address));
                    }
                    criteria.andOperator(createDefi);
                    break;
                case "burntoken":
                    Criteria burnToken = Criteria.where("tx.body.messages.@type").is(msgBurnToken);
                    if (StringUtils.isNotBlank(address)) {
                        burnToken.andOperator(Criteria.where("tx.body.messages.sender").is(address));
                    }
                    criteria.andOperator(burnToken);
                    break;
                case "burncoin":
                    Criteria burnCoin = Criteria.where("tx.body.messages.@type").is(msgBurnCoin);
                    if (StringUtils.isNotBlank(address)) {
                        burnCoin.andOperator(Criteria.where("tx.body.messages.address").is(address));
                    }
                    criteria.andOperator(burnCoin);
                    break;
                case "mintcoin":
                    Criteria mintCoin = Criteria.where("tx.body.messages.@type").is(msgMintCoin);
                    if (StringUtils.isNotBlank(address)) {
                        mintCoin.orOperator(Criteria.where("tx.body.messages.from_address").is(address), Criteria.where("tx.body.messages.to_address").is(address));
                    }
                    criteria.andOperator(mintCoin);
                    break;
                case "loancoin":
                    Criteria loanCoin = Criteria.where("tx.body.messages.@type").is(msgLoanCoin);
                    if (StringUtils.isNotBlank(address)) {
                        loanCoin.orOperator(Criteria.where("tx.body.messages.validator_dst_address").is(address), Criteria.where("tx.body.messages.validator_src_address").is(address));
                    }
                    criteria.andOperator(loanCoin);
                    break;
                case "revokeorder":
                    Criteria revokeorder = Criteria.where("tx.body.messages.@type").is(msgRevokeOrder);
                    if (StringUtils.isNotBlank(address)) {
                        revokeorder.orOperator(Criteria.where("tx.body.messages.pool_address").is(address), Criteria.where("tx.body.messages.owner_address").is(address));
                    }
                    criteria.andOperator(revokeorder);
                    break;
                case "channelopenconfirm":
                    Criteria channelopenconfirm = Criteria.where("tx.body.messages.@type").is(msgChannelOpenConfirm);
                    if (StringUtils.isNotBlank(address)) {
                        channelopenconfirm.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(channelopenconfirm);
                    break;
                case "acknowledgement":
                    Criteria acknowledgement = Criteria.where("tx.body.messages.@type").is(msgAcknowledgement);
                    if (StringUtils.isNotBlank(address)) {
                        acknowledgement.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(acknowledgement);
                    break;
                case "defidelegate":
                    Criteria defiDelegate = Criteria.where("tx.body.messages.@type").is(msgDefiDelegate);
                    if (StringUtils.isNotBlank(address)) {
                        defiDelegate.orOperator(Criteria.where("tx.body.messages.delegator_address").is(address), Criteria.where("tx.body.messages.defi_address").is(address));
                    }
                    criteria.andOperator(defiDelegate);
                    break;
                case "withdrawdefidelegatorreward":
                    Criteria withdrawDefiDelegatorReward = Criteria.where("tx.body.messages.@type").is(msgWithdrawDefiDelegatorReward);
                    if (StringUtils.isNotBlank(address)) {
                        withdrawDefiDelegatorReward.orOperator(Criteria.where("tx.body.messages.delegator_address").is(address), Criteria.where("tx.body.messages.defi_address").is(address));
                    }
                    criteria.andOperator(withdrawDefiDelegatorReward);
                    break;
                case "defiundelegate":
                    Criteria defiUndelegate = Criteria.where("tx.body.messages.@type").is(msgDefiUndelegate);
                    if (StringUtils.isNotBlank(address)) {
                        defiUndelegate.orOperator(Criteria.where("tx.body.messages.delegator_address").is(address), Criteria.where("tx.body.messages.defi_address").is(address));
                    }
                    criteria.andOperator(defiUndelegate);
                    break;
                case "swapwithinbatch":
                    Criteria swapWithinBatch = Criteria.where("tx.body.messages.@type").is(msgSwapWithinBatch);
                    if (StringUtils.isNotBlank(address)) {
                        swapWithinBatch.andOperator(Criteria.where("tx.body.messages.swap_requester_address").is(address));
                    }
                    criteria.andOperator(swapWithinBatch);
                    break;
                case "depositwithinbatch":
                    Criteria depositWithinBatch = Criteria.where("tx.body.messages.@type").is(msgDepositWithinBatch);
                    if (StringUtils.isNotBlank(address)) {
                        depositWithinBatch.andOperator(Criteria.where("tx.body.messages.depositor_address").is(address));
                    }
                    criteria.andOperator(depositWithinBatch);
                    break;
                case "withdrawwithinbatch":
                    Criteria withdrawWithinBatch = Criteria.where("tx.body.messages.@type").is(msgWithdrawWithinBatch);
                    if (StringUtils.isNotBlank(address)) {
                        withdrawWithinBatch.andOperator(Criteria.where("tx.body.messages.withdrawer_address").is(address));
                    }
                    criteria.andOperator(withdrawWithinBatch);
                    break;
                case "setorchestratoraddress":
                    Criteria setOrchestratorAddress = Criteria.where("tx.body.messages.@type").is(msgSetOrchestratorAddress);
                    if (StringUtils.isNotBlank(address)) {
                        setOrchestratorAddress.orOperator(Criteria.where("tx.body.messages.validator").is(address), Criteria.where("tx.body.messages.orchestrator").is(address));
                    }
                    criteria.andOperator(setOrchestratorAddress);
                    break;
                case "valsetconfirm":
                    Criteria valsetConfirm = Criteria.where("tx.body.messages.@type").is(msgValsetConfirm);
                    if (StringUtils.isNotBlank(address)) {
                        valsetConfirm.andOperator(Criteria.where("tx.body.messages.orchestrator").is(address));
                    }
                    criteria.andOperator(valsetConfirm);
                    break;
                case "depositclaim":
                    Criteria depositClaim = Criteria.where("tx.body.messages.@type").is(msgDepositClaim);
                    if (StringUtils.isNotBlank(address)) {
                        depositClaim.orOperator(Criteria.where("tx.body.messages.cosmos_receiver").is(address),
                                Criteria.where("tx.body.messages.orchestrator").is(address));
                    }
                    criteria.andOperator(depositClaim);
                    break;
                case "valsetupdatedclaim":
                    Criteria valsetUpdatedClaim = Criteria.where("tx.body.messages.@type").is(msgValsetUpdatedClaim);
                    if (StringUtils.isNotBlank(address)) {
                        valsetUpdatedClaim.andOperator(Criteria.where("tx.body.messages.orchestrator").is(address));
                    }
                    criteria.andOperator(valsetUpdatedClaim);
                    break;
                case "confirmbatch":
                    Criteria confirmBatch = Criteria.where("tx.body.messages.@type").is(msgConfirmBatch);
                    if (StringUtils.isNotBlank(address)) {
                        confirmBatch.andOperator(Criteria.where("tx.body.messages.orchestrator").is(address));
                    }
                    criteria.andOperator(confirmBatch);
                    break;
                case "requestbatch":
                    Criteria requestBatch = Criteria.where("tx.body.messages.@type").is(msgRequestBatch);
                    if (StringUtils.isNotBlank(address)) {
                        requestBatch.andOperator(Criteria.where("tx.body.messages.sender").is(address));
                    }
                    criteria.andOperator(requestBatch);
                    break;
                case "sendtoeth":
                    Criteria sendToEth = Criteria.where("tx.body.messages.@type").is(msgSendToEth);
                    if (StringUtils.isNotBlank(address)) {
                        sendToEth.andOperator(Criteria.where("tx.body.messages.sender").is(address));
                    }
                    criteria.andOperator(sendToEth);
                    break;
                case "withdrawclaim":
                    Criteria withdrawClaim = Criteria.where("tx.body.messages.@type").is(msgWithdrawClaim);
                    if (StringUtils.isNotBlank(address)) {
                        withdrawClaim.andOperator(Criteria.where("tx.body.messages.orchestrator").is(address));
                    }
                    criteria.andOperator(withdrawClaim);
                    break;
                case "submitethereumevent":
                    Criteria submitEthereumEvent = Criteria.where("tx.body.messages.@type").is(msgSubmitEthereumEvent);
                    if (StringUtils.isNotBlank(address)) {
                        submitEthereumEvent.orOperator(Criteria.where("tx.body.messages.signer").is(address), Criteria.where("tx.body.messages.event.cosmos_receiver").is(address));
                    }
                    criteria.andOperator(submitEthereumEvent);
                    break;
                case "submitethereumtxconfirmation":
                    Criteria submitEthereumTxConfirmation = Criteria.where("tx.body.messages.@type").is(msgSubmitEthereumTxConfirmation);
                    if (StringUtils.isNotBlank(address)) {
                        submitEthereumTxConfirmation.andOperator(Criteria.where("tx.body.messages.signer").is(address));
                    }
                    criteria.andOperator(submitEthereumTxConfirmation);
                    break;
                case "setbridgecommission":
                    Criteria setBridgeCommission = Criteria.where("tx.body.messages.@type").is(msgSetBridgeCommission);
                    if (StringUtils.isNotBlank(address)) {
                        setBridgeCommission.andOperator(Criteria.where("tx.body.messages.sender").is(address));
                    }
                    criteria.andOperator(setBridgeCommission);
                    break;
                case "delegatekeys":
                    Criteria delegateKeys = Criteria.where("tx.body.messages.@type").is(msgDelegateKeys);
                    if (StringUtils.isNotBlank(address)) {
                        delegateKeys.orOperator(Criteria.where("tx.body.messages.validator_address").is(address), Criteria.where("tx.body.messages.orchestrator_address").is(address));
                    }
                    criteria.andOperator(delegateKeys);
                    break;
                case "createvestingaccount":
                    Criteria createVestingAccount = Criteria.where("tx.body.messages.@type").is(msgCreateVestingAccount);
                    if (StringUtils.isNotBlank(address)) {
                        createVestingAccount.orOperator(Criteria.where("tx.body.messages.from_address").is(address), Criteria.where("tx.body.messages.to_address").is(address));
                    }
                    criteria.andOperator(createVestingAccount);
                    break;
                case "stake":
                    Criteria stake = Criteria.where("tx.body.messages.@type").is(msgStake);
                    if (StringUtils.isNotBlank(address)) {
                        stake.andOperator(Criteria.where("tx.body.messages.sender").is(address));
                    }
                    criteria.andOperator(stake);
                    break;
                case "withdraw":
                    Criteria withdraw = Criteria.where("tx.body.messages.@type").is(msgWithdraw);
                    if (StringUtils.isNotBlank(address)) {
                        withdraw.andOperator(Criteria.where("tx.body.messages.sender").is(address));
                    }
                    criteria.andOperator(withdraw);
                    break;
                case "destroypool":
                    Criteria destroyPool = Criteria.where("tx.body.messages.@type").is(msgDestroyPool);
                    if (StringUtils.isNotBlank(address)) {
                        destroyPool.andOperator(Criteria.where("tx.body.messages.creator").is(address));
                    }
                    criteria.andOperator(destroyPool);
                    break;
                case "farmcreatepool":
                    Criteria farmCreatePool = Criteria.where("tx.body.messages.@type").is(msgFarmCreatePool);
                    if (StringUtils.isNotBlank(address)) {
                        farmCreatePool.andOperator(Criteria.where("tx.body.messages.creator").is(address));
                    }
                    criteria.andOperator(farmCreatePool);
                    break;
                case "unbond":
                    Criteria unbond = Criteria.where("tx.body.messages.@type").is(msgUnbond);
                    if (StringUtils.isNotBlank(address)) {
                        unbond.andOperator(Criteria.where("tx.body.messages.sender").is(address));
                    }
                    criteria.andOperator(unbond);
                    break;
                case "mintnft":
                    Criteria mintNft = Criteria.where("tx.body.messages.@type").is(msgMintNft);
                    if (StringUtils.isNotBlank(address)) {
                        mintNft.orOperator(Criteria.where("tx.body.messages.sender").is(address), Criteria.where("tx.body.messages.recipient").is(address));
                    }
                    criteria.andOperator(mintNft);
                    break;
                case "bid":
                    Criteria bid = Criteria.where("tx.body.messages.@type").is(msgBid);
                    if (StringUtils.isNotBlank(address)) {
                        bid.orOperator(Criteria.where("tx.body.messages.sender").is(address), Criteria.where("tx.body.messages.pool_address").is(address));
                    }
                    criteria.andOperator(bid);
                    break;
                case "createorder":
                    Criteria createOrder = Criteria.where("tx.body.messages.@type").is(msgCreateOrder);
                    if (StringUtils.isNotBlank(address)) {
                        createOrder.orOperator(Criteria.where("tx.body.messages.sender").is(address), Criteria.where("tx.body.messages.pool_address").is(address));
                    }
                    criteria.andOperator(createOrder);
                    break;
                case "mintbatch":
                    Criteria mintBatch = Criteria.where("tx.body.messages.@type").is(msgMintBatch);
                    if (StringUtils.isNotBlank(address)) {
                        mintBatch.orOperator(Criteria.where("tx.body.messages.sender").is(address), Criteria.where("tx.body.messages.recipient").is(address));
                    }
                    criteria.andOperator(mintBatch);
                    break;
                case "frozennft":
                    Criteria frozenNft = Criteria.where("tx.body.messages.@type").is(msgFrozenNft);
                    if (StringUtils.isNotBlank(address)) {
                        frozenNft.andOperator(Criteria.where("tx.body.messages.sender").is(address));
                    }
                    criteria.andOperator(frozenNft);
                    break;
                case "transfernft":
                    Criteria transferNft = Criteria.where("tx.body.messages.@type").is(msgTransferNft);
                    if (StringUtils.isNotBlank(address)) {
                        transferNft.orOperator(Criteria.where("tx.body.messages.sender").is(address), Criteria.where("tx.body.messages.recipient").is(address));
                    }
                    criteria.andOperator(transferNft);
                    break;
                case "deleteorder":
                    Criteria deleteOrder = Criteria.where("tx.body.messages.@type").is(msgDeleteOrder);
                    if (StringUtils.isNotBlank(address)) {
                        deleteOrder.orOperator(Criteria.where("tx.body.messages.creator").is(address), Criteria.where("tx.body.messages.pool_address").is(address));
                    }
                    criteria.andOperator(deleteOrder);
                    break;
                case "agreeorder":
                    Criteria agreeorder = Criteria.where("tx.body.messages.@type").is(msgAgreeOrder);
                    if (StringUtils.isNotBlank(address)) {
                        agreeorder.orOperator(Criteria.where("tx.body.messages.creator").is(address), Criteria.where("tx.body.messages.pool_address").is(address));
                    }
                    criteria.andOperator(agreeorder);
                    break;
                case "updateorder":
                    Criteria updateOrder = Criteria.where("tx.body.messages.@type").is(msgUpdateOrder);
                    if (StringUtils.isNotBlank(address)) {
                        updateOrder.orOperator(Criteria.where("tx.body.messages.pool_creator").is(address), Criteria.where("tx.body.messages.pool_address").is(address));
                    }
                    criteria.andOperator(updateOrder);
                    break;
                case "updatepool":
                    Criteria updatePool = Criteria.where("tx.body.messages.@type").is(msgUpdatePool);
                    if (StringUtils.isNotBlank(address)) {
                        updatePool.orOperator(Criteria.where("tx.body.messages.creator").is(address), Criteria.where("tx.body.messages.pool").is(address),
                                Criteria.where("tx.body.messages.commission_address").is(address));
                    }
                    criteria.andOperator(updatePool);
                    break;
                case "bidorder":
                    Criteria bidOrder = Criteria.where("tx.body.messages.@type").is(msgBidOrder);
                    if (StringUtils.isNotBlank(address)) {
                        bidOrder.orOperator(Criteria.where("tx.body.sender").is(address), Criteria.where("tx.body.messages.pool_address").is(address));
                    }
                    criteria.andOperator(bidOrder);
                    break;
                default:
                    log.error("未知类型:" + smallType.trim().toLowerCase());
                    break;
            }
        }
        if (StringUtils.isNotBlank(address) && StringUtils.isBlank(bigType) && StringUtils.isBlank(smallType)) {
            address = address.trim();
            Criteria sendFrom = Criteria.where("tx.body.messages.from_address").is(address);
            Criteria sendTo = Criteria.where("tx.body.messages.to_address").is(address);
            Criteria transferFrom = Criteria.where("tx.body.messages.sender").is(address);
            Criteria transferTo = Criteria.where("tx.body.messages.receiver").is(address);
            Criteria multiFrom = Criteria.where("tx.body.messages.inputs.address").is(address);
            Criteria multiTo = Criteria.where("tx.body.messages.outputs.address").is(address);
            Criteria delegatorAddress = Criteria.where("tx.body.messages.delegator_address").is(address);
            Criteria validator = Criteria.where("tx.body.messages.validator_address").is(address);
            Criteria voteAddress = Criteria.where("tx.body.messages.voter").is(address);
            Criteria jailAddress = Criteria.where("tx.body.messages.validator_addr").is(address);
            Criteria depositorAddress = Criteria.where("tx.body.messages.depositor").is(address);
            Criteria editAddress = Criteria.where("tx.body.messages.validator_address").is(address);
            Criteria oldOwner = Criteria.where("tx.body.messages.old_owner").is(address);
            Criteria newOwner = Criteria.where("tx.body.messages.new_owner").is(address);
            Criteria submitAddress = Criteria.where("tx.body.messages.proposer").is(address);
            Criteria issueToken = Criteria.where("tx.body.messages.owner").is(address);
            Criteria to = Criteria.where("tx.body.messages.to").is(address);
            Criteria defiAddress = Criteria.where("tx.body.messages.defi_address").is(address);
            Criteria ownerAddress = Criteria.where("tx.body.messages.owner_address").is(address);
            Criteria poolAddress = Criteria.where("tx.body.messages.pool_address").is(address);
            Criteria delegateAddress = Criteria.where("tx.body.messages.delegate_address").is(address);
            Criteria signer = Criteria.where("tx.body.messages.signer").is(address);
            Criteria addressTx = Criteria.where("tx.body.messages.address").is(address);
            Criteria recipientTx = Criteria.where("tx.body.messages.recipient").is(address);
            Criteria ethereumEvent = Criteria.where("tx.body.messages.event.cosmos_receiver").is(address);
            Criteria ethereumFrom1 = Criteria.where("tx_response.logs.events.attributes.value").is(address)
                    .andOperator(Criteria.where("tx_response.logs.events.type").is("message"));
            Criteria ethereumFrom2 = Criteria.where("tx_response.logs.events.attributes.value").is(Keys.toChecksumAddress(AddressUtil.convertCosmosToEthAddress(address)))
                    .andOperator(Criteria.where("tx_response.logs.events.type").is("message"));
            Criteria ethereumTo = Criteria.where("tx.body.messages.to").is(Keys.toChecksumAddress(AddressUtil.convertCosmosToEthAddress(address)));

            Criteria creator = Criteria.where("tx.body.messages.creator").is(address);
            Criteria commission_address = Criteria.where("tx.body.messages.commission_address").is(address);
            Criteria value_added_tax_address = Criteria.where("tx.body.messages.value_added_tax_address").is(address);
            Criteria pool = Criteria.where("tx.body.messages.pool").is(address);
            Criteria pool_creator = Criteria.where("tx.body.messages.pool_creator").is(address);

            criteria.orOperator(sendFrom,
                    sendTo,
                    transferFrom,
                    transferTo,
                    multiFrom,
                    multiTo,
                    delegatorAddress,
                    validator,
                    voteAddress,
                    jailAddress,
                    depositorAddress,
                    editAddress,
                    submitAddress,
                    issueToken,
                    oldOwner,
                    newOwner,
                    to,
                    defiAddress,
                    ownerAddress,
                    poolAddress,
                    signer,
                    delegateAddress,
                    addressTx,
                    recipientTx,
                    ethereumEvent,
                    ethereumFrom1,
                    ethereumFrom2,
                    ethereumTo,
                    creator,
                    commission_address,
                    value_added_tax_address,
                    pool,
                    pool_creator);
        }

        query.addCriteria(criteria);
        if (status != null && status == 0) {
            query.addCriteria(Criteria.where("tx_response.code").is(0));
        }

        if (status != null && status == 1) {
            query.addCriteria(Criteria.where("tx_response.code").ne(0));
        }

        if (StringUtils.isNotBlank(startTime)) {
            if (StringUtils.isNotBlank(endTime)) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                endTime = df.format(TimeUtil.addOneDate(TimeUtil.StrToDateThird(endTime)));

                query.addCriteria(Criteria.where("tx_response.timestamp").gte(startTime).lte(endTime));
            } else {
                query.addCriteria(Criteria.where("tx_response.timestamp").gte(startTime));
            }
        }
        if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            endTime = df.format(TimeUtil.addOneDate(TimeUtil.StrToDateThird(endTime)));
            query.addCriteria(Criteria.where("tx_response.timestamp").lte(endTime));
        }

        if (StringUtils.isNotBlank(blockHeight)) {
            if (null == isAccording || isAccording == 0) {
                query.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
            } else if (isAccording == 1) {
                query.addCriteria(Criteria.where("tx_response.height").lte(blockHeight.trim()));
            }

        }

        long count = 0;
        if (StringUtils.isBlank(blockHeight) && StringUtils.isBlank(bigType) && StringUtils.isBlank(smallType) && StringUtils.isBlank(address)
                && StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime) && status == null) {
            count = mongoTemplate.getCollection(getCollectionName()).estimatedDocumentCount();
        } else {
            count = mongoTemplate.count(query, getCollectionName());
        }
        query.skip((pageIndex - 1) * pageSize);
        query.limit(pageSize);
        query.with(Sort.by(
                Sort.Order.desc("_id"))
        );
        List<JSONObject> transferList = mongoTemplate.find(query, JSONObject.class, getCollectionName());
        transferList = transferList.stream().map(tx -> {
            JSONObject txJson = getTx(tx);
            return txJson;
        }).collect(Collectors.toList());
        PageModel<JSONObject> pageModel = new PageModel<>(transferList, pageIndex, pageSize, count);
        return pageModel;
    }


    @SneakyThrows
    public JSONObject getAllTypeCount(String address, String blockHeight) throws Exception {
        Long delegatorCount = null;
        Long rewardCount = null;
        Long voteCount = null;
        Long jailCount = null;
        Long businessCount = null;
        Long transferCount = null;
        Long ibcCount = null;
        Long issuesCount = null;
        Long defiCount = null;
        Long ethBridgeCount = null;
        Long ammswapCount = null;

        Criteria transfer = new Criteria();
        if (address != null) {
            address = address.trim();
        }
        getByType(transfer, "transfer", address);
        Query transferQuery = new Query(transfer);
        if (blockHeight != null) {
            transferQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        transferCount = mongoTemplate.count(transferQuery, getCollectionName());

        Criteria delegator = new Criteria();
        getByType(delegator, "staking", address);
        Query delegatorQuery = new Query(delegator);
        if (blockHeight != null) {
            delegatorQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }

        delegatorCount = mongoTemplate.count(delegatorQuery, getCollectionName());

        Criteria reward = new Criteria();
        getByType(reward, "reward", address);
        Query rewardQuery = new Query(reward);
        if (blockHeight != null) {
            rewardQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }

        rewardCount = mongoTemplate.count(rewardQuery, getCollectionName());

        Criteria vote = new Criteria();
        getByType(vote, "vote", address);
        Query voteQuery = new Query(vote);
        if (blockHeight != null) {
            voteQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        voteCount = mongoTemplate.count(voteQuery, getCollectionName());

        Criteria jail = new Criteria();
        getByType(jail, "jail", address);
        Query jailQuery = new Query(jail);
        if (blockHeight != null) {
            jailQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        jailCount = mongoTemplate.count(jailQuery, getCollectionName());

        Criteria business = new Criteria();
        getByType(business, "business", address);
        Query businessQuery = new Query(business);
        if (blockHeight != null) {
            businessQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        businessCount = mongoTemplate.count(businessQuery, getCollectionName());

        Criteria ibc = new Criteria();
        getByType(ibc, "ibc", address);
        Query ibcQuery = new Query(ibc);
        if (blockHeight != null) {
            ibcQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        ibcCount = mongoTemplate.count(ibcQuery, getCollectionName());

        Criteria issues = new Criteria();
        getByType(issues, "issues", address);
        Query issuesQuery = new Query(issues);
        if (blockHeight != null) {
            issuesQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        issuesCount = mongoTemplate.count(issuesQuery, getCollectionName());

        Criteria defi = new Criteria();
        getByType(defi, "defi", address);
        Query defiQuery = new Query(defi);
        if (blockHeight != null) {
            defiQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        defiCount = mongoTemplate.count(defiQuery, getCollectionName());

        Criteria ethBridge = new Criteria();
        getByType(ethBridge, "ethbridge", address);
        Query ethBridgeQuery = new Query(ethBridge);
        if (blockHeight != null) {
            ethBridgeQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        ethBridgeCount = mongoTemplate.count(ethBridgeQuery, getCollectionName());

        Criteria ammswap = new Criteria();
        getByType(ammswap, "ammswap", address);
        Query ammswapQuery = new Query(ammswap);
        if (blockHeight != null) {
            ammswapQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        ammswapCount = mongoTemplate.count(ammswapQuery, getCollectionName());


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("transferCount", transferCount);
        jsonObject.put("delegatorCount", delegatorCount);
        jsonObject.put("rewardCount", rewardCount);
        jsonObject.put("voteCount", voteCount);
        jsonObject.put("businessCount", businessCount);
        jsonObject.put("jailCount", jailCount);
        jsonObject.put("ibcCount", ibcCount);
        jsonObject.put("issuesCount", issuesCount);
        jsonObject.put("defiCount", defiCount);
        jsonObject.put("ethBridgeCount", ethBridgeCount);
        jsonObject.put("ammswapCount", ammswapCount);
        Criteria ethereumtx = new Criteria();
        getByType(ethereumtx, "ethereumtx", address);
        Query ethereumtxQuery = new Query(ethereumtx);
        if (blockHeight != null) {
            ethereumtxQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        jsonObject.put("ethereumtx", mongoTemplate.count(ethereumtxQuery, getCollectionName()));

        Long nftCount = null;
        Criteria nft = new Criteria();
        getByType(nft, "nft", address);
        Query nftQuery = new Query(nft);
        if (blockHeight != null) {
            nftQuery.addCriteria(Criteria.where("tx_response.height").is(blockHeight.trim()));
        }
        jsonObject.put("nftCount", mongoTemplate.count(nftQuery, getCollectionName()));
        return jsonObject;
    }

    public void getByType(Criteria criteria, String bigType, String address) throws Exception {
        switch (bigType.trim().toLowerCase()) {
            case "transfer":
                Criteria send = Criteria.where("tx.body.messages.@type").in(msgSend, msgCreateVestingAccount, CreatePermanentVestingAccount);
                if (StringUtils.isNotBlank(address)) {
                    send.orOperator(Criteria.where("tx.body.messages.from_address").is(address), Criteria.where("tx.body.messages.to_address").is(address));
                }
                criteria.andOperator(send);
                break;
            case "staking":
                Criteria delegator = Criteria.where("tx.body.messages.@type").in(msgDelegate, msgUnDelegate, msgBeginRedelegate);
                if (StringUtils.isNotBlank(address)) {
                    Criteria delegators = Criteria.where("tx.body.messages.delegator_address").is(address);
                    Criteria validators = Criteria.where("tx.body.messages.validator_address").is(address);
                    delegator.orOperator(delegators, validators, Criteria.where("tx.body.messages.validator_src_address").is(address),
                            Criteria.where("tx.body.messages.validator_dst_address").is(address));
                }
                criteria.andOperator(delegator);
                break;
            case "issues":
                Criteria issue = Criteria.where("tx.body.messages.@type").in(msgIssueToken, msgUnlockToken, msgEditToken, msgMintToken,
                        msgBurnToken, msgBurnCoin, msgMintCoin, msgLoanCoin, msgTransferTokenOwner);
                if (StringUtils.isNotBlank(address)) {
                    issue.orOperator(Criteria.where("tx.body.messages.owner").is(address),
                            Criteria.where("tx.body.messages.to").is(address),
                            Criteria.where("tx.body.messages.sender").is(address),
                            Criteria.where("tx.body.messages.address").is(address),
                            Criteria.where("tx.body.messages.from_address").is(address),
                            Criteria.where("tx.body.messages.to_address").is(address),
                            Criteria.where("tx.body.messages.validator_dst_address").is(address),
                            Criteria.where("tx.body.messages.validator_src_address").is(address),
                            Criteria.where("tx.body.messages.old_owner").is(address),
                            Criteria.where("tx.body.messages.new_owner").is(address)
                    );
                }
                criteria.andOperator(issue);
                break;
            case "reward":
                Criteria withdrawDelegatorReward = Criteria.where("tx.body.messages.@type").in(msgDelegateReward, msgWithdrawAddress, msgWithdrawCommission);
                if (StringUtils.isNotBlank(address)) {
                    Criteria delegatorAddress = Criteria.where("tx.body.messages.delegator_address").is(address);
                    Criteria validatorAddress = Criteria.where("tx.body.messages.validator_address").is(address);
                    withdrawDelegatorReward.orOperator(delegatorAddress, validatorAddress);
                }
                criteria.andOperator(withdrawDelegatorReward);
                break;
            case "vote":
                Criteria editValidator = Criteria.where("tx.body.messages.@type").in(msgEditValidator, msgCreateValidator, msgSubmitProposal, msgVote,
                        msgDeposit);
                if (StringUtils.isNotBlank(address)) {
                    editValidator.orOperator(Criteria.where("tx.body.messages.validator_address").is(address),
                            Criteria.where("tx.body.messages.delegator_address").is(address),
                            Criteria.where("tx.body.messages.proposer").is(address),
                            Criteria.where("tx.body.messages.voter").is(address),
                            Criteria.where("tx.body.messages.depositor").is(address)
                    );
                }
                criteria.andOperator(editValidator);
                break;
            case "ibc":
                Criteria transfer = Criteria.where("tx.body.messages.@type").in(msgTransfer, msgAcknowledgement, msgTimeout, msgRecvPacket,
                        msgCreateClient, msgUpdateClient, msgConnectionOpenInit, msgConnectionOpenConfirm, msgChannelOpenInit, msgChannelOpenAck, msgConnectionOpenAck,
                        msgChannelOpenTry, msgConnectionOpenTry, msgChannelOpenConfirm);
                if (StringUtils.isNotBlank(address)) {
                    transfer.orOperator(Criteria.where("tx.body.messages.sender").is(address),
                            Criteria.where("tx.body.messages.receiver").is(address),
                            Criteria.where("tx.body.messages.signer").is(address),
                            Criteria.where("tx_response.logs.events.attributes.value").is(address));
                }
                criteria.andOperator(transfer);
                break;
            case "business":
                Criteria business = Criteria.where("tx.body.messages.@type").in(msgPlaceOrder, msgAgreeOrderPair, msgCreatePool, msgAddPledge, msgRedeemPledge, msgRevokeOrder);
                if (StringUtils.isNotBlank(address)) {
                    business.orOperator(Criteria.where("tx.body.messages.pool_address").is(address), Criteria.where("tx.body.messages.owner_address").is(address),
                            Criteria.where("tx.body.messages.delegate_address").is(address), Criteria.where("tx.body.messages.defi_address").is(address),
                            Criteria.where("tx_response.logs.events.attributes.value").is(address));
                }
                criteria.andOperator(business);
                break;
            case "ethbridge":
                Criteria ethBridge = Criteria.where("tx.body.messages.@type").in(msgSetOrchestratorAddress, msgValsetConfirm,
                        msgDepositClaim, msgValsetUpdatedClaim, msgConfirmBatch, msgRequestBatch, msgSendToEth, msgWithdrawClaim, msgSubmitEthereumEvent,
                        msgSubmitEthereumTxConfirmation, msgSetBridgeCommission, msgDelegateKeys);
                if (StringUtils.isNotBlank(address)) {
                    ethBridge.orOperator(Criteria.where("tx.body.messages.validator").is(address), Criteria.where("tx.body.messages.orchestrator").is(address),
                            Criteria.where("tx.body.messages.cosmos_receiver").is(address), Criteria.where("tx.body.messages.sender").is(address),
                            Criteria.where("tx.body.messages.validator_address").is(address), Criteria.where("tx.body.messages.signer").is(address),
                            Criteria.where("tx.body.messages.event.cosmos_receiver").is(address));
                }
                criteria.andOperator(ethBridge);
                break;
            case "defi":
                Criteria defi = Criteria.where("tx.body.messages.@type").in(msgCreateDefi, msgDefiDelegate, msgWithdrawDefiDelegatorReward, msgDefiUndelegate);
                if (StringUtils.isNotBlank(address)) {
                    defi.orOperator(
                            Criteria.where("tx.body.messages.defi_address").is(address), Criteria.where("tx.body.messages.delegator_address").is(address)
                    );
                }
                criteria.andOperator(defi);
                break;
            case "ammswap":
                Criteria ammswap = Criteria.where("tx.body.messages.@type").in(msgCreatePool2, msgSwapWithinBatch, msgDepositWithinBatch, msgWithdrawWithinBatch);
                if (StringUtils.isNotBlank(address)) {
                    ammswap.orOperator(Criteria.where("tx.body.messages.swap_requester_address").is(address),
                            Criteria.where("tx.body.messages.pool_creator_address").is(address),
                            Criteria.where("tx.body.messages.depositor_address").is(address),
                            Criteria.where("tx.body.messages.withdrawer_address").is(address)
                    );
                }
                criteria.andOperator(ammswap);
                break;
            case "farm":
                Criteria farm = Criteria.where("tx.body.messages.@type").in(msgStake, msgWithdraw, msgDestroyPool, msgFarmCreatePool, msgUnbond);
                if (StringUtils.isNotBlank(address)) {
                    farm.orOperator(Criteria.where("tx.body.messages.sender").is(address),
                            Criteria.where("tx.body.messages.creator").is(address)
                    );
                }
                criteria.andOperator(farm);
                break;
            case "jail":
                Criteria unJail = Criteria.where("tx.body.messages.@type").is(msgUnJail);
                if (StringUtils.isNotBlank(address)) {
                    unJail.andOperator(Criteria.where("tx.body.messages.validator_addr").is(address));
                }
                criteria.andOperator(unJail);
                break;
            case "nft":
                Criteria nft = Criteria.where("tx.body.messages.@type").in(msgMintNft, msgCreateNftPool, msgCreateOrder, msgUpdatePool, msgBid, msgMintBatch,
                        msgFrozenNft, msgTransferNft, msgDeleteOrder, msgAgreeOrder, msgUpdateOrder, msgBidOrder);
                if (StringUtils.isNotBlank(address)) {
                    nft.orOperator(Criteria.where("tx.body.messages.sender").is(address), Criteria.where("tx.body.messages.recipient").is(address),
                            Criteria.where("tx.body.messages.creator").is(address), Criteria.where("tx.body.messages.pool_address").is(address),
                            Criteria.where("tx.body.messages.commission_address").is(address), Criteria.where("tx.body.messages.value_added_tax_address").is(address),
                            Criteria.where("tx.body.messages.pool").is(address), Criteria.where("tx.body.messages.pool_creator").is(address)
                    );
                }
                criteria.andOperator(nft);
                break;
            case "ethtx":
                Criteria ethereumTx = Criteria.where("tx.body.messages.@type").in(msgEthereumTx);
                if (StringUtils.isNotBlank(address)) {
                    ethereumTx.orOperator(Criteria.where("tx_response.logs.events.attributes.value").is(address)
                                    .andOperator(Criteria.where("tx_response.logs.events.type").is("message")),
                            Criteria.where("tx_response.logs.events.attributes.value").is(Keys.toChecksumAddress(AddressUtil.convertCosmosToEthAddress(address)))
                                    .andOperator(Criteria.where("tx_response.logs.events.type").is("message")),
                            Criteria.where("tx.body.messages.to").is(Keys.toChecksumAddress(AddressUtil.convertCosmosToEthAddress(address)))
                    );
                }
                criteria.andOperator(ethereumTx);
                break;
            case "ethereumtx":
                ethereumTx = Criteria.where("tx.body.messages.@type").in(msgEthereumTx);
                if (StringUtils.isNotBlank(address)) {
                    ethereumTx.orOperator(Criteria.where("tx_response.logs.events.attributes.value").is(address)
                                    .andOperator(Criteria.where("tx_response.logs.events.type").is("message")),
                            Criteria.where("tx_response.logs.events.attributes.value").is(Keys.toChecksumAddress(AddressUtil.convertCosmosToEthAddress(address)))
                                    .andOperator(Criteria.where("tx_response.logs.events.type").is("message")),
                            Criteria.where("tx.body.messages.to").is(Keys.toChecksumAddress(AddressUtil.convertCosmosToEthAddress(address)))
                    );
                }
                criteria.andOperator(ethereumTx);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + bigType);
        }

    }

    public JSONObject getTx(JSONObject transfer) {
        JSONObject transaction = new JSONObject();
        JSONObject txResponse = transfer.getJSONObject("tx_response");
        JSONObject tx = transfer.getJSONObject("tx");
        transaction.put("txhash", txResponse.getString("txhash"));
        transaction.put("timestamp", txResponse.getString("timestamp"));
        transaction.put("gas_used", txResponse.getString("gas_used"));
        transaction.put("gas_wanted", txResponse.getString("gas_wanted"));
        transaction.put("height", txResponse.getString("height"));
        transaction.put("code", txResponse.getIntValue("code"));
        JSONArray fee = txResponse.getJSONObject("tx").getJSONObject("auth_info").getJSONObject("fee").getJSONArray("amount");
        if (fee != null && fee.size() > 0) {
            String demno = fee.getJSONObject(0).getString("denom");

            transaction.put("fee", new BigDecimal(fee.getJSONObject(0).getString("amount"))
                    .divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
            if (StringUtils.isNotBlank(demno)) {
                transaction.put("feeCoinName", demno.substring(1));
            }

        } else {
            transaction.put("fee", BigDecimal.ZERO);
            transaction.put("feeCoinName", configService.getChainName());
        }
        transaction.put("raw_log", txResponse.getString("raw_log"));
        transaction.put("chainName", configService.getChainName());
        transaction.put("memo", tx.getJSONObject("body").getString("memo"));

        JSONArray typeArray = tx.getJSONObject("body").getJSONArray("messages");
        if (typeArray != null && typeArray.size() > 1) {
            transaction.put("isMore", 1);
        } else {
            transaction.put("isMore", 0);
        }

        JSONArray messages = new JSONArray();
        typeArray.stream().forEach(typeMessage -> {

            JSONObject value = JSON.parseObject(JSON.toJSONString(typeMessage), Feature.DisableSpecialKeyDetect);
            String type = value.getString("@type");
            JSONObject txJson = new JSONObject();
            String types = type.substring(type.indexOf("Msg") + 3).toLowerCase();
            switch (types) {
                case "createpermanentvestingaccount":
                case "send":
//                    txJson.put("type", "Send");
                    txJson.put("type", StringTransferUtil.camelToSpace(type.substring(type.indexOf("Msg") + 3)));
                    txJson.put("from", value.getString("from_address"));
                    txJson.put("to", value.getString("to_address"));
                    txJson.put("chainName", configService.getChainName());
                    JSONObject amJson = value.getJSONArray("amount").getJSONObject(0);
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 0);
                    if (amJson != null) {
                        String tokenName = amJson.getString("denom");
                        if (configService.getCoinName().equalsIgnoreCase(tokenName)) {
                            txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                            txJson.put("coinName", configService.getCoinName1());
                        } else if (tokenName.startsWith("gravity")) {
                            String hash = tokenName.split("gravity")[1];
                            JSONObject contract = getContract(hash);
                            int decimals = contract.getIntValue("decimals");
                            String symbol = contract.getString("symbol");
                            String amount = amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, decimals))).setScale(decimals).toPlainString();
                            txJson.put("coinName", symbol);
                            txJson.put("amount", amount);
                        } else if (tokenName.startsWith("ibc/")) {
                            String hash = tokenName.substring(4);
                            String result = CosmosUtil.getTransferTokenByHash(configService.getConfig(ConfigUtil.chainUrlKey), hash);
                            if (result != null) {
                                JSONObject denomTrace = JSON.parseObject(result);
                                String coinName = denomTrace.getJSONObject("denom_trace").getString("base_denom");

                                if (coinName.startsWith("gravity")) {
                                    String contractAddress = coinName.split("gravity")[1];
                                    JSONObject contract = getContract(contractAddress);
                                    int decimals = contract.getIntValue("decimals");
                                    String symbol = contract.getString("symbol");
                                    String amount = amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, decimals))).setScale(decimals).toPlainString();
                                    txJson.put("amount", amount);
                                    txJson.put("coinName", symbol);
                                } else {
                                    txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                                    txJson.put("coinName", coinName.substring(1));
                                }

                            } else {
                                txJson.put("amount", amJson.getBigDecimal("amount").toPlainString());
                                txJson.put("coinName", tokenName);
                            }
                        } else {
                            JSONObject token = tokenService.getTokenByUnit(tokenName);
                            if (token == null) {
                                txJson.put("amount", amJson.getString("amount"));
                                txJson.put("coinName", tokenName);
                            } else {
                                txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString());
                                txJson.put("coinName", token.getString("symbol"));
                            }
                        }
                    } else {
                        txJson.put("amount", BigDecimal.ZERO.toPlainString());
                        txJson.put("coinName", configService.getCoinName1());
                    }
                    break;
                case "beginredelegate":
                    txJson.put("type", "Begin Redelegate");
                    txJson.put("from", value.getString("delegator_address"));
                    JSONObject srcV = getValidatorMoniker(value.getString("validator_src_address"));
                    JSONObject srcD = getValidatorMoniker(value.getString("validator_dst_address"));
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    if (srcV != null) {
                        txJson.put("validator_src_address", srcV);
                    } else {
                        txJson.put("validator_src_address", value.getString("validator_src_address"));
                    }
                    if (srcD != null) {
                        txJson.put("validator_dst_address", srcD);
                    } else {
                        txJson.put("validator_dst_address", value.getString("validator_dst_address"));
                    }
                    amJson = value.getJSONObject("amount");
                    txJson.put("chainName", configService.getChainName());
                    if (amJson != null) {
                        String tokenName = amJson.getString("denom");
                        if (configService.getCoinName().equalsIgnoreCase(tokenName)) {
                            txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                            txJson.put("coinName", configService.getCoinName1());
                        } else {
                            JSONObject token = tokenService.getTokenByUnit(tokenName);
                            if (token == null) {
                                txJson.put("amount", amJson.getString("amount"));
                                txJson.put("coinName", tokenName);
                            } else {
                                txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString());
                                txJson.put("coinName", token.getString("symbol"));
                            }
                        }
                    } else {
                        txJson.put("amount", BigDecimal.ZERO.toPlainString());
                        txJson.put("coinName", configService.getCoinName1());
                    }
                    break;
                case "withdrawdelegatorreward":
                    txJson.put("type", "Withdraw Delegator Reward");
                    txJson.put("chainName", configService.getChainName());
                    JSONObject fromV = getValidatorMoniker(value.getString("validator_address"));
                    if (fromV != null) {
                        txJson.put("from", fromV);
                        txJson.put("fromIsNull", 2);
                    } else {
                        txJson.put("from", value.getString("validator_address"));
                        txJson.put("fromIsNull", 0);
                    }
                    String to = value.getString("delegator_address");
                    txJson.put("to", to);
                    txJson.put("coinName", configService.getCoinName1());
                    txJson.put("toIsNull", 0);
                    txJson.put("amount", "0");
                    JSONArray eventLogs = txResponse.getJSONArray("logs");
                    eventLogs.stream().forEach(log -> {
                        JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                        events.stream().forEach(event -> {
                            JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                            if (txEvent.containsKey("type") && txEvent.getString("type").equals("transfer")) {
                                JSONArray attributes = txEvent.getJSONArray("attributes");
                                for (int i = 0; i < attributes.size(); i++) {
                                    JSONObject attribute = attributes.getJSONObject(i);
                                    String key = attribute.getString("value");
                                    if (i == 0 && key.equals(to)) {
                                        attribute = attributes.getJSONObject(i + 2);
                                        String amount = attribute.getString("value");
                                        if (StringUtils.isNotBlank(amount)) {
                                            if (amount.indexOf(",") > 0) {
                                                String[] amounts = amount.split(",");
                                                AmountUtil amountUtil = AmountUtil.initAmount(amounts[0], new BigDecimal(Math.pow(10, configService.getChaindecimal())), configService.getChaindecimal());
                                                String leftAmount = amountUtil.getAmount().toPlainString();
                                                if ("udga".equalsIgnoreCase(amountUtil.getCoin())) {
                                                    txJson.put("coinName", "dga");
                                                    leftAmount = amountUtil.getAmount().multiply(new BigDecimal(1000000)).toPlainString();
                                                }
                                                txJson.put("amount", leftAmount);

                                                AmountUtil amountUtil2 = AmountUtil.initAmount(amounts[1], new BigDecimal(Math.pow(10, configService.getChaindecimal())), configService.getChaindecimal());
                                                String rightAmount = amountUtil2.getAmount().toPlainString();
                                                if ("udga".equalsIgnoreCase(amountUtil2.getCoin())) {
                                                    txJson.put("rightCoinName", "dga");
                                                    rightAmount = amountUtil2.getAmount().multiply(new BigDecimal(1000000)).toPlainString();
                                                } else {
                                                    txJson.put("rightCoinName", amountUtil2.getCoin().substring(1));
                                                }
                                                txJson.put("rightAmount", rightAmount);
                                            } else {
                                                AmountUtil amountUtil = AmountUtil.initAmount(amount, new BigDecimal(Math.pow(10, configService.getChaindecimal())), configService.getChaindecimal());
                                                amount = amountUtil.getAmount().toPlainString();
                                                if ("udga".equalsIgnoreCase(amountUtil.getCoin())) {
                                                    txJson.put("coinName", "dga");
                                                    amount = amountUtil.getAmount().multiply(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString();
                                                }
                                                txJson.put("amount", amount);
                                            }
                                        } else {
                                            amount = "0";
                                            txJson.put("amount", amount);
                                        }
                                    }
                                }
                            }
                        });
                    });
                    break;
                case "createvalidator":
                    txJson.put("type", "Create Validator");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("from", value.getString("delegator_address"));
                    JSONObject validaot = getValidatorMoniker(value.getString("validator_address"));
                    if (validaot != null) {
                        txJson.put("to", validaot);
                        txJson.put("toIsNull", 2);
                        txJson.put("selfDelegateAmount", validaot.getString("selfBonded"));
                        txJson.put("coinName", configService.getCoinName1());
                    } else {
                        txJson.put("to", value.getString("validator_address"));
                        txJson.put("toIsNull", 0);
                        txJson.put("coinName", configService.getCoinName1());
                    }
                    txJson.put("fromIsNull", 0);
                    txJson.put("description", value.getJSONObject("description"));
                    txJson.put("commission", value.getJSONObject("commission"));
                    txJson.put("min_self_delegation", value.getBigInteger("min_self_delegation"));
                    String conAddress = PubkeyUtil.getAddressFromPubkey(value.getJSONObject("pubkey").getString("key"));
                    try {
                        conAddress = PubkeyUtil.hexToBech32(conAddress, configService.getBech32Prefix());
                    } catch (UnsupportedEncodingException e) {

                    }
                    txJson.put("consensus_pubkey", conAddress);
                    JSONObject valueJson = value.getJSONObject("value");
                    txJson.put("coinName", configService.getCoinName1());
                    String demon = valueJson.getString("denom");
                    String amount = valueJson.getString("amount");
                    if ("udga".equals(demon)) {
                        txJson.put("coinName", "dga");
                    }
                    if (valueJson != null) {
                        txJson.put("value", new BigDecimal(amount).divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                    } else {
                        txJson.put("value", 0);
                    }
                    break;
                case "delegate":
                    txJson.put("type", "Delegate");
                    txJson.put("chainName", configService.getChainName());
                    String delegateAddress = value.getString("delegator_address");
                    txJson.put("from", delegateAddress);
                    txJson.put("fromIsNull", 0);
                    JSONObject v = getValidatorMoniker(value.getString("validator_address"));
                    if (v != null) {
                        txJson.put("to", v);
                        txJson.put("toIsNull", 2);
                    } else {
                        txJson.put("to", value.getString("validator_address"));
                        txJson.put("toIsNull", 0);
                    }
                    amJson = value.getJSONObject("amount");
                    if (amJson != null) {
                        String tokenName = amJson.getString("denom");
                        if (configService.getCoinName().equalsIgnoreCase(tokenName)) {
                            txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).setScale(configService.getChaindecimal()).toPlainString());
                            txJson.put("coinName", configService.getCoinName1());
                        } else if ("udga".equalsIgnoreCase(tokenName)) {
                            txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(1000000)).setScale(6).toPlainString());
                            txJson.put("coinName", "dga");
                        } else {
                            JSONObject token = tokenService.getTokenByUnit(tokenName);
                            if (token == null) {
                                txJson.put("amount", amJson.getString("amount"));
                                txJson.put("coinName", tokenName);
                            } else {
                                txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString());
                                txJson.put("coinName", token.getString("symbol"));
                            }
                        }
                    } else {
                        txJson.put("amount", BigDecimal.ZERO.toPlainString());
                        txJson.put("coinName", configService.getCoinName1());
                    }

                    if (txResponse.getIntValue("code") == 0) {
                        JSONArray logArray = txResponse.getJSONArray("logs");
                        logArray.stream().forEach(log -> {
                            JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                            events.stream().forEach(event -> {
                                JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                                if (txEvent.containsKey("type") && txEvent.getString("type").equals("transfer")) {
                                    JSONArray attributes = txEvent.getJSONArray("attributes");
                                    for (int i = 0; i < attributes.size(); i++) {
                                        JSONObject attribute = attributes.getJSONObject(i);
                                        if ("recipient".equals(attribute.getString("key")) && delegateAddress.equals(attribute.getString("value"))) {
                                            attribute = attributes.getJSONObject(i + 2);
                                            if ("amount".equals(attribute.getString("key"))) {
                                                String reward = attribute.getString("value");
                                                AmountUtil amountUtil = AmountUtil.initAmount(reward, new BigDecimal(Math.pow(10, configService.getChaindecimal())), configService.getChaindecimal());
                                                reward = amountUtil.getAmount().toPlainString();
                                                if ("udga".equalsIgnoreCase(amountUtil.getCoin())) {
                                                    txJson.put("coinName", "dga");
                                                }
                                                txJson.put("AutoClaimReward", reward);
                                            }
                                        }
                                    }
                                } else {
                                    txJson.put("AutoClaimReward", 0);
                                }
                            });
                        });
                    } else {
                        txJson.put("AutoClaimReward", 0);
                    }
                    break;
                case "deposit":
                    txJson.put("type", "Deposit");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("from", value.getString("depositor"));
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    String proposalId = value.getString("proposal_id");
                    JSONObject proposal = voteService.getProposalByProposalId(proposalId);
                    if (proposal != null) {
                        txJson.put("tx_type", getType(proposal.getJSONObject("content").getString("@type")));
                        txJson.put("title", proposal.getJSONObject("content").getString("title"));
                    }
                    txJson.put("proposal_id", proposalId);
                    JSONArray jr = value.getJSONArray("amount");
                    txJson.put("coinName", configService.getCoinName1());
                    if (jr != null && jr.size() > 0) {
                        amJson = value.getJSONArray("amount").getJSONObject(0);
                        if (amJson != null) {
                            txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                        } else {
                            txJson.put("amount", 0);
                        }
                    } else {
                        txJson.put("amount", 0);
                    }
                    txJson.put("isValidator", 0);
                    break;
                case "editvalidator":
                    txJson.put("type", "Edit Validator");
                    txJson.put("chainName", configService.getChainName());
                    JSONObject editV = getValidatorMoniker(value.getString("validator_address"));
                    if (editV != null) {
                        txJson.put("from", editV);
                        txJson.put("fromIsNull", 2);
                    } else {
                        txJson.put("from", value.getString("validator_address"));
                        txJson.put("fromIsNull", 0);
                    }
                    txJson.put("commission_rate", value.getString("commission_rate"));
                    txJson.put("description", value.getJSONObject("description"));
                    txJson.put("min_self_delegation", value.getJSONObject("min_self_delegation"));
                    txJson.put("toIsNull", 1);
                    break;
                case "multisend":
                    txJson.put("type", "Multi Send");
                    txJson.put("chainName", configService.getChainName());
                    JSONObject input = value.getJSONArray("inputs").getJSONObject(0);
                    txJson.put("from", input.getString("address"));
                    JSONObject coin = input.getJSONArray("coins").getJSONObject(0);
                    txJson.put("coinName", configService.getCoinName1());
                    txJson.put("amount", coin.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                    JSONObject output = value.getJSONArray("outputs").getJSONObject(0);
                    txJson.put("to", output.getString("address"));
                    txJson.put("isValidator", 0);
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 0);
                    break;
                case "submitproposal":
                    txJson.put("type", "Submit Proposal");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("from", value.getString("proposer"));
                    JSONObject content = value.getJSONObject("content");
                    txJson.put("tx_type", getType(content.getString("@type")));
                    txJson.put("title", content.getString("title"));
                    txJson.put("description", content.getString("description"));
                    JSONArray ar = value.getJSONArray("initial_deposit");
                    txJson.put("coinName", configService.getCoinName1());
                    if (ar != null && ar.size() > 0) {
                        amJson = ar.getJSONObject(0);
                        if (amJson != null) {
                            txJson.put("initial_deposit", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                        } else {
                            txJson.put("initial_deposit", 0);
                        }
                    }
                    JSONArray logArray = txResponse.getJSONArray("logs");
                    logArray.stream().forEach(log -> {
                        JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                        events.stream().forEach(event -> {
                            JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                            if (txEvent.containsKey("type") && txEvent.getString("type").equals("submit_proposal")) {
                                JSONArray attributes = txEvent.getJSONArray("attributes");
                                for (int i = 0; i < attributes.size(); i++) {
                                    JSONObject attribute = attributes.getJSONObject(i);
                                    if (attribute.getString("key").equals("proposal_id")) {
                                        txJson.put("proposal_id", attribute.getIntValue("value"));
                                    }
                                }
                            }
                        });
                    });
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    break;
                case "transfer":
                    txJson.put("type", "Transfer");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("from", value.getString("sender"));
                    txJson.put("to", value.getString("receiver"));
                    amJson = value.getJSONObject("token");
                    if (amJson != null) {
                        String tokenName = amJson.getString("denom");
                        JSONObject token = getDecimal(tokenName);
                        amount = amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString();
                        txJson.put("amount", amount);
                        txJson.put("coinName", token.getString("coinName"));
                    } else {
                        txJson.put("amount", BigDecimal.ZERO.toPlainString());
                        txJson.put("coinName", configService.getCoinName1());
                    }
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 0);
                    break;
                case "undelegate":
                    txJson.put("type", "Undelegate");
                    txJson.put("chainName", configService.getChainName());
                    delegateAddress = value.getString("delegator_address");
                    txJson.put("from", delegateAddress);

                    JSONObject unDele = getValidatorMoniker(value.getString("validator_address"));
                    if (unDele != null) {
                        txJson.put("to", unDele);
                        txJson.put("toIsNull", 2);
                    } else {
                        txJson.put("to", value.getString("validator_address"));
                        txJson.put("toIsNull", 0);
                    }
                    amJson = value.getJSONObject("amount");
                    txJson.put("coinName", configService.getCoinName1());
                    if (amJson != null) {
                        String tokenName = amJson.getString("denom");
                        if (configService.getCoinName().equalsIgnoreCase(tokenName)) {
                            txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                            txJson.put("coinName", configService.getCoinName1());
                        } else {
                            JSONObject token = tokenService.getTokenByUnit(tokenName);
                            if (token == null) {
                                txJson.put("amount", amJson.getString("amount"));
                                txJson.put("coinName", tokenName);
                            } else {
                                txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString());
                                txJson.put("coinName", token.getString("symbol"));
                            }
                        }
                    } else {
                        txJson.put("amount", BigDecimal.ZERO.toPlainString());
                        txJson.put("coinName", configService.getCoinName1());
                    }
                    txJson.put("fromIsNull", 0);

                    if (txResponse.getIntValue("code") == 0) {
                        logArray = txResponse.getJSONArray("logs");
                        logArray.stream().forEach(log -> {
                            JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                            events.stream().forEach(event -> {
                                JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                                if (txEvent.containsKey("type") && txEvent.getString("type").equals("transfer")) {
                                    JSONArray attributes = txEvent.getJSONArray("attributes");
                                    for (int i = 0; i < attributes.size(); i++) {
                                        JSONObject attribute = attributes.getJSONObject(i);
                                        if ("recipient".equals(attribute.getString("key")) && delegateAddress.equals(attribute.getString("value"))) {
                                            attribute = attributes.getJSONObject(i + 2);
                                            if ("amount".equals(attribute.getString("key"))) {
                                                String reward = attribute.getString("value");
                                                AmountUtil amountUtil = AmountUtil.initAmount(reward, new BigDecimal(Math.pow(10, configService.getChaindecimal())), configService.getChaindecimal());
                                                reward = amountUtil.getAmount().toPlainString();
                                                if ("udga".equalsIgnoreCase(amountUtil.getCoin())) {
                                                    txJson.put("coinName", "dga");

                                                }
                                                txJson.put("AutoClaimReward", reward);
                                            }
                                        }
                                    }
                                } else {
                                    txJson.put("AutoClaimReward", 0);
                                }
                            });
                        });
                    } else {
                        txJson.put("AutoClaimReward", "0");
                    }
                    break;
                case "unjail":
                    txJson.put("type", "Unjail");
                    txJson.put("chainName", configService.getChainName());
                    JSONObject unJailV = getValidatorMoniker(value.getString("validator_addr"));
                    if (unJailV != null) {
                        txJson.put("to", unJailV);
                        txJson.put("toIsNull", 2);
                    } else {
                        txJson.put("to", value.getString("validator_addr"));
                        txJson.put("toIsNull", 0);
                    }
                    txJson.put("fromIsNull", 1);

                    break;
                case "vote":
                    txJson.put("type", "Vote");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("from", value.getString("voter"));
                    proposalId = value.getString("proposal_id");
                    proposal = voteService.getProposalByProposalId(proposalId);
                    if (proposal != null) {
                        txJson.put("tx_type", getType(proposal.getJSONObject("content").getString("@type")));
                        txJson.put("title", proposal.getJSONObject("content").getString("title"));
                    }
                    txJson.put("proposal_id", proposalId);
                    String option = value.getString("option");
                    switch (option) {
                        case "VOTE_OPTION_YES":
                            txJson.put("option", "YES");
                            break;
                        case "VOTE_OPTION_NO":
                            txJson.put("option", "NO");
                            break;
                        case "VOTE_OPTION_ABSTAIN":
                            txJson.put("option", "Abstain");
                            break;
                        case "VOTE_OPTION_NO_WITH_VETO":
                            txJson.put("option", "NoWithVeto");
                            break;
                    }
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    txJson.put("isValidator", 0);
                    break;
                case "withdrawvalidatorcommission":
                    txJson.put("type", "Withdraw Validator Commission");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("coinName", configService.getCoinName1());
                    JSONObject withDrawV = getValidatorMoniker(value.getString("validator_address"));
                    if (withDrawV != null) {
                        txJson.put("from", withDrawV);
                        txJson.put("fromIsNull", 2);
                    } else {
                        txJson.put("from", value.getString("validator_address"));
                        txJson.put("fromIsNull", 0);
                    }
                    eventLogs = txResponse.getJSONArray("logs");
                    eventLogs.stream().forEach(log -> {
                        JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                        events.stream().forEach(event -> {
                            JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                            if (txEvent.containsKey("type") && txEvent.getString("type").equals("withdraw_commission")) {
                                JSONArray attributes = txEvent.getJSONArray("attributes");
                                for (int i = 0; i < attributes.size(); i++) {
                                    JSONObject attribute = attributes.getJSONObject(i);
                                    String key = attribute.getString("key");
                                    if (key.equals("amount")) {
                                        String money = attribute.getString("value");
                                        if (StringUtils.isNotBlank(money)) {
                                            AmountUtil amountUtil = AmountUtil.initAmount(money, new BigDecimal(Math.pow(10, configService.getChaindecimal())), configService.getChaindecimal());
                                            money = amountUtil.getAmount().toPlainString();
                                            if ("udga".equalsIgnoreCase(amountUtil.getCoin())) {
                                                txJson.put("coinName", "dga");
                                            }
                                        } else {
                                            money = "0";
                                        }
                                        txJson.put("amount", money);
                                    }
                                }
                            }
                        });
                    });

                    txJson.put("toIsNull", 1);
                    break;
                case "issuetoken":
                    txJson.put("type", "Issue Token");
                    txJson.put("name", value.getString("name"));
                    Integer decimals = value.getInteger("decimals");
                    String symbol = value.getString("symbol");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("coinName", symbol);
                    txJson.put("symbol", symbol);
                    txJson.put("initial_supply", new BigDecimal(value.getString("initial_supply")).divide(new BigDecimal(Math.pow(10, decimals))));
                    txJson.put("smallest_unit", value.getString("smallest_unit"));
                    txJson.put("decimals", value.getString("decimals"));
                    txJson.put("total_supply", new BigDecimal(value.getString("total_supply")).divide(new BigDecimal(Math.pow(10, decimals))));
                    txJson.put("mintable", value.getString("mintable"));
                    txJson.put("from", value.getString("owner"));
                    txJson.put("isValidator", 0);
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    break;
                case "unlocktoken":
                    txJson.put("type", "Unlock Token");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("symbol", value.getString("symbol"));
                    txJson.put("from", value.getString("owner"));
                    txJson.put("isValidator", 0);
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    break;
                case "transfertokenowner":
                    txJson.put("type", "Transfer Token Owner");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("symbol", value.getString("symbol"));
                    txJson.put("from", value.getString("old_owner"));
                    txJson.put("to", value.getString("new_owner"));
                    txJson.put("isValidator", 0);
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 0);
                    break;
                case "minttoken":
                    txJson.put("type", "Mint Token");
                    symbol = value.getString("symbol");
                    JSONObject token = tokenService.getTokenBySymbol(symbol);
                    txJson.put("coinName", symbol);
                    if (token == null) {
                        txJson.put("amount", value.getString("amount"));

                    } else {
                        txJson.put("amount", new BigDecimal(value.getString("amount")).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString() + symbol);
                    }
                    txJson.put("symbol", symbol);
                    txJson.put("to", value.getString("to"));
                    txJson.put("from", value.getString("owner"));
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 0);
                    txJson.put("isValidator", 0);
                    break;
                case "createpool":
                    type = value.getString("@type");
                    if (msgFarmCreatePool.equals(type)) {
                        txJson.put("type", "Create Farm");
                        txJson.put("from", value.getString("creator"));
                        txJson.put("fromIsNull", 0);
                        txJson.put("toIsNull", 1);
                        txJson.put("name", value.getString("name"));
                        txJson.put("description", value.getString("description"));
                        txJson.put("startHeight", value.getLongValue("start_height"));
                        txJson.put("minStaking", value.getJSONObject("min_staking"));
                        txJson.put("rewardsPerBlock", value.getJSONArray("rewards_per_block"));
                        txJson.put("totalRewards", value.getJSONArray("total_rewards"));
                    } else {
                        txJson.put("type", "Create Pool");
                        JSONObject pledge = value.getJSONObject("pledge");
                        if (pledge != null) {
                            String tokenSmall = pledge.getString("denom");
                            if (tokenSmall.equals(configService.getCoinName())) {
                                txJson.put("coinName", configService.getCoinName1());
                                txJson.put("pledge", new BigDecimal(pledge.getString("amount")).divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                            } else {
                                JSONObject tokens = tokenService.getTokenByUnit(tokenSmall);
                                if (tokens != null && tokens.getInteger("decimals") != null) {
                                    txJson.put("coinName", tokens.getString("symbol"));
                                    txJson.put("pledge", pledge.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, tokens.getIntValue("decimals")))).toPlainString());
                                } else {
                                    txJson.put("coinName", tokenSmall);
                                    txJson.put("pledge", pledge.getString("amount"));
                                }
                            }
                            JSONObject defiAddress = getValidatorMoniker(value.getString("defi_address"));
                            if (defiAddress != null) {
                                txJson.put("defi_address", defiAddress);
                            } else {
                                txJson.put("defi_address", value.getString("defi_address"));
                            }
                            txJson.put("coinName", configService.getCoinName1());
                            txJson.put("from", value.getString("owner_address"));
                            txJson.put("fromIsNull", 0);
                            txJson.put("toIsNull", 1);
                        } else {
                            JSONArray depositCoins = value.getJSONArray("deposit_coins");
                            if (depositCoins != null) {
                                txJson.put("pledge", depositCoins.toJSONString());
                                txJson.put("pool_type_id", value.getString("pool_type_id"));
                                txJson.put("from", value.getString("pool_creator_address"));
                                txJson.put("fromIsNull", 0);
                                txJson.put("toIsNull", 1);
                            } else {
                                txJson.put("from", value.getString("creator"));
                                txJson.put("pool_type_id", value.getString("pool_type"));

                                txJson.put("poolAddress", value.getString("pool_address"));
                                txJson.put("valueAddedTaxAddress", value.getString("value_added_tax_address"));
                                txJson.put("commissionRate", value.getString("commission_rate"));
                                txJson.put("commissionAddress", value.getString("commission_address"));

                                txJson.put("fromIsNull", 0);
                                txJson.put("toIsNull", 1);
                            }
                        }
                    }
                    break;
                case "addpledge":
                    txJson.put("type", "Add Pledge");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("coinName", configService.getCoinName1());
                    JSONObject pledge = value.getJSONObject("pledge");
                    String tokenSmall = pledge.getString("denom");
                    if (tokenSmall.equals(configService.getCoinName())) {
                        txJson.put("coinName", configService.getCoinName1());
                        txJson.put("amount", new BigDecimal(pledge.getString("amount")).divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                    } else {
                        JSONObject tokens = tokenService.getTokenByUnit(tokenSmall);
                        if (tokens != null && tokens.getInteger("decimals") != null) {
                            txJson.put("coinName", tokens.getString("symbol"));
                            txJson.put("amount", pledge.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, tokens.getIntValue("decimals")))).toPlainString());
                        } else {
                            txJson.put("coinName", tokenSmall);
                            txJson.put("amount", pledge.getString("amount"));
                        }
                    }
                    txJson.put("from", value.getString("owner_address"));
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    txJson.put("isValidator", 0);
                    break;
                case "edittoken":
                    txJson.put("type", "Edit Token");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("mintable", value.getBoolean("mintable"));
                    txJson.put("symbol", value.getString("symbol"));
                    txJson.put("from", value.getString("owner"));
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    break;
                case "placeorder":
                    txJson.put("type", "Place Order");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("pool_address", value.getString("pool_address"));
                    txJson.put("from", value.getString("owner_address"));
                    txJson.put("expect_asset", value.getString("expect_asset"));
                    txJson.put("order_id", value.getString("order_id"));
                    JSONObject myAsset = value.getJSONObject("my_asset");
                    amount = myAsset.getString("amount");
                    String denom = myAsset.getString("denom");
                    if (denom.equals(configService.getCoinName())) {
                        txJson.put("my_asset", new BigDecimal(amount).divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString() + configService.getChainName());
                    } else {
                        token = tokenService.getTokenByUnit(denom);
                        if (token != null && token.getInteger("decimals") != null) {
                            txJson.put("my_asset", new BigDecimal(amount).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString() + token.getString("symbol"));
                        } else {
                            txJson.put("my_asset", amount + denom);
                        }
                    }
                    JSONObject price = JSON.parseObject(value.getString("price"));
                    String money = price.getString("amount");
                    String unit = price.getString("denom");
                    if (money != null) {
                        if (unit.equals(configService.getCoinName())) {
                            txJson.put("price", new BigDecimal(money).divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString() + configService.getChainName());
                        } else {
                            token = tokenService.getTokenByUnit(unit);
                            if (token != null && token.getInteger("decimals") != null) {
                                txJson.put("price", new BigDecimal(money).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString() + token.getString("symbol"));
                            } else {
                                txJson.put("price", money + unit);
                            }
                        }

                    } else {
                        txJson.put("price", 0 + unit);
                    }
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    break;
                case "agreeorderpair":
                    txJson.put("type", "Agree Order Pair");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("pool_address", value.getString("pool_address"));
                    txJson.put("from", value.getString("delegator_address"));
                    txJson.put("tx_pair", value.getString("tx_pair"));
                    txJson.put("left_order_id", value.getString("left_order_id"));
                    txJson.put("right_order_id", value.getString("right_order_id"));
                    price = JSON.parseObject(value.getString("price"));
                    money = price.getString("amount");
                    unit = price.getString("denom");
                    if (unit.equals(configService.getCoinName())) {
                        txJson.put("price", new BigDecimal(money).divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                        txJson.put("coinName", configService.getCoinName1());
                    } else {
                        if (money != null) {
                            token = tokenService.getTokenByUnit(unit);
                            if (token != null && token.getInteger("decimals") != null) {
                                txJson.put("price", new BigDecimal(money).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))));
                                txJson.put("coinName", token.getString("symbol"));
                            } else {
                                txJson.put("price", money);
                                txJson.put("coinName", unit);
                            }
                        } else {
                            txJson.put("price", 0);
                            txJson.put("coinName", unit);
                        }
                    }
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    break;
                case "setwithdrawaddress":
                    txJson.put("type", "Set Withdraw Address");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("from", value.getString("delegator_address"));
                    txJson.put("to", value.getString("withdraw_address"));
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 0);
                    break;
                case "createclient":
                    txJson.put("type", "Create Client");
                    txJson.put("chainName", configService.getChainName());
                    JSONObject clientState = value.getJSONObject("client_state");
                    txJson.put("chain_id", clientState.getString("chain_id"));
                    txJson.put("trusting_period", clientState.getString("trusting_period"));
                    txJson.put("unbonding_period", clientState.getString("unbonding_period"));
                    txJson.put("signer", value.getString("signer"));
                    JSONArray logs = txResponse.getJSONArray("logs");
                    logs.stream().forEach(log -> {
                        JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                        events.stream().forEach(event -> {
                            JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                            if (txEvent.containsKey("type") && txEvent.getString("type").equals("create_client")) {
                                JSONArray attributes = txEvent.getJSONArray("attributes");
                                for (int i = 0; i < attributes.size(); i++) {
                                    JSONObject attribute = attributes.getJSONObject(i);
                                    String key = attribute.getString("key");
                                    switch (key) {
                                        case "client_id":
                                            txJson.put("client_id", attribute.getString("value"));
                                            break;
                                        case "client_type":
                                            txJson.put("client_type", attribute.getString("value"));
                                            break;
                                    }
                                }
                            }
                        });
                    });
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "updateclient":
                    txJson.put("type", "Update Client");
                    txJson.put("signer", value.getString("signer"));
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("client_id", value.getString("client_id"));
                    JSONObject header = value.getJSONObject("header");
                    String height = header.getJSONObject("signed_header").getJSONObject("commit").getString("height");
                    txJson.put("height", height);
                    JSONObject version = header.getJSONObject("signed_header").getJSONObject("header").getJSONObject("version");
                    txJson.put("app", version.getIntValue("app"));
                    txJson.put("block", version.getIntValue("block"));
                    JSONObject headers = header.getJSONObject("signed_header").getJSONObject("header");
                    txJson.put("app_hash", headers.getString("app_hash"));
                    txJson.put("chain_id", headers.getString("chain_id"));
                    txJson.put("time", headers.getString("time"));
                    txJson.put("data_hash", headers.getString("data_hash"));
                    txJson.put("evidence_hash", headers.getString("evidence_hash"));
                    txJson.put("hash", headers.getJSONObject("last_block_id").getJSONObject("part_set_header").getString("hash"));
                    txJson.put("total", headers.getJSONObject("last_block_id").getJSONObject("part_set_header").getIntValue("total"));
                    txJson.put("consensus_hash", headers.getString("consensus_hash"));
                    txJson.put("validators_hash", headers.getString("validators_hash"));
                    txJson.put("last_commit_hash", headers.getString("last_commit_hash"));
                    txJson.put("proposer_address", headers.getString("proposer_address"));
                    txJson.put("last_results_hash", headers.getString("last_results_hash"));
                    txJson.put("next_validators_hash", headers.getString("next_validators_hash"));
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "connectionopenconfirm":
                    txJson.put("type", "Connection Open Confirm");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("signer", value.getString("signer"));
                    txJson.put("proof_ack", value.getString("proof_ack"));
                    txJson.put("revision_height", value.getJSONObject("proof_height").getString("revision_height"));
                    txJson.put("revision_number", value.getJSONObject("proof_height").getString("revision_number"));
                    txJson.put("connection_id", value.getString("connection_id"));
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "connectionopeninit":
                    txJson.put("type", "Connection Open Init");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("signer", value.getString("signer"));
                    txJson.put("connection_id", value.getJSONObject("counterparty").getString("connection_id"));
                    txJson.put("counterparty_client_id", value.getJSONObject("counterparty").getString("client_id"));
                    txJson.put("client_id", value.getString("client_id"));
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "channelopeninit":
                    txJson.put("type", "Channel Open Init");
                    txJson.put("signer", value.getString("signer"));
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("port_id", value.getString("port_id"));
                    JSONObject channel = value.getJSONObject("channel");
                    txJson.put("state", channel.getString("state"));
                    txJson.put("ordering", channel.getString("ordering"));
                    txJson.put("version", channel.getString("version"));
                    txJson.put("channel_id", channel.getJSONObject("counterparty").getString("channel_id"));
                    JSONArray hops = channel.getJSONArray("connection_hops");
                    if (hops != null && hops.size() > 0) {
                        txJson.put("connection_hops", hops.getString(0));
                    } else {
                        txJson.put("connection_hops", " ");
                    }
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "timeout":
                    txJson.put("type", "Timeout");
                    JSONObject packet = value.getJSONObject("packet");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("signer", value.getString("signer"));
                    txJson.put("data", packet.getString("data"));
                    txJson.put("sequence", packet.getString("sequence"));
                    txJson.put("source_port", packet.getString("source_port"));
                    txJson.put("source_channel", packet.getString("source_channel"));
                    txJson.put("revision_height", packet.getJSONObject("timeout_height").getString("revision_height"));
                    txJson.put("revision_number", packet.getJSONObject("timeout_height").getString("revision_number"));
                    txJson.put("destination_port", packet.getString("destination_port"));
                    txJson.put("timeout_timestamp", packet.getString("timeout_timestamp"));
                    txJson.put("destination_channel", packet.getString("destination_channel"));

                    txJson.put("proof_unreceived", value.getString("proof_unreceived"));
                    txJson.put("next_sequence_recv", value.getString("next_sequence_recv"));
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "channelopenack":
                    txJson.put("type", "Channel Open Ack");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("port_id", value.getString("port_id"));
                    txJson.put("channel_id", value.getString("channel_id"));
                    txJson.put("counterparty_channel_id", value.getString("counterparty_channel_id"));
                    txJson.put("counterparty_version", value.getString("counterparty_version"));
                    txJson.put("proof_try", value.getString("proof_try"));
                    txJson.put("signer", value.getString("signer"));
                    JSONObject proof_height = value.getJSONObject("proof_height");
                    txJson.put("revision_number", proof_height.getString("revision_number"));
                    txJson.put("revision_height", proof_height.getString("revision_height"));
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "connectionopenack":
                    txJson.put("type", "Connection Open Ack");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("connection_id", value.getString("connection_id"));
                    txJson.put("counterparty_connection_id", value.getString("counterparty_connection_id"));
                    txJson.put("signer", value.getString("signer"));
                    txJson.put("version", JSON.toJSONString(value.getJSONObject("version")));
                    txJson.put("proof_client", value.getString("proof_client"));
                    txJson.put("proof_consensus", value.getString("proof_consensus"));
                    txJson.put("proof_try", value.getString("proof_try"));
                    JSONObject client_stateJson = value.getJSONObject("client_state");
                    txJson.put("aiTeType", client_stateJson.getString("@type"));
                    txJson.put("chain_id", client_stateJson.getString("chain_id"));
                    txJson.put("proof_specs", JSON.toJSONString(client_stateJson.getJSONArray("proof_specs")));
                    txJson.put("numerator", client_stateJson.getJSONObject("trust_level").getString("numerator"));
                    txJson.put("denominator", client_stateJson.getJSONObject("trust_level").getString("denominator"));
                    txJson.put("upgrade_path", JSON.toJSONString(client_stateJson.getJSONArray("upgrade_path")));
                    txJson.put("revision_height", client_stateJson.getJSONObject("latest_height").getString("revision_height"));
                    txJson.put("revision_number", client_stateJson.getJSONObject("latest_height").getString("revision_number"));
                    txJson.put("max_clock_drift", client_stateJson.getString("max_clock_drift"));
                    txJson.put("trusting_period", client_stateJson.getString("trusting_period"));
                    txJson.put("unbonding_period", client_stateJson.getString("unbonding_period"));
                    txJson.put("allow_update_after_expiry", client_stateJson.getBoolean("allow_update_after_expiry"));
                    txJson.put("allow_update_after_misbehaviour", client_stateJson.getBoolean("allow_update_after_misbehaviour"));
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "connectionopentry":
                    txJson.put("type", "Connection Open Try");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("signer", value.getString("signer"));
                    txJson.put("client_id", value.getString("client_id"));
                    txJson.put("proof_init", value.getString("proof_init"));
                    JSONObject client_state = value.getJSONObject("client_state");
                    txJson.put("aiTeType", client_state.getString("@type"));
                    txJson.put("chain_id", client_state.getString("chain_id"));
                    txJson.put("proof_specs", JSON.toJSONString(client_state.getJSONArray("proof_specs")));
                    txJson.put("numerator", client_state.getJSONObject("trust_level").getString("numerator"));
                    txJson.put("denominator", client_state.getJSONObject("trust_level").getString("denominator"));
                    txJson.put("upgrade_path", JSON.toJSONString(client_state.getJSONArray("upgrade_path")));
                    txJson.put("revision_height", client_state.getJSONObject("latest_height").getString("revision_height"));
                    txJson.put("revision_number", client_state.getJSONObject("latest_height").getString("revision_number"));
                    txJson.put("max_clock_drift", client_state.getString("max_clock_drift"));
                    txJson.put("trusting_period", client_state.getString("trusting_period"));
                    txJson.put("unbonding_period", client_state.getString("unbonding_period"));
                    txJson.put("allow_update_after_expiry", client_state.getBoolean("allow_update_after_expiry"));
                    txJson.put("allow_update_after_misbehaviour", client_state.getBoolean("allow_update_after_misbehaviour"));
                    JSONObject counterparty = value.getJSONObject("counterparty");
                    txJson.put("key_prefix", counterparty.getJSONObject("prefix").getString("key_prefix"));
                    txJson.put("delay_period", value.getString("delay_period"));
                    txJson.put("proof_client", value.getString("proof_client"));
                    txJson.put("proof_consensus", value.getString("proof_consensus"));
                    txJson.put("counterparty_versions", JSON.toJSONString(value.getJSONArray("counterparty_versions")));
                    String previousConnectionId = value.getString("previous_connection_id");
                    if (StringUtils.isNotBlank(previousConnectionId)) {
                        txJson.put("previous_connection_id", previousConnectionId);
                    } else {
                        txJson.put("previous_connection_id", "--");
                    }
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "recvpacket":
                    txJson.put("type", "Recv Packet");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("signer", value.getString("signer"));
                    JSONObject packetJson = value.getJSONObject("packet");
                    txJson.put("data", packetJson.getString("data"));
                    txJson.put("sequence", packetJson.getString("sequence"));
                    txJson.put("source_port", packetJson.getString("source_port"));
                    txJson.put("source_channel", packetJson.getString("source_channel"));
                    txJson.put("destination_port", packetJson.getString("destination_port"));
                    txJson.put("timeout_timestamp", packetJson.getString("timeout_timestamp"));
                    txJson.put("destination_channel", packetJson.getString("destination_channel"));
                    JSONObject proofHeight = value.getJSONObject("proof_height");
                    txJson.put("revision_number", proofHeight.getString("revision_number"));
                    txJson.put("revision_height", proofHeight.getString("revision_height"));
                    txJson.put("proof_commitment", value.getString("proof_commitment"));
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 0);
                    logs = txResponse.getJSONArray("logs");
                    logs.stream().forEach(log -> {
                        JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                        events.stream().forEach(event -> {
                            JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                            if (txEvent.containsKey("type") && txEvent.getString("type").equals("recv_packet")) {
                                JSONArray attributes = txEvent.getJSONArray("attributes");
                                for (int i = 0; i < attributes.size(); i++) {
                                    JSONObject attribute = attributes.getJSONObject(i);
                                    String key = attribute.getString("key");
                                    if ("packet_data".equals(key)) {
                                        String json = attribute.getString("value");
                                        if (StringUtils.isNotBlank(json)) {
                                            JSONObject packetData = JSON.parseObject(json);
                                            txJson.put("from", packetData.getString("sender"));
                                            txJson.put("to", packetData.getString("receiver"));


                                            String coinName = packetData.getString("denom");
                                            if (coinName.contains("/")) {
                                                coinName = coinName.split("/")[2];
                                            }

                                            JSONObject recvAmountJson = getDecimal(coinName);
                                            String amounts = packetData.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, recvAmountJson.getIntValue("decimal")))).toPlainString();
                                            txJson.put("amount", amounts);
                                            txJson.put("coinName", recvAmountJson.getString("coinName"));
                                        }
                                    }
                                }
                            }
                        });
                    });
                    break;
                case "channelopentry":
                    txJson.put("type", "Channel Open Try");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("signer", value.getString("signer"));
                    JSONObject channelJson = value.getJSONObject("channel");
                    txJson.put("state", channelJson.getString("state"));
                    txJson.put("version", channelJson.getString("version"));
                    txJson.put("ordering", channelJson.getString("ordering"));
                    txJson.put("channel_id", channelJson.getJSONObject("counterparty").getString("channel_id"));
                    txJson.put("connection_hops", channelJson.getJSONArray("connection_hops").getString(0));
                    txJson.put("port_id", value.getString("port_id"));
                    String previousId = value.getString("previous_channel_id");
                    if (StringUtils.isNotBlank(previousId)) {
                        txJson.put("previous_channel_id", previousId);
                    } else {
                        txJson.put("previous_channel_id", "--");
                    }
                    txJson.put("proof_init", value.getString("proof_init"));
                    txJson.put("revision_number", value.getJSONObject("proof_height").getString("revision_number"));
                    txJson.put("revision_height", value.getJSONObject("proof_height").getString("revision_height"));
                    txJson.put("counterparty_version", value.getString("counterparty_version"));
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "redeempledge":
                    txJson.put("type", "Redeem Pledge");
                    txJson.put("from", value.getString("owner_address"));
                    pledge = value.getJSONObject("pledge");
                    tokenSmall = pledge.getString("denom");
                    if (tokenSmall.equals(configService.getCoinName())) {
                        txJson.put("coinName", configService.getCoinName1());
                        txJson.put("pledge", new BigDecimal(pledge.getString("amount")).divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                    } else {
                        txJson.put("coinName", tokenSmall);
                        JSONObject tokens = tokenService.getTokenByUnit(tokenSmall);
                        if (tokens != null && tokens.getInteger("decimals") != null) {
                            txJson.put("pledge", pledge.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, tokens.getIntValue("decimals")))).toPlainString());
                        } else {
                            txJson.put("pledge", pledge.getString("amount"));
                        }
                    }
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    break;
                case "createdefi":
                    txJson.put("type", "Create Defi");
                    txJson.put("chainName", configService.getChainName());
                    JSONObject description = value.getJSONObject("description");
                    txJson.put("moniker", description.getString("moniker"));
                    txJson.put("min_self_delegation", value.getString("min_self_delegation"));
                    txJson.put("from", value.getString("delegator_address"));
                    JSONObject defiV = getValidatorMoniker(value.getString("defi_address"));
                    if (defiV != null) {
                        txJson.put("to", defiV);
                        txJson.put("toIsNull", 2);
                    } else {
                        txJson.put("to", value.getString("defi_address"));
                        txJson.put("toIsNull", 0);
                    }
                    txJson.put("fromIsNull", 0);
                    break;
                case "burntoken":
                    txJson.put("type", "Burn Token");
                    symbol = value.getString("symbol");
                    token = tokenService.getTokenBySymbol(symbol);
                    txJson.put("symbol", symbol);
                    txJson.put("from", value.getString("sender"));
                    if (token != null && token.getInteger("decimals") != null) {
                        txJson.put("amount", new BigDecimal(value.getString("amount")).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString());
                    } else {
                        txJson.put("amount", value.getString("amount"));
                    }
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("coinName", symbol);
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    break;
                case "mintcoin":
                    txJson.put("type", "Mint Coin");
                    txJson.put("from", value.getString("from_address"));
                    txJson.put("to", value.getString("to_address"));
                    coin = value.getJSONObject("coin");
                    denom = coin.getString("denom");
                    txJson.put("chainName", configService.getChainName());
                    if (denom.equals(configService.getCoinName())) {
                        txJson.put("coinName", configService.getCoinName1());
                        txJson.put("coin", new BigDecimal(coin.getString("amount")).divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                        txJson.put("amount", new BigDecimal(coin.getString("amount")).divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString());
                    } else {
                        token = tokenService.getTokenByUnit(denom);
                        if (token != null && token.getInteger("decimals") != null) {
                            txJson.put("coinName", token.getString("symbol"));
                            txJson.put("coin", new BigDecimal(coin.getString("amount")).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString());
                            txJson.put("amount", new BigDecimal(coin.getString("amount")).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString());
                        } else {
                            txJson.put("coinName", denom);
                            txJson.put("amount", coin.getString("amount"));
                            txJson.put("coin", coin.getString("amount"));
                        }
                    }
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 0);
                    break;
                case "burncoin":
                    txJson.put("type", "Burn Coin");
                    txJson.put("from", value.getString("address"));
                    coin = value.getJSONObject("coin");
                    denom = coin.getString("denom");
                    txJson.put("chainName", configService.getChainName());
                    if (denom.equals(configService.getCoinName())) {
                        txJson.put("coinName", configService.getCoinName1());
                        amount = new BigDecimal(coin.getString("amount")).divide(new BigDecimal(Math.pow(10, configService.getChaindecimal()))).toPlainString();
                        txJson.put("coin", amount);
                        txJson.put("amount", amount);
                    } else {
                        token = tokenService.getTokenByUnit(denom);
                        if (token != null && token.getInteger("decimals") != null) {
                            txJson.put("coinName", token.getString("symbol"));
                            amount = new BigDecimal(coin.getString("amount")).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimals")))).toPlainString();
                            txJson.put("coin", amount);
                            txJson.put("amount", amount);
                        } else {
                            txJson.put("coinName", denom);
                            txJson.put("coin", coin.getString("amount"));
                            txJson.put("amount", coin.getString("amount"));
                        }
                    }
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    break;
                case "loancoin":
                    txJson.put("type", "Loan Coin");
                    txJson.put("chainName", configService.getChainName());
                    JSONObject loanV = getValidatorMoniker(value.getString("validator_src_address"));
                    if (loanV != null) {
                        txJson.put("from", loanV);
                    } else {
                        txJson.put("from", value.getString("validator_src_address"));
                    }
                    JSONObject loanDstV = getValidatorMoniker(value.getString("validator_dst_address"));
                    if (loanDstV != null) {
                        txJson.put("to", loanDstV);
                    } else {
                        txJson.put("to", value.getString("validator_dst_address"));
                    }
                    amJson = value.getJSONObject("amount");
                    if (amJson != null) {
                        txJson.put("coinName", configService.getCoinName1());
                        txJson.put("amount", amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, configService.getChaindecimal())), configService.getChaindecimal(), BigDecimal.ROUND_HALF_UP));
                    } else {
                        txJson.put("coinName", configService.getCoinName1());
                        tx.put("amount", BigDecimal.ZERO);
                    }
                    txJson.put("fromIsNull", 2);
                    txJson.put("toIsNull", 2);
                    break;
                case "revokeorder":
                    txJson.put("type", "Revoke Order");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("pool_address", value.getString("pool_address"));

                    logs = txResponse.getJSONArray("logs");
                    logs.stream().forEach(log -> {
                        JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                        events.stream().forEach(event -> {
                            JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                            if (txEvent.containsKey("type") && txEvent.getString("type").equals("revoke_order")) {
                                JSONArray attributes = txEvent.getJSONArray("attributes");
                                for (int i = 0; i < attributes.size(); i++) {
                                    JSONObject attribute = attributes.getJSONObject(i);
                                    String key = attribute.getString("key");
                                    if ("order-owner".equals(key)) {
                                        String ownerAddress = attribute.getString("value");
                                        txJson.put("owner_address", ownerAddress);
                                    }
                                }
                            }
                        });
                    });
                    txJson.put("tx_pair", value.getString("tx_pair"));
                    txJson.put("order_id", value.getString("order_id"));
                    txJson.put("is_left_order", value.getString("is_left_order"));
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    txJson.put("isValidator", 0);
                    break;
                case "channelopenconfirm":
                    txJson.put("type", "Channel Open Confirm");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("signer", value.getString("signer"));
                    txJson.put("channel_id", value.getString("channel_id"));
                    txJson.put("port_id", value.getString("port_id"));
                    txJson.put("proof_ack", value.getString("proof_ack"));
                    proofHeight = value.getJSONObject("proof_height");
                    txJson.put("revision_height", proofHeight.getString("revision_height"));
                    txJson.put("revision_number", proofHeight.getString("revision_number"));
                    txJson.put("fromIsNull", 1);
                    txJson.put("toIsNull", 1);
                    break;
                case "acknowledgement":
                    txJson.put("type", "Acknowledgement");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("signer", value.getString("signer"));
                    packet = value.getJSONObject("packet");
                    txJson.put("sequence", packet.getString("sequence"));
                    txJson.put("source_port", packet.getString("source_port"));
                    txJson.put("destination_port", packet.getString("destination_port"));
                    txJson.put("destination_channel", packet.getString("destination_channel"));
                    txJson.put("data", packet.getString("data"));
                    proof_height = value.getJSONObject("proof_height");
                    txJson.put("revision_number", proof_height.getString("revision_number"));
                    txJson.put("revision_height", proof_height.getString("revision_height"));
                    txJson.put("acknowledgement", value.getString("acknowledgement"));
                    break;
                case "defidelegate":
                    txJson.put("type", "Defi Delegate");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("from", value.getString("delegator_address"));
                    defiV = getValidatorMoniker(value.getString("defi_address"));
                    if (defiV != null) {
                        txJson.put("to", defiV);
                        txJson.put("toIsNull", 2);
                    } else {
                        txJson.put("to", value.getString("defi_address"));
                        txJson.put("toIsNull", 0);
                    }
                    txJson.put("fromIsNull", 0);

                    coin = value.getJSONObject("amount");
                    denom = coin.getString("denom");
                    token = getDecimal(denom);
                    amount = new BigDecimal(coin.getString("amount")).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString();
                    txJson.put("coinName", token.getString("coinName"));
                    txJson.put("amount", amount);
                    break;
                case "withdrawdefidelegatorreward":
                    txJson.put("type", "Withdraw Defi Delegator Reward");
                    txJson.put("chainName", configService.getChainName());

                    txJson.put("from", value.getString("delegator_address"));
                    defiV = getValidatorMoniker(value.getString("defi_address"));
                    if (defiV != null) {
                        txJson.put("to", defiV);
                        txJson.put("toIsNull", 2);
                    } else {
                        txJson.put("to", value.getString("defi_address"));
                        txJson.put("toIsNull", 0);
                    }
                    txJson.put("fromIsNull", 0);

                    if (txResponse.getIntValue("code") == 0) {
                        logArray = txResponse.getJSONArray("logs");
                        logArray.stream().forEach(log -> {
                            JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                            events.stream().forEach(event -> {
                                JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                                if (txEvent.containsKey("type") && txEvent.getString("type").equals("withdraw_rewards")) {
                                    JSONArray attributes = txEvent.getJSONArray("attributes");
                                    for (int i = 0; i < attributes.size(); i++) {
                                        JSONObject attribute = attributes.getJSONObject(i);
                                        if ("amount".equals(attribute.getString("key"))) {

                                            String reward = attribute.getString("value");
                                            if (StringUtils.isNotBlank(reward)) {
                                                AmountUtil amountUtil = AmountUtil.initAmount(reward, new BigDecimal(Math.pow(10, configService.getChaindecimal())), configService.getChaindecimal());
                                                reward = amountUtil.getAmount().toPlainString();
                                                JSONObject tokenJson = getDecimal(amountUtil.getCoin());
                                                txJson.put("coinName", tokenJson.getString("coinName"));
                                                txJson.put("amount", reward);
                                            } else {
                                                txJson.put("amount", BigDecimal.ZERO.toPlainString());
                                            }
                                        }
                                    }
                                } else {
                                    txJson.put("amount", 0);
                                    txJson.put("coinName", configService.getCoinName1());
                                }
                            });
                        });
                    } else {
                        txJson.put("amount", "0");
                    }
                    break;
                case "defiundelegate":
                    txJson.put("type", "Defi Undelegate");
                    txJson.put("chainName", configService.getChainName());

                    txJson.put("from", value.getString("delegator_address"));
                    defiV = getValidatorMoniker(value.getString("defi_address"));
                    if (defiV != null) {
                        txJson.put("to", defiV);
                        txJson.put("toIsNull", 2);
                    } else {
                        txJson.put("to", value.getString("defi_address"));
                        txJson.put("toIsNull", 0);
                    }
                    txJson.put("fromIsNull", 0);

                    coin = value.getJSONObject("amount");
                    denom = coin.getString("denom");
                    token = getDecimal(denom);
                    amount = new BigDecimal(coin.getString("amount")).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString();
                    txJson.put("coinName", token.getString("coinName"));
                    txJson.put("amount", amount);

                    break;
                case "swapwithinbatch":
                    txJson.put("type", "Swap Within Batch");
                    txJson.put("chainName", configService.getChainName());

                    String address = value.getString("swap_requester_address");
                    txJson.put("from", address);
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    JSONObject offerCoin = value.getJSONObject("offer_coin");
                    denom = offerCoin.getString("denom");//交易人出的资产
                    BigDecimal offerCoinAmount = offerCoin.getBigDecimal("amount");//交易人出的金额
                    JSONObject offerCoinFee = value.getJSONObject("offer_coin_fee");
                    token = getDecimal(denom);
                    txJson.put("offerCoin", token.getString("coinName"));
                    txJson.put("offerAmount", offerCoinAmount.divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString());

                    BigDecimal feePoolAmount = offerCoinFee.getBigDecimal("amount");//池收取的手续费金额
                    txJson.put("feePoolAmount", feePoolAmount.divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString());

                    String buyCoin = value.getString("demand_coin_denom");//要买的资产
                    token = getDecimal(buyCoin);
                    String orderPrice = value.getString("order_price");//资产的价格
                    BigDecimal buyAmount = offerCoinAmount.multiply(new BigDecimal(orderPrice));
                    txJson.put("buyCoin", token.getString("coinName"));
                    txJson.put("buyAmount", buyAmount.divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString());
                    break;
                case "depositwithinbatch":
                    txJson.put("type", "Deposit Within Batch");
                    txJson.put("chainName", configService.getChainName());

                    address = value.getString("depositor_address");
                    txJson.put("from", address);
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    String poolId = value.getString("pool_id");
                    txJson.put("poolId", poolId);
                    JSONArray depositCoins = value.getJSONArray("deposit_coins");
                    JSONObject leftCoin = depositCoins.getJSONObject(0);
                    JSONObject rightCoin = depositCoins.getJSONObject(1);

                    String left = leftCoin.getString("denom");
                    BigDecimal leftAmount = leftCoin.getBigDecimal("amount");
                    token = getDecimal(left);
                    txJson.put("leftCoin", token.getString("coinName"));
                    txJson.put("leftAmount", leftAmount.divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString());
                    String txPairName = token.getString("coinName");

                    String right = rightCoin.getString("denom");
                    BigDecimal rightAmount = rightCoin.getBigDecimal("amount");
                    token = getDecimal(right);
                    txJson.put("rightCoin", token.getString("coinName"));
                    txJson.put("rightAmount", rightAmount.divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString());
                    txPairName = txPairName + "/" + token.getString("coinName");
                    txJson.put("txPairName", txPairName);
                    break;
                case "withdrawwithinbatch":
                    txJson.put("type", "Withdraw Within Batch");
                    txJson.put("chainName", configService.getChainName());

                    address = value.getString("withdrawer_address");
                    txJson.put("from", address);
                    txJson.put("fromIsNull", 0);
                    txJson.put("toIsNull", 1);
                    JSONObject poolCoin = value.getJSONObject("pool_coin");
                    String poolCoinDenom = poolCoin.getString("denom");
                    BigDecimal withdrawAmount = poolCoin.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, configService.getChaindecimal())));//获取移除的pool资产价格

                    txJson.put("coinName", poolCoinDenom);
                    txJson.put("amount", withdrawAmount);
                    break;
                case "setorchestratoraddress":
                    txJson.put("type", "Set Orchestrator Address");
                    txJson.put("chainName", configService.getChainName());
                    txJson.put("toIsNull", 1);
                    defiV = getValidatorMoniker(value.getString("validator"));
                    if (defiV != null) {
                        txJson.put("from", defiV);
                        txJson.put("fromIsNull", 2);
                    } else {
                        txJson.put("from", value.getString("validator"));
                        txJson.put("fromIsNull", 0);
                    }
                    txJson.put("orchestrator", value.getString("orchestrator"));
                    txJson.put("ethAddress", value.getString("eth_address"));
                    break;
                case "valsetconfirm":
                    txJson.put("type", "Valset Confirm");
                    txJson.put("chainName", configService.getChainName());
                    long nonce = value.getLong("nonce");
                    String orchestrator = value.getString("orchestrator");
                    String ethAddress = value.getString("eth_address");
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 1);
                    txJson.put("nonce", nonce);
                    txJson.put("orchestrator", orchestrator);
                    txJson.put("ethAddress", ethAddress);
                    txJson.put("signature", value.getString("signature"));
                    break;
                case "depositclaim":
                    txJson.put("type", "Deposit Claim");
                    String receiver = value.getString("cosmos_receiver");
                    String ethSend = value.getString("ethereum_sender");
                    long ethBlockHeight = value.getLongValue("block_height");
                    long eventNonce = value.getLongValue("event_nonce");

                    txJson.put("from", ethSend);
                    txJson.put("to", receiver);
                    txJson.put("toIsNull", 0);
                    txJson.put("fromIsNull", 0);

                    txJson.put("ethBlockHeight", ethBlockHeight);
                    txJson.put("eventNonce", eventNonce);
                    String contractAddress = value.getString("token_contract");
                    txJson.put("contractAddress", contractAddress);
                    txJson.put("eventNonce", eventNonce);
                    JSONObject contract = getContract(contractAddress);
                    int decimal = contract.getIntValue("decimal");
                    amount = value.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, decimal))).setScale(decimal).toPlainString();
                    txJson.put("amount", amount);
                    txJson.put("coinName", contract.getString("symbol"));
                    break;
                case "valsetupdatedclaim":
                    txJson.put("type", "Valset Updated Claim");
                    ethBlockHeight = value.getLongValue("block_height");
                    eventNonce = value.getLongValue("event_nonce");
                    long valsetNonce = value.getLong("valset_nonce");
                    String ethereumAddress = value.getJSONArray("members").getJSONObject(0).getString("ethereum_address");
                    orchestrator = value.getString("orchestrator");

                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 1);
                    txJson.put("ethBlockHeight", ethBlockHeight);
                    txJson.put("eventNonce", eventNonce);
                    txJson.put("valsetNonce", valsetNonce);
                    txJson.put("ethereum_address", ethereumAddress);
                    txJson.put("orchestrator", orchestrator);
                    break;
                case "sendtoeth":
                    txJson.put("type", "Send To Eth");
                    String sender = value.getString("sender");
                    String ethDest = value.getString("eth_dest");

                    txJson.put("from", sender);
                    txJson.put("to", ethDest);
                    txJson.put("toIsNull", 0);
                    txJson.put("fromIsNull", 0);

                    JSONObject amountJson = value.getJSONObject("amount");
                    JSONObject bridgeFeeJson = value.getJSONObject("bridge_fee");
                    denom = amountJson.getString("denom");
                    if (denom.startsWith("gravity")) {
                        denom = denom.split("gravity")[1];
                        contract = getContract(denom);
                        decimals = contract.getIntValue("decimals");
                        symbol = contract.getString("symbol");
                        amount = amountJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, decimals))).setScale(decimals).toPlainString();
                        txJson.put("coinName", symbol);
                        txJson.put("amount", amount);
                        String bridgeFee = bridgeFeeJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, decimals))).setScale(decimals).toPlainString();
                        txJson.put("bridgeFee", bridgeFee);
                    }

                    logs = txResponse.getJSONArray("logs");
                    if (logs.size() > 0) {
                        logs.toJavaList(JSONObject.class).forEach(log -> {
                            log.getJSONArray("events").toJavaList(JSONObject.class).stream().forEach(event -> {
                                if ("withdrawal_received".equals(event.getString("type"))) {
                                    JSONArray attributes = event.getJSONArray("attributes");
                                    for (int i = 0; i < attributes.size(); i++) {
                                        JSONObject attribute = attributes.getJSONObject(i);
                                        if ("outgoing_tx_id".equals(attribute.getString("key"))) {
                                            long outGoingTxId = attribute.getLong("value");
                                            txJson.put("outgoingTxId", outGoingTxId);
                                        }
                                    }
                                }
                            });
                        });
                    }
                    break;
                case "confirmbatch"://gauss跨eth打包确认
                    txJson.put("type", "Confirm Batch");
                    contractAddress = value.getString("token_contract");
                    contract = getContract(contractAddress);
                    txJson.put("contractAddress", contractAddress);
                    txJson.put("coinName", contract.getString("symbol"));

                    ethAddress = value.getString("eth_signer");
                    orchestrator = value.getString("orchestrator");
                    nonce = value.getLong("nonce");

                    txJson.put("from", ethAddress);
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);

                    txJson.put("nonce", nonce);
                    txJson.put("orchestrator", orchestrator);
                    txJson.put("signature", value.getString("signature"));
                    break;
                case "requestbatch":
                    txJson.put("type", "Request Batch");
                    sender = value.getString("sender");
                    txJson.put("from", sender);
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    denom = value.getString("denom");
                    if (denom.startsWith("gravity")) {
                        denom = denom.split("gravity")[1];
                        contract = getContract(denom);
                        txJson.put("contractAddress", denom);
                        txJson.put("coinName", contract.getString("symbol"));
                    }
                    logs = txResponse.getJSONArray("logs");
                    if (logs.size() > 0) {
                        logs.toJavaList(JSONObject.class).stream().forEach(log -> {
                            log.getJSONArray("events").toJavaList(JSONObject.class).stream().forEach(event -> {
                                if ("outgoing_batch".equals(event.getString("type"))) {
                                    JSONArray attributes = event.getJSONArray("attributes");
                                    for (int i = 0; i < attributes.size(); i++) {
                                        JSONObject attribute = attributes.getJSONObject(i);
                                        if ("nonce".equals(attribute.getString("key"))) {
                                            long nonceBatch = attribute.getLong("value");
                                            txJson.put("batchNonce", nonceBatch);
                                        }
                                    }
                                }
                            });
                        });
                    }

                    break;
                case "withdrawclaim":
                    txJson.put("type", "Withdraw Claim");
                    eventNonce = value.getLongValue("event_nonce");
                    ethBlockHeight = value.getLongValue("block_height");
                    contractAddress = value.getString("token_contract");
                    long bathNonce = value.getLongValue("batch_nonce");
                    orchestrator = value.getString("orchestrator");

                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 1);

                    txJson.put("eventNonce", eventNonce);
                    txJson.put("ethBlockHeight", ethBlockHeight);
                    txJson.put("contractAddress", contractAddress);
                    txJson.put("orchestrator", orchestrator);
                    txJson.put("bathNonce", bathNonce);
                    break;
                case "submitethereumevent":
                    txJson.put("type", "Submit Ethereum Event");
                    txJson.put("event", value.getJSONObject("event"));
                    txJson.put("signer", value.getString("signer"));
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 1);
                    break;
                case "submitethereumtxconfirmation":
                    txJson.put("type", "submit Ethereum Tx Confirmation");
                    txJson.put("confirmation", value.getJSONObject("confirmation"));
                    txJson.put("signer", value.getString("signer"));
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 1);
                    break;
                case "sendtoethereum":
                    txJson.put("type", "Send To Ethereum");
                    sender = value.getString("sender");
                    ethDest = value.getString("ethereum_recipient");

                    txJson.put("from", sender);
                    txJson.put("to", ethDest);
                    txJson.put("toIsNull", 0);
                    txJson.put("fromIsNull", 0);

                    amountJson = value.getJSONObject("amount");
                    bridgeFeeJson = value.getJSONObject("bridge_fee");
                    denom = amountJson.getString("denom");
                    if (denom.startsWith("gravity")) {
                        denom = denom.split("gravity")[1];
                        contract = getContract(denom);
                        decimals = contract.getIntValue("decimals");
                        symbol = contract.getString("symbol");
                        amount = amountJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, decimals))).setScale(decimals).toPlainString();
                        txJson.put("coinName", symbol);
                        txJson.put("amount", amount);
                        String bridgeFee = bridgeFeeJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, decimals))).setScale(decimals).toPlainString();
                        txJson.put("bridgeFee", bridgeFee);
                    }

                    logs = txResponse.getJSONArray("logs");
                    if (logs.size() > 0) {
                        logs.toJavaList(LinkedHashMap.class).forEach(log -> {
                            JSONObject eventLog = JSON.parseObject(JSON.toJSONString(log));
                            eventLog.getJSONArray("events").toJavaList(JSONObject.class).stream().forEach(event -> {
                                if ("withdrawal_received".equals(eventLog.getString("type"))) {
                                    JSONArray attributes = eventLog.getJSONArray("attributes");
                                    for (int i = 0; i < attributes.size(); i++) {
                                        JSONObject attribute = attributes.getJSONObject(i);
                                        if ("outgoing_tx_id".equals(attribute.getString("key"))) {
                                            long outGoingTxId = attribute.getLong("value");
                                            txJson.put("outgoingTxId", outGoingTxId);
                                        }
                                    }
                                }
                            });
                        });
                    }
                    break;
                case "setbridgecommission":
                    txJson.put("type", "Set Bridge Commission");
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    txJson.put("from", value.getString("sender"));
                    txJson.put("remove", value.getString("remove"));
                    amountJson = value.getJSONObject("commission");
                    denom = amountJson.getString("denom");
                    if (denom.startsWith("gravity")) {
                        denom = denom.split("gravity")[1];
                        contract = getContract(denom);
                        decimals = contract.getIntValue("decimals");
                        symbol = contract.getString("symbol");
                        amount = amountJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, decimals))).setScale(decimals).toPlainString();
                        txJson.put("coinName", symbol);
                        txJson.put("amount", amount);
                    }
                    break;
                case "delegatekeys":
                    txJson.put("type", "Delegate Keys");
                    txJson.put("toIsNull", 1);
                    validaot = getValidatorMoniker(value.getString("validator_address"));
                    if (validaot != null) {
                        txJson.put("from", validaot);
                        txJson.put("fromIsNull", 2);
                    } else {
                        txJson.put("from", value.getString("validator_address"));
                        txJson.put("fromIsNull", 0);
                    }

                    txJson.put("orchestratorAddress", value.getString("orchestrator_address"));
                    txJson.put("ethereumAddress", value.getString("ethereum_address"));
                    txJson.put("ethSignature", value.getString("eth_signature"));
                    break;
                case "createvestingaccount":
                    txJson.put("toIsNull", 0);
                    txJson.put("fromIsNull", 0);
                    txJson.put("from", value.getString("from_address"));
                    txJson.put("to", value.getString("to_address"));
                    amountJson = value.getJSONArray("amount").getJSONObject(0);
                    denom = amountJson.getString("denom");
                    token = getDecimal(denom);
                    amount = amountJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString();
                    txJson.put("coinName", token.getString("coinName"));
                    txJson.put("amount", amount);
                    txJson.put("endTime", value.getLongValue("end_time"));
                    boolean delayed = value.getBoolean("delayed");
                    txJson.put("delayed", delayed);
                    if (delayed) {
                        txJson.put("type", "Send Delayed");
                    } else {
                        txJson.put("type", "Send Continued");
                    }
                    break;
                case "stake":
                    txJson.put("type", "Farm Stake");
                    txJson.put("poolName", value.getString("pool_name"));

                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    txJson.put("from", value.getString("sender"));
                    amountJson = value.getJSONObject("amount");
                    denom = amountJson.getString("denom");
                    token = getDecimal(denom);
                    amount = amountJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString();
                    txJson.put("coinName", token.getString("coinName"));
                    txJson.put("amount", amount);
                    break;
                case "withdraw":
                    txJson.put("type", "Farm Withdraw");
                    txJson.put("poolName", value.getString("pool_name"));

                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    txJson.put("from", value.getString("sender"));

                    List<JSONObject> amountList = new ArrayList<>();
                    if (txResponse.getIntValue("code") == 0) {
                        logArray = txResponse.getJSONArray("logs");
                        logArray.stream().forEach(log -> {
                            JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                            events.stream().forEach(event -> {
                                JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                                if (txEvent.containsKey("type") && txEvent.getString("type").equals("transfer")) {
                                    JSONArray attributes = txEvent.getJSONArray("attributes");
                                    JSONObject attribute = attributes.getJSONObject(5);
                                    String str = attribute.getString("value");

                                    if (StringUtils.isNotBlank(str)) {
                                        List<String> strs = Arrays.asList(str.split(","));
                                        for (String amountStr : strs) {
                                            AmountUtil amountUtil = AmountUtil.initAmount(amountStr);
                                            String coinUint = amountUtil.getCoin();
                                            JSONObject tokenJson = getDecimal(coinUint);
                                            String withdraw = amountUtil.getAmount().divide(new BigDecimal(Math.pow(10, tokenJson.getIntValue("decimal")))).toPlainString();
                                            JSONObject amountJSONObject = new JSONObject();
                                            amountJSONObject.put("coinName", tokenJson.getString("coinName"));
                                            amountJSONObject.put("amount", withdraw);
                                            amountList.add(amountJSONObject);
                                        }
                                    }
                                }
                            });
                        });
                    }
                    txJson.put("rewardList", amountList);
                    break;
                case "destroypool":
                    txJson.put("type", "Destroy Pool");
                    txJson.put("poolName", value.getString("pool_name"));
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    txJson.put("from", value.getString("creator"));
                    break;
                case "unbond":
                    txJson.put("type", "Farm Unbond");
                    txJson.put("poolName", value.getString("pool_name"));
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    txJson.put("from", value.getString("sender"));
                    amJson = value.getJSONObject("amount");
                    denom = amJson.getString("denom");
                    token = getDecimal(denom);
                    amount = amJson.getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString();
                    txJson.put("coinName", token.getString("coinName"));
                    txJson.put("amount", amount);
                    amountList = new ArrayList<>();
                    if (txResponse.getIntValue("code") == 0) {
                        logArray = txResponse.getJSONArray("logs");
                        logArray.stream().forEach(log -> {
                            JSONArray events = JSON.parseObject(JSON.toJSONString(log)).getJSONArray("events");
                            events.stream().forEach(event -> {
                                JSONObject txEvent = JSON.parseObject(JSON.toJSONString(event));
                                if (txEvent.containsKey("type") && txEvent.getString("type").equals("unstake")) {
                                    JSONArray attributes = txEvent.getJSONArray("attributes");

                                    JSONObject attribute = attributes.getJSONObject(3);
                                    if (attribute != null && "reward".equals(attribute.getString("key"))) {
                                        String str = attribute.getString("value");
                                        if (StringUtils.isNotBlank(str)) {
                                            List<String> strs = Arrays.asList(str.split(","));
                                            for (String amountStr : strs) {
                                                AmountUtil amountUtil = AmountUtil.initAmount(amountStr);
                                                String coinUint = amountUtil.getCoin();
                                                JSONObject tokenJson = getDecimal(coinUint);
                                                String withdraw = amountUtil.getAmount().divide(new BigDecimal(Math.pow(10, tokenJson.getIntValue("decimal")))).toPlainString();
                                                JSONObject amountJSONObject = new JSONObject();
                                                amountJSONObject.put("coinName", tokenJson.getString("coinName"));
                                                amountJSONObject.put("amount", withdraw);
                                                amountList.add(amountJSONObject);
                                            }
                                        }
                                    }
                                }
                            });
                        });
                    }
                    txJson.put("rewardList", amountList);
                    break;
                case "mintnft":
                    txJson.put("type", "Mint Nft");
                    txJson.put("from", value.getString("sender"));
                    txJson.put("to", value.getString("recipient"));
                    txJson.put("tokenId", value.getString("token_id"));
                    txJson.put("cateId", value.getString("cate_id"));
                    txJson.put("tokenUri", value.getString("token_uri"));
                    txJson.put("companyUri", value.getString("company_uri"));
                    txJson.put("valueAddedTax", value.getString("value_added_tax"));
                    txJson.put("copyrightTax", value.getString("copyright_tax"));
                    txJson.put("toIsNull", 0);
                    txJson.put("fromIsNull", 0);
                    break;
                case "bid":
                    txJson.put("type", "Bid");
                    txJson.put("from", value.getString("sender"));
                    txJson.put("tokenId", value.getString("token_id"));
                    txJson.put("poolAddress", value.getString("pool_address"));
                    txJson.put("isFixedPrice", value.getString("is_fixed_price"));
                    price = value.getJSONObject("price");
                    if (price != null) {
                        Map<String, Object> info = getAmountInfo(price.getString("denom"));
                        txJson.put("coinName", info.get("coinName"));
                        decimal = (int) info.get("decimal");
                        txJson.put("amount", price.getBigDecimal("amount")
                                .divide(new BigDecimal(Math.pow(10, decimal)))
                                .setScale(decimal, RoundingMode.HALF_UP)
                                .toPlainString());
                    }
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    break;
                case "createorder":
                    txJson.put("type", "Create Order");
                    txJson.put("from", value.getString("sender"));
                    txJson.put("tokenId", value.getString("token_id"));
                    txJson.put("cateId", value.getString("cate_id"));
                    txJson.put("poolAddress", value.getString("pool_address"));
                    txJson.put("isFixedPrice", value.getString("is_fixed_price"));
                    price = value.getJSONObject("price");
                    if (price != null) {
                        Map<String, Object> info = getAmountInfo(price.getString("denom"));
                        txJson.put("coinName", info.get("coinName"));
                        decimal = (int) info.get("decimal");
                        txJson.put("amount", price.getBigDecimal("amount")
                                .divide(new BigDecimal(Math.pow(10, decimal)))
                                .setScale(decimal, RoundingMode.HALF_UP)
                                .toPlainString());
                    }
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    break;
                case "mintbatch":
                    txJson.put("type", "Mint Batch");
                    txJson.put("from", value.getString("sender"));
                    txJson.put("to", value.getString("recipient"));

                    txJson.put("items", value.getJSONArray("items"));

                    txJson.put("toIsNull", 0);
                    txJson.put("fromIsNull", 0);
                    break;
                case "frozennft":
                    txJson.put("type", "Frozen Nft");
                    txJson.put("from", value.getString("sender"));

                    txJson.put("cateId", value.getString("cate_id"));
                    txJson.put("tokenId", value.getString("token_id"));

                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    break;
                case "transfernft":
                    txJson.put("type", "Transfer Nft");
                    txJson.put("from", value.getString("sender"));
                    txJson.put("to", value.getString("recipient"));

                    txJson.put("cateId", value.getString("cate_id"));
                    txJson.put("tokenId", value.getString("token_id"));

                    txJson.put("toIsNull", 0);
                    txJson.put("fromIsNull", 0);
                    break;
                case "deleteorder":
                    txJson.put("type", "Delete Order");
                    txJson.put("from", value.getString("creator"));
                    txJson.put("poolAddress", value.getString("pool_address"));

                    txJson.put("isFixedPrice", value.getString("is_fixed_price"));
                    txJson.put("tokenId", value.getString("token_id"));

                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    break;
                case "agreeorder":
                    txJson.put("type", "Agree Order");
                    txJson.put("from", value.getString("creator"));
                    txJson.put("poolAddress", value.getString("pool_address"));

                    txJson.put("tokenId", value.getString("token_id"));

                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    break;
                case "updateorder":
                    txJson.put("type", "Update Order");
                    txJson.put("from", value.getString("pool_creator"));
                    txJson.put("poolAddress", value.getString("pool_address"));

                    txJson.put("tokenId", value.getString("token_id"));
                    price = value.getJSONObject("price");
                    if (price != null) {
                        Map<String, Object> info = getAmountInfo(price.getString("denom"));
                        txJson.put("coinName", info.get("coinName"));
                        decimal = (int) info.get("decimal");
                        txJson.put("amount", price.getBigDecimal("amount")
                                .divide(new BigDecimal(Math.pow(10, decimal)))
                                .setScale(decimal, RoundingMode.HALF_UP)
                                .toPlainString());
                    }

                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    break;
                case "updatepool":
                    txJson.put("type", "Update Pool");
                    txJson.put("from", value.getString("creator"));
                    txJson.put("poolAddress", value.getString("pool"));
                    txJson.put("commissionAddress", value.getString("commission_address"));
                    txJson.put("valueAddedTaxAddress", value.getString("value_added_tax_address"));
                    txJson.put("commissionRate", value.getString("commission_rate"));

                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    break;
                case "bidorder":
                    txJson.put("type", "Bid Order");
                    txJson.put("from", value.getString("sender"));
                    txJson.put("tokenId", value.getString("token_id"));
                    txJson.put("poolAddress", value.getString("pool_address"));
                    coin = value.getJSONObject("price");
                    denom = coin.getString("denom");
                    token = getDecimal(denom);
                    amount = new BigDecimal(coin.getString("amount")).divide(new BigDecimal(Math.pow(10, token.getIntValue("decimal")))).toPlainString();
                    txJson.put("coinName", token.getString("coinName"));
                    txJson.put("amount", amount);
                    txJson.put("toIsNull", 1);
                    txJson.put("fromIsNull", 0);
                    break;
                case "createbox":
                    txJson.put("type", "Create Box");
                    Map<String, Object> innerMap = value.getInnerMap();
                    innerMap.remove("@type");
                    txJson.putAll(innerMap);
                    JSONObject box_price = value.getJSONObject("box_price");
                    txJson.put("box_price", BigDecimal.ZERO);
                    if (box_price != null) {
                        BigDecimal priceBox = box_price.getBigDecimal("amount");
                        if (priceBox != null) {
                            txJson.put("box_price", priceBox.divide(new BigDecimal(Math.pow(10, configService.getChaindecimal())), configService.getChaindecimal(), RoundingMode.HALF_UP));
                        }
                    }
                    txJson.put("fromIsNull", 0);
                    txJson.put("from", txJson.getString("creator"));
                    break;
                case "ethereumtx":
                    innerMap = value.getInnerMap();
                    innerMap.remove("@type");
                    txJson.putAll(innerMap);
                    txJson.put("type", StringTransferUtil.camelToSpace(type.substring(type.indexOf("Msg") + 3)));
                    org.web3j.protocol.core.methods.response.Transaction hash = evMosTransactionService.getTransaction(txJson.getString("hash"));
                    if (hash != null) {
                        try {
                            txJson.put("toIsNull", 0);
                            txJson.put("fromIsNull", 0);
                            txJson.put("from", AddressUtil.convertEthAddressToCosmos(hash.getFrom(), configService.getChainName()));
                            txJson.put("to", AddressUtil.convertEthAddressToCosmos(hash.getTo(), configService.getChainName()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
//                        txJson.put("type",types);
//                        txJson.put("",value.getString(""));
                    txJson.put("type", StringTransferUtil.camelToSpace(type.substring(type.indexOf("Msg") + 3)));
                    innerMap = value.getInnerMap();
                    innerMap.remove("@type");
                    txJson.putAll(innerMap);
                    log.error("未知事件类型:" + txResponse.getString("txhash"));
                    if (StringUtils.isBlank(txJson.getString("from"))) {
                        txJson.put("from", txJson.getString("creator"));
                        txJson.put("fromIsNull", 0);
                    } else {
                        txJson.put("fromIsNull", 0);
                    }
                    break;
            }
            if ("usdg".equalsIgnoreCase(configService.getChainName()) && "dga".equalsIgnoreCase(txJson.getString("coinName"))) {
                BigDecimal amount = txJson.getBigDecimal("amount");
                if (null != amount) {
                    amount = amount.divide(new BigDecimal(1000000));
                    txJson.put("amount", amount);
                }
            }
            messages.add(txJson);
        });
        transaction.put("event", messages);
        return transaction;
    }

    public JSONObject getContract(String contractAddress) {
        JSONObject contract = null;
        if (contractService.isExistContract(contractAddress) > 0) {
            contract = contractService.getContract(contractAddress);

        } else {
            contract = new JSONObject();
            int decimals = getTokenDecimal(contractAddress);
            String symbol = getTokenSymbol(contractAddress);
            contract.put("decimals", decimals);
            contract.put("symbol", symbol);
            contract.put("contractAddress", contractAddress);
            contractService.saveContract(contract);
        }
        return contract;
    }

    /**
     * 获取代币精度
     *
     * @param contractAddress 代币合约地址
     * @return
     */
    public int getTokenDecimal(String contractAddress) {
        Function function = new Function("decimals", Arrays.asList(), Arrays.asList(new TypeReference<Uint8>() {
        }));
        int decimals = 0;
        EthCall ethCall = null;
        try {
            ethCall = web3j.ethCall(Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send();
        } catch (IOException exception) {
            log.error("【获取合约 {} Token 精度失败】", contractAddress, ethCall.getError().getMessage());
            exception.printStackTrace();
        }
        if (ethCall.hasError()) {
            log.error("【获取合约 {} Token 精度失败】", contractAddress, ethCall.getError().getMessage());
            return decimals;
        }
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.isEmpty()) {
            return decimals;
        }
        decimals = Integer.parseInt(decode.get(0).getValue().toString());
        log.info("decimals = " + decimals);
        return decimals;
    }

    /**
     * 获取代币符号(缩写的名称)
     *
     * @param contractAddress 代币合约地址
     * @return
     */
    public String getTokenSymbol(String contractAddress) {
        Function function = new Function("symbol", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
        }));
        String tokenSymbol = contractAddress;
        EthCall ethCall = null;
        try {
            ethCall = web3j.ethCall(Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send();
        } catch (IOException exception) {
            log.error("【获取合约 {} Token Symbol失败】", contractAddress, ethCall.getError().getMessage());
            exception.printStackTrace();
        }
        if (ethCall.hasError()) {
            log.error("【获取合约 {} Token Symbol失败】", contractAddress, ethCall.getError().getMessage());
            return tokenSymbol;
        }
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.isEmpty()) {
            return tokenSymbol;
        }
        tokenSymbol = decode.get(0).getValue().toString();
        return tokenSymbol;
    }

    public JSONObject getDecimal(String uintSymbol) {
        JSONObject token = new JSONObject();
        String coinName = uintSymbol;
        int decimal = 0;
        if (uintSymbol.equals(configService.getCoinName())) {
            coinName = configService.getCoinName1();
            decimal = configService.getChaindecimal();
        } else if (uintSymbol.equals("udga")) {
            coinName = "dga";
            decimal = 6;
        } else if (uintSymbol.startsWith("ibc/")) {
            String hash = uintSymbol.substring(4);
            String result = CosmosUtil.getTransferTokenByHash(configService.getConfig(ConfigUtil.chainUrlKey), hash);
            if (result != null) {
                JSONObject denomTrace = JSON.parseObject(result);
                coinName = denomTrace.getJSONObject("denom_trace").getString("base_denom").substring(1);
                if (coinName.startsWith("gravity")) {
                    String contractAddress = coinName.split("gravity")[1];
                    JSONObject contract = getContract(contractAddress);
                    int decimals = contract.getIntValue("decimals");
                    String symbol = contract.getString("symbol");
                    decimal = decimals;
                    coinName = symbol;
                } else {
                    coinName = coinName.substring(1);
                    decimal = configService.getChaindecimal();
                }
            }
        } else if (uintSymbol.startsWith("gravity")) {
            uintSymbol = uintSymbol.split("gravity")[1];
            JSONObject contract = getContract(uintSymbol);
            decimal = contract.getIntValue("decimals");
            coinName = contract.getString("symbol");
        } else {
            JSONObject tokenJson = tokenService.getTokenByUnit(uintSymbol);
            if (tokenJson != null && tokenJson.getInteger("decimals") != null) {
                coinName = tokenJson.getString("symbol");
                decimal = tokenJson.getIntValue("decimals");
            } else {
                coinName = uintSymbol.substring(1);
                decimal = configService.getChaindecimal();
            }
        }
        token.put("coinName", coinName);
        token.put("decimal", decimal);
        return token;
    }


    public PageModel<JSONObject> getValidatorRelatedTx(String bigType, String smallType, String startTime, String endTime, Integer status, Integer pageIndex, Integer pageSize, String proposalId) {
        Criteria criteria = new Criteria();
        switch (bigType.toLowerCase()) {
            case "delegation":
                if (StringUtils.isNotBlank(smallType)) {
                    switch (smallType.trim().toLowerCase()) {
                        case "delegator":
                            Criteria delegator = Criteria.where("tx.body.messages.@type").is(msgDelegate);
                            criteria.andOperator(delegator);
                            break;
                        case "undelegate":
                            Criteria unDelegator = Criteria.where("tx.body.messages.@type").is(msgUnDelegate);
                            criteria.andOperator(unDelegator);
                            break;
                        case "withdrawdelegatorreward":
                            Criteria withdrawDelegatorReward = Criteria.where("tx.body.messages.@type").is(msgDelegateReward);
                            criteria.andOperator(withdrawDelegatorReward);
                            break;
                        case "setwithdrawaddress":
                            Criteria setWithdrawAddress = Criteria.where("tx.body.messages.@type").is(msgWithdrawAddress);
                            criteria.andOperator(setWithdrawAddress);
                            break;
                        case "beginredelegate":
                            Criteria beginRedelegate = Criteria.where("tx.body.messages.@type").is(msgBeginRedelegate);
                            criteria.andOperator(beginRedelegate);
                            break;
                    }
                } else {
                    Criteria delegator = Criteria.where("tx.body.messages.@type").is(msgDelegate);
                    Criteria unDelegator = Criteria.where("tx.body.messages.@type").is(msgUnDelegate);
                    Criteria withdrawDelegatorReward = Criteria.where("tx.body.messages.@type").is(msgDelegateReward);
                    Criteria setWithdrawAddress = Criteria.where("tx.body.messages.@type").is(msgWithdrawAddress);
                    Criteria beginRedelegate = Criteria.where("tx.body.messages.@type").is(msgBeginRedelegate);
                    criteria.orOperator(delegator, unDelegator, withdrawDelegatorReward, setWithdrawAddress, beginRedelegate);
                }
                break;
            case "validation":
                if (StringUtils.isNotBlank(smallType)) {
                    switch (smallType.trim().toLowerCase()) {
                        case "createvalidator":
                            Criteria createValidator = Criteria.where("tx.body.messages.@type").is(msgCreateValidator);
                            criteria.andOperator(createValidator);
                            break;
                        case "editvalidator":
                            Criteria editValidator = Criteria.where("tx.body.messages.@type").is(msgEditValidator);
                            criteria.andOperator(editValidator);
                            break;
                        case "unjail":
                            Criteria unjail = Criteria.where("tx.body.messages.@type").is(msgUnJail);
                            criteria.andOperator(unjail);
                            break;
                        case "withdrawvalidatorcommission":
                            Criteria withdrawValidatorCommission = Criteria.where("tx.body.messages.@type").is(msgWithdrawCommission);
                            criteria.andOperator(withdrawValidatorCommission);
                            break;
                    }
                } else {
                    Criteria createValidator = Criteria.where("tx.body.messages.@type").is(msgCreateValidator);
                    Criteria editValidator = Criteria.where("tx.body.messages.@type").is(msgEditValidator);
                    Criteria unjail = Criteria.where("tx.body.messages.@type").is(msgUnJail);
                    Criteria withdrawValidatorCommission = Criteria.where("tx.body.messages.@type").is(msgWithdrawCommission);
                    criteria.orOperator(createValidator, editValidator, unjail, withdrawValidatorCommission);
                }
                break;
            case "gov":
                if (StringUtils.isNotBlank(smallType)) {
                    switch (smallType.trim().toLowerCase()) {
                        case "vote":
                            Criteria createValidator = Criteria.where("tx.body.messages.@type").is(msgVote);
                            criteria.andOperator(createValidator);
                            break;
                        case "submitproposal":
                            Criteria editValidator = Criteria.where("tx.body.messages.@type").is(msgSubmitProposal);
                            criteria.andOperator(editValidator);
                            break;
                        case "deposit":
                            Criteria unjail = Criteria.where("tx.body.messages.@type").is(msgDeposit);
                            criteria.andOperator(unjail);
                            break;
                    }
                } else {
                    Criteria voter = Criteria.where("tx.body.messages.@type").is(msgVote);
                    Criteria deposit = Criteria.where("tx.body.messages.@type").is(msgDeposit);
                    Criteria submitProposal = Criteria.where("tx.body.messages.@type").is(msgSubmitProposal);
                    criteria.orOperator(voter, deposit, submitProposal);
                }
                break;
            case "deposit":
                Criteria deposit = new Criteria();
                Criteria submitProposal = new Criteria();

                Criteria depositor = Criteria.where("tx.body.messages.@type").is(msgDeposit);
                Criteria submit = Criteria.where("tx.body.messages.@type").is(msgSubmitProposal);
                if (StringUtils.isNotBlank(proposalId)) {
                    Criteria depositId = Criteria.where("tx.body.messages.proposal_id").is(proposalId);
                    Criteria submitId = Criteria.where("tx_response.logs.events.attributes.value").is(proposalId);
                    deposit.andOperator(depositor, depositId);
                    submitProposal.andOperator(submit, submitId);

                } else {
                    deposit.andOperator(depositor);
                    submitProposal.andOperator(submit);
//                    criteria.orOperator(deposit,submitProposal);
                }
                criteria.orOperator(deposit, submitProposal);
                break;


        }
        Query query = new Query(criteria);

        if (StringUtils.isNotBlank(startTime)) {
            if (StringUtils.isNotBlank(endTime)) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                endTime = df.format(TimeUtil.addOneDate(TimeUtil.StrToDateThird(endTime)));
                query.addCriteria(Criteria.where("tx_response.timestamp").gte(startTime).lte(endTime));
            } else {
                query.addCriteria(Criteria.where("tx_response.timestamp").gte(startTime));
            }
        }
        if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            endTime = df.format(TimeUtil.addOneDate(TimeUtil.StrToDateThird(endTime)));
            query.addCriteria(Criteria.where("tx_response.timestamp").lte(endTime));
        }
        if (status != null && status == 0) {
            query.addCriteria(Criteria.where("tx_response.code").is(0));
        }

        if (status != null && status == 1) {
            query.addCriteria(Criteria.where("tx_response.code").ne(0));
        }
        long count = mongoTemplate.count(query, getCollectionName());
        query.skip((pageIndex - 1) * pageSize);
        query.limit(pageSize);
        query.with(Sort.by(
                Sort.Order.desc("_id"))
        );
        List<JSONObject> transferList = mongoTemplate.find(query, JSONObject.class, getCollectionName());
        transferList = transferList.stream().map(tx -> {
            return getTx(tx);
        }).collect(Collectors.toList());
        PageModel<JSONObject> pageModel = null;
        String chainName = configService.getChainName();
        if (bigType.equals("delegation") && "usdg".equals(chainName)) {
            pageModel = new PageModel<>(transferList, pageIndex, pageSize, count, "dga");
        } else {
            pageModel = new PageModel<>(transferList, pageIndex, pageSize, count, chainName);
        }
        return pageModel;
    }


    public String getCollectionName() {
        if (collectionName == null) {
            collectionName = configService.getChainName() + "_" + Collocation.collection_transaction;
        }
        return collectionName;
    }

    public JSONObject getValidatorMoniker(String operatorAddress) {
        List<JSONObject> validators = validatorService.getValidators();
        AtomicReference<JSONObject> moniker = new AtomicReference<>();
        validators.stream().forEach(validator -> {
            if (validator.getString("operator_address").equals(operatorAddress)) {
                JSONObject j = new JSONObject();
                String s = validator.getJSONObject("description").getString("moniker");
                String coin = validator.getString("icon");
                j.put("moniker", s);
                j.put("icon", coin);
                j.put("operatorAddress", operatorAddress);
                j.put("selfBonded", validator.getString("selfDelegateAmount"));
                moniker.set(j);
            }
        });
        return moniker.get();
    }

    public JSONObject getProposal(String proposalId) {
        List<JSONObject> proposalList = voteService.getProposalList();
        AtomicReference<JSONObject> jsonObject = new AtomicReference<>();
        proposalList.stream().forEach(proposal -> {
            if (proposal.getString("proposal_id").equals(proposalId)) {
                jsonObject.set(proposal);
            }
        });
        return jsonObject.get();
    }


    public Long getTXCount() {
        Long num = mongoTemplate.getCollection(getCollectionName()).estimatedDocumentCount();
        return num;
    }

    public PageModel<JSONObject> getNFTTxByTokenId(String tokenId, Integer pageIndex, Integer pageSize) {
        Criteria criteria = new Criteria();
        Criteria mint = Criteria.where("tx.body.messages.token_id").is(tokenId);
        Criteria batch = Criteria.where("tx.body.messages.items.token_id").is(tokenId);
        criteria.orOperator(mint, batch);
        Query query = new Query(criteria);
        long cout = mongoTemplate.count(query, getCollectionName());
        query.limit(pageSize);
        query.skip((pageIndex - 1) * pageSize);
        query.with(Sort.by(
                Sort.Order.desc("_id"))
        );
        List<JSONObject> list = mongoTemplate.find(query, JSONObject.class, getCollectionName());
        list = list.stream().map(tx -> {
            JSONObject txJson = getTx(tx);
            return txJson;
        }).collect(Collectors.toList());
        PageModel<JSONObject> pageModel = new PageModel<>(list, pageIndex, pageSize, cout);
        return pageModel;
    }

    public String getType(String type) {
        type = type.substring(type.indexOf("v1beta1.") + 8);
        type = type.substring(0, type.length() - 8);
        return type;
    }

    public Map<String, Object> getAmountInfo(String denom) {
        Map<String, Object> info = new HashMap<>();
        String coinName = null;
        Integer decimal = null;
        if (denom.equals(configService.getCoinName())) {
            coinName = configService.getCoinName1();
            decimal = configService.getChaindecimal();
        } else if (denom.startsWith("ibc/")) {
            String hash = denom.substring(4);
            String result = CosmosUtil.getTransferTokenByHash(configService.getConfig(ConfigUtil.chainUrlKey), hash);
            if (result != null) {
                JSONObject denomTrace = JSON.parseObject(result);
                coinName = denomTrace.getJSONObject("denom_trace").getString("base_denom").substring(1);
            } else {
                coinName = denom;
            }
            decimal = configService.getChaindecimal();
        } else {
            if (StringUtils.isNotBlank(denom)) {
                JSONObject token = tokenService.getTokenByUnit(denom);
                if (token != null && token.getInteger("decimals") != null) {
                    decimal = token.getInteger("decimals");
                    coinName = token.getString("symbol");
                } else {
                    decimal = 0;
                    coinName = denom;
                }
            } else {
                coinName = "";
                decimal = 0;
            }
        }
        info.put("decimal", decimal);
        info.put("coinName", coinName);
        return info;
    }
    public List<JSONObject> getTransactionsByBlock(long block,String type){
        Query query=new Query();
        Criteria criteria=Criteria.where("tx_response.height").is(String.valueOf(block));
        criteria.andOperator(Criteria.where("tx.body.messages.@type").is(type));
        query.addCriteria(criteria);
        return mongoTemplate.find(query,JSONObject.class,getCollectionName());
    }
}

