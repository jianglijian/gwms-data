package com.best.gwms.data.shiro.config;

import org.crazycake.shiro.RedisManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.Serializable;
import java.util.Set;

/**
 * @descreption:
 * @author: Created by maz on 2018/6/14.
 */
public class ShiroRedisManager extends RedisManager implements Serializable {
    private static final int DEFAULT_EXPIRE = 3600;
    private String host;

    private int port = Protocol.DEFAULT_PORT;
    // expire time in seconds
    private int expire = DEFAULT_EXPIRE;
    // timeout for jedis try to connect to redis server, not expire time! In milliseconds
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    private String password;
    private boolean ssl;
    private int database = Protocol.DEFAULT_DATABASE;
    private transient volatile JedisPool jedisPool = null;

    public ShiroRedisManager(String host) {
        this.host = host;
    }

    private void initialize() {
        synchronized (this) {
            if (jedisPool == null) {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password, database, ssl);
            }
        }
    }

    private void checkAndInitialize() {
        if (jedisPool == null) {
            initialize();
        }
    }

    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    @Override
    public byte[] get(byte[] key) {
        checkAndInitialize();
        if (key == null) {
            return new byte[0];
        }
        byte[] value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public byte[] set(byte[] key, byte[] value) {
        checkAndInitialize();
        if (key == null) {
            return new byte[0];
        }
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    @Override
    public byte[] set(byte[] key, byte[] value, int expire) {
        checkAndInitialize();
        if (key == null) {
            return new byte[0];
        }
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * del
     *
     * @param key
     */
    @Override
    public void del(byte[] key) {
        checkAndInitialize();
        if (key == null) {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * size
     */
    @Override
    public Long dbSize() {
        checkAndInitialize();
        Long dbSize = 0L;
        Jedis jedis = jedisPool.getResource();
        try {
            dbSize = jedis.dbSize();
        } finally {
            jedis.close();
        }
        return dbSize;
    }

    /**
     * keys
     *
     * @param pattern
     * @return
     */
    @Override
    public Set<byte[]> keys(byte[] pattern) {
        checkAndInitialize();
        Set<byte[]> keys = null;
        Jedis jedis = jedisPool.getResource();
        try {
            keys = jedis.keys(pattern);
        } finally {
            jedis.close();
        }
        return keys;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int getExpire() {
        return expire;
    }

    @Override
    public void setExpire(int expire) {
        this.expire = expire;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int getDatabase() {
        return database;
    }

    @Override
    public void setDatabase(int database) {
        this.database = database;
    }

    public boolean getSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
}
