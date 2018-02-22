package com.tspeiz.modules.util.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.tspeiz.modules.util.PropertiesUtil;

public class RedisManageUtil {
	/*private static String REDIS_IP;
	private static Integer REDIS_PORT;
	private static int MAX_ACTIVE = 1024;
	private static int MAX_IDLE = 200;
	private static int MAX_WAIT = 10000;
	private static int TIMEOUT = 10000;
	private static boolean TEST_ON_BORROW = true;
	private static JedisPool jedisPool = null;
	static {
		try {
			REDIS_IP = PropertiesUtil.getString("redis_ip");
			REDIS_PORT = PropertiesUtil.getInteger("redis_port");
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWait(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			jedisPool = new JedisPool(config, REDIS_IP, REDIS_PORT, TIMEOUT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}

	public static void main(String[] args) {
		System.out.println(RedisManageUtil.getJedis().get("2_lis"));
	}*/
}
