package com.gitee.freakchicken.dbapi.basic.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: api
 * @description:
 * @author: jiangqiang
 * @create: 2020-12-11 10:51
 **/
@Slf4j
public class PoolManager {

    //所有数据源的连接池存在map里
    static ConcurrentHashMap<String, DruidDataSource> map = new ConcurrentHashMap<>();

    public static DruidDataSource getJdbcConnectionPool(DataSource ds) {
        if (map.containsKey(ds.getId())) {
            return map.get(ds.getId());
        } else {

            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setName(ds.getName());
            druidDataSource.setUrl(ds.getUrl());
            druidDataSource.setUsername(ds.getUsername());
            druidDataSource.setPassword(ds.getPassword());
            druidDataSource.setDriverClassName(ds.getDriver());
            druidDataSource.setConnectionErrorRetryAttempts(3);       //失败后重连次数
            druidDataSource.setBreakAfterAcquireFailure(true);

            map.put(ds.getId(), druidDataSource);
            log.info("create druid datasource：{}", ds.getName());
            return map.get(ds.getId());

        }
    }

    //删除数据库连接池
    public static void removeJdbcConnectionPool(String id) {
        if (map.containsKey(id)) {
            DruidDataSource old = map.get(id);
            map.remove(id);
            old.close();
            log.info("remove druid datasource: {}", old.getName());
        }
    }

    public static DruidPooledConnection getPooledConnection(DataSource ds) throws SQLException {
        DruidDataSource pool = PoolManager.getJdbcConnectionPool(ds);
        DruidPooledConnection connection = pool.getConnection();
//        log.info("获取连接成功");
        return connection;
    }
}
