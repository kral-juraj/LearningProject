package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.HiveDao;
import com.beekeeper.shared.entity.Hive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * JDBC implementation of HiveDao for desktop application.
 * Uses SQLite database via JDBC.
 */
public class JdbcHiveDao implements HiveDao {

    @Override
    public Completable insert(Hive hive) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO hives (id, apiaryId, name, type, queenId, queenYear, queenColor, active, notes, createdAt, updatedAt) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hive.getId());
                stmt.setString(2, hive.getApiaryId());
                stmt.setString(3, hive.getName());
                stmt.setString(4, hive.getType());
                stmt.setString(5, hive.getQueenId());
                stmt.setInt(6, hive.getQueenYear());
                stmt.setString(7, hive.getQueenColor());
                stmt.setInt(8, hive.isActive() ? 1 : 0);
                stmt.setString(9, hive.getNotes());
                stmt.setLong(10, hive.getCreatedAt());
                stmt.setLong(11, hive.getUpdatedAt());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable insertAll(List<Hive> hives) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO hives (id, apiaryId, name, type, queenId, queenYear, queenColor, active, notes, createdAt, updatedAt) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Hive hive : hives) {
                    stmt.setString(1, hive.getId());
                    stmt.setString(2, hive.getApiaryId());
                    stmt.setString(3, hive.getName());
                    stmt.setString(4, hive.getType());
                    stmt.setString(5, hive.getQueenId());
                    stmt.setInt(6, hive.getQueenYear());
                    stmt.setString(7, hive.getQueenColor());
                    stmt.setInt(8, hive.isActive() ? 1 : 0);
                    stmt.setString(9, hive.getNotes());
                    stmt.setLong(10, hive.getCreatedAt());
                    stmt.setLong(11, hive.getUpdatedAt());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        });
    }

    @Override
    public Completable update(Hive hive) {
        return Completable.fromAction(() -> {
            String sql = "UPDATE hives SET apiaryId=?, name=?, type=?, queenId=?, queenYear=?, queenColor=?, active=?, notes=?, updatedAt=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hive.getApiaryId());
                stmt.setString(2, hive.getName());
                stmt.setString(3, hive.getType());
                stmt.setString(4, hive.getQueenId());
                stmt.setInt(5, hive.getQueenYear());
                stmt.setString(6, hive.getQueenColor());
                stmt.setInt(7, hive.isActive() ? 1 : 0);
                stmt.setString(8, hive.getNotes());
                stmt.setLong(9, hive.getUpdatedAt());
                stmt.setString(10, hive.getId());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable delete(Hive hive) {
        return deleteById(hive.getId());
    }

    @Override
    public Single<Hive> getById(String id) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM hives WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToHive(rs);
                    }
                    throw new RuntimeException("Hive not found: " + id);
                }
            }
        });
    }

    @Override
    public Flowable<List<Hive>> getByApiaryId(String apiaryId) {
        return Flowable.fromCallable(() -> {
            List<Hive> list = new ArrayList<>();
            String sql = "SELECT * FROM hives WHERE apiaryId = ? ORDER BY name ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, apiaryId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToHive(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Flowable<List<Hive>> getActiveByApiaryId(String apiaryId) {
        return Flowable.fromCallable(() -> {
            List<Hive> list = new ArrayList<>();
            String sql = "SELECT * FROM hives WHERE apiaryId = ? AND active = 1 ORDER BY name ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, apiaryId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToHive(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Flowable<List<Hive>> getAll() {
        return Flowable.fromCallable(() -> {
            List<Hive> list = new ArrayList<>();
            String sql = "SELECT * FROM hives ORDER BY name ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSetToHive(rs));
                }
            }
            return list;
        });
    }

    @Override
    public Completable deleteById(String id) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM hives WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Single<Integer> getCountByApiaryId(String apiaryId) {
        return Single.fromCallable(() -> {
            String sql = "SELECT COUNT(*) FROM hives WHERE apiaryId = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, apiaryId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                    return 0;
                }
            }
        });
    }

    private Hive mapResultSetToHive(ResultSet rs) throws Exception {
        Hive hive = new Hive();
        hive.setId(rs.getString("id"));
        hive.setApiaryId(rs.getString("apiaryId"));
        hive.setName(rs.getString("name"));
        hive.setType(rs.getString("type"));
        hive.setQueenId(rs.getString("queenId"));
        hive.setQueenYear(rs.getInt("queenYear"));
        hive.setQueenColor(rs.getString("queenColor"));
        hive.setActive(rs.getInt("active") == 1);
        hive.setNotes(rs.getString("notes"));
        hive.setCreatedAt(rs.getLong("createdAt"));
        hive.setUpdatedAt(rs.getLong("updatedAt"));
        return hive;
    }
}
