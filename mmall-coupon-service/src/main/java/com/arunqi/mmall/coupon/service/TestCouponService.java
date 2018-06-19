package com.arunqi.mmall.coupon.service;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.arunqi.mmall.coupon.facade.TestCouponFacade;
import com.arunqi.mmall.order.facade.TestFacade;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/16 下午8:55
 */
@Path("/")
@Service("testCouponService")
public class TestCouponService implements TestCouponFacade {

    @Resource
    private TestFacade testFacade;

    @GET
    @Path("/hello")
    @Produces({MediaType.APPLICATION_JSON})
    public String hello() {
        testFacade.hello();
        return "hello";
    }
}
