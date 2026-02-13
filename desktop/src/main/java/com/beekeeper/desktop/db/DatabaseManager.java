package com.beekeeper.desktop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages SQLite database connection and schema for desktop application.
 * Handles connection lifecycle and table creation with foreign key relationships.
 */
public class DatabaseManager {

    private static Connection connection;
    private static String dbPath;

    /**
     * Initialize database connection and create schema if needed.
     * @param databasePath Path to SQLite database file (e.g., "beekeeper.db")
     * @throws SQLException if connection or schema creation fails
     */
    public static void initialize(String databasePath) throws SQLException {
        dbPath = databasePath;
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        connection.setAutoCommit(true);

        // Enable foreign keys (disabled by default in SQLite)
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        createTables();
    }

    /**
     * Get the active database connection.
     * @return Database connection
     * @throws SQLException if connection is closed
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Database not initialized. Call initialize() first.");
        }
        return connection;
    }

    /**
     * Close the database connection.
     */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create all database tables with proper foreign key relationships.
     * Tables are created in dependency order to satisfy foreign key constraints.
     */
    private static void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            // 1. Apiaries table (no dependencies)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS apiaries (" +
                "id TEXT PRIMARY KEY NOT NULL, " +
                "name TEXT, " +
                "location TEXT, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "createdAt INTEGER, " +
                "updatedAt INTEGER)"
            );

            // 2. Hives table (depends on apiaries)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS hives (" +
                "id TEXT PRIMARY KEY NOT NULL, " +
                "apiaryId TEXT, " +
                "name TEXT, " +
                "type TEXT, " +
                "queenId TEXT, " +
                "queenYear INTEGER, " +
                "queenColor TEXT, " +
                "active INTEGER, " +
                "notes TEXT, " +
                "createdAt INTEGER, " +
                "updatedAt INTEGER, " +
                "FOREIGN KEY (apiaryId) REFERENCES apiaries(id) ON DELETE CASCADE)"
            );
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_hives_apiaryId ON hives(apiaryId)");

            // 3. Inspections table (depends on hives)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS inspections (" +
                "id TEXT PRIMARY KEY NOT NULL, " +
                "hiveId TEXT, " +
                "inspectionDate INTEGER, " +
                "temperature REAL, " +
                "strengthEstimate INTEGER, " +
                "foodStoresKg REAL, " +
                "broodFrames INTEGER, " +
                "cappedBroodDm INTEGER, " +
                "uncappedBroodDm INTEGER, " +
                "pollenFrames INTEGER, " +
                "totalFrames INTEGER, " +
                "queenSeen INTEGER, " +
                "queenNote TEXT, " +
                "varroa INTEGER, " +
                "varroaCount INTEGER, " +
                "aggression INTEGER, " +
                "behavior TEXT, " +
                "notes TEXT, " +
                "recordingId TEXT, " +
                "extractedFromAudio INTEGER, " +
                "createdAt INTEGER, " +
                "updatedAt INTEGER, " +
                "FOREIGN KEY (hiveId) REFERENCES hives(id) ON DELETE CASCADE)"
            );
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_inspections_hiveId ON inspections(hiveId)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_inspections_inspectionDate ON inspections(inspectionDate)");

            // 4. Feedings table (depends on hives)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS feedings (" +
                "id TEXT PRIMARY KEY NOT NULL, " +
                "hiveId TEXT, " +
                "feedingDate INTEGER, " +
                "weightBefore REAL, " +
                "weightAfter REAL, " +
                "feedType TEXT, " +
                "amountKg REAL, " +
                "notes TEXT, " +
                "createdAt INTEGER, " +
                "FOREIGN KEY (hiveId) REFERENCES hives(id) ON DELETE CASCADE)"
            );
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_feedings_hiveId ON feedings(hiveId)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_feedings_feedingDate ON feedings(feedingDate)");

            // 5. Taxations table (depends on hives)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS taxations (" +
                "id TEXT PRIMARY KEY NOT NULL, " +
                "hiveId TEXT, " +
                "taxationDate INTEGER, " +
                "temperature REAL, " +
                "totalFrames INTEGER, " +
                "foodStoresKg REAL, " +
                "notes TEXT, " +
                "createdAt INTEGER, " +
                "updatedAt INTEGER, " +
                "FOREIGN KEY (hiveId) REFERENCES hives(id) ON DELETE CASCADE)"
            );
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_taxations_hiveId ON taxations(hiveId)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_taxations_taxationDate ON taxations(taxationDate)");

            // 6. TaxationFrames table (depends on taxations)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS taxation_frames (" +
                "id TEXT PRIMARY KEY NOT NULL, " +
                "taxationId TEXT, " +
                "position INTEGER, " +
                "cappedBroodDm INTEGER, " +
                "uncappedBroodDm INTEGER, " +
                "pollenDm INTEGER, " +
                "frameType TEXT, " +
                "frameYear INTEGER, " +
                "isStarter INTEGER, " +
                "hasQueen INTEGER, " +
                "hasCage INTEGER, " +
                "hasNucBox INTEGER, " +
                "notes TEXT, " +
                "FOREIGN KEY (taxationId) REFERENCES taxations(id) ON DELETE CASCADE)"
            );
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_taxation_frames_taxationId ON taxation_frames(taxationId)");

            // 7. CalendarEvents table (optional foreign keys to hives and apiaries)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS calendar_events (" +
                "id TEXT PRIMARY KEY NOT NULL, " +
                "title TEXT, " +
                "description TEXT, " +
                "eventDate INTEGER, " +
                "eventType TEXT, " +
                "hiveId TEXT, " +
                "apiaryId TEXT, " +
                "completed INTEGER, " +
                "notes TEXT, " +
                "createdAt INTEGER)"
            );
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_calendar_events_eventDate ON calendar_events(eventDate)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_calendar_events_hiveId ON calendar_events(hiveId)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_calendar_events_apiaryId ON calendar_events(apiaryId)");

            // 8. Settings table (no dependencies)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS settings (" +
                "key TEXT PRIMARY KEY NOT NULL, " +
                "value TEXT, " +
                "updatedAt INTEGER)"
            );

            // 9. InspectionRecordings table (optional foreign key to inspections)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS inspection_recordings (" +
                "id TEXT PRIMARY KEY NOT NULL, " +
                "inspectionId TEXT, " +
                "filePath TEXT, " +
                "fileType TEXT, " +
                "duration INTEGER, " +
                "fileSize INTEGER, " +
                "transcription TEXT, " +
                "extractedJson TEXT, " +
                "processed INTEGER, " +
                "recordedAt INTEGER)"
            );
        }
    }

    /**
     * Drop all tables (for testing purposes).
     * WARNING: This will delete all data!
     */
    public static void dropAllTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS inspection_recordings");
            stmt.execute("DROP TABLE IF EXISTS calendar_events");
            stmt.execute("DROP TABLE IF EXISTS taxation_frames");
            stmt.execute("DROP TABLE IF EXISTS taxations");
            stmt.execute("DROP TABLE IF EXISTS feedings");
            stmt.execute("DROP TABLE IF EXISTS inspections");
            stmt.execute("DROP TABLE IF EXISTS hives");
            stmt.execute("DROP TABLE IF EXISTS apiaries");
            stmt.execute("DROP TABLE IF EXISTS settings");
        }
    }
}
