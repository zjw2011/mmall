package com.arunqi.mmall.server.openapi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.common.model.DubboRequest;
import com.arunqi.mmall.common.model.DubboResponse;
import com.arunqi.mmall.facade.order.service.OrderService;
import com.arunqi.mmall.server.openapi.service.OrderApiService;
import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Service;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/24 上午9:44
 */
@Path("/orders")
@Service("orderApiService")
@Produces({MediaType.APPLICATION_JSON})
public class OrderApiServiceImpl implements OrderApiService {

    @Resource
    private OrderService orderService;

    /**
     * 根据订单ID查询订单.
     * @param id 订单ID
     * @return 
     */
    @GET
    @Path("/{id : \\d+}")
    public String queryOrderById(@PathParam("id") Long id) {
        DubboRequest dubboRequest = new DubboRequest();
        DubboResponse dubboResponse = orderService.doOrder(dubboRequest);
        JSONObject body = new JSONObject();
        body.put("a", "b");

        //公共请求参数
        orderService.doOrder(body);

        return "aaa";
    }

    /**
     * 查询订单.
     * @return
     */
    @GET
    @Path("/id/{bizName}")
    public String getOrderId(@PathParam("bizName") String bizName) throws InterruptedException {
        return orderService.getOrderId(bizName);
    }


    /**
     * 查询订单.
     * @return
     */
    @GET
    @Path("/delete/{id : \\d+}")
    public void deleteOrder(@PathParam("id") Long id) {
        orderService.deleteOrder(id);
    }

}
