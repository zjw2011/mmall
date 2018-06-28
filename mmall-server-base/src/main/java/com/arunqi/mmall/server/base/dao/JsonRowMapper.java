package com.arunqi.mmall.server.base.dao;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;


/**
 * desc.
 * 
 * @author 交通运输部规划研究院（bhz）
 * @since 2013-10-28
 */
public class JsonRowMapper implements RowMapper<JSONObject> {

    /**
     * 映射行数据.
     *
     * @param rs 结果集
     * @param row 行号
     * @return JSONObject 数据
     * @throws SQLException SQL异常错误
     * @see RowMapper#mapRow(ResultSet,
     *      int)
     */
    public JSONObject mapRow(ResultSet rs, int row) throws SQLException {
        String key = null;
        Object obj = null;
        JSONObject json = new JSONObject();
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int i = 1; i <= count; i++) {
            key = JdbcUtils.lookupColumnName(rsmd, i);
            obj = JdbcUtils.getResultSetValue(rs, i);
            try {
                json.put(key, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json;
    }
}
