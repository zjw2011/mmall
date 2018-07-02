package com.arunqi.mmall.server.leaf.model;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/7/1 上午11:10
 */
public class IdModel {

    private AtomicLong id;
    private Long maxId;
    private Long loadId;
    private Integer step;
    private AtomicBoolean hasLoadNext;
    private Boolean hasMaxLoad;

    public AtomicLong getId() {
        return id;
    }

    public void setId(AtomicLong id) {
        this.id = id;
    }

    public Long getMaxId() {
        return maxId;
    }

    public void setMaxId(Long maxId) {
        this.maxId = maxId;
    }

    public Long getLoadId() {
        return loadId;
    }

    public void setLoadId(Long loadId) {
        this.loadId = loadId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public AtomicBoolean getHasLoadNext() {
        return hasLoadNext;
    }

    public void setHasLoadNext(AtomicBoolean hasLoadNext) {
        this.hasLoadNext = hasLoadNext;
    }

    public Boolean getHasMaxLoad() {
        return hasMaxLoad;
    }

    public void setHasMaxLoad(Boolean hasMaxLoad) {
        this.hasMaxLoad = hasMaxLoad;
    }

    /**
     * desc.
     * @param newIdModel 新的模型
     * @return 
     */
    public synchronized void copy(final IdModel newIdModel) {
        if (!newIdModel.getHasMaxLoad()) {
            this.id = newIdModel.getId();
            this.maxId = newIdModel.getMaxId();
            this.loadId = newIdModel.getLoadId();
            this.step = newIdModel.getStep();
            this.hasLoadNext = newIdModel.getHasLoadNext();
            this.hasMaxLoad = newIdModel.getHasMaxLoad();
            newIdModel.setHasMaxLoad(true);
        }
    }

    @Override
    public String toString() {
        return "IdModel{" +
                "id=" + id +
                ", maxId=" + maxId +
                ", loadId=" + loadId +
                ", step=" + step +
                ", hasLoadNext=" + hasLoadNext +
                ", hasMaxLoad=" + hasMaxLoad +
                '}';
    }
}
