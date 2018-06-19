package com.arunqi.mmall.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试.
 *
 * @author jiawei zhang
 * @datetime 2018/6/14 下午2:04
*/
public class HelloApplication {

    private static Logger logger = LoggerFactory.getLogger(HelloApplication.class);

    /**
     * desc.
     * @param args 参数
     * @return 
     */
    public static void main(String[] args) throws InterruptedException {
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        //TimeUnit.SECONDS.sleep(20);
    }
}
