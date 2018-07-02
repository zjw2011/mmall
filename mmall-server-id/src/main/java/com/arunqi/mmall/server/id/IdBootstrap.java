package com.arunqi.mmall.server.id;

import com.alibaba.fastjson.JSONObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/30 下午11:40
 */
public class IdBootstrap {

    private static final ConcurrentMap<String, AtomicLong> ID_SEQUENTIAL =
            new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, Long> NEXT_ID_SEQUENTIAL =
            new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, Integer> LOAD_FACTOR =
            new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, Integer> SETP =
            new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, AtomicBoolean> HAS_LOAD_NEXT =
            new ConcurrentHashMap<>();

    static {
        ID_SEQUENTIAL.putIfAbsent("order", new AtomicLong(0));
        NEXT_ID_SEQUENTIAL.putIfAbsent("order", new Long(100));
    }

    /**
     * 入口函数.
     * @param args 参数
     * @return 
     */
    public static void main(String[] args) {
        AtomicLong id = ID_SEQUENTIAL.get("order");
        if (id == null) {
            JSONObject segment = new JSONObject();
            segment.put("biz_tag", "order");
            segment.put("max_id", 10000);
            segment.put("step", 1000);
            segment.put("load_factor", 30);
            //从数据库中获取 阻塞
            final String key = segment.getString("biz_tag");
            ID_SEQUENTIAL.putIfAbsent(key, new AtomicLong(segment.getLongValue("max_id")));
            NEXT_ID_SEQUENTIAL.putIfAbsent(key, new Long(segment.getLongValue("max_id")));
            LOAD_FACTOR.putIfAbsent(key, segment.getInteger("load_factor"));
            SETP.putIfAbsent(key, segment.getInteger("step"));
        }
        final long loadId = 100;
        System.out.println(id);
        for (int i = 0; i < 1000; i++) {
            long currId = ID_SEQUENTIAL.get("order").incrementAndGet();
            System.out.println(currId);
            if (i > loadId) {
                currId = ID_SEQUENTIAL.get("order").addAndGet(NEXT_ID_SEQUENTIAL.get("order").get() - i - 1);
                System.out.println(currId);
                //非阻塞
                //NEXT_ID_SEQUENTIAL.get("order").addAndGet(100);

            }
        }


        System.out.println("b->" + ID_SEQUENTIAL.get("order").incrementAndGet());
    }
}
