package com.wuyuan.database.util;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;

public interface Web3jInterfaceUtil extends Web3j {
    static Web3jUtil build(Web3jService web3jService) {
        return new Web3jUtil(web3jService);
    }
}