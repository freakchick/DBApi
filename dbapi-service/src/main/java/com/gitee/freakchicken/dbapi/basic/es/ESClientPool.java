package com.gitee.freakchicken.dbapi.basic.es;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.RestClient;

public class ESClientPool {

    GenericObjectPool<RestClient> pool;

    public ESClientPool(String address) {
        GenericObjectPoolConfig<RestClient> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMinEvictableIdleTimeMillis(10000);
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setMaxIdle(3);
        poolConfig.setMaxTotal(5);
        poolConfig.setMinIdle(1);
        poolConfig.setMinEvictableIdleTimeMillis(-1);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);
        pool = new GenericObjectPool(new RestClientFactory(address), poolConfig);
    }

    public RestClient getConnection() throws Exception {
        return pool.borrowObject();
    }

    public void closeConnection(RestClient client){
        pool.returnObject(client);
    }

    public void close(){
        pool.close();
    }

}
