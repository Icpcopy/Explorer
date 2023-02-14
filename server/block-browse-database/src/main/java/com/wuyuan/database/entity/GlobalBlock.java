package com.wuyuan.database.entity;

import lombok.Data;

@Data
public class GlobalBlock {
    private String _id;
    private long blockNum;
    private String chainName;
    private Long nftNum;
    private Long evmOsNumber;

    private Long evmStatsNumber;
}
