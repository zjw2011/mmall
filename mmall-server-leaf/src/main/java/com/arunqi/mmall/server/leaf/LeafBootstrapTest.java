package com.arunqi.mmall.server.leaf;

import com.arunqi.mmall.facade.leaf.service.LeafService;
import com.arunqi.mmall.server.base.common.IdRedisUtil;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/7/1 上午10:51
 */
public class LeafBootstrapTest {

    /**
     * 启动.
     * @param args 参数
     * @return
     */
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:/spring-context-test.xml");
        System.out.println(context.getBean("leafService"));
        final LeafService leafService = (LeafService) context.getBean("leafService");
        final StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) context.getBean("stringRedisTemplate");
        final JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
        final String bizName = "order";
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newCachedThreadPool();
        //workID 最大256
        IdRedisUtil redisIdUtil = new IdRedisUtil(stringRedisTemplate, 0,"mmall:test:id:");
        for (int i = 0; i < 100; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int j = 0; j< 1000;j++) {
                        final Long id = redisIdUtil.createLongId(0);
                        final String sql = "insert into test_id (id) values(?)";
                        jdbcTemplate.update(sql, id);
                        System.out.println(Thread.currentThread().getName() + ",id:" + id);
                    }
                }
            });
        }
        countDownLatch.countDown();
        executorService.shutdown();


        DateTime dateTime = new DateTime(2000, 1, 1, 0, 0, 0, 0);
    }
}
