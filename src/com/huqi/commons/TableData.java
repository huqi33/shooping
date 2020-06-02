package com.huqi.commons;

import java.util.List;

public class TableData<T> {

    private List<T> rows;
    private Integer total;

    public TableData(List<T> rows, Integer total) {
        this.rows = rows;
        this.total = total;
    }

    public TableData() {
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
