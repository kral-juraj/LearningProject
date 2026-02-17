package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC implementation for Settings DAO.
 * Provides key-value storage for application settings.
 *
 * Use case: Load/save user preferences like date formats, language, etc.
 */
public class JdbcSettingsDao {

    /**
     * Gets setting value by key.
     *
     * @param key Setting key (e.g., "dateFormat", "language")
     * @return Setting value or null if not found
     */
    public String getSetting(String key) {
        String sql = "SELECT value FROM settings WHERE key = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, key);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading setting '" + key + "': " + e.getMessage());
        }

        return null;
    }

    /**
     * Saves setting value (insert or update).
     *
     * @param key Setting key
     * @param value Setting value
     */
    public void saveSetting(String key, String value) {
        String sql = "INSERT OR REPLACE INTO settings (key, value, updatedAt) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.setLong(3, System.currentTimeMillis());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error saving setting '" + key + "': " + e.getMessage());
        }
    }

    /**
     * Gets date format setting.
     * Returns default "dd.MM.yyyy" if not set.
     *
     * @return Date format pattern
     */
    public String getDateFormat() {
        String value = getSetting("dateFormat");
        return (value != null) ? value : "dd.MM.yyyy";
    }

    /**
     * Gets time format setting.
     * Returns default "HH:mm" if not set.
     *
     * @return Time format pattern
     */
    public String getTimeFormat() {
        String value = getSetting("timeFormat");
        return (value != null) ? value : "HH:mm";
    }

    /**
     * Gets datetime format setting.
     * Returns default "dd.MM.yyyy HH:mm" if not set.
     *
     * @return DateTime format pattern
     */
    public String getDateTimeFormat() {
        String value = getSetting("dateTimeFormat");
        return (value != null) ? value : "dd.MM.yyyy HH:mm";
    }

    /**
     * Saves date format setting.
     *
     * @param format Date format pattern (e.g., "dd.MM.yyyy")
     */
    public void saveDateFormat(String format) {
        saveSetting("dateFormat", format);
    }

    /**
     * Saves time format setting.
     *
     * @param format Time format pattern (e.g., "HH:mm")
     */
    public void saveTimeFormat(String format) {
        saveSetting("timeFormat", format);
    }

    /**
     * Saves datetime format setting.
     *
     * @param format DateTime format pattern (e.g., "dd.MM.yyyy HH:mm")
     */
    public void saveDateTimeFormat(String format) {
        saveSetting("dateTimeFormat", format);
    }
}
