package com.arunqi.mmall.order.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.RpcException;
import com.arunqi.mmall.order.facade.TestFacade;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/6/16 下午8:55
 */
@Path("/")
@Service("testService")
public class TestService implements TestFacade {
    @GET
    @Path("/hello")
    public String hello() {
        throw new RpcException("RPC错误");
        //return "hello from order";
    }

}
