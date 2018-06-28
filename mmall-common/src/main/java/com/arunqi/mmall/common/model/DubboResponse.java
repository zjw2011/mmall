package com.arunqi.mmall.common.model;

import com.alibaba.fastjson.JSONObject;

/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/24 上午8:31
 */
public class DubboResponse {
    private JSONObject body;

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }
}
