package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.TaxationDao;
import com.beekeeper.shared.entity.Taxation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * JDBC implementation of TaxationDao for desktop application.
 */
public class JdbcTaxationDao implements TaxationDao {

    @Override
    public Completable insert(Taxation taxation) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, " +
                        "foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, " +
                        "totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, taxation.getId());
                stmt.setString(2, taxation.getHiveId());
                stmt.setLong(3, taxation.getTaxationDate());
                stmt.setDouble(4, taxation.getTemperature());
                stmt.setInt(5, taxation.getTotalFrames());
                stmt.setDouble(6, taxation.getFoodStoresKg());
                stmt.setString(7, taxation.getNotes());
                stmt.setLong(8, taxation.getCreatedAt());
                stmt.setLong(9, taxation.getUpdatedAt());
                stmt.setInt(10, taxation.getTotalPollenDm());
                stmt.setInt(11, taxation.getTotalCappedStoresDm());
                stmt.setInt(12, taxation.getTotalUncappedStoresDm());
                stmt.setInt(13, taxation.getTotalCappedBroodDm());
                stmt.setInt(14, taxation.getTotalUncappedBroodDm());
                stmt.setInt(15, taxation.getTotalStarterFrames());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable insertAll(List<Taxation> taxations) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, " +
                        "foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, " +
                        "totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Taxation taxation : taxations) {
                    stmt.setString(1, taxation.getId());
                    stmt.setString(2, taxation.getHiveId());
                    stmt.setLong(3, taxation.getTaxationDate());
                    stmt.setDouble(4, taxation.getTemperature());
                    stmt.setInt(5, taxation.getTotalFrames());
                    stmt.setDouble(6, taxation.getFoodStoresKg());
                    stmt.setString(7, taxation.getNotes());
                    stmt.setLong(8, taxation.getCreatedAt());
                    stmt.setLong(9, taxation.getUpdatedAt());
                    stmt.setInt(10, taxation.getTotalPollenDm());
                    stmt.setInt(11, taxation.getTotalCappedStoresDm());
                    stmt.setInt(12, taxation.getTotalUncappedStoresDm());
                    stmt.setInt(13, taxation.getTotalCappedBroodDm());
                    stmt.setInt(14, taxation.getTotalUncappedBroodDm());
                    stmt.setInt(15, taxation.getTotalStarterFrames());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        });
    }

    @Override
    public Completable update(Taxation taxation) {
        return Completable.fromAction(() -> {
            String sql = "UPDATE taxations SET hiveId=?, taxationDate=?, temperature=?, totalFrames=?, " +
                        "foodStoresKg=?, notes=?, updatedAt=?, totalPollenDm=?, totalCappedStoresDm=?, " +
                        "totalUncappedStoresDm=?, totalCappedBroodDm=?, totalUncappedBroodDm=?, totalStarterFrames=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, taxation.getHiveId());
                stmt.setLong(2, taxation.getTaxationDate());
                stmt.setDouble(3, taxation.getTemperature());
                stmt.setInt(4, taxation.getTotalFrames());
                stmt.setDouble(5, taxation.getFoodStoresKg());
                stmt.setString(6, taxation.getNotes());
                stmt.setLong(7, taxation.getUpdatedAt());
                stmt.setInt(8, taxation.getTotalPollenDm());
                stmt.setInt(9, taxation.getTotalCappedStoresDm());
                stmt.setInt(10, taxation.getTotalUncappedStoresDm());
                stmt.setInt(11, taxation.getTotalCappedBroodDm());
                stmt.setInt(12, taxation.getTotalUncappedBroodDm());
                stmt.setInt(13, taxation.getTotalStarterFrames());
                stmt.setString(14, taxation.getId());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable delete(Taxation taxation) {
        return deleteById(taxation.getId());
    }

    @Override
    public Single<Taxation> getById(String id) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM taxations WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToTaxation(rs);
                    }
                    throw new RuntimeException("Taxation not found: " + id);
                }
            }
        });
    }

    @Override
    public Flowable<List<Taxation>> getByHiveId(String hiveId) {
        return Flowable.fromCallable(() -> {
            List<Taxation> list = new ArrayList<>();
            String sql = "SELECT * FROM taxations WHERE hiveId = ? ORDER BY taxationDate DESC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hiveId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToTaxation(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Single<List<Taxation>> getByHiveIdAndDateRange(String hiveId, long startDate, long endDate) {
        return Single.fromCallable(() -> {
            List<Taxation> list = new ArrayList<>();
            String sql = "SELECT * FROM taxations WHERE hiveId = ? AND taxationDate BETWEEN ? AND ? ORDER BY taxationDate DESC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hiveId);
                stmt.setLong(2, startDate);
                stmt.setLong(3, endDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToTaxation(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Completable deleteById(String id) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM taxations WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Flowable<List<Taxation>> getByApiaryId(String apiaryId) {
        return Flowable.fromCallable(() -> {
            List<Taxation> list = new ArrayList<>();
            String sql = "SELECT t.*, h.name as hiveName, h.frameType as hiveType " +
                        "FROM taxations t " +
                        "INNER JOIN hives h ON t.hiveId = h.id " +
                        "WHERE h.apiaryId = ? " +
                        "ORDER BY t.taxationDate DESC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, apiaryId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToTaxation(rs));
                    }
                }
            }
            return list;
        });
    }

    private Taxation mapResultSetToTaxation(ResultSet rs) throws Exception {
        Taxation taxation = new Taxation();
        taxation.setId(rs.getString("id"));
        taxation.setHiveId(rs.getString("hiveId"));
        taxation.setTaxationDate(rs.getLong("taxationDate"));
        taxation.setTemperature(rs.getDouble("temperature"));
        taxation.setTotalFrames(rs.getInt("totalFrames"));
        taxation.setFoodStoresKg(rs.getDouble("foodStoresKg"));
        taxation.setNotes(rs.getString("notes"));
        taxation.setCreatedAt(rs.getLong("createdAt"));
        taxation.setUpdatedAt(rs.getLong("updatedAt"));
        taxation.setTotalPollenDm(rs.getInt("totalPollenDm"));
        taxation.setTotalCappedStoresDm(rs.getInt("totalCappedStoresDm"));
        taxation.setTotalUncappedStoresDm(rs.getInt("totalUncappedStoresDm"));
        taxation.setTotalCappedBroodDm(rs.getInt("totalCappedBroodDm"));
        taxation.setTotalUncappedBroodDm(rs.getInt("totalUncappedBroodDm"));
        taxation.setTotalStarterFrames(rs.getInt("totalStarterFrames"));

        // Load hiveName and hiveType if available (from JOIN query)
        try {
            String hiveName = rs.getString("hiveName");
            taxation.setHiveName(hiveName);
        } catch (SQLException e) {
            // Column doesn't exist in this query, ignore
        }

        try {
            String hiveType = rs.getString("hiveType");
            taxation.setHiveType(hiveType);
        } catch (SQLException e) {
            // Column doesn't exist in this query, ignore
        }

        return taxation;
    }
}
