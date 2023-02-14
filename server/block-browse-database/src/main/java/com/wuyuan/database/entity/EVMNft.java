package com.wuyuan.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "evm_nft")
@CompoundIndexes({
        @CompoundIndex(name = "contentAddress", def = "{'contentAddress' : 1}"),
        @CompoundIndex(name = "owner", def = "{'owner' : 1}"),
        @CompoundIndex(name = "evm_nft_Id", def = "{'nftId' : 1}"),
        @CompoundIndex(name = "evm_nft_transactionCount", def = "{'transactionCount' : -1}"),
        @CompoundIndex(name = "hash", def = "{'evm_nft_hash' : 1}"),
})
public class EVMNft {
    private String hash;
    private String contentAddress;
    private String owner;
    private String nftId;
    private String mintTime;
    private Date saveTime;
    private String issuer;

    private long transactionCount;
}
