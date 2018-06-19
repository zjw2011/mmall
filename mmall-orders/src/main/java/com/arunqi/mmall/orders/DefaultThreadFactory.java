package com.arunqi.mmall.orders;

import java.util.concurrent.ThreadFactory;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/6/16 下午12:04
 */
public class DefaultThreadFactory implements ThreadFactory {

    private final String threadName;
    private final boolean daemon;

    public DefaultThreadFactory(String threadName, boolean daemon) {
        this.threadName = threadName;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, threadName);
        thread.setDaemon(daemon);
        return thread;
    }
}
