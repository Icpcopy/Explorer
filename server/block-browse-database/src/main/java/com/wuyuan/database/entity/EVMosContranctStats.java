package com.wuyuan.database.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "evm_contract_stats")
@CompoundIndexes({
        @CompoundIndex(name = "day", def = "{'day' : 1}"),
        @CompoundIndex(name = "contactAddress", def = "{'contactAddress' : 1}"),
        @CompoundIndex(name = "startBlockNumber", def = "{'startBlockNumber' : 1}"),
        @CompoundIndex(name = "endBlockNumber", def = "{'endBlockNumber' : 1}"),
})
public class EVMosContranctStats {
    @Id
    private long id;
    private String day;
    private long startBlockNumber;
    private long endBlockNumber;
    private long transactionCount;
    private Double amountTotal;
    private String contactAddress;
}
