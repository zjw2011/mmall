package com.arunqi.mmall.server.order.repository;

import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.server.base.dao.Page;
import com.arunqi.mmall.server.order.cache.OrderRecordCache;
import com.arunqi.mmall.server.order.dao.OrderRecordDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 可以提供事务相关的配置 可以调用dao和cache.
 * 基本上就是对于订单表的增删改查，分页查询.
 *
 * @author jiawei zhang
 * @datetime 2018/6/28 下午5:44
 */
@Repository
public class OrderRecordRepository {

    private OrderRecordCache orderRecordCache;

    private OrderRecordDao orderRecordDao;

    public OrderRecordRepository() {
    }

    /**
     * 通过ID查询.
     * @param id id
     * @return
     */
    public JSONObject queryById(final Long id) {
        final JSONObject cacheData = orderRecordCache.getRecord(String.valueOf(id));
        if (cacheData != null) {
            return cacheData;
        }

        final JSONObject data = orderRecordDao.queryById(id);
        if (data == null) {
            return null;
        }
        orderRecordCache.saveRecord(data);
        return data;
    }

    /**
     * desc.
     * @param conditions 条件
     * @return 
     */
    public List<JSONObject> queryForList(final JSONObject conditions) {
        final List<Long> data = orderRecordDao.queryOnlyId(conditions);
        final List<JSONObject> newData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            final JSONObject record = queryById(data.get(i));
            if (record == null) {
                return null;
            }
            newData.add(record);
        }
        return newData;
    }

    /**
     * desc.
     * @param conditions 条件
     * @param pageNo 页码 初始是1
     * @param pageSize 页大小
     * @return
     */
    public Page queryForPage(final JSONObject conditions, final int pageNo, final int pageSize) {
        final Page page = orderRecordDao.queryOnlyIdByPage(conditions, pageNo, pageSize);
        final List<JSONObject> data = page.getData();
        final List<JSONObject> newData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            final JSONObject element = data.get(i);
            final long id = element.getLongValue("id");
            if (id == 0) {
                return null;
            }
            final JSONObject record = queryById(id);
            if (record == null) {
                return null;
            }
            newData.add(record);
        }
        page.setData(newData);
        return page;
    }

    public Page queryForPage(final JSONObject conditions) {
        return queryForPage(conditions, 1, 20);
    }

    /**
     * 添加数据.
     * @param data 数据
     * @return
     */
    public boolean save(final JSONObject data) {
        if (orderRecordDao.insert(data) != 1) {
            return false;
        }
        orderRecordCache.saveRecord(data);
        return true;
    }

    /**
     * 修改.
     * @param data 数据
     * @param conditions 条件
     * @return
     */
    @Transactional
    public void update(final JSONObject data, final JSONObject conditions) {
        Assert.isTrue((data != null && data.size() > 0), "更新数据不能为空");
        Assert.isTrue((conditions != null), "更新条件不能为空");
        //强制从数据库中查询 一般是根据ID更新
        final List<JSONObject> dataList = orderRecordDao.query(conditions);
        for (int i = 0; i < dataList.size(); i++) {
            final JSONObject record = dataList.get(i);
            final JSONObject idOfCondition = new JSONObject();
            //行版本号 别人不能更改
            final long rowversion = record.getLongValue("rowversion"); //1 1
            idOfCondition.put("id", record.getLongValue("id"));
            idOfCondition.put("rowversion <", rowversion + 1); //1->2
            data.put("rowversion", rowversion + 1);
            if (orderRecordDao.update(data, idOfCondition) != 1) {
                throw new RuntimeException("更新失败");
            }
            for (Map.Entry<String, Object> entry: data.entrySet()) {
                record.put(entry.getKey(), entry.getValue());
            }
        }

        //直接从缓存中更新
        for (int i = 0; i < dataList.size(); i++) {
            orderRecordCache.saveRecord(dataList.get(i));
        }
    }

    /**
     * 删除.
     * @param conditions 条件
     * @return
     */
    @Transactional
    public void delete(final JSONObject conditions) {
        Assert.isTrue((conditions != null && conditions.size() > 0), "删除条件不能为空");
        //强制从数据库中查询 考虑并发的问题
        final List<JSONObject> dataList = orderRecordDao.query(conditions);
        for (int i = 0; i < dataList.size(); i++) {
            final JSONObject record = dataList.get(i);
            final JSONObject idOfCondition = new JSONObject();
            final long rowversion = record.getLongValue("rowversion"); //1 1
            idOfCondition.put("id", record.getLongValue("id"));
            idOfCondition.put("rowversion <", rowversion + 1); //1->2
            final JSONObject data = new JSONObject();
            data.put("data_status", 99);
            data.put("rowversion", rowversion + 1);
            if (orderRecordDao.update(data, idOfCondition) != 1) {
                throw new RuntimeException("更新失败");
            }
        }

        //直接从缓存中更新
        for (int i = 0; i < dataList.size(); i++) {
            final JSONObject record = dataList.get(i);
            orderRecordCache.delete(String.valueOf(record.getLongValue("id")));
        }
    }

    @Autowired
    public void setOrderRecordCache(OrderRecordCache orderRecordCache) {
        this.orderRecordCache = orderRecordCache;
    }

    @Autowired
    public void setOrderRecordDao(OrderRecordDao orderRecordDao) {
        this.orderRecordDao = orderRecordDao;
    }
}
