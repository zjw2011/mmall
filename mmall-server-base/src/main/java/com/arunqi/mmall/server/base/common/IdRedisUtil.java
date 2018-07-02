package com.arunqi.mmall.server.base.common;

import org.joda.time.DateTime;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/7/2 上午10:00
 */
public class IdRedisUtil {

    private static final int DEFAULT_START_YEAR = 2018;

    private static final String DEFAULT_KEY = "default:id:";

    private StringRedisTemplate stringRedisTemplate;

    private int startYear;

    private String key;

    private String shortKey;

    private String longKey;

    private int workId;

    /**
     * 构造函数.
     */
    public IdRedisUtil(final StringRedisTemplate stringRedisTemplate,
                       final int workId,
                       final String key,
                       final int startYear
                       ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.startYear = startYear;
        this.key = key;
        this.workId = workId;
        this.shortKey = this.key + "short";
        this.longKey = this.key + "long";
    }

    /**
     * 构造函数.
     * @param stringRedisTemplate 工作ID
     * @param workId 工作ID
     */
    public IdRedisUtil(final StringRedisTemplate stringRedisTemplate,
                       final int workId,
                       final String key) {
        this(stringRedisTemplate, workId, key, DEFAULT_START_YEAR);
    }

    /**
     * 构造函数.
     * @param stringRedisTemplate 工作ID
     * @param workId 工作ID
     */
    public IdRedisUtil(final StringRedisTemplate stringRedisTemplate,
                       final int workId) {
        this(stringRedisTemplate, workId, DEFAULT_KEY);
    }

    private long[] buildMetadata(final long incr) {
        DateTime dateTime = new DateTime();
        long days = dateTime.getDayOfYear() + incr;
        long year = dateTime.getYear();
        if (year < startYear) {
            return null;
        }
        if (year != startYear)  { //不是同一年
            int timeDistance = 0;
            for (int i = startYear; i < year; i++) {
                timeDistance += 366;
            }

            days += timeDistance;
        }

        final long[] data = new long[2];
        data[0] = days;
        data[1] = dateTime.getMinuteOfDay();

        return data;
    }

    /**
     * desc.
     * @param incr 增长值
     * @return
     */
    public Long createLongId(final long incr) {
        Long seq = stringRedisTemplate.opsForValue().increment(this.longKey, 1);
        //抛出异常
        if (seq == null) {
            return null;
        }

        final long[] metadata = buildMetadata(incr);
        if (metadata == null) {
            return null;
        }

        long orderId =
                (metadata[0] << 40) | (workId << 32) | (metadata[1] << 20) | (seq & 0x0fffffL);

        return orderId;
    }

    /**
     * desc.
     * @param incr 增长值
     * @return 
     */
    public Long buildShortId(final long incr) {
        Long seq = stringRedisTemplate.opsForValue().increment(this.shortKey, 1);
        //抛出异常
        if (seq == null) {
            return null;
        }

        final long[] metadata = buildMetadata(incr);
        if (metadata == null) {
            return null;
        }

        //(119208L << 23)  | (1440 << 12) | 0x0fffL = 999995084799
        if (metadata == null || metadata[0] > 119208L) {
            return null;
        }
        //1分钟之内最多 4096笔

        long orderId = (metadata[0] << 23)  | (metadata[1] << 12) | (seq & 0x0fffL);

        return orderId;
    }

    /**
     * desc.
     * @param incr 增长值
     * @return
     */
    public Long buildMultiShortId(final long incr) {
        Long seq = stringRedisTemplate.opsForValue().increment(this.shortKey, 1);
        //抛出异常
        if (seq == null) {
            return null;
        }

        final long[] metadata = buildMetadata(incr);
        if (metadata == null) {
            return null;
        }

        //(7449L << 27)  | (0xffL << 19) | (1440L << 8) | 0xffL = 999921918207;
        if (metadata == null || metadata[0] > 7449L) {
            return null;
        }
        //1分钟之内最多 256笔

        long orderId = (metadata[0] << 27) | (workId << 19) | (metadata[1] << 8) | (seq & 0x0ffL);

        return orderId;
    }

    public long createShort12Id() {
        //(11921L << 23)  | (0 << 12) | 0x0L = 100000595968
        return buildShortId(11921L);
    }

    public long createShortId() {
        return buildShortId(0);
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
