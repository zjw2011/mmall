package com.arunqi.mmall.orders;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.common.dao.BaseJdbcDao;
import com.arunqi.mmall.common.dao.EntityEnum;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/15 下午2:04
 */
public class TestJdbc {

    private static final Logger logger = LoggerFactory.getLogger(TestJdbc.class);

    /**
     * desc.
     * @param args 参数
     * @return 
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        ClassPathXmlApplicationContext xmlApplicationContext =
                new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        xmlApplicationContext.start();
        BaseJdbcDao baseJdbcDao = (BaseJdbcDao) xmlApplicationContext.getBean("baseJdbcDao");
        List<JSONObject> lists = baseJdbcDao.queryForJsonList("select public_key from app");
        logger.info("lists"+ lists);

        Map<String, String> map = new HashMap<>();
        Map<String, String> revertMap = new HashMap<>();
        map.put("a", "b");
        EntityEnum entityEnum = EntityEnum.valueOf("name");
        /*
        for (Map.Entry<? extends K, ? extends List<? extends V>> entry : map.entrySet()) {
            List<? extends V> values = Collections.unmodifiableList(entry.getValue());
            result.put(entry.getKey(), (List<V>) values);
        }*/
        TimeUnit.SECONDS.sleep(60);
        xmlApplicationContext.close();
        //System.in.read();
    }
}
