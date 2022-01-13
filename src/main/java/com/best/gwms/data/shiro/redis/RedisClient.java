package com.best.gwms.data.shiro.redis;

import com.best.gwms.common.base.AbstractPo;
import com.best.gwms.common.cache.SerializeUtil;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.Map;

/**    redis工具类     @author bl03846    @version 1.0.1  */
@Component
public class RedisClient implements Serializable {
    public static final String PREFIX_LOGIN_FAIL_COUNT = "LFC";
    // 修改密码的redis类型
    public static final String PREFIX_CHANG_PW_FAIL_COUNT = "CPFC";
    public static final String PREFIX_MESSAGE = "MESSAGE_RESOURCE";
    public static final String PREFIX_MESSAGE_INITED = "MESSAGE_RESOURCE_INITED";
    private static final Logger log = LoggerFactory.getLogger(com.best.gwms.common.cache.RedisClient.class);
    String prefix = "GWMS";

    /** 数据有效期 */
    private int expireTime = 7 * 24 * 3600;

    @Autowired
    private JedisPool jedisPool;

    /**
     * @param key
     * @return 1-设置成功 0-未设置
     */
    public Long setNx(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.setnx(key, "");
        } catch (Exception e) {

            jedis.close();
            return 0L;
        } finally {
            jedis.close();
        }
    }

       public void setMapValue(String key, Map<String, String> values) {
        Jedis jedis = jedisPool.getResource();
        try {

            jedis.hmset(key, values);
        } catch (Exception e) {

            jedis.close();
        } finally {
            jedis.close();
        }
    }

    /**
     * 删除redis中的数据
     *
     * @param key
     */
    public void removeValueByKey(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);

        } catch (Exception e) {

            jedis.close();
        } finally {
            jedis.close();
        }
    }

    /**
     * 创建redis中的key
     *
     * @param po
     * @return
     */
    public String buildKey(AbstractPo po) {
        if (po == null) {
            return "";
        }
        Class clazz = po.getClass();
        String poName = clazz.getSimpleName();
        return prefix + "_" + poName + "_" + po.getId();
    }

    public String buildKey(String poName, Long id) {
        return prefix + "_" + poName + "_" + id;
    }

    public String buildKey(String[] tokens) {
        return Joiner.on("_").skipNulls().join(tokens);
    }

    public void setObject(String key, int seconds, Object obj) {
        if (obj == null) {
            return;
        }

        Jedis jedis = jedisPool.getResource();

        try {
            jedis.setex(key.getBytes(), seconds, SerializeUtil.serialize(obj));
        } catch (Exception e) {

            jedis.close();
        } finally {
            jedis.close();
        }
    }

    public void setObject(String key, Object obj) {
        setObject(key, expireTime, obj);
    }

    public Object getObject(String key) {

        Jedis jedis = jedisPool.getResource();
        byte[] value = new byte[0];
        try {
            value = jedis.get(key.getBytes());
        } catch (Exception e) {

            jedis.close();
        } finally {
            jedis.close();
        }
        if (value != null) {
            return SerializeUtil.unserialize(value);
        }

        return null;
    }

    /** 更新redis中的数据，并保持生存时间不变 */
    public void updateValue(String key, String newValue) {
        Jedis jedis = jedisPool.getResource();
        try {
            // 剩余生存时间
            int ttl = jedis.ttl(key).intValue();
            jedis.setex(key, ttl, newValue);

        } catch (Exception e) {

            jedis.close();
        } finally {
            jedis.close();
        }
    }

    /** 更新redis中的数据，并保持生存时间不变 */
    public void lpush(String key, Object newValue) {
        Jedis jedis = jedisPool.getResource();
        try {
            // 剩余生存时间
         jedis.lpush(key.getBytes(), SerializeUtil.serialize(newValue));
        } catch (Exception e) {
            e.printStackTrace();
           // jedisPool.returnBrokenResource(jedis);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }



    public Long length(String key) {
        Long llen= 0L;
        Jedis jedis = jedisPool.getResource();
        try {
             llen = jedis.llen(key);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                jedisPool.returnBrokenResource(jedis);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
                jedisPool.returnResource(jedis);
        }
        return llen;
    }


    public Object rpop(String key) {
        Object rpobj = null;
        Jedis jedis = jedisPool.getResource();
        try {
            byte[] listob = jedis.rpop(key.getBytes());
            if (listob != null) {
                return SerializeUtil.unserialize(listob);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                jedisPool.returnBrokenResource(jedis);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            jedisPool.returnResource(jedis);
        }
        return rpobj;
    }


}
