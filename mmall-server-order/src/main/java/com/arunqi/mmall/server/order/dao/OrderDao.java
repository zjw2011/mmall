package com.arunqi.mmall.server.order.dao;

import com.alibaba.fastjson.JSONObject;
import com.arunqi.mmall.server.base.dao.BaseJdbcDao;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 * desc.
 *
 * @author jiawei zhang
 * @datetime 2018/6/25 上午10:49
 */
@Repository
public class OrderDao extends BaseJdbcDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public OrderDao() {
    }

    /**
     * 查询.
     * @return
     */
    public void queryById() {
        final String sql = "select * from id";
        List<JSONObject> list = queryForJsonList(sql);
        System.out.println("list:" + list);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
