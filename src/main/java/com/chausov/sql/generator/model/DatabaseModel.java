package com.chausov.sql.generator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseModel {
    private List<SchemaModel> schemas;

    public List<SchemaModel> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<SchemaModel> schemas) {
        this.schemas = schemas;
    }

    public Map<String, TableModel> getAllTableModels() {
        var tableModels = new HashMap<String, TableModel>();
        for (var schema : schemas) {
            var schemaName = schema.getName();
            for (var table : schema.getTables()) {
                var tableName = table.getName();
                var tableFqn = String.format("%s.%s", schemaName, tableName);
                tableModels.put(tableFqn, table);
            }
        }
        return tableModels;
    }
}
