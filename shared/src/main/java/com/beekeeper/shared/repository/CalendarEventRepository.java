package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.CalendarEventDao;
import com.beekeeper.shared.entity.CalendarEvent;
import com.beekeeper.shared.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Repository for CalendarEvent entities.
 * Contains business logic for calendar operations.
 * 100% shared between Android and Desktop.
 */
public class CalendarEventRepository {

    private final CalendarEventDao calendarEventDao;

    public CalendarEventRepository(CalendarEventDao calendarEventDao) {
        this.calendarEventDao = calendarEventDao;
    }

    public Completable insertEvent(CalendarEvent event) {
        if (event.getId() == null || event.getId().isEmpty()) {
            event.setId(UUID.randomUUID().toString());
        }
        if (event.getCreatedAt() == 0) {
            event.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        return calendarEventDao.insert(event);
    }

    public Completable updateEvent(CalendarEvent event) {
        return calendarEventDao.update(event);
    }

    public Completable deleteEvent(CalendarEvent event) {
        return calendarEventDao.delete(event);
    }

    public Single<CalendarEvent> getEventById(String id) {
        return calendarEventDao.getById(id);
    }

    public Flowable<List<CalendarEvent>> getEventsByDateRange(long startDate, long endDate) {
        return calendarEventDao.getByDateRange(startDate, endDate);
    }

    public Flowable<List<CalendarEvent>> getEventsByHiveId(String hiveId) {
        return calendarEventDao.getByHiveId(hiveId);
    }

    public Flowable<List<CalendarEvent>> getEventsByApiaryId(String apiaryId) {
        return calendarEventDao.getByApiaryId(apiaryId);
    }

    public Flowable<List<CalendarEvent>> getUpcomingEvents(long currentDate) {
        return calendarEventDao.getUpcoming(currentDate);
    }

    public Flowable<List<CalendarEvent>> getAllEvents() {
        return calendarEventDao.getAll();
    }

    public Completable deleteEventById(String id) {
        return calendarEventDao.deleteById(id);
    }
}
