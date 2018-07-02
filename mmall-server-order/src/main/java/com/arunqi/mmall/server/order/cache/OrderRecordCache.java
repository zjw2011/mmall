package com.arunqi.mmall.server.order.cache;

import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.server.base.cache.BaseJdbcCache;
import com.arunqi.mmall.server.order.dao.OrderRecordDao;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 数据库表数据缓存订单表.
 *
 * @author jiawei zhang
 * @datetime 2018/6/24 下午12:18
 */
@Component
public class OrderRecordCache extends BaseJdbcCache {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${redis.data.timeout}")
    private int expire;

    public OrderRecordCache() {
    }

    //curd create update read delete
    public JSONObject getRecord(final String id) {
        return super.read(id, expire);
    }

    public void saveRecord(final JSONObject data) {
        super.create(data, expire);
    }

    @Override
    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    @Override
    public String getNamespace() {
        return OrderRecordDao.getSchameTableName();
    }
}
