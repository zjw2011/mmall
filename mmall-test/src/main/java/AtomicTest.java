import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/7/1 下午7:25
 */
public class AtomicTest {

    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + ",id:");

                }
            });
        }
        countDownLatch.countDown();
        executorService.shutdown();
    }
}
