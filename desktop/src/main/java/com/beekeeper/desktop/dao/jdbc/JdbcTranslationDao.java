package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.i18n.TranslationLoader;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC implementation of TranslationLoader.
 * Loads all translations for given language from SQLite database.
 *
 * Use case: Desktop application loads translations at startup and
 * saves/loads user's language preference.
 */
public class JdbcTranslationDao implements TranslationLoader {

    /**
     * Load all translations for specified language from database.
     *
     * @param language Language code (e.g., "sk", "en")
     * @return Map of translation keys to values
     */
    @Override
    public Map<String, String> loadTranslations(String language) {
        Map<String, String> translations = new HashMap<>();
        String sql = "SELECT key, value FROM translations WHERE language = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, language);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    translations.put(rs.getString("key"), rs.getString("value"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading translations: " + e.getMessage());
            e.printStackTrace();
        }

        return translations;
    }

    /**
     * Get user's saved language preference from settings table.
     * Returns default language "sk" if not found.
     *
     * Use case: Called during application startup to restore user's language choice.
     *
     * @return Language code (e.g., "sk", "en")
     */
    public String getSavedLanguage() {
        String sql = "SELECT language FROM settings LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lang = rs.getString("language");
                return (lang != null && !lang.isEmpty()) ? lang : "sk";
            }
        } catch (SQLException e) {
            System.err.println("Error loading language setting: " + e.getMessage());
        }
        return "sk"; // Default to Slovak
    }

    /**
     * Save user's language preference to settings table.
     *
     * Use case: Called when user changes language in UI.
     *
     * @param language Language code to save (e.g., "sk", "en")
     */
    public void saveLanguage(String language) {
        // First, check if settings row exists
        String checkSql = "SELECT COUNT(*) FROM settings";
        String updateSql = "UPDATE settings SET language = ?";
        String insertSql = "INSERT INTO settings (key, language, updatedAt) VALUES ('default', ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            // Check if settings table has any rows
            boolean hasRows = false;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(checkSql)) {
                if (rs.next() && rs.getInt(1) > 0) {
                    hasRows = true;
                }
            }

            if (hasRows) {
                // Update existing row
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setString(1, language);
                    stmt.executeUpdate();
                }
            } else {
                // Insert new row
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, language);
                    stmt.setLong(2, System.currentTimeMillis());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving language: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
