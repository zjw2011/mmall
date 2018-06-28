package com.arunqi.mmall.server.order.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

/**
 * 订单表的增删改查，基本上都是但表操作.
 *
 * @author jiawei zhang
 * @datetime 2018/6/24 下午12:15
 */
@Repository
public class OrderRepo {

    private static final String TABLE_NAME = "orders";

    private String querySqlById;
    //需要增加条件
    private String querySql;

    private List<String> columns = new ArrayList<>();

    /**
     * desc.
     */
    public OrderRepo() {
        initColumns();
        initQuery(true);
        initQuery(false);
        initInsert();
    }

    private void initColumns() {
        columns.add("id");
        columns.add("order_status");
    }

    private void initQuery(boolean byId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        for (String column: columns) {
            sb.append(column).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" from ");
        sb.append(TABLE_NAME);

        if (byId) {
            sb.append(" where id = ?");
            this.querySqlById = sb.toString();
            return;
        }

        sb.append(" where 1=1 ");
        this.querySql = sb.toString();
    }

    private void initInsert() {
        StringBuilder sb = new StringBuilder();
        StringBuilder values = new StringBuilder();
        sb.append("insert into ");
        sb.append(TABLE_NAME);
        sb.append(" (");
        for (String column: columns) {
            sb.append(column).append(",");
            values.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        values.deleteCharAt(values.length() - 1);
        sb.append(")values(");
        sb.append(values.toString());
        sb.append(")");
    }

    /**
     * desc.
     * @param cols cols
     * @return
     */
    public void init(JSONObject cols) {
        StringBuilder sb = new StringBuilder();
        int colIndex = querySqlById.indexOf("*");
        sb.append(querySqlById.substring(0, colIndex));
        for (Map.Entry<String, Object> entry : cols.entrySet()) {
            sb.append(entry.getKey()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(querySqlById.substring(colIndex + 1));
        this.querySqlById = sb.toString();
    }

    public String getQuerySqlById() {
        return querySqlById;
    }

    public void setQuerySqlById(String querySqlById) {
        this.querySqlById = querySqlById;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }
}
