package com.wuyuan.blockbrowse.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.web3j.abi.datatypes.Address;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AbiMethodUtil {
    public static void main(String[] args) {
        JSONObject jsonObject = inputDecode("0x40c10f1900000000000000000000000038b3b0ed2685a21e6bd0bdd719f98ee64ecd9c6c00000000000000000000000000000000000000000000000000000182335cc953", "{\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_to\",\"type\":\"address\"},{\"name\":\"_amount\",\"type\":\"uint256\"}],\"name\":\"mint\",\"stateMutability\":\"nonpayable\",\"type\":\"function\"}", "mint","0x40c10f19");
        System.out.println(jsonObject);
    }
    public static JSONObject methodDecode(String  input, String abi) {
        JSONObject result=new JSONObject();
        List<MethodInputs> methodInputs=getMethodInputs(abi);
        input=input.substring(10);
        for (int i = 0; i < methodInputs.size(); i++) {
            MethodInputs inputs=methodInputs.get(i);
            String param=input.substring(i*64,(i+1)*64);
            switch (inputs.type.toLowerCase()){
                case "address":
                    Address address=new Address(new BigInteger(param));
                    result.put(inputs.name,address.getValue());
                    break;
                case "uint256":
                    result.put(inputs.name, new BigInteger(param));
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    public static JSONObject inputDecode(String input,String abi,String method,String methodId){
        JSONObject result=new JSONObject();
        result.put("function", getfunction(abi));
        result.put("method",method);
        result.put("inputs",inputsDecode(input));
        result.put("methodId",methodId);
        return  result;
    }
    public static String getfunction(String abi){
        JSONObject json=JSON.parseObject(abi);
        List<MethodInputs> methodInputs = getMethodInputs(abi);
        StringBuilder sb=new StringBuilder();
        sb.append(json.getString("name"))
                .append("(");
        for (int i = 0; i < methodInputs.size(); i++) {
            if(i!=0){
                sb.append(",");
            }
            sb.append(methodInputs.get(i).type)
                    .append(" ")
                    .append(methodInputs.get(i).name);
        }
        sb.append(")");
        return sb.toString();
    }
    public static List<String> inputsDecode(String input){
        input=input.substring(10);
        int size=input.length()/ 64;
        List<String> list=new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String param=input.substring(i*64,(i+1)*64);
            list.add(param);
        }
        return list;
    }
    public static List<MethodInputs> getMethodInputs(String abi){
        com.alibaba.fastjson.JSONObject json= JSON.parseObject(abi);
        JSONArray inputs=json.getJSONArray("inputs");
        return inputs.toJavaList(MethodInputs.class);
    }
    public static class MethodInputs{
        private String name;
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
