package com.chausov.sql.generator.model;

import java.util.HashMap;
import java.util.Map;

public class DatabaseModelUtils {
    public static Map<String, TableModel> getAllTableModels(DatabaseModel databaseModel) {
        var tableModels = new HashMap<String, TableModel>();
        var schemas = databaseModel.getSchemas();
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
