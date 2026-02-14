package com.beekeeper.desktop.integration;

import com.beekeeper.desktop.db.DatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Base class for integration tests.
 * Creates ONE shared database for all tests.
 * After each test, all data is deleted to ensure test isolation.
 */
public abstract class IntegrationTestBase {

    private static String testDatabasePath;

    @BeforeAll
    static void setUpDatabase() throws Exception {
        // Initialize ONE database for all integration tests
        // Get project root directory (user.dir points to project root when gradle runs)
        String projectRoot = System.getProperty("user.dir");
        java.io.File testDbDir = new java.io.File(projectRoot, "test-db");

        if (!testDbDir.exists()) {
            testDbDir.mkdirs();
        }

        // Create single test database file
        java.io.File dbFile = new java.io.File(testDbDir, "integration-test.db");
        testDatabasePath = dbFile.getAbsolutePath();

        System.out.println("✓ Creating SHARED test database: " + testDatabasePath);
        DatabaseManager.initialize(testDatabasePath);
        System.out.println("✓ Integration test database initialized (shared by all tests)");
    }

    @AfterEach
    void cleanUpAfterTest() throws Exception {
        // Delete all data from all tables after each test to ensure isolation
        System.out.println("✓ Cleaning up test data after test...");

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // Disable foreign key constraints temporarily
            stmt.execute("PRAGMA foreign_keys = OFF");

            // Delete data from all tables (in reverse dependency order)
            stmt.execute("DELETE FROM taxation_frames");
            stmt.execute("DELETE FROM taxations");
            stmt.execute("DELETE FROM inspection_recordings");
            stmt.execute("DELETE FROM inspections");
            stmt.execute("DELETE FROM feedings");
            stmt.execute("DELETE FROM calendar_events");
            stmt.execute("DELETE FROM hives");
            stmt.execute("DELETE FROM apiaries");
            stmt.execute("DELETE FROM settings");

            // Re-enable foreign key constraints
            stmt.execute("PRAGMA foreign_keys = ON");

            System.out.println("✓ Test data cleaned up");
        }
    }

    /**
     * Create a test apiary ID for use in tests.
     */
    protected String createTestApiaryId() {
        return "test-apiary-" + System.currentTimeMillis();
    }

    /**
     * Create a test hive ID for use in tests.
     */
    protected String createTestHiveId() {
        return "test-hive-" + System.currentTimeMillis();
    }

    /**
     * Create a test taxation ID for use in tests.
     */
    protected String createTestTaxationId() {
        return "test-taxation-" + System.currentTimeMillis();
    }
}
