package com.arunqi.mmall.common.util;

import com.alibaba.fastjson.JSONObject;

public class FastJsonConvert {

    /**
     * <B>方法名称：</B>将JSONObject对象转换为实体对象<BR>.
     * <B>概要说明：</B>将JSONObject对象转换为实体对象<BR>
     * @param data JSONObject对象
     * @param clzss 转换对象
     * @return T
     */
    public static <T> T convertJsonToObject(JSONObject data, Class<T> clzss) {
        return JSONObject.toJavaObject(data, clzss);
    }
}
