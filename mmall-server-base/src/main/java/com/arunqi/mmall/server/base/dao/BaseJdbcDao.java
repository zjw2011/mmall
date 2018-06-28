package com.arunqi.mmall.server.base.dao;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.common.dao.JsonRowMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * desc.
 * @author jiawei zhang
 * @since
 */
public abstract class BaseJdbcDao {

    private static final ExecutorService COUNT_EXCUTOR = Executors.newCachedThreadPool();

    //JSON数据行映射器
    private static final JsonRowMapper JSON_ROW_MAPPER = new JsonRowMapper();

    //启动时间
    private Date startTime;

    /**
     * 初始化JDBC调用模板.
     *
     */
    public BaseJdbcDao() {
    }

    /**
     * JDBC调用模板.
     *
     * @return JdbcTemplate JDBC调用模板
     */
    public abstract JdbcTemplate getJdbcTemplate();

    /**
     * 取得启动时间.
     * @return Date 启动时间
     */
    public Date getStartTime() {
        return startTime;
    }

    public Date getCurrentTime() {
        return this.getJdbcTemplate().queryForObject("SELECT CURRENT_TIMESTAMP", Date.class);
    }

    /**
     * 查询JSON列表.
     *
     * @param sql SQL语句
     * @param args 参数
     * @return List JSON列表
     */
    public List<JSONObject> queryForJsonList(String sql, Object... args) {
        return this.getJdbcTemplate().query(sql, JSON_ROW_MAPPER, args);
    }

    /**
     * 查询JSON数据.
     *
     * @param sql SQL语句
     * @param args 参数
     * @return JSONObject JSON数据
     */
    public JSONObject queryForJsonObject(String sql, Object... args) {
        List<JSONObject> jsonList = queryForJsonList(sql, args);
        if (jsonList == null || jsonList.size() < 1) {
            return null;
        }
        return jsonList.get(0);
    }

    /**
     * 查询文本.
     *
     * @param sql SQL语句
     * @param args 参数
     * @return String 文本
     */
    public String queryForString(String sql, Object... args) {
        List<String> dataList = this.getJdbcTemplate().queryForList(sql, args, String.class);
        if (dataList == null || dataList.size() < 1) {
            return null;
        }
        return dataList.get(0);
    }

    /**
     * 查询文本.
     *
     * @param sql SQL语句
     * @param args 参数
     * @return String 文本
     */
    public Page queryForPage(String sql, int pageNum, int pageSize, Object... args) {

        final Page page = new Page();
        page.setCurrNum(pageNum);
        page.setPreSize(pageSize);
        page.setCount(0);
        //可以考虑并发 查询和查询总数并行
        final Future<Long> totalFuture = queryPageCount(sql, args);

        page.setData(queryForJsonList(buildSqlWithPageLimit(page, sql), args));
        try {
            long total = totalFuture.get();
            if (total > 0) {
                page.setCount(total);
                return page;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        page.setData(null);
        return page;
    }

    /**
     * 查询文本.
     *
     * @param sql SQL语句
     * @param args 参数
     * @return String 文本
     */
    public Page queryForPage(String sql, Object... args) {
        return queryForPage(sql, 1, 10, args);
    }


    /**
     * 专门为?的自定义sql获取总数.
     * @param sql 语句
     * @param args 参数
     * @return
     */
    private Future<Long> queryPageCount(String sql, Object[] args) {
        return COUNT_EXCUTOR.submit(new Callable<Long>() {
            @Override
            public Long call() {
                final String e = sql.substring(sql.indexOf("from", 8));
                final String c = "select count(*) " + e;
                final long cnt = getJdbcTemplate().queryForObject(c, args, Long.class);
                return cnt;
            }
        });
    }

    /**
     * 为自定义的sql添加Limit.
     * @param page 分页
     * @return
     */
    private String buildSqlWithPageLimit(final Page page, final String sql) {
        int pageOffset = 0;
        int pageNum = page.getCurrNum();
        int pageSize = page.getPreSize();
        if (pageNum > 0) {
            pageOffset = (pageNum - 1) * pageSize;
        } else {
            pageOffset = 0;
        }
        page.setOffset(pageOffset);
        page.setPreSize(pageSize);

        StringBuilder sb = new StringBuilder(sql);
        sb.append(" limit ");
        sb.append(pageOffset);
        sb.append(",");
        sb.append(pageSize);
        return sb.toString();
    }

    /**
     * 适应SQL列名.
     *
     * @param c 原列名
     * @return String 调整后列名
     */
    public static String co(String c) {
        if (StringUtils.isBlank(c)) {
            return null;
        }
        return c.trim().toUpperCase();
    }

    /**
     * 适应SQL参数.
     *
     * @param v 参数
     * @return String 调整后参数
     */
    public static String vo(String v) {
        if (StringUtils.isBlank(v)) {
            return null;
        }
        return v.trim().replaceAll("'", "''");
    }

    /**
     * 单表INSERT方法.
     *
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

    /**
     * 批量新增数据方法.
     * [{"a":1}]
     *
     * @param tableName 数据库表名称
     * @param list 插入数据集合
     */
    protected void insertBatch(String tableName, final List<LinkedHashMap<String, Object>> list) {

        if (list.size() <= 0) {
            return;
        }

        LinkedHashMap<String, Object> linkedHashMap = list.get(0);

        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO ");
        sql.append(tableName + " ( ");

        final String[] keyset = (String[]) linkedHashMap.keySet()
                .toArray(new String[linkedHashMap.size()]);

        for (int i = 0; i < linkedHashMap.size(); i++) {
            sql.append(keyset[i] + ",");
        }

        sql.delete(sql.length() - 1, sql.length());

        sql.append(" ) VALUES ( ");
        for (int i = 0; i < linkedHashMap.size(); i++) {
            sql.append("?,");
        }

        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) ");

        this.getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                LinkedHashMap<String, Object> map = list.get(i);
                Object[] valueset = map.values().toArray(new Object[map.size()]);
                int len = map.keySet().size();
                for (int j = 0; j < len; j++) {
                    ps.setObject(j + 1, valueset[j]);
                }
            }

            public int getBatchSize() {
                return list.size();
            }
        });
    }

}