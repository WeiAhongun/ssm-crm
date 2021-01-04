package com.sm.cn.common.http;

import java.util.List;

public class PageVo {

    private long total;
    private List employeeList;

    public PageVo(long total, List employeeList) {
        this.total = total;
        this.employeeList = employeeList;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getData() {
        return employeeList;
    }

    public void setData(List employeeList) {
        this.employeeList = employeeList;
    }


}

