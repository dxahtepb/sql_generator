package com.chausov.sql.generator;

import com.chausov.sql.generator.model.DatabaseModelFactory;

import java.io.IOException;
import java.nio.file.Path;

public class SqlSelectGeneratorFactory {
    public static SqlSelectGenerator getGeneratorForYamlModel(Path databaseModelYaml) throws IOException {
        return new SqlSelectGeneratorImpl(DatabaseModelFactory.createFromYaml(databaseModelYaml));
    }
}
