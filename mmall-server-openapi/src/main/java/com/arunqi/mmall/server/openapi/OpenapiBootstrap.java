package com.arunqi.mmall.server.openapi;

import com.alibaba.dubbo.container.Main;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/24 上午9:39
 */
public class OpenapiBootstrap {
    
    /**
     * 启动.
     * @param args 请求参数
     * @return 
     */
    public static void main(String[] args) {
        System.setProperty("dubbo.application.logger","slf4j");
        System.setProperty("dubbo.spring.config","spring-context.xml");
        //System.setProperty("dubbo.shutdown.hook", "true");
        Main.main(args);
    }
}
