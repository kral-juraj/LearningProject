package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.CalendarEventDao;
import com.beekeeper.shared.entity.CalendarEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * JDBC implementation of CalendarEventDao for desktop application.
 */
public class JdbcCalendarEventDao implements CalendarEventDao {

    @Override
    public Completable insert(CalendarEvent event) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO calendar_events (id, title, description, eventDate, eventType, " +
                        "hiveId, apiaryId, completed, notes, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, event.getId());
                stmt.setString(2, event.getTitle());
                stmt.setString(3, event.getDescription());
                stmt.setLong(4, event.getEventDate());
                stmt.setString(5, event.getEventType());
                stmt.setString(6, event.getHiveId());
                stmt.setString(7, event.getApiaryId());
                stmt.setInt(8, event.isCompleted() ? 1 : 0);
                stmt.setString(9, event.getNotes());
                stmt.setLong(10, event.getCreatedAt());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable insertAll(List<CalendarEvent> events) {
        return Completable.fromAction(() -> {
            String sql = "INSERT OR REPLACE INTO calendar_events (id, title, description, eventDate, eventType, " +
                        "hiveId, apiaryId, completed, notes, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (CalendarEvent event : events) {
                    stmt.setString(1, event.getId());
                    stmt.setString(2, event.getTitle());
                    stmt.setString(3, event.getDescription());
                    stmt.setLong(4, event.getEventDate());
                    stmt.setString(5, event.getEventType());
                    stmt.setString(6, event.getHiveId());
                    stmt.setString(7, event.getApiaryId());
                    stmt.setInt(8, event.isCompleted() ? 1 : 0);
                    stmt.setString(9, event.getNotes());
                    stmt.setLong(10, event.getCreatedAt());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        });
    }

    @Override
    public Completable update(CalendarEvent event) {
        return Completable.fromAction(() -> {
            String sql = "UPDATE calendar_events SET title=?, description=?, eventDate=?, eventType=?, " +
                        "hiveId=?, apiaryId=?, completed=?, notes=? WHERE id=?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, event.getTitle());
                stmt.setString(2, event.getDescription());
                stmt.setLong(3, event.getEventDate());
                stmt.setString(4, event.getEventType());
                stmt.setString(5, event.getHiveId());
                stmt.setString(6, event.getApiaryId());
                stmt.setInt(7, event.isCompleted() ? 1 : 0);
                stmt.setString(8, event.getNotes());
                stmt.setString(9, event.getId());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Completable delete(CalendarEvent event) {
        return deleteById(event.getId());
    }

    @Override
    public Single<CalendarEvent> getById(String id) {
        return Single.fromCallable(() -> {
            String sql = "SELECT * FROM calendar_events WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToCalendarEvent(rs);
                    }
                    throw new RuntimeException("CalendarEvent not found: " + id);
                }
            }
        });
    }

    @Override
    public Flowable<List<CalendarEvent>> getByDateRange(long startDate, long endDate) {
        return Flowable.fromCallable(() -> {
            List<CalendarEvent> list = new ArrayList<>();
            String sql = "SELECT * FROM calendar_events WHERE eventDate BETWEEN ? AND ? ORDER BY eventDate ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, startDate);
                stmt.setLong(2, endDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToCalendarEvent(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Flowable<List<CalendarEvent>> getByHiveId(String hiveId) {
        return Flowable.fromCallable(() -> {
            List<CalendarEvent> list = new ArrayList<>();
            String sql = "SELECT * FROM calendar_events WHERE hiveId = ? ORDER BY eventDate ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, hiveId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToCalendarEvent(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Flowable<List<CalendarEvent>> getByApiaryId(String apiaryId) {
        return Flowable.fromCallable(() -> {
            List<CalendarEvent> list = new ArrayList<>();
            String sql = "SELECT * FROM calendar_events WHERE apiaryId = ? ORDER BY eventDate ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, apiaryId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToCalendarEvent(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Flowable<List<CalendarEvent>> getUpcoming(long currentDate) {
        return Flowable.fromCallable(() -> {
            List<CalendarEvent> list = new ArrayList<>();
            String sql = "SELECT * FROM calendar_events WHERE completed = 0 AND eventDate >= ? ORDER BY eventDate ASC";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, currentDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToCalendarEvent(rs));
                    }
                }
            }
            return list;
        });
    }

    @Override
    public Completable deleteById(String id) {
        return Completable.fromAction(() -> {
            String sql = "DELETE FROM calendar_events WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }
        });
    }

    private CalendarEvent mapResultSetToCalendarEvent(ResultSet rs) throws Exception {
        CalendarEvent event = new CalendarEvent();
        event.setId(rs.getString("id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setEventDate(rs.getLong("eventDate"));
        event.setEventType(rs.getString("eventType"));
        event.setHiveId(rs.getString("hiveId"));
        event.setApiaryId(rs.getString("apiaryId"));
        event.setCompleted(rs.getInt("completed") == 1);
        event.setNotes(rs.getString("notes"));
        event.setCreatedAt(rs.getLong("createdAt"));
        return event;
    }
}
