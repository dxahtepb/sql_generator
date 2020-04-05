package com.chausov.sql.generator;

import java.util.List;

public interface SqlSelectGenerator {
    /**
     * tablePattern - regexp of qualified table name
     * e.g. "sakila\\.actor ", "sakila\\..*", ".*\\.person"
     * query - text to find in database e.g. "Alice", "42", "true"
     * caseSensitive - whether to use LIKE or ILIKE operation for varchar columns
     */
    List<String> generateSelects(String tablePattern, String query, boolean caseSensitive);
}
