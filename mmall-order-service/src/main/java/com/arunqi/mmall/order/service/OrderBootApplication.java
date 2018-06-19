package com.arunqi.mmall.order.service;

import com.alibaba.dubbo.container.Main;

/**
 * desc.
 *
 * @author jiawei zhang
 * @dateime 2018/6/17 上午12:01
 */
public class OrderBootApplication {

    /**
     * 启动.
     * @param args 参数列表
     */
    public static void main(String[] args) {
        System.setProperty("dubbo.application.logger","slf4j");
        System.setProperty("dubbo.spring.config","spring-context.xml");
        //System.setProperty("dubbo.shutdown.hook", "true");
        Main.main(args);
    }
}
