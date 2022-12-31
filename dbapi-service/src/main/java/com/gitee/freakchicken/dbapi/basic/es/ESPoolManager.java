package com.gitee.freakchicken.dbapi.basic.es;
import org.elasticsearch.client.RestClient;

import com.gitee.freakchicken.dbapi.basic.domain.DataSource;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: api
 * @description:
 * @author: jiangqiang
 * @create: 2020-12-11 10:51
 **/

public class ESPoolManager {

    //所有数据源的连接池存在map里
    static ConcurrentHashMap<String, ESClientPool> map = new ConcurrentHashMap<>();

    public static RestClient getJdbcConnectionPool(DataSource dataSource) throws Exception {
        ESClientPool pool = new ESClientPool(dataSource.getUrl());
        map.put(dataSource.getId(),pool);
        return pool.getConnection();
    }

    //删除数据库连接池
    public static void removeJdbcConnectionPool(String id) {
        ESClientPool pool = map.get(id);
        pool.close();

    }

    public static RestClient getPooledConnection(DataSource dataSource) throws Exception {
        ESClientPool pool = map.get(dataSource.getId());
        RestClient connection = pool.getConnection();
        return connection;
    }
}