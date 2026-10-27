package com.negocio.analitica.repository.sqlite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class SqliteDatabase {
    private final String jdbcUrl;

    public SqliteDatabase(String jdbcUrl) {
        if (jdbcUrl == null || !jdbcUrl.startsWith("jdbc:sqlite:")) {
            throw new IllegalArgumentException("A valid jdbc:sqlite URL is required.");
        }
        this.jdbcUrl = jdbcUrl;
        prepareParentDirectory(jdbcUrl);
        initializeSchema();
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcUrl);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    private void initializeSchema() {
        String categories = """
                CREATE TABLE IF NOT EXISTS categories (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    description TEXT
                )
                """;
        String products = """
                CREATE TABLE IF NOT EXISTS products (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    price REAL NOT NULL CHECK (price > 0),
                    stock INTEGER NOT NULL CHECK (stock >= 0),
                    minimum_stock INTEGER NOT NULL CHECK (minimum_stock >= 0),
                    category_id INTEGER NOT NULL,
                    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
                )
                """;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(categories);
            statement.execute(products);
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not initialize SQLite schema.", exception);
        }
    }

    private static void prepareParentDirectory(String url) {
        String path = url.substring("jdbc:sqlite:".length());
        if (path.isBlank() || path.equals(":memory:") || path.startsWith("file:")) return;
        try {
            Path parent = Path.of(path).toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create database directory.", exception);
        }
    }
}
