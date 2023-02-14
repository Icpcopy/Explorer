package com.wuyuan.database.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {

    public static void main(String[] args) {
        System.out.println(computeDecimal(new BigDecimal(456564564654645654l),6));;
    }
    public static BigDecimal computeDecimal(BigDecimal d,int decimal){
        return  d.divide(new BigDecimal(Math.pow(10,decimal)),decimal, RoundingMode.HALF_UP);
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
}
