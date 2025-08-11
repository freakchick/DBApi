package com.jq.dbapi.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.jq.dbapi.domain.DataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
public class PoolManager {

    private static Lock lock = new ReentrantLock();

    //所有数据源的连接池存在map里
    static Map<Integer, DruidDataSource> map = new HashMap<>();

    public static DruidDataSource getPool(DataSource ds) {
        if (map.containsKey(ds.getId())) {
            return map.get(ds.getId());
        } else {
            lock.lock();
            try {
                log.info(Thread.currentThread().getName() + "获取锁");
                if (!map.containsKey(ds.getId())) {
                    DruidDataSource druidDataSource = new DruidDataSource();
                    druidDataSource.setUrl(ds.getUrl());
                    druidDataSource.setUsername(ds.getUsername());
                    druidDataSource.setPassword(ds.getPassword());
                    druidDataSource.setDriverClassName(ds.getClassName());
                    map.put(ds.getId(), druidDataSource);
                    log.info("创建Druid连接池成功：{}", JSON.toJSONString(ds));
                }
                return map.get(ds.getId());
            } catch (Exception e) {
                return null;
            } finally {
                lock.unlock();
            }
        }
    }

    public static DruidPooledConnection getPooledConnection(DataSource ds) throws SQLException {
        DruidDataSource pool = PoolManager.getPool(ds);
        DruidPooledConnection connection = pool.getConnection();
//        log.info("获取连接成功");
        return connection;
    }
}
