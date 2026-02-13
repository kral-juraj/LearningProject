package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.ApiaryDao;
import com.beekeeper.shared.entity.Apiary;

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
 * JDBC implementation of ApiaryDao for desktop application.
 * Uses SQLite database via JDBC.
 */
public class JdbcApiaryDao implements ApiaryDao {

    @Override
    public Completable insert(Apiary apiary) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO apiaries (id, name, location, latitude, longitude, createdAt, updatedAt) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, apiary.getId());
                stmt.setString(2, apiary.getName());
                stmt.setString(3, apiary.getLocation());
                stmt.setDouble(4, apiary.getLatitude());
                stmt.setDouble(5, apiary.getLongitude());
                stmt.setLong(6, apiary.getCreatedAt());
                stmt.setLong(7, apiary.getUpdatedAt());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable insertAll(List<Apiary> apiaries) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO apiaries (id, name, location, latitude, longitude, createdAt, updatedAt) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Apiary apiary : apiaries) {
                    stmt.setString(1, apiary.getId());
                    stmt.setString(2, apiary.getName());
                    stmt.setString(3, apiary.getLocation());
                    stmt.setDouble(4, apiary.getLatitude());
                    stmt.setDouble(5, apiary.getLongitude());
                    stmt.setLong(6, apiary.getCreatedAt());
                    stmt.setLong(7, apiary.getUpdatedAt());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        });
    }

    @Override
    public Completable update(Apiary apiary) {
        return Completable.fromAction(() -> {
            String sql = "UPDATE apiaries SET name=?, location=?, latitude=?, longitude=?, updatedAt=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, apiary.getName());
                stmt.setString(2, apiary.getLocation());
                stmt.setDouble(3, apiary.getLatitude());
                stmt.setDouble(4, apiary.getLongitude());
                stmt.setLong(5, apiary.getUpdatedAt());
                stmt.setString(6, apiary.getId());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable delete(Apiary apiary) {
        return deleteById(apiary.getId());
    }

    @Override
    public Single<Apiary> getById(String id) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM apiaries WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToApiary(rs);
                    }
                    throw new RuntimeException("Apiary not found: " + id);
                }
            }
        });
    }

    @Override
    public Flowable<List<Apiary>> getAll() {
        return Flowable.fromCallable(() -> {
            List<Apiary> list = new ArrayList<>();
            String sql = "SELECT * FROM apiaries ORDER BY name ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSetToApiary(rs));
                }
            }
            return list;
        });
    }

    @Override
    public Single<List<Apiary>> getAllOnce() {
        return Single.fromCallable(() -> {
            List<Apiary> list = new ArrayList<>();
            String sql = "SELECT * FROM apiaries ORDER BY name ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSetToApiary(rs));
                }
            }
            return list;
        });
    }

    @Override
    public Completable deleteById(String id) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM apiaries WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Single<Integer> getCount() {
        return Single.fromCallable(() -> {
            String sql = "SELECT COUNT(*) FROM apiaries";
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        });
    }

    private Apiary mapResultSetToApiary(ResultSet rs) throws Exception {
        Apiary apiary = new Apiary();
        apiary.setId(rs.getString("id"));
        apiary.setName(rs.getString("name"));
        apiary.setLocation(rs.getString("location"));
        apiary.setLatitude(rs.getDouble("latitude"));
        apiary.setLongitude(rs.getDouble("longitude"));
        apiary.setCreatedAt(rs.getLong("createdAt"));
        apiary.setUpdatedAt(rs.getLong("updatedAt"));
        return apiary;
    }
}
