package com.wuyuan.database.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Document(collection = "evm_contract")
@CompoundIndexes({
        @CompoundIndex(name = "name", def = "{'name' : 1}"),
        @CompoundIndex(name = "code", def = "{'code' : 1}"),
})
@ApiModel
public class EVMEvents {
    @Id
    private long id;
    @ApiModelProperty( "事件或方法名称")
    private String name;
    @ApiModelProperty( "事件或方法编码")
    private String code;
    @ApiModelProperty( "事件或方法abi")
    private String abi;
    @ApiModelProperty( "事件或方法abi")
    private String type;
    @ApiModelProperty("所属合约类型")
    private String contractType;
}
