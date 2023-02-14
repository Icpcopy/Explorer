package com.wuyuan.database.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "evm_address")
@CompoundIndexes({
        @CompoundIndex(name = "address", def = "{'address' : 1}"),
        @CompoundIndex(name = "contactAddress", def = "{'contactAddress' : 1}"),
        @CompoundIndex(name = "balance", def = "{'balance' : 1}"),
        @CompoundIndex(name = "address_contactAddress", def = "{'address' : 1,'contactAddress' : 1}"),
        @CompoundIndex(name = "address_contactAddress_startBlockNumber", def = "{'address' : 1,'contactAddress' : 1,'startBlockNumber':1}"),

})
@ApiModel
public class EvmAddress {
    @Id
    private long id;
    private String address;
    private Double balance;
    private String symbol;
    private String contactAddress;
    private long blockNumber;
    private int decimal;
    private long startBlockNumber;
}

