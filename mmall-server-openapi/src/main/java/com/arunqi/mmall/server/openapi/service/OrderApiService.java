package com.arunqi.mmall.server.openapi.service;

import javax.ws.rs.PathParam;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/6/24 上午9:43
 */
public interface OrderApiService {
    String queryOrderById(Long id);
    String getOrderId(String bizName) throws InterruptedException;
    void deleteOrder(Long id);
}
