package com.chausov.sql.generator.model;

import java.util.List;

public class SchemaModel {
    private String name;
    private List<TableModel> tables;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TableModel> getTables() {
        return tables;
    }

    public void setTables(List<TableModel> tables) {
        this.tables = tables;
    }
}
