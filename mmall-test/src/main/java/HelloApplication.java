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

        long a = (1L << 2) | (1L << 4) | (1L << 60);
        for (int i = 0; i < 62; i++) {
            if (((1L << i) & a) == 0) {
                continue;
            }
            System.out.println("i:" + i);
        }


    }
}
