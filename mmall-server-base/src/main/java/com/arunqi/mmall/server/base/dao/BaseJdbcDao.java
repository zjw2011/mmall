package com.arunqi.mmall.server.base.dao;

import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.common.dao.JsonRowMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * id 在数据库里面是long.
 * @author jiawei zhang
 * @since
 */
public abstract class BaseJdbcDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseJdbcDao.class);

    private static final ExecutorService COUNT_EXCUTOR = Executors.newCachedThreadPool();

    //JSON数据行映射器
    private static final JsonRowMapper JSON_ROW_MAPPER = new JsonRowMapper();

    //启动时间
    private Date startTime;

    /**
     *  是否包含创建时间.
     */
    private boolean includCreatetime = true;
    /**
     *  是否包含更新时间.
     */
    private boolean includUpdatetime = true;

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

    private String buildUpdateCondition(final JSONObject conditions,
                                        final List<Object> queryConditions) {
        return buildCondition(conditions, queryConditions, false);
    }

    private String buildCondition(final JSONObject conditions, final List<Object> queryConditions) {
        return buildCondition(conditions, queryConditions, true);
    }

    private String buildCondition(final JSONObject conditions,
                                  final List<Object> queryConditions, final boolean query) {
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sbOrderBy = new StringBuilder();
        for (Map.Entry<String, Object> entry: conditions.entrySet()) {
            //如果值为空，则不过滤
            if (ObjectUtils.isEmpty(entry.getValue())) {
                continue;
            }
            final String key = entry.getKey();
            if (query && key.charAt(0) == '+') {
                sbOrderBy.append(" order by ").append(key.substring(1))
                        .append(" ASC,");
                continue;
            }

            if (query && key.charAt(0) == '-') {
                sbOrderBy.append(" order by ").append(key.substring(1))
                        .append(" DESC,");
                continue;
            }

            final int posGt = key.indexOf(">");
            final int posLt = key.indexOf("<");
            if (posGt > 0 || posLt > 0) {
                queryConditions.add(entry.getValue());
                sb.append(" and ").append(key).append("?");
                continue;
            }
            queryConditions.add(entry.getValue());
            sb.append(" and ").append(key).append("=?");
        }
        if (query && sbOrderBy.length() > 0) {
            sbOrderBy.deleteCharAt(sbOrderBy.length() - 1);
            sb.append(sbOrderBy.toString());
        }
        return sb.toString();
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
        LOGGER.debug("args:" + ObjectUtils.nullSafeToString(args));
        final List<JSONObject> result = this.getJdbcTemplate().query(sql, JSON_ROW_MAPPER, args);
        LOGGER.debug("result:", result);
        return result;
    }

    /**
     * 按照条件查询.
     * @param conditions 条件
     * @param sqlQuery sql查询语句
     * @return
     */
    public List<JSONObject> queryForJsonList(final JSONObject conditions,
                                             final String sqlQuery) {
        Assert.notNull(conditions, "条件不能为空");
        final List<Object> queryConditions = new ArrayList<>();
        final StringBuilder sb = new StringBuilder(sqlQuery);
        sb.append(buildCondition(conditions, queryConditions));
        final List<JSONObject> result = queryForJsonList(sb.toString(), queryConditions.toArray());
        return result;
    }

    /**
     * 查询文本.
     *
     * @param sql SQL语句
     * @param args 参数
     * @return String 文本
     */
    public String queryForString(String sql, Object... args) {
        LOGGER.debug("args:" + ObjectUtils.nullSafeToString(args));
        List<String> dataList = this.getJdbcTemplate().queryForList(sql, args, String.class);
        if (dataList == null || dataList.size() < 1) {
            return null;
        }
        final String result = dataList.get(0);
        LOGGER.debug("result:", result);
        return result;
    }

    /**
     * 查询文本.
     *
     * @param sql SQL语句
     * @param args 参数
     * @return String 文本
     */
    public List<String> queryForStringList(String sql, Object... args) {
        LOGGER.debug("args:", ObjectUtils.nullSafeToString(args));
        final List<String> result = this.getJdbcTemplate().queryForList(sql, args, String.class);
        LOGGER.debug("result:", result);
        return result;
    }

    /**
     * 按照条件查询.
     * @param conditions 条件
     * @param sqlQuery sql查询语句
     * @return
     */
    public List<String> queryForStringList(final JSONObject conditions,
                                           final String sqlQuery) {
        Assert.notNull(conditions, "条件不能为空");
        final List<Object> queryConditions = new ArrayList<>();
        final StringBuilder sb = new StringBuilder(sqlQuery);
        sb.append(buildCondition(conditions, queryConditions));
        return queryForStringList(sb.toString(), queryConditions.toArray());
    }

    /**
     * 查询数字列表.
     *
     * @param sql SQL语句
     * @param args 参数
     * @return String 文本
     */
    public List<Long> queryForLongList(String sql, Object... args) {
        LOGGER.debug("args:" + ObjectUtils.nullSafeToString(args));
        List<Long> result = this.getJdbcTemplate().queryForList(sql, args, Long.class);
        LOGGER.debug("result:", result);
        return result;
    }

    /**
     * 按照条件查询.
     * @param conditions 条件
     * @param sqlQuery sql查询语句
     * @return
     */
    public List<Long> queryForLongList(final JSONObject conditions,
                                           final String sqlQuery) {
        Assert.notNull(conditions, "条件不能为空");
        final List<Object> queryConditions = new ArrayList<>();
        final StringBuilder sb = new StringBuilder(sqlQuery);
        sb.append(buildCondition(conditions, queryConditions));
        return queryForLongList(sb.toString(), queryConditions.toArray());
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
     * 通过ID查询.
     *
     * @return
     */
    public JSONObject queryById(final String sqlQueryById,
                                final Long id) {
        Assert.isTrue(id != null, "id不能为空");
        return queryForJsonObject(sqlQueryById, id);
    }


    /**
     * 查询文本.
     *
     * @param sql SQL语句
     * @param args 参数
     * @return String 文本
     */
    public Page queryForPage(String sql, int pageNo, int pageSize, Object... args) {

        final Page page = new Page();
        page.setCurrNum(pageNo);
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
     * 分页查询.
     *
     * @return
     */
    public Page queryForPage(final JSONObject conditions,
                            final int pageNo,
                            final int pageSize,
                            final String sqlQuery) {
        Assert.notNull(conditions, "条件不能为空");
        Assert.isTrue(pageNo > 0, "参数[页码]不正确");
        Assert.isTrue(pageSize > 0, "参数[页大小]不正确");

        final List<Object> queryConditions = new ArrayList<>();
        final StringBuilder sb = new StringBuilder(sqlQuery);
        sb.append(buildCondition(conditions, queryConditions));
        return queryForPage(sb.toString(), pageNo - 1, pageSize, queryConditions.toArray());
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
                LOGGER.debug("count:", cnt);
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
     * 单表INSERT方法.
     *
     * @param tableName 表名
     * @param data JSONObject对象
     */
    protected int insert(String tableName, JSONObject data) {
        Assert.isTrue((data != null && data.size() > 0), "数据不能为空");

        LOGGER.debug("data:" + data.toJSONString());

        StringBuilder sql = new StringBuilder();
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
        if (includCreatetime) {
            sql.append(",createtime");
        }
        if (includUpdatetime) {
            sql.append(",updatetime");
        }
        sql.append(") VALUES ( ");
        for (int i = 0; i < set.size(); i++) {
            sql.append("?,");
        }

        sql.delete(sql.length() - 1, sql.length());
        if (includCreatetime) {
            sql.append(",CURRENT_TIMESTAMP()");
        }
        if (includUpdatetime) {
            sql.append(",CURRENT_TIMESTAMP()");
        }

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

        Assert.isTrue((list != null && list.size() > 0), "数据不能为空");

        LinkedHashMap<String, Object> linkedHashMap = list.get(0);

        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO ");
        sql.append(tableName + " ( ");

        final String[] keyset = (String[]) linkedHashMap.keySet()
                .toArray(new String[linkedHashMap.size()]);

        for (int i = 0; i < linkedHashMap.size(); i++) {
            sql.append(keyset[i] + ",");
        }

        sql.delete(sql.length() - 1, sql.length());
        if (includCreatetime) {
            sql.append(",createtime");
        }
        if (includUpdatetime) {
            sql.append(",updatetime");
        }

        sql.append(" ) VALUES ( ");
        for (int i = 0; i < linkedHashMap.size(); i++) {
            sql.append("?,");
        }

        sql.delete(sql.length() - 1, sql.length());
        if (includCreatetime) {
            sql.append(",CURRENT_TIMESTAMP()");
        }
        if (includUpdatetime) {
            sql.append(",CURRENT_TIMESTAMP()");
        }
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

    /**
     * desc.
     * @param tableName 表名
     * @param data 数据
     * @param conditions 条件
     * @return
     */
    protected int update(final String tableName,
                         final JSONObject data,
                         final JSONObject conditions) {
        Assert.hasText(tableName, "表名不能为空");
        Assert.isTrue((data != null && data.size() > 0), "数据不能为空");

        LOGGER.debug("data:" + data.toJSONString());
        LOGGER.debug("conditions:" + conditions.toJSONString());

        final List<Object> objectList = new ArrayList<>();
        final StringBuilder sb = new StringBuilder("update ");
        sb.append(tableName);
        sb.append(" set ");
        for (Map.Entry<String, Object> entry: data.entrySet()) {
            //如果值为空，则不过滤
            if (ObjectUtils.isEmpty(entry.getValue())) {
                continue;
            }
            sb.append(entry.getKey()).append("=?,");
            objectList.add(entry.getValue());
        }
        if (includUpdatetime) {
            sb.append("updatetime=CURRENT_TIMESTAMP()");
        } else {
            sb.deleteCharAt(sb.length() - 1);
        }

        if (conditions == null) {
            return this.getJdbcTemplate().update(sb.toString(), objectList.toArray());
        }

        sb.append(" where 1=1 ");
        sb.append(buildUpdateCondition(conditions, objectList));
        /*
        for (Map.Entry<String, Object> entry: conditions.entrySet()) {
            //如果值为空，则不过滤
            if (ObjectUtils.isEmpty(entry.getValue())) {
                continue;
            }
            objectList.add(entry.getValue());
            sb.append(" and ").append(entry.getKey()).append("=?");
        }*/

        return this.getJdbcTemplate().update(sb.toString(), objectList.toArray());
    }

    public boolean isIncludCreatetime() {
        return includCreatetime;
    }

    public void setIncludCreatetime(boolean includCreatetime) {
        this.includCreatetime = includCreatetime;
    }

    public boolean isIncludUpdatetime() {
        return includUpdatetime;
    }

    public void setIncludUpdatetime(boolean includUpdatetime) {
        this.includUpdatetime = includUpdatetime;
    }
}