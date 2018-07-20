package com.arunqi.mmall.server.leaf;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/7/1 上午10:51
 */
public class RedissonTest {

    /**
     * 启动.
     * @param args 参数
     * @return
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:/spring-context-test.xml");
        System.out.println(context.getBean("redisson"));

        RedissonClient client = (RedissonClient)  context.getBean("redisson");
        RAtomicLong longObject = client.getAtomicLong("myLong");
        // 同步执行方式
        //longObject.compareAndSet(401, 402);

        RFuture<Boolean> future = longObject.compareAndSetAsync(1, 401);
        future.addListener(new FutureListener<Boolean>() {
            @Override
            public void operationComplete(Future<Boolean> future) throws Exception {
                if (future.isSuccess()) {
                    // 取得结果
                    Boolean result = future.getNow();
                    System.out.println(result);
                } else {
                    // 对发生错误的处理
                    Throwable cause = future.cause();
                }
            }
        });
        System.out.println(longObject);
        context.close();

    }
}
