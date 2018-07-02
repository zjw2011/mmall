package com.arunqi.mmall.server.order.service;

import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.common.model.DubboRequest;
import com.arunqi.mmall.common.model.DubboResponse;
import com.arunqi.mmall.facade.leaf.service.LeafService;
import com.arunqi.mmall.facade.order.service.OrderService;
import com.arunqi.mmall.server.base.dao.Page;
import com.arunqi.mmall.server.base.service.CommonServiceImpl;
import com.arunqi.mmall.server.order.dao.OrderRecordDao;
import com.arunqi.mmall.server.order.repository.OrderRecordRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

/**
 * 数据的校验 业务逻辑处理.
 *
 * @author jiawei zhang
 * @datetime 2018/6/24 上午7:41
 */
@Service("orderService")
public class OrderServiceImpl extends CommonServiceImpl implements OrderService {

    @Resource
    private OrderRecordDao orderDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OrderRecordRepository orderRecordRepository;

    @Resource
    private LeafService leafService;


    @Override
    public DubboResponse doOrder(DubboRequest request) {

        //1 其他的调用这个需要传递的参数

        //这个是入口
        Map<String, Object> body = request.getBody();

        return new DubboResponse();
    }

    @Override
    public JSONObject doOrder(final JSONObject request) {
//        Date date = orderDao.getCurrentTime();
//        final String sql = "select * from zipkin_spans";
//        final Page page = orderDao.queryForPage(sql);
//        System.out.println("date:" + date);
//        System.out.println("page:" + page.getCount() + "," + page.getData());
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("a", "b");
//        final JSONObject jsonObject = new JSONObject();
//        jsonObject.put("id", 1);
//        jsonObject.put("order_status", 0);
//        jsonObject.put("pay_status", 0);
//        jsonObject.put("rowversion", 0);
//
//        int result = orderDao.insert(jsonObject);
//        System.out.println("result:" + result);

        final JSONObject jsonObject2 = new JSONObject();
        final JSONObject condition = new JSONObject();
        condition.put("id", 1);
        jsonObject.put("rowversion", 2);
        jsonObject.put("order_status", 1);
        int result = orderDao.update(jsonObject, condition);
        System.out.println("result:" + result);

        Page page = orderDao.queryByPage(condition, 1, 20);
        System.out.println("page:" + page.getData());

        List<Long> ids = orderDao.queryOnlyId(new JSONObject());
        //jsonObject2.toJSONString();
        //Page page1orderDao.queryForPage(sql);
        //orderRepo.init(jsonObject);
        //System.out.println(orderRepo.getQuerySqlById());
        stringRedisTemplate.opsForValue().set("a", "");
        System.out.println(ClassUtils.getShortName(OrderServiceImpl.class));
        return null;
    }

    @Override
    public void deleteOrder(final Long id) {
        final JSONObject conditions = new JSONObject();
        conditions.put("id", id);
        orderRecordRepository.delete(conditions);
    }

    @Override
    public String getOrderId(String bizName) throws InterruptedException {
        final Long id = leafService.getId(bizName);
        return (id == null) ? "" : id.toString();
    }
}
