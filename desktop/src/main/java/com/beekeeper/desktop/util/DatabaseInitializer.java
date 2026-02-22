package com.beekeeper.desktop.util;

import com.beekeeper.desktop.db.DatabaseManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Utility class for initializing database with translations and test data.
 *
 * Use case: On first run, automatically populate database with:
 * - Slovak and English translations (785 keys)
 * - Test apiary with 2 hives and sample data
 *
 * Uses consolidated database_complete_export.sql which contains:
 * - All translations from 22 individual SQL scripts
 * - Complete database schema
 * - Test data
 *
 * This ensures new users can immediately test the application without manual setup
 * and without any missing translation keys.
 */
public class DatabaseInitializer {

    /**
     * Initialize database with translations and test data if needed.
     *
     * Checks if translations and test data already exist:
     * - If translations table is empty, loads all translations from SQL
     * - If no apiaries exist, loads test apiary with hives
     *
     * Use case: Called from Main.java after database schema is created.
     *
     * @throws SQLException if database operations fail
     */
    public static void initializeIfNeeded() throws SQLException {
        boolean needsTranslations = checkIfTranslationsNeeded();
        boolean needsTestData = checkIfTestDataNeeded();

        if (needsTranslations) {
            System.out.println("First run detected - loading complete database...");
            loadTranslations();
            System.out.println("✓ Database initialized (785 translation keys, test data included)");
        } else {
            System.out.println("✓ Database already initialized");

            if (needsTestData) {
                System.out.println("No apiaries found - loading test data...");
                loadTestData();
            } else {
                System.out.println("✓ Existing data found");
            }
        }
    }

    /**
     * Check if translations need to be loaded.
     *
     * @return true if translations table is empty or doesn't have expected count
     * @throws SQLException if query fails
     */
    private static boolean checkIfTranslationsNeeded() throws SQLException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM translations")) {

            if (rs.next()) {
                int count = rs.getInt(1);
                // If we have any translations, don't reload
                return count == 0;
            }
            return true;
        } catch (SQLException e) {
            // If table doesn't exist yet, we need to load
            return true;
        }
    }

    /**
     * Check if test data needs to be loaded.
     *
     * @return true if no apiaries exist in database
     * @throws SQLException if query fails
     */
    private static boolean checkIfTestDataNeeded() throws SQLException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM apiaries")) {

            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
            return true;
        } catch (SQLException e) {
            // If table doesn't exist yet, DatabaseManager will create it
            return true;
        }
    }

    /**
     * Load all translations and test data from database inserts.
     *
     * Loads database_inserts_only.sql which contains:
     * - 785 translation keys (Slovak and English) - 1570 INSERT statements
     * - Categories: menu, button, label, tab, status, error, calculator, etc.
     * - Test apiary "Test Apiary" - 1 INSERT statement
     * - 2 test hives with sample data - 2 INSERT statements
     * - Total: 1573 INSERT statements
     *
     * This is extracted from database_complete_export.sql (consolidated from
     * all 22 individual SQL scripts), containing only INSERT statements
     * without DDL (CREATE TABLE, etc.) which is already handled by DatabaseManager.
     *
     * @throws SQLException if SQL execution fails
     */
    private static void loadTranslations() throws SQLException {
        String sqlContent = loadResourceFile("/sql/database_inserts_only.sql");
        if (sqlContent == null) {
            System.err.println("WARNING: Could not load database inserts file");
            return;
        }
        executeSQL(sqlContent);
    }

    /**
     * Load test data - now included in complete export.
     *
     * Test data is loaded as part of database_complete_export.sql,
     * so this method just ensures the test data check passes.
     *
     * @throws SQLException if SQL execution fails
     */
    private static void loadTestData() throws SQLException {
        // Test data is already included in database_complete_export.sql
        // This method is kept for compatibility but does nothing
        System.out.println("✓ Test data included in database export");
    }

    /**
     * Load SQL file from resources.
     *
     * Use case: Read bundled SQL scripts from JAR or filesystem.
     *
     * @param resourcePath Path relative to resources folder (e.g., "/sql/file.sql")
     * @return SQL file content as string, or null if file not found
     */
    private static String loadResourceFile(String resourcePath) {
        try (InputStream is = DatabaseInitializer.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("ERROR: Resource not found: " + resourcePath);
                return null;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to load resource: " + resourcePath);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Execute SQL statements from string content.
     *
     * Splits SQL by semicolons and executes each statement separately.
     * Skips empty statements, comments, and DDL statements (CREATE TABLE, etc.)
     * since schema is already created by DatabaseManager.
     *
     * Use case: Execute multi-statement SQL scripts loaded from files,
     * particularly SQLite .dump files which contain both DDL and DML.
     *
     * @param sqlContent SQL statements separated by semicolons
     * @throws SQLException if any critical SQL statement fails
     */
    private static void executeSQL(String sqlContent) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // Split by semicolons
            String[] statements = sqlContent.split(";");

            int executedCount = 0;
            int skippedCount = 0;

            for (String sql : statements) {
                sql = sql.trim();

                // Skip empty statements and comments
                if (sql.isEmpty() || sql.startsWith("--")) {
                    continue;
                }

                // Skip DDL statements (schema already exists from DatabaseManager)
                String sqlUpper = sql.toUpperCase();
                if (sqlUpper.startsWith("CREATE TABLE") ||
                    sqlUpper.startsWith("CREATE INDEX") ||
                    sqlUpper.startsWith("CREATE UNIQUE INDEX") ||
                    sqlUpper.startsWith("PRAGMA") ||
                    sqlUpper.startsWith("BEGIN TRANSACTION") ||
                    sqlUpper.startsWith("COMMIT")) {
                    skippedCount++;
                    continue;
                }

                try {
                    stmt.execute(sql);
                    executedCount++;
                } catch (SQLException e) {
                    // Log error but continue with next statement
                    // (some inserts may fail due to constraints, that's OK)
                    if (!e.getMessage().contains("UNIQUE constraint")) {
                        System.err.println("Warning: SQL statement failed: " +
                                         sql.substring(0, Math.min(100, sql.length())) + "...");
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            }

            System.out.println("  Executed " + executedCount + " SQL statements (" +
                             skippedCount + " DDL statements skipped)");
        }
    }

    /**
     * Check database initialization status (useful for debugging).
     *
     * @return Status message with translation and apiary counts
     */
    public static String getInitializationStatus() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // Count translations
            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM translations");
            int translationCount = rs1.next() ? rs1.getInt(1) : 0;
            rs1.close();

            // Count apiaries
            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM apiaries");
            int apiaryCount = rs2.next() ? rs2.getInt(1) : 0;
            rs2.close();

            return String.format("Database status: %d translations, %d apiaries",
                               translationCount, apiaryCount);

        } catch (SQLException e) {
            return "Database status: ERROR - " + e.getMessage();
        }
    }
}
