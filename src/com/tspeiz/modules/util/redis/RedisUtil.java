package com.tspeiz.modules.util.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.tspeiz.modules.util.PropertiesUtil;

public class RedisUtil {

	private static String REDIS_IP;
	private static Integer REDIS_PORT;
	private static String AUTH;
	private static int MAX_ACTIVE = 4000;
	private static int MAX_IDLE = 200;
	private static int MAX_WAIT = 10000;
	private static int TIMEOUT = 10000;
	private static boolean TEST_ON_BORROW = true;
	private static JedisPool jedisPool = null;
	private static Jedis resource = null;
	static {
		try {
			REDIS_IP = PropertiesUtil.getString("redis_ip");
			REDIS_PORT = PropertiesUtil.getInteger("redis_port");
			AUTH=PropertiesUtil.getString("redis_auth");
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWait(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			jedisPool = new JedisPool(config, REDIS_IP, REDIS_PORT, TIMEOUT,AUTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static boolean borrowOrOprSuccess = true;

	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}

	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				resource = jedisPool.getResource();
			} else {
				return null;
			}
		} catch (Exception e) {
			borrowOrOprSuccess = false;
			if (resource != null)
				jedisPool.returnBrokenResource(resource);
		} finally {
			if (borrowOrOprSuccess)
				jedisPool.returnResource(resource);
		}
		resource = jedisPool.getResource();
		return resource;
	}

	public static void main(String[] args) {
		Jedis edis = RedisUtil.getJedis();
		//edis.set("101_lis","");
		System.out.println(edis.get("xiaohe"));
		//System.out.println(edis.get("128_pacs"));
		RedisUtil.returnResource(edis);

	}
}
