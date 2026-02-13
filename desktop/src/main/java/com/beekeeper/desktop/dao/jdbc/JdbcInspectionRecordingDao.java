package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.InspectionRecordingDao;
import com.beekeeper.shared.entity.InspectionRecording;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * JDBC implementation of InspectionRecordingDao for desktop application.
 */
public class JdbcInspectionRecordingDao implements InspectionRecordingDao {

    @Override
    public Completable insert(InspectionRecording recording) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO inspection_recordings (id, inspectionId, filePath, fileType, " +
                        "duration, fileSize, transcription, extractedJson, processed, recordedAt) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, recording.getId());
                stmt.setString(2, recording.getInspectionId());
                stmt.setString(3, recording.getFilePath());
                stmt.setString(4, recording.getFileType());
                stmt.setLong(5, recording.getDuration());
                stmt.setLong(6, recording.getFileSize());
                stmt.setString(7, recording.getTranscription());
                stmt.setString(8, recording.getExtractedJson());
                stmt.setInt(9, recording.isProcessed() ? 1 : 0);
                stmt.setLong(10, recording.getRecordedAt());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable update(InspectionRecording recording) {
        return Completable.fromAction(() -> {
            String sql = "UPDATE inspection_recordings SET inspectionId=?, filePath=?, fileType=?, " +
                        "duration=?, fileSize=?, transcription=?, extractedJson=?, processed=?, recordedAt=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, recording.getInspectionId());
                stmt.setString(2, recording.getFilePath());
                stmt.setString(3, recording.getFileType());
                stmt.setLong(4, recording.getDuration());
                stmt.setLong(5, recording.getFileSize());
                stmt.setString(6, recording.getTranscription());
                stmt.setString(7, recording.getExtractedJson());
                stmt.setInt(8, recording.isProcessed() ? 1 : 0);
                stmt.setLong(9, recording.getRecordedAt());
                stmt.setString(10, recording.getId());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable delete(InspectionRecording recording) {
        return deleteById(recording.getId());
    }

    @Override
    public Single<InspectionRecording> getById(String id) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM inspection_recordings WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToInspectionRecording(rs);
                    }
                    throw new RuntimeException("InspectionRecording not found: " + id);
                }
            }
        });
    }

    @Override
    public Single<InspectionRecording> getByInspectionId(String inspectionId) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM inspection_recordings WHERE inspectionId = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, inspectionId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToInspectionRecording(rs);
                    }
                    throw new RuntimeException("InspectionRecording not found for inspection: " + inspectionId);
                }
            }
        });
    }

    @Override
    public Single<List<InspectionRecording>> getUnprocessed() {
        return Single.fromCallable(() -> {
            List<InspectionRecording> list = new ArrayList<>();
            String sql = "SELECT * FROM inspection_recordings WHERE processed = 0";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToInspectionRecording(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Single<List<InspectionRecording>> getOlderThan(long thresholdDate) {
        return Single.fromCallable(() -> {
            List<InspectionRecording> list = new ArrayList<>();
            String sql = "SELECT * FROM inspection_recordings WHERE recordedAt < ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, thresholdDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToInspectionRecording(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Completable deleteById(String id) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM inspection_recordings WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        });
    }

    private InspectionRecording mapResultSetToInspectionRecording(ResultSet rs) throws Exception {
        InspectionRecording recording = new InspectionRecording();
        recording.setId(rs.getString("id"));
        recording.setInspectionId(rs.getString("inspectionId"));
        recording.setFilePath(rs.getString("filePath"));
        recording.setFileType(rs.getString("fileType"));
        recording.setDuration(rs.getLong("duration"));
        recording.setFileSize(rs.getLong("fileSize"));
        recording.setTranscription(rs.getString("transcription"));
        recording.setExtractedJson(rs.getString("extractedJson"));
        recording.setProcessed(rs.getInt("processed") == 1);
        recording.setRecordedAt(rs.getLong("recordedAt"));
        return recording;
    }
}
