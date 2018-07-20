package com.arunqi.mmall.server.base.common;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 分布式锁.
 * 同一时间只有一个线程可以访问，其他线程访问返回失败，适合于可以重复通知的代码.
 *
 * @author jiawei zhang
 * @datetiem 2018/7/15 下午5:39
 */
public class MutexLockRedisUtil {

    private static final String DEFAULT_SUBFIX = ":mutex:lock:";

    private StringRedisTemplate stringRedisTemplate;

    public MutexLockRedisUtil(final StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * desc.
     * @param name name
     * @return
     */
    public boolean lock(final String name) {
        final String key = DEFAULT_SUBFIX + name;
        final Boolean resultSet =
                stringRedisTemplate.opsForValue().setIfAbsent(key, name);
        if (!resultSet) {
            return false;
        }

        return true;
    }

    public void unlock(final String name) {
        final String key = DEFAULT_SUBFIX + name;
        stringRedisTemplate.delete(key);
    }
}
