package com.beekeeper.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.beekeeper.app.data.local.entity.CalendarEvent;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CalendarEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(CalendarEvent event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<CalendarEvent> events);

    @Update
    Completable update(CalendarEvent event);

    @Delete
    Completable delete(CalendarEvent event);

    @Query("SELECT * FROM calendar_events WHERE id = :id")
    Single<CalendarEvent> getById(String id);

    @Query("SELECT * FROM calendar_events WHERE eventDate BETWEEN :startDate AND :endDate ORDER BY eventDate ASC")
    Flowable<List<CalendarEvent>> getByDateRange(long startDate, long endDate);

    @Query("SELECT * FROM calendar_events WHERE hiveId = :hiveId ORDER BY eventDate ASC")
    Flowable<List<CalendarEvent>> getByHiveId(String hiveId);

    @Query("SELECT * FROM calendar_events WHERE apiaryId = :apiaryId ORDER BY eventDate ASC")
    Flowable<List<CalendarEvent>> getByApiaryId(String apiaryId);

    @Query("SELECT * FROM calendar_events WHERE completed = 0 AND eventDate >= :currentDate ORDER BY eventDate ASC")
    Flowable<List<CalendarEvent>> getUpcoming(long currentDate);

    @Query("DELETE FROM calendar_events WHERE id = :id")
    Completable deleteById(String id);
}
