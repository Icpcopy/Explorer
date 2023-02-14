package com.wuyuan.database.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "evm_contract")
@CompoundIndexes({
        @CompoundIndex(name = "owner", def = "{'owner' : 1}"),
        @CompoundIndex(name = "tokenSymbol", def = "{'tokenSymbol' : 1}"),
        @CompoundIndex(name = "contractAddress", def = "{'contractAddress' : 1}"),
        @CompoundIndex(name = "transactionCount_index", def = "{'transactionCount' : -1}"),
        @CompoundIndex(name = "transactionCount_totalCount", def = "{'transactiontotalCount' : -1}"),
})
public class EVMosContract {
    @Id
    private Long ids;
    private String owner;
    private String tokenSymbol;
    private String tokenName;
    private String hash;
    private String contractAddress;
    private String totalSupply;
    private int decimal;
    /**
     *  0 默认为erc20类型
      */
    private int contractType;
    private Date saveTime;
    private String abi;
    private long transactiontotalCount;
    private long addresstotal;
    private long transactionCount;
    private BigInteger time;

}
