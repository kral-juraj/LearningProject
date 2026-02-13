package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.InspectionDao;
import com.beekeeper.shared.entity.Inspection;

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
 * JDBC implementation of InspectionDao for desktop application.
 */
public class JdbcInspectionDao implements InspectionDao {

    @Override
    public Completable insert(Inspection inspection) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, " +
                        "foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, " +
                        "queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, recordingId, " +
                        "extractedFromAudio, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, inspection.getId());
                stmt.setString(2, inspection.getHiveId());
                stmt.setLong(3, inspection.getInspectionDate());
                stmt.setDouble(4, inspection.getTemperature());
                stmt.setInt(5, inspection.getStrengthEstimate());
                stmt.setDouble(6, inspection.getFoodStoresKg());
                stmt.setInt(7, inspection.getBroodFrames());
                stmt.setInt(8, inspection.getCappedBroodDm());
                stmt.setInt(9, inspection.getUncappedBroodDm());
                stmt.setInt(10, inspection.getPollenFrames());
                stmt.setInt(11, inspection.getTotalFrames());
                stmt.setInt(12, inspection.isQueenSeen() ? 1 : 0);
                stmt.setString(13, inspection.getQueenNote());
                stmt.setInt(14, inspection.isVarroa() ? 1 : 0);
                stmt.setInt(15, inspection.getVarroaCount());
                stmt.setInt(16, inspection.getAggression());
                stmt.setString(17, inspection.getBehavior());
                stmt.setString(18, inspection.getNotes());
                stmt.setString(19, inspection.getRecordingId());
                stmt.setInt(20, inspection.isExtractedFromAudio() ? 1 : 0);
                stmt.setLong(21, inspection.getCreatedAt());
                stmt.setLong(22, inspection.getUpdatedAt());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable insertAll(List<Inspection> inspections) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, " +
                        "foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, " +
                        "queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, recordingId, " +
                        "extractedFromAudio, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Inspection inspection : inspections) {
                    stmt.setString(1, inspection.getId());
                    stmt.setString(2, inspection.getHiveId());
                    stmt.setLong(3, inspection.getInspectionDate());
                    stmt.setDouble(4, inspection.getTemperature());
                    stmt.setInt(5, inspection.getStrengthEstimate());
                    stmt.setDouble(6, inspection.getFoodStoresKg());
                    stmt.setInt(7, inspection.getBroodFrames());
                    stmt.setInt(8, inspection.getCappedBroodDm());
                    stmt.setInt(9, inspection.getUncappedBroodDm());
                    stmt.setInt(10, inspection.getPollenFrames());
                    stmt.setInt(11, inspection.getTotalFrames());
                    stmt.setInt(12, inspection.isQueenSeen() ? 1 : 0);
                    stmt.setString(13, inspection.getQueenNote());
                    stmt.setInt(14, inspection.isVarroa() ? 1 : 0);
                    stmt.setInt(15, inspection.getVarroaCount());
                    stmt.setInt(16, inspection.getAggression());
                    stmt.setString(17, inspection.getBehavior());
                    stmt.setString(18, inspection.getNotes());
                    stmt.setString(19, inspection.getRecordingId());
                    stmt.setInt(20, inspection.isExtractedFromAudio() ? 1 : 0);
                    stmt.setLong(21, inspection.getCreatedAt());
                    stmt.setLong(22, inspection.getUpdatedAt());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        });
    }

    @Override
    public Completable update(Inspection inspection) {
        return Completable.fromAction(() -> {
            String sql = "UPDATE inspections SET hiveId=?, inspectionDate=?, temperature=?, strengthEstimate=?, " +
                        "foodStoresKg=?, broodFrames=?, cappedBroodDm=?, uncappedBroodDm=?, pollenFrames=?, totalFrames=?, " +
                        "queenSeen=?, queenNote=?, varroa=?, varroaCount=?, aggression=?, behavior=?, notes=?, " +
                        "recordingId=?, extractedFromAudio=?, updatedAt=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, inspection.getHiveId());
                stmt.setLong(2, inspection.getInspectionDate());
                stmt.setDouble(3, inspection.getTemperature());
                stmt.setInt(4, inspection.getStrengthEstimate());
                stmt.setDouble(5, inspection.getFoodStoresKg());
                stmt.setInt(6, inspection.getBroodFrames());
                stmt.setInt(7, inspection.getCappedBroodDm());
                stmt.setInt(8, inspection.getUncappedBroodDm());
                stmt.setInt(9, inspection.getPollenFrames());
                stmt.setInt(10, inspection.getTotalFrames());
                stmt.setInt(11, inspection.isQueenSeen() ? 1 : 0);
                stmt.setString(12, inspection.getQueenNote());
                stmt.setInt(13, inspection.isVarroa() ? 1 : 0);
                stmt.setInt(14, inspection.getVarroaCount());
                stmt.setInt(15, inspection.getAggression());
                stmt.setString(16, inspection.getBehavior());
                stmt.setString(17, inspection.getNotes());
                stmt.setString(18, inspection.getRecordingId());
                stmt.setInt(19, inspection.isExtractedFromAudio() ? 1 : 0);
                stmt.setLong(20, inspection.getUpdatedAt());
                stmt.setString(21, inspection.getId());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable delete(Inspection inspection) {
        return deleteById(inspection.getId());
    }

    @Override
    public Single<Inspection> getById(String id) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM inspections WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToInspection(rs);
                    }
                    throw new RuntimeException("Inspection not found: " + id);
                }
            }
        });
    }

    @Override
    public Flowable<List<Inspection>> getByHiveId(String hiveId) {
        return Flowable.fromCallable(() -> {
            List<Inspection> list = new ArrayList<>();
            String sql = "SELECT * FROM inspections WHERE hiveId = ? ORDER BY inspectionDate DESC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hiveId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToInspection(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Single<List<Inspection>> getByHiveIdAndDateRange(String hiveId, long startDate, long endDate) {
        return Single.fromCallable(() -> {
            List<Inspection> list = new ArrayList<>();
            String sql = "SELECT * FROM inspections WHERE hiveId = ? AND inspectionDate BETWEEN ? AND ? ORDER BY inspectionDate DESC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hiveId);
                stmt.setLong(2, startDate);
                stmt.setLong(3, endDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToInspection(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Flowable<List<Inspection>> getRecent(int limit) {
        return Flowable.fromCallable(() -> {
            List<Inspection> list = new ArrayList<>();
            String sql = "SELECT * FROM inspections ORDER BY inspectionDate DESC LIMIT ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, limit);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToInspection(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Single<List<Inspection>> getByDateRange(long startDate, long endDate) {
        return Single.fromCallable(() -> {
            List<Inspection> list = new ArrayList<>();
            String sql = "SELECT * FROM inspections WHERE inspectionDate BETWEEN ? AND ? ORDER BY inspectionDate DESC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, startDate);
                stmt.setLong(2, endDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToInspection(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Completable deleteById(String id) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM inspections WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Single<Integer> getCountByHiveId(String hiveId) {
        return Single.fromCallable(() -> {
            String sql = "SELECT COUNT(*) FROM inspections WHERE hiveId = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hiveId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                    return 0;
                }
            }
        });
    }

    private Inspection mapResultSetToInspection(ResultSet rs) throws Exception {
        Inspection inspection = new Inspection();
        inspection.setId(rs.getString("id"));
        inspection.setHiveId(rs.getString("hiveId"));
        inspection.setInspectionDate(rs.getLong("inspectionDate"));
        inspection.setTemperature(rs.getDouble("temperature"));
        inspection.setStrengthEstimate(rs.getInt("strengthEstimate"));
        inspection.setFoodStoresKg(rs.getDouble("foodStoresKg"));
        inspection.setBroodFrames(rs.getInt("broodFrames"));
        inspection.setCappedBroodDm(rs.getInt("cappedBroodDm"));
        inspection.setUncappedBroodDm(rs.getInt("uncappedBroodDm"));
        inspection.setPollenFrames(rs.getInt("pollenFrames"));
        inspection.setTotalFrames(rs.getInt("totalFrames"));
        inspection.setQueenSeen(rs.getInt("queenSeen") == 1);
        inspection.setQueenNote(rs.getString("queenNote"));
        inspection.setVarroa(rs.getInt("varroa") == 1);
        inspection.setVarroaCount(rs.getInt("varroaCount"));
        inspection.setAggression(rs.getInt("aggression"));
        inspection.setBehavior(rs.getString("behavior"));
        inspection.setNotes(rs.getString("notes"));
        inspection.setRecordingId(rs.getString("recordingId"));
        inspection.setExtractedFromAudio(rs.getInt("extractedFromAudio") == 1);
        inspection.setCreatedAt(rs.getLong("createdAt"));
        inspection.setUpdatedAt(rs.getLong("updatedAt"));
        return inspection;
    }
}
