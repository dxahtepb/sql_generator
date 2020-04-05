package com.chausov.sql.generator.model;

import com.chausov.sql.generator.SqlGeneratorException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class DatabaseModelFactory {
    public static DatabaseModel createFromYaml(Path databaseModelYaml) throws IOException {
        try (var fileReader = new FileReader(databaseModelYaml.toFile(), StandardCharsets.UTF_8)) {
            var yaml = new Yaml(new Constructor(DatabaseModel.class));
            var databaseModel = yaml.load(fileReader);
            return (DatabaseModel) databaseModel;
        } catch (YAMLException e) {
            throw new SqlGeneratorException("Bad database model!", e);
        }
    }
}
