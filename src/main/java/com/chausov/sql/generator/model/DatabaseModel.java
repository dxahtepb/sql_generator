package com.chausov.sql.generator.model;

import java.util.List;

public class DatabaseModel {
    private List<SchemaModel> schemas;

    public List<SchemaModel> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<SchemaModel> schemas) {
        this.schemas = schemas;
    }
}
