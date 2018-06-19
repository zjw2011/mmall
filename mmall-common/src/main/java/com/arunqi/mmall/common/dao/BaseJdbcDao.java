package com.arunqi.mmall.common.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSONObject;

public class BaseJdbcDao {

    /**
     * JSON数据行映射器
     */
    private static final JsonRowMapper JSON_ROW_MAPPER = new JsonRowMapper();

    /** JDBC调用模板 */
    private JdbcTemplate jdbcTemplate;

    /**
     * <B>方法名称：</B>初始化JDBC调用模板<BR>
     * <B>概要说明：</B><BR>
     *
     * @param dataSource 数据源
     */
    public BaseJdbcDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * <B>取得：</B>JDBC调用模板<BR>
     *
     * @return JdbcTemplate JDBC调用模板
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * <B>方法名称：</B>查询JSON列表<BR>
     * <B>概要说明：</B><BR>
     *
     * @param sql SQL语句
     * @param args 参数
     * @return List<JSONObject> JSON列表
     */
    public List<JSONObject> queryForJsonList(String sql, Object... args) {
        return this.jdbcTemplate.query(sql, JSON_ROW_MAPPER, args);
    }

    /**
     * <B>方法名称：</B>单表INSERT方法<BR>
     * <B>概要说明：</B>单表INSERT方法<BR>
     * @param tableName 表名
     * @param data JSONObject对象
     */
    protected int insert(String tableName, JSONObject data) {

        if (data.size() <= 0) {
            return 0;
        }

        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO ");
        sql.append(tableName + " ( ");

        Set<Entry<String, Object>> set = data.entrySet();
        List<Object> sqlArgs = new ArrayList<>();
        for (Iterator<Entry<String, Object>> iterator = set.iterator(); iterator.hasNext();) {
            Entry<String, Object> entry = iterator.next();
            sql.append(entry.getKey() + ",");
            sqlArgs.add(entry.getValue());
        }

        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) VALUES ( ");
        for (int i = 0; i < set.size(); i++) {
            sql.append("?,");
        }

        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) ");

        return this.getJdbcTemplate().update(sql.toString(), sqlArgs.toArray());
    }

}