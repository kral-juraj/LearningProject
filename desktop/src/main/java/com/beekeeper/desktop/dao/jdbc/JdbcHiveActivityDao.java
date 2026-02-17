package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.HiveActivityDao;
import com.beekeeper.shared.entity.HiveActivity;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of HiveActivityDao.
 *
 * Use case: Persist and retrieve hive activity history from SQLite database.
 */
public class JdbcHiveActivityDao implements HiveActivityDao {

    /**
     * Map ResultSet row to HiveActivity entity.
     */
    private HiveActivity mapResultSetToHiveActivity(ResultSet rs) throws SQLException {
        HiveActivity activity = new HiveActivity();
        activity.setId(rs.getString("id"));
        activity.setHiveId(rs.getString("hiveId"));
        activity.setActivityType(rs.getString("activityType"));
        activity.setActivityDate(rs.getLong("activityDate"));
        activity.setDescription(rs.getString("description"));
        activity.setOldValue(rs.getString("oldValue"));
        activity.setNewValue(rs.getString("newValue"));
        activity.setCreatedAt(rs.getLong("createdAt"));
        activity.setUpdatedAt(rs.getLong("updatedAt"));
        return activity;
    }

    @Override
    public Completable insert(HiveActivity activity) {
        return Completable.fromAction(() -> {
            String sql = "INSERT INTO hive_activities " +
                "(id, hiveId, activityType, activityDate, description, oldValue, newValue, createdAt, updatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, activity.getId());
                stmt.setString(2, activity.getHiveId());
                stmt.setString(3, activity.getActivityType());
                stmt.setLong(4, activity.getActivityDate());
                stmt.setString(5, activity.getDescription());
                stmt.setString(6, activity.getOldValue());
                stmt.setString(7, activity.getNewValue());
                stmt.setLong(8, activity.getCreatedAt());
                stmt.setLong(9, activity.getUpdatedAt());

                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable update(HiveActivity activity) {
        return Completable.fromAction(() -> {
            String sql = "UPDATE hive_activities SET " +
                "hiveId = ?, activityType = ?, activityDate = ?, description = ?, " +
                "oldValue = ?, newValue = ?, updatedAt = ? " +
                "WHERE id = ?";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, activity.getHiveId());
                stmt.setString(2, activity.getActivityType());
                stmt.setLong(3, activity.getActivityDate());
                stmt.setString(4, activity.getDescription());
                stmt.setString(5, activity.getOldValue());
                stmt.setString(6, activity.getNewValue());
                stmt.setLong(7, activity.getUpdatedAt());
                stmt.setString(8, activity.getId());

                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable delete(HiveActivity activity) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM hive_activities WHERE id = ?";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, activity.getId());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Flowable<List<HiveActivity>> getByHiveId(String hiveId) {
        return Flowable.fromCallable(() -> {
            List<HiveActivity> list = new ArrayList<>();
            String sql = "SELECT * FROM hive_activities WHERE hiveId = ? ORDER BY activityDate DESC";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, hiveId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToHiveActivity(rs));
                    }
                }
            }

            return list;
        });
    }

    @Override
    public Single<HiveActivity> getById(String id) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM hive_activities WHERE id = ?";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToHiveActivity(rs);
                    } else {
                        throw new SQLException("HiveActivity not found: " + id);
                    }
                }
            }
        });
    }

    @Override
    public Flowable<List<HiveActivity>> getAll() {
        return Flowable.fromCallable(() -> {
            List<HiveActivity> list = new ArrayList<>();
            String sql = "SELECT * FROM hive_activities ORDER BY activityDate DESC";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(mapResultSetToHiveActivity(rs));
                }
            }

            return list;
        });
    }
}
