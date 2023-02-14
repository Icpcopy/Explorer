package com.wuyuan.blockbrowse.utils;

import com.wuyuan.blockbrowse.entity.rsp.EVMNftRsp;
import com.wuyuan.blockbrowse.entity.rsp.TransactionEvmRso;
import com.wuyuan.database.entity.EVMNft;
import com.wuyuan.database.entity.EVMosContract;
import com.wuyuan.database.util.BigDecimalUtil;
import io.cosmos.util.AmountUtil;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TransferUtil {

    public static EVMNftRsp getEvmNftRsp(EVMNft nft){
        EVMNftRsp rsp=new EVMNftRsp();
        rsp.setIssuer(nft.getIssuer());
        rsp.setContentAddress(nft.getContentAddress());
        rsp.setMintTime(nft.getMintTime());
        rsp.setNftId(nft.getNftId());
        rsp.setOwner(nft.getOwner());
        rsp.setTransactionCount(nft.getTransactionCount());
        return rsp;
    }


    public static String[] decodeData(String data) {
        if (data.startsWith("0x")) {
            data = data.substring(2);
        }
        String[] datas = new String[data.length() / 64];
        for (int i = 0; i < datas.length; i++) {
//            if(i< datas.length-1){
            datas[i] = "0x"+data.substring(64 * i, 64 * (i + 1));
//            }else {
//                datas[i]=data.substring(64*i);
//            }

        }
        return datas;
    }

    public static void main(String[] args) {
        String[] strings = decodeData("0x000000000000000000000000000000000000000000000000007c585087237fff000000000000000000000000000000000000000000000001e845279d57e18049");

        System.out.println(strings);
    }


}
