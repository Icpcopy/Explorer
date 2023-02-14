package com.wuyuan.database.util;

/**
 * @author wuyuan
 */

public enum CateIdUtil {
    /*
    * 票务
    * */
    TICKETING("001"),
    /*
     * 轻奢品
     * */
    LIGHT_LUXURY("004"),
    /*
     * 艺术品
     * */
    ART("003"),
    /*
     * 收藏品
     * */
    COLLECTION("002"),
    ;

    private String cateId;

    CateIdUtil(String s) {
        this.cateId = s;
    }

    public String getCateId() {
        return cateId;
    }
}
