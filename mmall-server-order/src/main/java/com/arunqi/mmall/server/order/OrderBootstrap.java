package com.arunqi.mmall.server.order;

import com.alibaba.dubbo.container.Main;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/23 下午10:00
 */
public class OrderBootstrap {

    /**
     * 启动.
     * @param args 参数
     * @return
     */
    public static void main(String[] args) {
        System.setProperty("dubbo.application.logger","slf4j");
        System.setProperty("dubbo.spring.config","spring-context.xml");
        //System.setProperty("dubbo.shutdown.hook", "true");
        Main.main(args);
    }

}
