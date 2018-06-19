package com.arunqi.mmall.orders;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/16 下午3:11
 */
public class TestListener {

    private ScheduledThreadPoolExecutor houseKeepingExecutorService;

    /**
     * desc.
     */
    public TestListener() {
        ThreadFactory threadFactory = new DefaultThreadFactory("housekeeper", true);
        this.houseKeepingExecutorService = new ScheduledThreadPoolExecutor(
                1, threadFactory, new ThreadPoolExecutor.DiscardPolicy());
        this.houseKeepingExecutorService.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        this.houseKeepingExecutorService.setRemoveOnCancelPolicy(true);
        this.houseKeepingExecutorService.scheduleWithFixedDelay(
                new HouseKeeper(), 100L, SECONDS.toMillis(30), MILLISECONDS);
    }

    public void close() throws InterruptedException {
        System.out.println("close");
        this.houseKeepingExecutorService.shutdown();
        this.houseKeepingExecutorService.awaitTermination(5L, SECONDS);
    }

}
