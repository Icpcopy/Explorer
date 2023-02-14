package com.wuyuan.blockbrowse.config;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.wuyuan.database.util.ConfigUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: BigDecimalValueFilter
 *
 * @date 4/23/21 17:48
 */
public class BigDecimalValueFilter implements ValueFilter {
    public static final Pattern numberPattern  = Pattern.compile("-?[0-9]+.?[0-9]+");
    @Override
    public Object process(Object object, String name, Object value) {
        if(value instanceof BigDecimal) {
            return ((BigDecimal) value).setScale(ConfigUtil.ChainDecimal,RoundingMode.HALF_UP).toPlainString();

        }else if (value instanceof Double){
            return new BigDecimal(((Double) value).doubleValue()).setScale(ConfigUtil.ChainDecimal, RoundingMode.HALF_UP).toPlainString();
        }
//        else if (value instanceof String){
//            String result= (String) value;
//            if(BigDecimalValueFilter.isNumericzidai(result)&&result.indexOf('.')>0&&(result.length()-result.indexOf('.'))>6){
//                double v = new BigDecimal(result).doubleValue();
//                String rds=new BigDecimal(String.valueOf(v)).toPlainString();
//                if(rds.length()-rds.indexOf('.')>=6){
//                    return rds;
//                }
//
//                return new BigDecimal(result).setScale(ConfigUtil.ChainDecimal,RoundingMode.HALF_UP).toPlainString();
//            }
//        }
        return value;
    }
    public static boolean isNumericzidai(String str) {
        Matcher isNum = numberPattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}