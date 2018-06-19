package com.arunqi.mmall.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/6/16 下午12:08
 */
public class HouseKeeper implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HouseKeeper.class);
    @Override
    public void run() {
        logger.info("hello");
    }
}
