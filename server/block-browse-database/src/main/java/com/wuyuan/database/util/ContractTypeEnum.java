package com.wuyuan.database.util;

public enum ContractTypeEnum {
    /**
     *
     */
    erc20(0,"erc20"),
    /**
     *
     */
    other(1,"other"),
    /**
     *
     */
    erc721(2,"erc721");

    /**

     */
    private int type;
    /**

     */
    private String name;

    private ContractTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static String getContractType(int type){
        return ContractTypeEnum.values()[type].getName();
    }

    public static ContractTypeEnum init(int type){
        switch (type){
            case 0 :
                return erc20;
            case 1:
                return other;
            case 2:
                return erc721;
            default:
                return null;
        }

    }

}
