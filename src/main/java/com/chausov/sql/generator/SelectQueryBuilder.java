package com.chausov.sql.generator;

import com.chausov.sql.generator.model.ColumnModel;
import com.chausov.sql.generator.model.TableModel;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

class SelectQueryBuilder {
    private final boolean caseSensitive;
    private final String query;

    public SelectQueryBuilder(boolean caseSensitive, String query) {
        this.caseSensitive = caseSensitive;
        this.query = query;
    }

    public String buildQueryForTable(String tableFqn, TableModel tableModel) {
        var queryBuilder = new MyVisitor(caseSensitive, query);
        return queryBuilder.visitTable(tableFqn, tableModel);
    }

    private static class MyVisitor {
        private static final String TRUE = "true";
        private static final String FALSE = "false";
        private static final String LIKE = "LIKE";
        private static final String INSENSITIVE_LIKE = "ILIKE";
        private static final DateTimeFormatter DATE_TIME_FORMATTER
                = DateTimeFormatter.ofPattern("yyyy-MM-dd").withResolverStyle(ResolverStyle.STRICT);

        private final boolean caseSensitive;
        private final String query;
        private final List<String> whereClauses = new ArrayList<>();

        private MyVisitor(boolean caseSensitive, String query) {
            this.caseSensitive = caseSensitive;
            this.query = query;
        }

        private String visitTable(String tableFqn, TableModel tableModel) {
            for (var column : tableModel.getColumns()) {
                visitColumn(column);
            }
            if (whereClauses.isEmpty()) {
                return null;
            }
            return build(tableFqn);
        }

        private String build(String tableFqn) {
            var queryBuilder = new StringBuilder()
                    .append("SELECT * FROM ")
                    .append(tableFqn);
            queryBuilder.append(System.lineSeparator()).append("WHERE ");
            var joiner = new StringJoiner(System.lineSeparator() + "    OR ", "", "");
            for (var clause : whereClauses) {
                joiner.add(clause);
            }
            return queryBuilder.append(joiner).toString();
        }

        private void visitColumn(ColumnModel columnModel) {
            var columnType = columnModel.getType();
            if (columnType.equals("integer")) {
                visitInteger(columnModel);
            } else if (columnType.equals("boolean")) {
                visitBoolean(columnModel);
            } else if (columnType.equals("date")) {
                visitDate(columnModel);
            } else if (columnType.startsWith("varchar")) {
                visitVarchar(columnModel);
            } else {
                throw new SqlGeneratorException("Unknown column type: " + columnModel.getType());
            }
        }

        private void visitBoolean(ColumnModel columnModel) {
            if (TRUE.equals(query) || FALSE.equals(query)) {
                whereClauses.add(String.format("%s = %s", columnModel.getName(), query));
            }
        }

        private void visitVarchar(ColumnModel columnModel) {
            whereClauses.add(String.format("%s %s '%%%s%%'", columnModel.getName(), getLikeOperation(), query));
        }

        private String getLikeOperation() {
            return caseSensitive ? LIKE : INSENSITIVE_LIKE;
        }

        private void visitInteger(ColumnModel columnModel) {
            if (validateInteger(query)) {
                whereClauses.add(String.format("%s = %s", columnModel.getName(), query));
            }
        }

        private static boolean validateInteger(String integerText) {
            try {
                Integer.parseInt(integerText);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }

        private void visitDate(ColumnModel columnModel) {
            if (validateDate(query)) {
                whereClauses.add(String.format("%s = '%s'", columnModel.getName(), query));
            }
        }

        private static boolean validateDate(String dateText) {
            try {
                DATE_TIME_FORMATTER.parse(dateText);
            } catch (DateTimeParseException e) {
                return false;
            }
            return true;
        }
    }
}
