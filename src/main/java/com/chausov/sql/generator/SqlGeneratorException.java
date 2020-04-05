package com.chausov.sql.generator;

public class SqlGeneratorException extends RuntimeException {
    public SqlGeneratorException(String message) {
        super(message);
    }

    public SqlGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
