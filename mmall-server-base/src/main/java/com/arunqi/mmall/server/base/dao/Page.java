package com.arunqi.mmall.server.base.dao;

import com.alibaba.fastjson.JSONObject;
import java.util.List;

public class Page {

    //分页数据
    private List<JSONObject> data;

    //总记录数
    private long count;

    //分页大小
    private int preSize = 10;

    //偏移量
    private int offset = 0;

    //当前页
    private int currNum = 0;

    public List<JSONObject> getData() {
        return data;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPreSize() {
        return preSize;
    }

    public void setPreSize(int preSize) {
        this.preSize = preSize;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCurrNum() {
        return currNum;
    }

    public void setCurrNum(int currNum) {
        this.currNum = currNum;
    }
}