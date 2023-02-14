package com.wuyuan.database.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeeDestructionQuantity {

    private String _id;

    private BigDecimal amount;

    private String coinName;

    private Long blockNumber;
}
