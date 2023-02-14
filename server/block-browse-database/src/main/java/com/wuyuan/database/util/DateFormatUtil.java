package com.wuyuan.database.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatUtil {
    public static void main(String[] args) throws ParseException {
//        System.out.println(timeToSecond("2021-11-08"));
        System.out.println(new BigDecimal(Double.MIN_VALUE).toPlainString());
    }
	//13位毫秒时间戳  -->  yyyy-MM-dd HH:mm:ss
    public static String timeToFormat(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return sdf.format(time);
    }

    public static String timeToUTCFormat(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(time);
    }
    //yyyy-MM-dd HH:mm:ss  -->  13位毫秒时间戳
    public static long timeToSecond(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date).getTime();
    }

}