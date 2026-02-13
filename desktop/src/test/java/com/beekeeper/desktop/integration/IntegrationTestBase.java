package com.beekeeper.desktop.integration;

import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for integration tests.
 * Sets up test database and common fixtures.
 */
public abstract class IntegrationTestBase {

    @BeforeEach
    void setUp() {
        // Initialize test database
        // This would need to be implemented with actual database setup
        System.out.println("Setting up integration test environment");
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
