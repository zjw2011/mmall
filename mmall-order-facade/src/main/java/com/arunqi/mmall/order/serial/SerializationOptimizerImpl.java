package com.arunqi.mmall.order.serial;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/16 下午10:10
 */
public class SerializationOptimizerImpl implements SerializationOptimizer {

    /**
     * desc.
     * @return Collection
     */
    public Collection<Class> getSerializableClasses() {
        List<Class> classes = new LinkedList<Class>();
        //这里可以把所有需要进行序列化的类进行添加
        return classes;
    }
}
