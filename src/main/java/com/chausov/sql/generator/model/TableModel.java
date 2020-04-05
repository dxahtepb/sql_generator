package com.chausov.sql.generator.model;

import java.util.List;

public class TableModel {
    private String name;
    private List<ColumnModel> columns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }
}
