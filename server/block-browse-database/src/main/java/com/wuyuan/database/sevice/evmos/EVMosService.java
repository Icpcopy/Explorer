package com.wuyuan.database.sevice.evmos;

import com.wuyuan.database.entity.EVMosContract;
import com.wuyuan.database.util.Web3jInterfaceUtil;
import com.wuyuan.database.util.Web3jUtil;
import io.reactivex.Completable;
import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EVMosService {

    private Web3jUtil web3j;


    public BigInteger getBalances(String address) throws IOException {
        BigInteger balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        return balance;
    }

    public BigDecimal getNonce(String address) throws IOException {
        return new BigDecimal(web3j.ethGetTransactionCount(address,DefaultBlockParameterName.LATEST).send().getTransactionCount().longValue());
    }
    public String getChainId() throws IOException {
        return web3j.ethChainId().send().getChainId().toString();
    }
    public BigDecimal getGasPrice() throws IOException {
        return new BigDecimal(web3j.ethGasPrice().send().getGasPrice().longValue());
    }
    public EVMosService(String url){
        web3j = Web3jInterfaceUtil.build(new HttpService(url));
    }
    public Transaction getEVMosTransactions(String hash) throws IOException {
        return web3j.ethGetTransactionByHash(hash).send().getTransaction().get();
    }

    public long getNewBlock() throws IOException, ExecutionException, InterruptedException {
        return web3j.ethBlockNumber().sendAsync().get().getBlockNumber().longValue();
    }
    public EthBlock.Block getEVMosBlock(long blockNumber) throws IOException, ExecutionException, InterruptedException {
        return web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(new BigInteger(String.valueOf(blockNumber))),true).sendAsync().get().getBlock();
    }
    public TransactionReceipt getEVMosTransactionReceipt(String hash) throws IOException {
        return web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt().get();
    }
    public EVMosContract erc20Name(String contractAddress, String owner) throws Exception {
        EVMosContract contract=new EVMosContract();
        contract.setContractType(0);
        contract.setOwner(owner);
        contract.setContractAddress(contractAddress);
        String tokenName = getTokenName(contractAddress);
        contract.setTokenName(tokenName);
        if ("not_erc20".equalsIgnoreCase(tokenName)) {
            contract.setContractType(1);
            contract.setContractAddress(contractAddress);
        }

        System.out.println("tokenName:" + tokenName);
        String tokenSymbol = getTokenSymbol(contractAddress);
        if ("not_erc20".equalsIgnoreCase(tokenSymbol)) {
            contract.setContractType(1);
        }
        contract.setTokenSymbol(tokenSymbol);
        System.out.println("tokenSymbol:" + tokenSymbol);
        int decimal = getTokenDecimal(contractAddress);
        if (decimal == -1 || decimal == 0) {
            contract.setContractType(1);
        }
        contract.setDecimal(decimal);
        BigDecimal tokenBalance = getTokenBalance(owner, contractAddress, decimal);
        if (tokenBalance.doubleValue() == -1) {
            contract.setContractType(1);
        }
        BigDecimal totalSupply = getTotalSupply(contractAddress, decimal,owner);
        if (totalSupply.doubleValue() == -1) {
            contract.setContractType(1);
        }
        contract.setTotalSupply(totalSupply.toPlainString());
        return contract;


    }
    public String getOwner(String contractAddress) throws IOException {
        Function function = new Function("owner", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
        }));
        String owner = null;
        EthCall ethCall = web3j.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
                "0x0000000000000000000000000000000000000000", contractAddress,
                FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send();
        if (ethCall.hasError()) {
            return owner;
        }
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.size() > 0 && !decode.get(0).getValue().toString().equals("") && decode.get(0).getValue().toString() != null) {
            owner = decode.get(0).getValue().toString();
        } else {
            return "";
        }
        return owner;
    }

    public  String getTokenName(String contractAddress) throws Exception {
        Function function = new Function("name", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
        }));
        String tokenName = null;
        EthCall ethCall = web3j.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send();
        if (ethCall.hasError()) {
            return tokenName;
        }
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.size() > 0 && !decode.get(0).getValue().toString().equals("") && decode.get(0).getValue().toString() != null) {
            tokenName = decode.get(0).getValue().toString();
        } else {
            return "not_erc20";
        }
        return tokenName;
    }

    public  String getTokenSymbol(String contractAddress) throws Exception {
        Function function = new Function("symbol", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
        }));
        String tokenSymbol = null;
        EthCall ethCall = web3j.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send();
        if (ethCall.hasError()) {
            return "not_erc20";
        }
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.size() > 0 && !decode.get(0).getValue().toString().equals("") && decode.get(0).getValue().toString() != null) {
            tokenSymbol = decode.get(0).getValue().toString();
        }
        return tokenSymbol;
    }

    public  int getTokenDecimal(String contractAddress) throws Exception {
        Function function = new Function("decimals", Arrays.asList(), Arrays.asList(new TypeReference<Uint8>() {
        }));
        int decimals = 0;
        EthCall ethCall = web3j.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send();
        if (ethCall.hasError()) {
            return decimals;
        }
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.size() > 0 && !decode.get(0).getValue().toString().equals("") && decode.get(0).getValue().toString() != null) {
            decimals = Integer.parseInt(decode.get(0).getValue().toString());
        } else {
            return -1;
        }
        return decimals;
    }

    public  BigDecimal getTokenBalance(String address, String contractAddress, int decimals) {
        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address fromAddress;
        try {
            fromAddress = new Address(address);
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ONE.negate();
        }

        inputParameters.add(fromAddress);
        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);


        BigDecimal balanceValue = BigDecimal.ZERO;
        try {
            EthCall ethCall = web3j.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(address, contractAddress, data), DefaultBlockParameterName.PENDING).sendAsync().get();
            if (null == ethCall.getError()) {
                String value ="0";
                if(ethCall.getValue().length()>2){
                    value =  Numeric.decodeQuantity(ethCall.getValue()).toString();
                }
                balanceValue = new BigDecimal(value).divide(new BigDecimal(Math.pow(10, decimals)), decimals, RoundingMode.DOWN);
            } else {
                return BigDecimal.ONE.negate();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return balanceValue;
    }

    public  BigDecimal getTotalSupply(String contractAddress, int decimals,String address) throws Exception {
        Function function = new Function("totalSupply", Arrays.asList(), Arrays.asList(new TypeReference<Uint256>() {
        }));
        BigDecimal totalSupply = BigDecimal.ZERO;
        if(StringUtils.isBlank(address)){
            address="0x0000000000000000000000000000000000000000";
        }
        EthCall ethCall = web3j.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(address, contractAddress, FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send();
        if (ethCall.hasError()) {
            return BigDecimal.ONE.negate();
        }
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.size() > 0 && !decode.get(0).getValue().toString().equals("") && decode.get(0).getValue().toString() != null) {
            totalSupply = new BigDecimal(decode.get(0).getValue().toString()).divide(new BigDecimal(Math.pow(10,decimals)), decimals, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ONE.negate();
        }
        return totalSupply;
    }
    public EthSendTransaction sendRawTransaction(String hex) throws ExecutionException, InterruptedException {
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hex).sendAsync().get();
        return ethSendTransaction;
    }
    public String getNFTOwnerOf(String contractAddress,String nftId) throws IOException {
        Function function = new Function("ownerOf",
                Arrays.asList(new Uint256(new BigInteger(nftId))),
                Arrays.asList(new TypeReference<Address>() {
                }));
        String addr = null;
        EthCall ethCall = web3j.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send();
        if (ethCall.hasError()) {
            return "";
        }
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.size() > 0 && !decode.get(0).getValue().toString().equals("") && decode.get(0).getValue().toString() != null) {
            Object value = decode.get(0).getValue();
            addr=value.toString();
        }
        return addr;
    }
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
//        String transfer = FunctionEncoderUtil.getMethodId("transfer(address,uint256)");
//        System.out.println(transfer);
        String url = "http://192.168.3.23:8545/";
//        long newBlock = web3j.ethBlockNumber().sendAsync().get().getBlockNumber().longValue();
         Web3jUtil web3 = Web3jInterfaceUtil.build(new HttpService(url));
//        org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", "contractAddress", encode)
        //

        org.web3j.protocol.core.methods.request.Transaction ethCallTransaction =
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
                        "0x0ae9afa6547d4e0c916322b7db266358438b3a27", "0x84ab8272e6cd058053c1438f066ff3d13cfc0375",
                        "0xa9059cbb00000000000000000000000099672d693079ec6a5cd42cfdf09f1a4b961cc70a0000000000000000000000000000000000000000000000008ac7230489e80000");
        BigInteger amountUsed = web3.ethEstimateGas(ethCallTransaction).send().getAmountUsed();


        System.out.println(amountUsed);
    }
    public  boolean supportsInterface(byte[] code,String contractAddress) throws Exception {
        Function function = new Function("supportsInterface", Arrays.asList(new Bytes4(code)), Arrays.asList(new TypeReference<Bool>() {
        }));
        boolean  supportsInterface= false;
        String encode = FunctionEncoder.encode(function);
        System.out.println(encode);
        org.web3j.protocol.core.methods.request.Transaction ethCallTransaction = org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction("0x0000000000000000000000000000000000000000", contractAddress, encode);
        ethCallTransaction.getMaxFeePerGas();
        EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
        if (ethCall.hasError()) {
            return false;
        }
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.size() > 0 && !decode.get(0).getValue().toString().equals("") ) {
            Object value = decode.get(0).getValue();
            return ((Boolean)value);
        }
        return supportsInterface;
    }

}
