/* 
 * RedisFactory.java  
 * 
 * version TODO
 *
 * 2016年1月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.producer.redis;



import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Class Description
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年1月21日 下午5:24:55
 * @since 
 */
public class RedisFactory {
    private static final Logger log = LoggerFactory.getLogger(RedisFactory.class);
    private static final  ResourceBundle RESOURCE = ResourceBundle.getBundle("producer_order");
    private static  RedisFactory instance = new RedisFactory();
    private JedisPool pool;

    private RedisFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        pool = new JedisPool(config, RESOURCE.getString("redis.server.ip"));  
    }
    public static RedisFactory getInstance() {
        return instance;
    }

    public Jedis getRedis() {
        if (pool == null) {
            instance = new RedisFactory();
        }
        Jedis jedis = pool.getResource();
        if (jedis== null || !jedis.isConnected()) {
            log.debug("*****获取Redis实例失败*****");
        }
        return jedis;
    }
    public void close(Jedis jedis) {
        jedis.close();
    }

}
