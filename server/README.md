# explorer-backend

Blockchain explorer for Gauss,which includes explorer-backend

### ENV
**application-xxx.yml**

    uri: mongo url ( eg. 192.168.150.7:27017)
    
    database: mongo database name ( eg. browse_gauss )

You need to create a table called "config" in the Mongo database.

field: id, key,value

    key:                    value
    picture           Validator picture ( eg. https://podtest.oss-ap-southeast-1.aliyuncs.com/browse/logo/gauss.png)
    
    chainName:        Name of the chain(eg. gauss)
    
    chainUrl:         chainUrl ( eg. http://192.168.2.16:1317 )
    
    chainPrefix:      Prefix to call chainurl ( eg. gauss )
    
    bech32Prefix:     Resolve the prefix of the validators ( eg. gaussvalcons )
    
    coinName:         Chain minimum unit ( eg. ugauss )
    
    coinStartNum:     Starting from which block num ( eg. 1 )
    
    coinValueUrl:     Get currency price ( eg. http://192.168.2.13:32090/gauss-dex-wallet-api/api/token/queryAmount )
    
    httpHead:         Request header for coinValueUrl(if any)
    
    wsPort：          Port used by websocket ( eg. 12345 )
    
    httpxxxxUrl：     Url for PC browser(eg. http://192.168.2.13:7000)
    
    wapxxxxUrl:       Url for WAP browser(eg. http://192.168.2.13:8001)
    
    web3jUrl:         Eth url used by Cross chain browser (eg. http://120.79.14.55:18485)

### Description of each module

**block-blowse-cross-chain-api**
    
    Provide API services for cross chain browsers

**block-blowse-cross-chain-job**

    Data synchronization for cross chain browsers

**block-blowse-database** 
    Browser database service

**block-blowse-job**

    Data synchronization service of blockchain browser

**block-blowse-web**

    Provide API services for blockchain browser


​    

​    

​    