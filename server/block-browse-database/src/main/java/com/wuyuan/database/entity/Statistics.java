package com.wuyuan.database.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "statistics")
public class Statistics {

    private BigDecimal withdrawReward;
    private BigDecimal withdrawCommission;
    private long block;
    private BigDecimal totalUnDelegator;
    //    private BigDecimal ecologicalProPool=new BigDecimal(5.67*((long)Math.pow(10,8)));
    private BigDecimal ecologicalProPool = new BigDecimal(5.67 * ((long) Math.pow(10, 8)));
    private BigDecimal locked24 = BigDecimal.valueOf(24570000L);
    private BigDecimal locked36 = BigDecimal.valueOf(24570000L);
    private BigDecimal bondedLocked = BigDecimal.ZERO;
    private BigDecimal preMint=BigDecimal.valueOf(70000000);
    private BigDecimal withdrawalCommunityPool=BigDecimal.valueOf(299000);

}
