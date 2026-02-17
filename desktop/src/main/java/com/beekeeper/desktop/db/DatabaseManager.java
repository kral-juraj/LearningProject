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

    private static String dbPath;
    private static boolean initialized = false;

    /**
     * Initialize database connection and create schema if needed.
     * @param databasePath Path to SQLite database file (e.g., "beekeeper.db")
     * @throws SQLException if connection or schema creation fails
     */
    public static void initialize(String databasePath) throws SQLException {
        dbPath = databasePath;

        // Create initial connection to set up database and tables
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            conn.setAutoCommit(true);

            // Enable foreign keys (disabled by default in SQLite)
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }

            createTables(conn);
            migrateTaxationFrames(conn);
            migrateTaxations(conn);
            migrateHives(conn);
            migrateSettings(conn);
            initialized = true;
        }
    }

    /**
     * Get a new database connection.
     * Each call returns a fresh connection that should be closed after use.
     * @return New database connection
     * @throws SQLException if database is not initialized
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized || dbPath == null) {
            throw new SQLException("Database not initialized. Call initialize() first.");
        }
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        // Enable foreign keys for this connection
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    /**
     * Close is no longer needed since each connection is independent.
     * Kept for backwards compatibility.
     */
    public static void close() {
        // No-op: connections are managed by callers using try-with-resources
    }

    /**
     * Create all database tables with proper foreign key relationships.
     * Tables are created in dependency order to satisfy foreign key constraints.
     */
    private static void createTables(Connection connection) throws SQLException {
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

            // 10. Translations table (for i18n support)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS translations (" +
                "id TEXT PRIMARY KEY NOT NULL, " +
                "key TEXT NOT NULL, " +
                "language TEXT NOT NULL, " +
                "value TEXT NOT NULL, " +
                "category TEXT, " +
                "context TEXT, " +
                "createdAt INTEGER, " +
                "updatedAt INTEGER, " +
                "UNIQUE(key, language))"
            );
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_translations_key_language ON translations(key, language)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_translations_category ON translations(category)");

            // 11. HiveActivities table (depends on hives)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS hive_activities (" +
                "id TEXT PRIMARY KEY NOT NULL, " +
                "hiveId TEXT, " +
                "activityType TEXT, " +
                "activityDate INTEGER, " +
                "description TEXT, " +
                "oldValue TEXT, " +
                "newValue TEXT, " +
                "createdAt INTEGER, " +
                "updatedAt INTEGER, " +
                "FOREIGN KEY (hiveId) REFERENCES hives(id) ON DELETE CASCADE)"
            );
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_hive_activities_hiveId ON hive_activities(hiveId)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_hive_activities_activityDate ON hive_activities(activityDate)");
        }
    }

    /**
     * Migrate taxation_frames table to add new columns for stores.
     * Adds cappedStoresDm and uncappedStoresDm columns if they don't exist.
     */
    private static void migrateTaxationFrames(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Try to add new columns - SQLite will ignore if they already exist
            try {
                stmt.execute("ALTER TABLE taxation_frames ADD COLUMN cappedStoresDm INTEGER DEFAULT 0");
                System.out.println("[DatabaseManager] Added cappedStoresDm column to taxation_frames");
            } catch (SQLException e) {
                // Column already exists, ignore
                if (!e.getMessage().contains("duplicate column name")) {
                    throw e;
                }
            }

            try {
                stmt.execute("ALTER TABLE taxation_frames ADD COLUMN uncappedStoresDm INTEGER DEFAULT 0");
                System.out.println("[DatabaseManager] Added uncappedStoresDm column to taxation_frames");
            } catch (SQLException e) {
                // Column already exists, ignore
                if (!e.getMessage().contains("duplicate column name")) {
                    throw e;
                }
            }
        }
    }

    /**
     * Migrate taxations table to add aggregated columns.
     * Adds total columns for pollen, stores, and brood from all frames.
     */
    private static void migrateTaxations(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String[] columns = {
                "totalPollenDm",
                "totalCappedStoresDm",
                "totalUncappedStoresDm",
                "totalCappedBroodDm",
                "totalUncappedBroodDm",
                "totalStarterFrames"
            };

            for (String column : columns) {
                try {
                    stmt.execute("ALTER TABLE taxations ADD COLUMN " + column + " INTEGER DEFAULT 0");
                    System.out.println("[DatabaseManager] Added " + column + " column to taxations");
                } catch (SQLException e) {
                    // Column already exists, ignore
                    if (!e.getMessage().contains("duplicate column name")) {
                        throw e;
                    }
                }
            }
        }
    }

    /**
     * Migrate hives table to add extended hive details.
     * Adds columns for frame type, frame count, insulation, bottom board type, and grids.
     */
    private static void migrateHives(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String[] columns = {
                "frameType TEXT DEFAULT NULL",
                "frameCount INTEGER DEFAULT 0",
                "insulated INTEGER DEFAULT 0",
                "highBottomBoard INTEGER DEFAULT 0",
                "hasQueenExcluder INTEGER DEFAULT 0",
                "hasPropolisTrap INTEGER DEFAULT 0",
                "darkFrames INTEGER DEFAULT 0",
                "lightFrames INTEGER DEFAULT 0",
                "newFrames INTEGER DEFAULT 0",
                "foundationFrames INTEGER DEFAULT 0",
                "emptyFrames INTEGER DEFAULT 0",
                "hasEntranceReducer INTEGER DEFAULT 0",
                "hasPollenTrap INTEGER DEFAULT 0",
                "hasTopInsulation INTEGER DEFAULT 0",
                "hasFoil INTEGER DEFAULT 0",
                "foundationSheets INTEGER DEFAULT 0",
                "aggression TEXT DEFAULT NULL",
                "chalkBrood INTEGER DEFAULT 0",
                "droneCells INTEGER DEFAULT 0",
                "droneLaying INTEGER DEFAULT 0",
                "displayOrder INTEGER DEFAULT 0"
            };

            for (String column : columns) {
                String columnName = column.split(" ")[0];
                try {
                    stmt.execute("ALTER TABLE hives ADD COLUMN " + column);
                    System.out.println("[DatabaseManager] Added " + columnName + " column to hives");
                } catch (SQLException e) {
                    // Column already exists, ignore
                    if (!e.getMessage().contains("duplicate column name")) {
                        throw e;
                    }
                }
            }
        }
    }

    /**
     * Migrate settings table to add language column for i18n support.
     * Adds language column with default value 'sk' (Slovak).
     * Also ensures default date format settings exist.
     */
    private static void migrateSettings(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            try {
                stmt.execute("ALTER TABLE settings ADD COLUMN language TEXT DEFAULT 'sk'");
                System.out.println("[DatabaseManager] Added language column to settings");
            } catch (SQLException e) {
                // Column already exists, ignore
                if (!e.getMessage().contains("duplicate column name")) {
                    throw e;
                }
            }

            // Insert default date format settings if they don't exist
            stmt.execute(
                "INSERT OR IGNORE INTO settings (key, value, updatedAt) " +
                "VALUES ('dateFormat', 'dd.MM.yyyy', " + System.currentTimeMillis() + ")"
            );
            stmt.execute(
                "INSERT OR IGNORE INTO settings (key, value, updatedAt) " +
                "VALUES ('timeFormat', 'HH:mm', " + System.currentTimeMillis() + ")"
            );
            stmt.execute(
                "INSERT OR IGNORE INTO settings (key, value, updatedAt) " +
                "VALUES ('dateTimeFormat', 'dd.MM.yyyy HH:mm', " + System.currentTimeMillis() + ")"
            );
        }
    }

    /**
     * Drop all tables (for testing purposes).
     * WARNING: This will delete all data!
     */
    public static void dropAllTables() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS inspection_recordings");
            stmt.execute("DROP TABLE IF EXISTS calendar_events");
            stmt.execute("DROP TABLE IF EXISTS taxation_frames");
            stmt.execute("DROP TABLE IF EXISTS taxations");
            stmt.execute("DROP TABLE IF EXISTS feedings");
            stmt.execute("DROP TABLE IF EXISTS inspections");
            stmt.execute("DROP TABLE IF EXISTS hives");
            stmt.execute("DROP TABLE IF EXISTS apiaries");
            stmt.execute("DROP TABLE IF EXISTS settings");
            stmt.execute("DROP TABLE IF EXISTS translations");
        }
    }
}
