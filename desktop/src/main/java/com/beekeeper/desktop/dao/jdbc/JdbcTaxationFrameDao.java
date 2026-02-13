package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.TaxationFrameDao;
import com.beekeeper.shared.entity.TaxationFrame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * JDBC implementation of TaxationFrameDao for desktop application.
 */
public class JdbcTaxationFrameDao implements TaxationFrameDao {

    @Override
    public Completable insert(TaxationFrame frame) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO taxation_frames (id, taxationId, position, cappedBroodDm, " +
                        "uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, " +
                        "hasNucBox, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, frame.getId());
                stmt.setString(2, frame.getTaxationId());
                stmt.setInt(3, frame.getPosition());
                stmt.setInt(4, frame.getCappedBroodDm());
                stmt.setInt(5, frame.getUncappedBroodDm());
                stmt.setInt(6, frame.getPollenDm());
                stmt.setString(7, frame.getFrameType());
                stmt.setInt(8, frame.getFrameYear());
                stmt.setInt(9, frame.isStarter() ? 1 : 0);
                stmt.setInt(10, frame.isHasQueen() ? 1 : 0);
                stmt.setInt(11, frame.isHasCage() ? 1 : 0);
                stmt.setInt(12, frame.isHasNucBox() ? 1 : 0);
                stmt.setString(13, frame.getNotes());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable insertAll(List<TaxationFrame> frames) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO taxation_frames (id, taxationId, position, cappedBroodDm, " +
                        "uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, " +
                        "hasNucBox, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (TaxationFrame frame : frames) {
                    stmt.setString(1, frame.getId());
                    stmt.setString(2, frame.getTaxationId());
                    stmt.setInt(3, frame.getPosition());
                    stmt.setInt(4, frame.getCappedBroodDm());
                    stmt.setInt(5, frame.getUncappedBroodDm());
                    stmt.setInt(6, frame.getPollenDm());
                    stmt.setString(7, frame.getFrameType());
                    stmt.setInt(8, frame.getFrameYear());
                    stmt.setInt(9, frame.isStarter() ? 1 : 0);
                    stmt.setInt(10, frame.isHasQueen() ? 1 : 0);
                    stmt.setInt(11, frame.isHasCage() ? 1 : 0);
                    stmt.setInt(12, frame.isHasNucBox() ? 1 : 0);
                    stmt.setString(13, frame.getNotes());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        });
    }

    @Override
    public Completable update(TaxationFrame frame) {
        return Completable.fromAction(() -> {
            String sql = "UPDATE taxation_frames SET taxationId=?, position=?, cappedBroodDm=?, " +
                        "uncappedBroodDm=?, pollenDm=?, frameType=?, frameYear=?, isStarter=?, " +
                        "hasQueen=?, hasCage=?, hasNucBox=?, notes=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, frame.getTaxationId());
                stmt.setInt(2, frame.getPosition());
                stmt.setInt(3, frame.getCappedBroodDm());
                stmt.setInt(4, frame.getUncappedBroodDm());
                stmt.setInt(5, frame.getPollenDm());
                stmt.setString(6, frame.getFrameType());
                stmt.setInt(7, frame.getFrameYear());
                stmt.setInt(8, frame.isStarter() ? 1 : 0);
                stmt.setInt(9, frame.isHasQueen() ? 1 : 0);
                stmt.setInt(10, frame.isHasCage() ? 1 : 0);
                stmt.setInt(11, frame.isHasNucBox() ? 1 : 0);
                stmt.setString(12, frame.getNotes());
                stmt.setString(13, frame.getId());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable delete(TaxationFrame frame) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM taxation_frames WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, frame.getId());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Single<TaxationFrame> getById(String id) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM taxation_frames WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToTaxationFrame(rs);
                    }
                    throw new RuntimeException("TaxationFrame not found: " + id);
                }
            }
        });
    }

    @Override
    public Flowable<List<TaxationFrame>> getByTaxationId(String taxationId) {
        return Flowable.fromCallable(() -> {
            List<TaxationFrame> list = new ArrayList<>();
            String sql = "SELECT * FROM taxation_frames WHERE taxationId = ? ORDER BY position ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, taxationId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToTaxationFrame(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Single<List<TaxationFrame>> getByTaxationIdOnce(String taxationId) {
        return Single.fromCallable(() -> {
            List<TaxationFrame> list = new ArrayList<>();
            String sql = "SELECT * FROM taxation_frames WHERE taxationId = ? ORDER BY position ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, taxationId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToTaxationFrame(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Completable deleteByTaxationId(String taxationId) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM taxation_frames WHERE taxationId = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, taxationId);
                stmt.executeUpdate();
            }
        });
    }

    private TaxationFrame mapResultSetToTaxationFrame(ResultSet rs) throws Exception {
        TaxationFrame frame = new TaxationFrame();
        frame.setId(rs.getString("id"));
        frame.setTaxationId(rs.getString("taxationId"));
        frame.setPosition(rs.getInt("position"));
        frame.setCappedBroodDm(rs.getInt("cappedBroodDm"));
        frame.setUncappedBroodDm(rs.getInt("uncappedBroodDm"));
        frame.setPollenDm(rs.getInt("pollenDm"));
        frame.setFrameType(rs.getString("frameType"));
        frame.setFrameYear(rs.getInt("frameYear"));
        frame.setStarter(rs.getInt("isStarter") == 1);
        frame.setHasQueen(rs.getInt("hasQueen") == 1);
        frame.setHasCage(rs.getInt("hasCage") == 1);
        frame.setHasNucBox(rs.getInt("hasNucBox") == 1);
        frame.setNotes(rs.getString("notes"));
        return frame;
    }
}
