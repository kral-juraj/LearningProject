package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.CalendarEvent;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Data Access Object for CalendarEvent entities.
 * Platform-specific implementations:
 * - Android: Room-based implementation in app module
 * - Desktop: JDBC-based implementation in desktop module
 */
public interface CalendarEventDao {

    Completable insert(CalendarEvent event);

    Completable insertAll(List<CalendarEvent> events);

    Completable update(CalendarEvent event);

    Completable delete(CalendarEvent event);

    Single<CalendarEvent> getById(String id);

    Flowable<List<CalendarEvent>> getByDateRange(long startDate, long endDate);

    Flowable<List<CalendarEvent>> getByHiveId(String hiveId);

    Flowable<List<CalendarEvent>> getByApiaryId(String apiaryId);

    Flowable<List<CalendarEvent>> getUpcoming(long currentDate);

    Completable deleteById(String id);
}
