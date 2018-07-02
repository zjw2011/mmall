package com.arunqi.mmall.server.leaf.repository;

import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.server.leaf.dao.LeafSegmentDao;
import com.arunqi.mmall.server.leaf.model.IdModel;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 具体的业务逻辑.
 *
 * @author jiawei zhang
 * @datetime 2018/7/1 上午11:03
 */
@Repository
public class LeafRepository {

    private static final ConcurrentMap<String, IdModel> IDS =
            new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, IdModel> NEXT_IDS =
            new ConcurrentHashMap<>();

    private LeafSegmentDao leafSegmentDao;

    public LeafRepository() {
    }

    /**
     * desc.
     * @param bizName 业务名字
     * @param first 业务名字
     * @return
     */
    @Transactional
    public IdModel loadIdModel(final String bizName, final boolean first) {
        final JSONObject conditions = new JSONObject();
        conditions.put("biz_tag", bizName);
        final List<JSONObject> list = leafSegmentDao.query(conditions);
        if (list == null || list.size() < 1) {
            return null;
        }
        final JSONObject idEntity = list.get(0);
        final long maxId = idEntity.getLongValue("max_id");
        final int step = idEntity.getIntValue("step");
        final int loadFactor = idEntity.getIntValue("load_factor");
        final int rowversion = idEntity.getIntValue("rowversion");
        final long newMaxId = maxId + step;
        if (loadFactor < 1 || loadFactor > 99) {
            throw new RuntimeException("加载因子不正确");
        }

        final JSONObject updateCondition = new JSONObject();
        final JSONObject data = new JSONObject();
        data.put("max_id", newMaxId);
        data.put("rowversion", rowversion + 1);
        updateCondition.put("id", idEntity.getLongValue("id"));
        updateCondition.put("rowversion <", rowversion + 1);
        if (leafSegmentDao.update(data, updateCondition) != 1) {
            throw new RuntimeException("更新失败," + first);
        }

        final IdModel idModel = new IdModel();
        idModel.setId(new AtomicLong(maxId));
        idModel.setMaxId(maxId + step);
        idModel.setLoadId(maxId + ((step * loadFactor) / 100));
        idModel.setHasLoadNext(new AtomicBoolean(false));
        idModel.setHasMaxLoad(false);
        idModel.setStep(step);

        if (first) {
            IDS.putIfAbsent(bizName, idModel);
        } else {
            NEXT_IDS.put(bizName, idModel);
        }
        return idModel;
    }

    /**
     * desc.
     * @param bizName 业务名字
     * @param first 业务名字
     * @return
     */
    public IdModel tryLoadIdModel(final String bizName, final boolean first) {
        try {
            return loadIdModel(bizName, first);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * desc.
     * @param bizName 业务名字
     * @return
     */
    public Long getId(final String bizName) {
        final IdModel idModel = IDS.get(bizName);
        //第一次加载
        if (idModel == null) {
            //数据库里面会加锁
            tryLoadIdModel(bizName, true);

            final IdModel renewIdModel =  IDS.get(bizName);
            if (renewIdModel == null) {
                return null;
            }
            return renewIdModel.getId().incrementAndGet();
        }

        //防止并发操作 对象加锁 锁等待 粒度是整个对象 最好的方式是在每个bizName上加上锁
        //idModel是原来的
        if (idModel.getId().get() >= idModel.getMaxId()) {
            //如果还是没有加载下一个，就强制加载一次
            if (!idModel.getHasLoadNext().getAndSet(true)) {
                //操作失败
                if (tryLoadIdModel(bizName, false) == null) {
                    return null;
                }
            }
            idModel.copy(NEXT_IDS.get(bizName));
        }
        System.out.println(idModel.getId().get());
        //final long id = idModel.getId().incrementAndGet();
        //保证并发只有一个在操作 可以重复进行 更新nextIdModel 是否可以放到一个定时任务中
        if (idModel.getId().get() > idModel.getLoadId()) {
            //false -> true
            if (!idModel.getHasLoadNext().getAndSet(true)) {
                //操作失败可以重试
                if (tryLoadIdModel(bizName, false) == null) {
                    idModel.getHasLoadNext().set(false);
                }
            }
        }

        return idModel.getId().incrementAndGet();
    }

    @Autowired
    public void setLeafSegmentDao(LeafSegmentDao leafSegmentDao) {
        this.leafSegmentDao = leafSegmentDao;
    }

}
