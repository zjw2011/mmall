package com.arunqi.mmall.server.order.service;

import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.common.model.DubboRequest;
import com.arunqi.mmall.common.model.DubboResponse;
import com.arunqi.mmall.facade.order.service.OrderService;
import com.arunqi.mmall.server.base.dao.Page;
import com.arunqi.mmall.server.base.service.CommonServiceImpl;
import com.arunqi.mmall.server.order.dao.OrderDao;
import com.arunqi.mmall.server.order.repository.OrderRepo;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 基本上就是对于订单表的增删改查，分页查询.
 *
 * @author jiawei zhang
 * @datetime 2018/6/24 上午7:41
 */
@Service("orderService")
public class OrderServiceImpl extends CommonServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderRepo orderRepo;

    @Override
    public DubboResponse doOrder(DubboRequest request) {

        //1 其他的调用这个需要传递的参数

        //这个是入口
        Map<String, Object> body = request.getBody();

        return new DubboResponse();
    }

    @Override
    public JSONObject doOrder(JSONObject request) {
        Date date = orderDao.getCurrentTime();
        final String sql = "select * from zipkin_spans";
        final Page page = orderDao.queryForPage(sql);
        System.out.println("date:" + date);
        System.out.println("page:" + page.getCount() + "," + page.getData());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", "b");
        //Page page1orderDao.queryForPage(sql);
        //orderRepo.init(jsonObject);
        //System.out.println(orderRepo.getQuerySqlById());
        return null;
    }
}
