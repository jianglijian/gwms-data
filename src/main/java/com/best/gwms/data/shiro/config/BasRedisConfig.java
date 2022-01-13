package com.best.gwms.data.shiro.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/** redis 配置 */
@Component
@EnableCaching
public class BasRedisConfig {
    private static Logger logger = LoggerFactory.getLogger(BasRedisConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.ssl}")
    private boolean ssl;

    private int database = Protocol.DEFAULT_DATABASE;

    @Bean
    public JedisPool redisPoolFactory() {
        logger.info("JedisPool autowired success!!");
        logger.info("redis address:{}:{}", host, port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(50);
        jedisPoolConfig.setMinIdle(8);
        jedisPoolConfig.setMaxWaitMillis(10000);
        /** 测试的时候发现会出现'Could not get a resource from the pool'的问题，增加了以下的配置* */
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
        jedisPoolConfig.setNumTestsPerEvictionRun(10);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(60000);
        jedisPoolConfig.setBlockWhenExhausted(false);

        JedisPool jedisPool = null;
        try {
            // 生产环境是要ssl的方式连接redis，并且要有密码
                jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database, ssl);
        } catch (Exception e) {
            logger.error("", e);
        }

        return jedisPool;
    }
}
