package com.arunqi.mmall.server.order.dao;

import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.server.base.dao.BaseJdbcDao;
import com.arunqi.mmall.server.base.dao.Page;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 可以重写里面的SQL方法.
 *
 * @author jiawei zhang
 * @datetime 2018/6/25 上午10:49
 */
@Repository
public class OrderRecordDao extends BaseJdbcDao {

    private static final String DATABASE_NAME = "orders";
    private static final String TABLE_NAME = "order_record";
    private static final String COLUMNS =
            "id,order_status,pay_status,data_status,createtime,updatetime,rowversion";

    private static final String SCHAME_TABLE_NAME = DATABASE_NAME + "." + TABLE_NAME;

    private static final String DEAFULT_SQL_QUERY_BY_ID
            = "select " + COLUMNS + " from " + TABLE_NAME + " where data_status != 99 and id = ?";
    private static final String DEAFULT_SQL_QUERY
            = "select " + COLUMNS + " from " + TABLE_NAME + " where data_status != 99 ";

    private static final String DEAFULT_SQL_QUERY_ONLY_ID
            = "select id from " + TABLE_NAME + " where data_status != 99 ";


    private String sqlQueryById;
    private String sqlQuery;
    private String sqlQueryOnlyId;

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * desc.
     */
    public OrderRecordDao() {
        sqlQueryById = DEAFULT_SQL_QUERY_BY_ID;
        sqlQuery = DEAFULT_SQL_QUERY;
        sqlQueryOnlyId = DEAFULT_SQL_QUERY_ONLY_ID;
    }

    /**
     * 通过ID查询.
     * @param id id
     * @return
     */
    public JSONObject queryById(final Long id) {
        return super.queryById(sqlQueryById, id);
    }

    /**
     * 按照条件查询.
     * @param conditions 查询条件
     * @return
     */
    public List<JSONObject> query(final JSONObject conditions) {
        return super.queryForJsonList(conditions, sqlQuery);
    }

    /**
     * 按照条件查询.
     * @param conditions 查询条件
     * @return
     */
    public List<Long> queryOnlyId(final JSONObject conditions) {
        return super.queryForLongList(conditions, sqlQueryOnlyId);
    }

    /**
     * desc.
     * @param data 要插入的数据
     * @return
     */
    public int insert(final JSONObject data) {
        return super.insert(TABLE_NAME, data);
    }

    /**
     * desc.
     * @param data 要更新的数据
     * @param condition 条件
     * @return
     */
    public int update(final JSONObject data, final JSONObject condition) {
        return super.update(TABLE_NAME, data, condition);
    }

    /**
     * 分页查询.
     * @param condition 条件
     * @param pageNo 页码
     * @param pageSize 页大小
     * @return
     */
    public Page queryByPage(final JSONObject condition,
                            final int pageNo, final int pageSize) {
        return super.queryForPage(condition, pageNo, pageSize, sqlQuery);
    }

    /**
     * 分页查询.
     * @param condition 条件
     * @param pageNo 页码
     * @param pageSize 页大小
     * @return
     */
    public Page queryOnlyIdByPage(final JSONObject condition,
                            final int pageNo, final int pageSize) {
        return super.queryForPage(condition, pageNo, pageSize, sqlQueryOnlyId);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public String getSqlQueryById() {
        return sqlQueryById;
    }

    public void setSqlQueryById(String sqlQueryById) {
        this.sqlQueryById = sqlQueryById;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getSqlQueryOnlyId() {
        return sqlQueryOnlyId;
    }

    public void setSqlQueryOnlyId(String sqlQueryOnlyId) {
        this.sqlQueryOnlyId = sqlQueryOnlyId;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getSchameTableName() {
        return SCHAME_TABLE_NAME;
    }
}
