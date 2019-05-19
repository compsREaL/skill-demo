package com.real.skill.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/15 18:38
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 根据key值获取其对应对象
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix keyPrefix,String key,Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(keyPrefix.getPrefix()+key);
            T t = convertFromStringToBean(str,clazz);
            return t;
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    /**
     * 删除对应key的缓存
     *
     * @param keyPrefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix keyPrefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long effect = jedis.del(keyPrefix.getPrefix()+key);
            return effect>0;
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    public boolean delete(KeyPrefix keyPrefix){
        if (keyPrefix == null){
            return false;
        }
        List<String> keys = scanKeys(keyPrefix.getPrefix());
        if (keys==null || keys.size()<=0){
            return true;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(keys.toArray(new String[0]));
            return true;
        } catch (final Exception e){
            e.printStackTrace();
            return false;
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public List<String> scanKeys(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<>();
            String cursor="0";
            ScanParams params = new ScanParams();
            params.match("*"+key+"*");
            params.count(100);
            do {
                ScanResult<String> result = jedis.scan(cursor,params);
                List<String> resultList = result.getResult();
                if (resultList!=null && resultList.size()>0){
                    keys.addAll(resultList);
                }
                cursor = result.getStringCursor();
            } while (!cursor.equals("0"));
            return keys;
        } finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    /**
     * 根据key和value在Redis中加入缓存
     *
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix,String key,T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String valueStr = convertFromBeanToString(value);
            if (valueStr==null || valueStr.length()==0){
                return false;
            }
            int expireSeconds = keyPrefix.expireSeconds();
            if (expireSeconds<=0) {
                jedis.set(keyPrefix.getPrefix() + key, valueStr);
            }else {
                jedis.setex(keyPrefix.getPrefix() + key,expireSeconds,valueStr);
            }
            return true;
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    /**
     * 判断key是否存在
     *
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exists(KeyPrefix keyPrefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(keyPrefix.getPrefix()+key);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    /**
     * 递增1
     *
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix keyPrefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(keyPrefix.getPrefix()+key);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    /**
     * 递减1
     *
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix keyPrefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(keyPrefix.getPrefix()+key);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    /**
     * 将对象转换为json字符串
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String convertFromBeanToString(T value) {
        if (value==null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz==int.class || clazz==Integer.class){
            return ""+value;
        }else if (clazz==String.class){
            return (String) value;
        }else if(clazz==long.class || clazz==Long.class){
            return ""+value;
        }
        return JSON.toJSONString(value);
    }

    /**
     * 将json字符串还原为对象
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T convertFromStringToBean(String str,Class<T> clazz) {
        if (str == null || str.length()==0 || clazz==null){
            return null;
        }
        if (clazz==int.class || clazz==Integer.class){
            return (T) Integer.valueOf(str);
        }else if (clazz==String.class){
            return (T) str;
        }else if(clazz==long.class || clazz==Long.class){
            return (T) Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }


}
