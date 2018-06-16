package com.arunqi.mmall.order.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.alibaba.dubbo.config.annotation.Service;
import com.arunqi.mmall.order.facade.TestFacade;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/6/16 下午8:55
 */
@Path("/")
//@Service
@Service(interfaceClass = com.arunqi.mmall.order.facade.TestFacade.class, protocol = {"rest", "dubbo"})
public class TestService implements TestFacade {
    @Override
    @GET
    @Path("/hello")
    public String hello() {
        return "hello";
    }
}
