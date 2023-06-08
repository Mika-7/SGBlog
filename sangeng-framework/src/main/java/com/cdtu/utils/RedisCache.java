package com.cdtu.utils;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class RedisCache {
    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    /**
     * 缓存键值对
     * @param key   缓存键
     * @param value 缓存值
     * @param <T> 值类型
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }
    /**
     * 缓存键值对
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     * @param key Redis 键
     * @param timeout 超时时间
     * @return true 设置成功，false 设置失败
     */
    public boolean expire(final String key, final Long timeout) {
        return expire(key, timeout, TimeUnit.MICROSECONDS);
    }

    /**
     * 设置有效时间
     * @param key Redis 键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true 设置成功，false 设置失败
     */
    public boolean expire(final String key, final Long timeout, final TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    /**
     * 获取缓存对象
     * @param key 缓存键
     * @return 缓存值
     * @param <T> 缓存值类型
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<Object, Object> operation = redisTemplate.opsForValue();
        T value = (T) operation.get(key);
        return value;
    }

    /**
     * 删除缓存对象
     * @param key 缓存键
     * @return 操作是否成功
     */
    public boolean deleteObject(final String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 删除集合对象
     * @param collection 多个缓存对象
     * @return
     */
    public Long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存 List 数据
     * @param key 缓存键
     * @param dataList 列表数据
     * @return
     * @param <T> 列表类型
     */
    public <T> Long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获取缓存 List
     * @param key 缓存键
     * @return
     * @param <T> 列表类型
     */
    public <T> List<T> getCacheList(final String key) {
        // 从 0 开始，-1 代表末尾
        return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存 Set
     * @param key 缓存键
     * @param dataSet 缓存集合
     * @return
     * @param <T> 缓存数据的对象
     */
    public <T> BoundSetOperations<Object, Object> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<Object, Object> operations = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            operations.add(it.next());
        }
        return operations;
    }

    /**
     * 获取缓存的 Set
     * @param key
     * @return
     * @param <T>
     */
    public <T> Set<T> getCacheSet(final String key) {
        return (Set<T>) redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存 Map
     * @param key 缓存键
     * @param dataMap 缓存数据
     * @param <T> 缓存 map 中 value 类型
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获取 Map
     * @param key 缓存键
     * @return  Map
     */
    public Map<Object, Object> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 缓存 Hash
     * @param key
     * @param hkey
     * @param value
     * @param <T>
     */
    public <T> void setCacheMapValue(final String key, final String hkey, final T value) {
        redisTemplate.opsForHash().put(key, hkey, value);
    }

    /**
     * 获取 Hash
     * @param key
     * @param hKey
     * @return
     * @param <T>
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<Object, Object, Object> operations = redisTemplate.opsForHash();
        return (T) operations.get(key, hKey);
    }
    /**
     * 删除 Hash
     * @param key
     * @param hKey
     */
    public void deleteCacheMapValue(final String key, final String hKey) {
        HashOperations<Object, Object, Object> operations = redisTemplate.opsForHash();
        operations.delete(key, hKey);
    }

    /**
     * 获取多个 Hash 数据
     * @param key
     * @param hKeys
     * @return
     * @param <T>
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return (List<T>) redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获取缓存中基本对象列表
     * @param pattern
     * @return
     */
    public Collection<Object> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void incrementCacheMapValue(final String key, final String hKey, int val) {
        redisTemplate.opsForHash().increment(key, hKey, val);
    }
}
