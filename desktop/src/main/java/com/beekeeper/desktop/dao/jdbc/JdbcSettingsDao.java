package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.SettingsDao;
import com.beekeeper.shared.entity.Settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * JDBC implementation of SettingsDao for desktop application.
 */
public class JdbcSettingsDao implements SettingsDao {

    @Override
    public Completable insert(Settings settings) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO settings (key, value, updatedAt) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, settings.getKey());
                stmt.setString(2, settings.getValue());
                stmt.setLong(3, settings.getUpdatedAt());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Single<Settings> getByKey(String key) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM settings WHERE key = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, key);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToSettings(rs);
                    }
                    throw new RuntimeException("Settings not found: " + key);
                }
            }
        });
    }

    @Override
    public Single<String> getValue(String key) {
        return Single.fromCallable(() -> {
            String sql = "SELECT value FROM settings WHERE key = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, key);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("value");
                    }
                    return null;
                }
            }
        });
    }

    @Override
    public Completable deleteByKey(String key) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM settings WHERE key = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, key);
                stmt.executeUpdate();
            }
        });
    }

    private Settings mapResultSetToSettings(ResultSet rs) throws Exception {
        Settings settings = new Settings();
        settings.setKey(rs.getString("key"));
        settings.setValue(rs.getString("value"));
        settings.setUpdatedAt(rs.getLong("updatedAt"));
        return settings;
    }
}
