package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.FeedingDao;
import com.beekeeper.shared.entity.Feeding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * JDBC implementation of FeedingDao for desktop application.
 */
public class JdbcFeedingDao implements FeedingDao {

    @Override
    public Completable insert(Feeding feeding) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO feedings (id, hiveId, feedingDate, weightBefore, weightAfter, " +
                        "feedType, amountKg, notes, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, feeding.getId());
                stmt.setString(2, feeding.getHiveId());
                stmt.setLong(3, feeding.getFeedingDate());
                stmt.setDouble(4, feeding.getWeightBefore());
                stmt.setDouble(5, feeding.getWeightAfter());
                stmt.setString(6, feeding.getFeedType());
                stmt.setDouble(7, feeding.getAmountKg());
                stmt.setString(8, feeding.getNotes());
                stmt.setLong(9, feeding.getCreatedAt());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable insertAll(List<Feeding> feedings) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO feedings (id, hiveId, feedingDate, weightBefore, weightAfter, " +
                        "feedType, amountKg, notes, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Feeding feeding : feedings) {
                    stmt.setString(1, feeding.getId());
                    stmt.setString(2, feeding.getHiveId());
                    stmt.setLong(3, feeding.getFeedingDate());
                    stmt.setDouble(4, feeding.getWeightBefore());
                    stmt.setDouble(5, feeding.getWeightAfter());
                    stmt.setString(6, feeding.getFeedType());
                    stmt.setDouble(7, feeding.getAmountKg());
                    stmt.setString(8, feeding.getNotes());
                    stmt.setLong(9, feeding.getCreatedAt());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        });
    }

    @Override
    public Completable update(Feeding feeding) {
        return Completable.fromAction(() -> {
            String sql = "UPDATE feedings SET hiveId=?, feedingDate=?, weightBefore=?, weightAfter=?, " +
                        "feedType=?, amountKg=?, notes=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, feeding.getHiveId());
                stmt.setLong(2, feeding.getFeedingDate());
                stmt.setDouble(3, feeding.getWeightBefore());
                stmt.setDouble(4, feeding.getWeightAfter());
                stmt.setString(5, feeding.getFeedType());
                stmt.setDouble(6, feeding.getAmountKg());
                stmt.setString(7, feeding.getNotes());
                stmt.setString(8, feeding.getId());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable delete(Feeding feeding) {
        return deleteById(feeding.getId());
    }

    @Override
    public Single<Feeding> getById(String id) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM feedings WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToFeeding(rs);
                    }
                    throw new RuntimeException("Feeding not found: " + id);
                }
            }
        });
    }

    @Override
    public Flowable<List<Feeding>> getByHiveId(String hiveId) {
        return Flowable.fromCallable(() -> {
            List<Feeding> list = new ArrayList<>();
            String sql = "SELECT * FROM feedings WHERE hiveId = ? ORDER BY feedingDate DESC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hiveId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToFeeding(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Single<List<Feeding>> getByHiveIdAndDateRange(String hiveId, long startDate, long endDate) {
        return Single.fromCallable(() -> {
            List<Feeding> list = new ArrayList<>();
            String sql = "SELECT * FROM feedings WHERE hiveId = ? AND feedingDate BETWEEN ? AND ? ORDER BY feedingDate DESC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hiveId);
                stmt.setLong(2, startDate);
                stmt.setLong(3, endDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToFeeding(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Single<List<Feeding>> getByDateRange(long startDate, long endDate) {
        return Single.fromCallable(() -> {
            List<Feeding> list = new ArrayList<>();
            String sql = "SELECT * FROM feedings WHERE feedingDate BETWEEN ? AND ? ORDER BY feedingDate DESC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, startDate);
                stmt.setLong(2, endDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToFeeding(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Completable deleteById(String id) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM feedings WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Single<Double> getTotalAmountByHiveId(String hiveId, long startDate, long endDate) {
        return Single.fromCallable(() -> {
            String sql = "SELECT SUM(amountKg) FROM feedings WHERE hiveId = ? AND feedingDate BETWEEN ? AND ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hiveId);
                stmt.setLong(2, startDate);
                stmt.setLong(3, endDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble(1);
                    }
                    return 0.0;
                }
            }
        });
    }

    private Feeding mapResultSetToFeeding(ResultSet rs) throws Exception {
        Feeding feeding = new Feeding();
        feeding.setId(rs.getString("id"));
        feeding.setHiveId(rs.getString("hiveId"));
        feeding.setFeedingDate(rs.getLong("feedingDate"));
        feeding.setWeightBefore(rs.getDouble("weightBefore"));
        feeding.setWeightAfter(rs.getDouble("weightAfter"));
        feeding.setFeedType(rs.getString("feedType"));
        feeding.setAmountKg(rs.getDouble("amountKg"));
        feeding.setNotes(rs.getString("notes"));
        feeding.setCreatedAt(rs.getLong("createdAt"));
        return feeding;
    }
}
