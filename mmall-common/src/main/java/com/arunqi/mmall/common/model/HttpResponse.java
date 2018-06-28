package com.arunqi.mmall.common.model;

import java.util.Map;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/24 上午8:31
 */
public class HttpResponse {
    private Map<String, Object> body;

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
}
