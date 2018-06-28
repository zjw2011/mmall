package com.arunqi.mmall.server.base.service;

import com.arunqi.mmall.common.model.DubboRequest;
import com.arunqi.mmall.common.model.DubboResponse;

/**
 * 统一的业务逻辑处理.
 *
 * @author jiawei zhang
 * @datetime 2018/6/24 上午8:46
 */
public abstract class CommonServiceImpl {

    /**
     * 核心业务逻辑处理 可以考虑过滤器.
     * @param request 请求参数
     * @return HttpResponse
     */
    public DubboResponse doRun(DubboRequest request) {
        return null;
    }

}
