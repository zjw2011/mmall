package com.arunqi.mmall.order.facade;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * desc.
 *
 * @author jiawei zhang
 * 2018/6/16 下午4:06
 */
//@Path("/")
//@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
//@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface TestFacade {
//    @GET
//    @Path("/hello")
    String hello();
}
