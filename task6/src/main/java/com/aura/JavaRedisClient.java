package com.aura;//package bigdata.test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by qianxi.zhang on 5/23/18.
 */
public class JavaRedisClient {
  private static int MAX_IDLE = 200;
  private static int TIMEOUT = 10000;
  private static boolean TEST_ON_BORROW = true;

  private static String REDISSERVER = "192.168.21.128";
  private static int REDISPORT = 6379;
  ;
  private static JedisPool pool = null;


  public static JedisPoolConfig config() {
    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxIdle(MAX_IDLE);
    config.setTestOnBorrow(TEST_ON_BORROW);
    return config;
  }

  public static JedisPool get() {
    if (pool == null) {
      pool = new JedisPool(config(),
              REDISSERVER,
              REDISPORT,
          TIMEOUT);
    }
    return pool;
  }
}
