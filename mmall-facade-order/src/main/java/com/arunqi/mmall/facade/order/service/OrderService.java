package com.arunqi.mmall.facade.order.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.common.model.DubboRequest;
import com.arunqi.mmall.common.model.DubboResponse;

/**
 * 订单接口.
 *
 * @author jiawei zhang
 * @datetime 2018/6/24 上午7:42
 */
public interface OrderService {
    //1 下订单 订单状态 支付状态
    /**
     * 下订单.
     */
    DubboResponse doOrder(DubboRequest request);
    JSONObject doOrder(JSONObject request);

}
