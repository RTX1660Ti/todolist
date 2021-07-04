package org.example.todo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    public <T> T get(String key, Class<T> type) {
        return JSONObject.parseObject((String)redisTemplate.opsForValue().get(key), type);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long remove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 分布式锁
     *
     * @param key
     * @param value
     * @param timeout 秒为单位
     * @return
     */
    public boolean setNx(final String key, final String value, long timeout) {
        Object obj = redisTemplate.execute((RedisCallback<Object>) connection -> {
            Boolean success;
            try {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                success = connection.setNX(serializer.serialize(key), serializer.serialize(value));
                if (success && timeout > 0) {
                	connection.expire(serializer.serialize(key), timeout);
                }
            } finally {
                connection.close();
            }
            return success;
        });
        return obj != null ? (Boolean) obj : false;
    }

    /**
     * 获取list集合中的所有元素
     * @param key
     * @return
     */
    public List<String> lrange(final String key) {
        Object obj = redisTemplate.execute((RedisCallback<Object>) connection -> {
            List<String> result = new ArrayList<>();
            try {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                List<byte[]> ipList = connection.lRange(serializer.serialize(key), 0L, -1L);
                for(byte[] b : ipList){
                    result.add(serializer.deserialize(b));
                }
            } finally {
                connection.close();
            }
            return result;
        });
        return obj != null ? (List<String>) obj : new ArrayList<>();
    }

    /**
     * 获取list集合中的第一个元素
     * @param key
     * @return
     */
    public List<String> lrange1(final String key) {
        Object obj = redisTemplate.execute((RedisCallback<Object>) connection -> {
            List<String> result = new ArrayList<>();
            try {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                List<byte[]> ipList = connection.lRange(serializer.serialize(key), 0L, 0L);
                for(byte[] b : ipList){
                    result.add(serializer.deserialize(b));
                }
            } finally {
                connection.close();
            }
            return result;
        });
        return obj != null ? (List<String>) obj : new ArrayList<>();
    }

    /**
     * 实现命令：DEL key1 [key2 ...] 删除任意多个 key
     * @param keys
     * @return
     */
    public Long del(Collection<String> keys) {
        Set<String> keySet = new HashSet<>(keys);
        return redisTemplate.delete(keySet);
    }

    /**
     * 实现命令 : DEL key1 [key2 ...] 删除一个或多个key
     * @param keys
     * @return
     */
    public Long del(String... keys) {
        Set<String> keySet = Stream.of(keys).collect(Collectors.toSet());
        return redisTemplate.delete(keySet);
    }
    /**
     * @discription 设置key过期时间
     * @param key
     * @param timeout
     * @return boolean
     * @throws
     */
    public boolean expire(String key ,Long timeout){
        if (StringUtils.isEmpty(key) || null == timeout) {
            return false;
        }
        return redisTemplate.expire(key,timeout,TimeUnit.SECONDS);
    }

    public boolean exists(String key){
        if (StringUtils.isEmpty(key)){
            return false;
        }
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置键的步长
     * @param key 键
     * @param increment 增量
     * @return
     */
    public long generate(String key, int increment) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.addAndGet(increment);
    }
}
