package com.company.person.bdd.db;

import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

public final class TestDatabaseInitializer {

    private TestDatabaseInitializer() {
    }

    public static void runScript(DataSource dataSource, String classpathResource) throws Exception {
        String sql = readClasspathResource(classpathResource);

        try (Connection connection = dataSource.getConnection()) {
            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isBlank()) {
                    connection.createStatement().execute(trimmed);
                }
            }
        }
    }

    private static String readClasspathResource(String resource) throws Exception {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resource)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Classpath resource not found: " + resource);
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
