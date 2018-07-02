package com.arunqi.mmall.server.leaf;

import com.alibaba.dubbo.container.Main;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/7/1 上午10:51
 */
public class LeafBootstrap {

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
