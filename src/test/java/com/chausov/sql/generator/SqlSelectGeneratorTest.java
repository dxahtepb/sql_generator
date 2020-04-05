package com.chausov.sql.generator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SqlSelectGeneratorTest {
    private static final String TEST_DATA_FOLDER = "testData/";
    private SqlSelectGenerator generator;

    @Before
    public void getGenerator() throws Exception {
        var path = Paths.get(TEST_DATA_FOLDER + "sampleDbModel.yaml");
        generator = SqlSelectGeneratorFactory.getGeneratorForYamlModel(path);
    }

    @Test
    public void testFindIntegerInAllDatabasesFromSchema() {
        var expected = List.of(
                "SELECT * FROM sakila.actor" + System.lineSeparator() +
                        "WHERE actor_id = 42" + System.lineSeparator() +
                        "    OR first_name LIKE '%42%'" + System.lineSeparator() +
                        "    OR last_name LIKE '%42%'",
                "SELECT * FROM sakila.address" + System.lineSeparator() +
                        "WHERE address_id = 42" + System.lineSeparator() +
                        "    OR city_id = 42"
        );
        var queries = generator.generateSelects("sakila\\..*", "42", true);
        assertQueriesEqual(expected, queries);
    }

    @Test
    public void testFindIllegalIntegerInAllDatabasesFromSchema() {
        var expected = List.of(
                "SELECT * FROM sakila.actor" + System.lineSeparator() +
                        "WHERE first_name LIKE '%2147483689%'" + System.lineSeparator() +
                        "    OR last_name LIKE '%2147483689%'"
        );
        var queries = generator.generateSelects("sakila\\..*", String.valueOf((long) Integer.MAX_VALUE + 42), true);
        assertQueriesEqual(expected, queries);
    }

    @Test
    public void testFindBooleanInActorTable() {
        var expected = List.of(
                "SELECT * FROM sakila.actor" + System.lineSeparator() +
                        "WHERE first_name LIKE '%true%'" + System.lineSeparator() +
                        "    OR last_name LIKE '%true%'" + System.lineSeparator() +
                        "    OR has_kids = true"
        );
        var queries = generator.generateSelects(".*\\.actor", "true", true);
        assertQueriesEqual(expected, queries);
    }

    @Test
    public void testFindStringInActorTableCaseInsensitive() {
        var expected = List.of(
                "SELECT * FROM sakila.actor" + System.lineSeparator() +
                        "WHERE first_name ILIKE '%str%'" + System.lineSeparator() +
                        "    OR last_name ILIKE '%str%'"
        );
        var queries = generator.generateSelects(".*\\.actor", "str", false);
        assertQueriesEqual(expected, queries);
    }

    @Test
    public void testFindDateInAllTables() {
        var expected = List.of(
                "SELECT * FROM sakila.actor" + System.lineSeparator() +
                        "WHERE first_name LIKE '%2002-02-01%'" + System.lineSeparator() +
                        "    OR last_name LIKE '%2002-02-01%'" + System.lineSeparator() +
                        "    OR last_update = '2002-02-01'"
        );
        var queries = generator.generateSelects(".*", "2002-02-01", true);
        assertQueriesEqual(expected, queries);
    }

    @Test
    public void testFindIllegalDateInAllTables() {
        var expected = List.of(
                "SELECT * FROM sakila.actor" + System.lineSeparator() +
                        "WHERE first_name LIKE '%2042-99-111%'" + System.lineSeparator() +
                        "    OR last_name LIKE '%2042-99-111%'"
        );
        var queries = generator.generateSelects(".*", "2042-99-111", true);
        assertQueriesEqual(expected, queries);
    }

    private void assertQueriesEqual(List<String> expected, List<String> actual) {
        Assert.assertEquals(
                expected.stream().sorted().collect(Collectors.toList()),
                actual.stream().sorted().collect(Collectors.toList())
        );
    }
}
