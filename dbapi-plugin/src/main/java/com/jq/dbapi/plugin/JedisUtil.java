package com.jq.dbapi.plugin;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {

    public static JedisPool getPool() {
        JedisPoolConfig jcon = new JedisPoolConfig();
        jcon.setMaxTotal(200);
        jcon.setMaxIdle(50);
        jcon.setTestOnBorrow(true);
        jcon.setTestOnReturn(true);
        JedisPool jedisPool = new JedisPool();
        JedisPool jp = new JedisPool(jcon, PluginConf.getKey("RedisCachePlugin.ip"),
                Integer.parseInt(PluginConf.getKey("RedisCachePlugin.port")), 100,
                PluginConf.getKey("RedisCachePlugin.password"),
                Integer.parseInt(PluginConf.getKey("RedisCachePlugin.db")));
        return jedisPool;
    }
}
