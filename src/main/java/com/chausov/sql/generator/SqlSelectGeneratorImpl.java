package com.chausov.sql.generator;

import com.chausov.sql.generator.model.DatabaseModel;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class SqlSelectGeneratorImpl implements SqlSelectGenerator {
    private final DatabaseModel databaseModel;

    SqlSelectGeneratorImpl(DatabaseModel databaseModel) {
        this.databaseModel = databaseModel;
    }

    /**
     * tablePattern - regexp of qualified table name
     * e.g. "sakila\\.actor ", "sakila\\..*", ".*\\.person"
     * query - text to find in database e.g. "Alice", "42", "true"
     * caseSensitive - whether to use LIKE or ILIKE operation for varchar columns
     */
    @Override
    public List<String> generateSelects(String tablePattern, String query, boolean caseSensitive) {
        Objects.requireNonNull(tablePattern);
        Objects.requireNonNull(query);
        var pattern = Pattern.compile(tablePattern);
        var allTableModels = databaseModel.getAllTableModels();
        var queryBuilder = new SelectQueryBuilder(caseSensitive, query);
        return allTableModels.entrySet().stream()
                .filter(entry -> pattern.matcher(entry.getKey()).matches())
                .map(entry -> queryBuilder.buildQueryForTable(entry.getKey(), entry.getValue()))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }
}
