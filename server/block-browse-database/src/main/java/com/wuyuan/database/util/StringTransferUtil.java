package com.wuyuan.database.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTransferUtil {
    public  static  final Pattern camelPattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
    /**
     * 驼峰法转空格
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camelToSpace(String line){
        if(line==null||"".equals(line)){
            return "";
        }
        line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb=new StringBuffer();

        Matcher matcher=camelPattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(word.substring(0,1).toUpperCase());
            sb.append(word.substring(1).toLowerCase());
            sb.append(matcher.end()==line.length()?"":" ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(camelToSpace("RevokeBoxGroup"));
    }
}
