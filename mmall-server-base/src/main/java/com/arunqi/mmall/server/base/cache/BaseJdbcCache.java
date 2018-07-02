package com.arunqi.mmall.server.base.cache;

import com.alibaba.fastjson.JSONObject;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 保存为JSON.
 *
 * @author jiawei zhang
 * @datetime 2018/6/29 下午1:04
 */
public abstract class BaseJdbcCache {

    private static final String CONNECTOR = ":";

    public abstract StringRedisTemplate getStringRedisTemplate();

    //一般是数据库名+表名 orders.order_record:1
    public abstract String getNamespace();

    /**
     * 通过ID获取对象,同时设置过期时间.
     *
     * @param id     id.
     * @param expire 过期时间，单位：毫秒.
     * @return 实体对象.
     */
    protected JSONObject read(final String id, final int expire) {
        Assert.hasText(id, "id不能为空");
        final String key = getNamespace() + CONNECTOR + id;
        final ValueOperations<String, String> opsForValue = getStringRedisTemplate().opsForValue();
        final String jsonStr = opsForValue.get(key);
        if (StringUtils.hasText(jsonStr)) {
            getStringRedisTemplate().expire(key, expire, TimeUnit.MILLISECONDS);
            return JSONObject.parseObject(jsonStr);
        }

        return null;
    }

    /**
     * 保存实体.
     *
     * @param data v
     * @param expire 过期时间，单位：毫秒.
     */
    protected void create(final JSONObject data, final int expire) {
        Assert.isTrue((data != null && data.size() > 0), "data不能为空");
        final String key = getNamespace() + CONNECTOR + String.valueOf(data.get("id"));
        final ValueOperations<String, String> opsForValue = getStringRedisTemplate().opsForValue();
        opsForValue.set(key, data.toJSONString(), expire, TimeUnit.MILLISECONDS);
    }

    /**
     * 删除对象.
     *
     * @param id id.
     */
    public void delete(final String id) {
        final String key = getNamespace() + CONNECTOR + id;
        getStringRedisTemplate().delete(key);
    }
}
