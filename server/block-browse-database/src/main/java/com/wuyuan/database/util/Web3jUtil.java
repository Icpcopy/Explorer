package com.wuyuan.database.util;

import com.wuyuan.database.sevice.evmos.EVMosService;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.http.HttpService;

import java.math.BigDecimal;
import java.util.Arrays;

public class Web3jUtil extends JsonRpc2_0Web3j {
    public Web3jUtil(Web3jService web3jService) {
        super(web3jService);
    }

    public Request<?, NewAccountIdentifier> personalNewAccount(String password) {
        return new Request("personal_newAccount", Arrays.asList(password), this.web3jService, NewAccountIdentifier.class);
    }

    public static void main(String[] args) throws Exception {
        EVMosService service = new EVMosService("http://8.219.114.84:8545/");
        BigDecimal totalSupply = service.getTotalSupply("0x74a1f80c7aef21a010d230643b4b14c467b077e4", 6, "0x74a1f80c7aef21a010d230643b4b14c467b077e4");
        System.out.println(totalSupply);

    }
}
