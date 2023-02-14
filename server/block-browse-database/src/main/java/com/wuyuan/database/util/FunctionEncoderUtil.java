package com.wuyuan.database.util;

import org.web3j.abi.DefaultFunctionEncoder;

import java.math.BigInteger;

public class FunctionEncoderUtil extends DefaultFunctionEncoder {
    public static String getMethodId(String methodSignature) {
        final String methodId = buildMethodId(methodSignature);


        return methodId;
    }

    public static void main(String[] args) {
        String s = new BigInteger("16").toString(16);
    }
}
