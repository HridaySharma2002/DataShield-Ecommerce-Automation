package com.datashield.automation.db;

import com.datashield.automation.config.ConfigManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DBConnectionManager {

    private static Connection connection;

    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String driver = ConfigManager.get("db.driver");
                String url = ConfigManager.get("db.url");
                String user = ConfigManager.get("db.user");
                String password = ConfigManager.get("db.password");

                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
                initializeDatabase();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
        return connection;
    }

    private static void initializeDatabase() {
        try {
            executeSqlScript("schema.sql");
            executeSqlScript("data.sql");
            System.out.println("Database schema and seed data initialized successfully.");
        } catch (Exception e) {
            System.err.println("Warning during database initialization: " + e.getMessage());
        }
    }

    private static void executeSqlScript(String scriptName) {
        try (InputStream is = DBConnectionManager.class.getClassLoader().getResourceAsStream(scriptName)) {
            if (is == null) return;
            String sql = new BufferedReader(new InputStreamReader(is))
                    .lines().collect(Collectors.joining("\n"));

            try (Statement stmt = connection.createStatement()) {
                for (String sqlStatement : sql.split(";")) {
                    if (!sqlStatement.trim().isEmpty()) {
                        stmt.execute(sqlStatement.trim());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error executing script " + scriptName + ": " + e.getMessage());
        }
    }

    public static synchronized void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
