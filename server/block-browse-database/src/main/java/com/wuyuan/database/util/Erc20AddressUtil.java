package com.wuyuan.database.util;

import org.apache.commons.lang3.StringUtils;

public class Erc20AddressUtil {

    public static String topic2Address(String topic){
        if(StringUtils.isNotBlank(topic)){
            if(!"0x000000000000000000000000".equalsIgnoreCase(topic.substring(0,topic.length()-40))&&!"000000000000000000000000".equalsIgnoreCase(topic.substring(0,topic.length()-40))){
                return "";
            }
            return "0x"+topic.substring(topic.length()-40);
        }
        return null;
    }

    public static void main(String[] args) {
        String s = topic2Address("0x9f2df0fed2c77648de5860a4cc508cd0818c85b8b8a1ab4ceeef8d981c8956a6");
        System.out.println(s);
    }
}
