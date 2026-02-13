package com.beekeeper.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.beekeeper.app.data.local.entity.InspectionRecording;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface InspectionRecordingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(InspectionRecording recording);

    @Update
    Completable update(InspectionRecording recording);

    @Delete
    Completable delete(InspectionRecording recording);

    @Query("SELECT * FROM inspection_recordings WHERE id = :id")
    Single<InspectionRecording> getById(String id);

    @Query("SELECT * FROM inspection_recordings WHERE inspectionId = :inspectionId")
    Single<InspectionRecording> getByInspectionId(String inspectionId);

    @Query("SELECT * FROM inspection_recordings WHERE processed = 0")
    Single<List<InspectionRecording>> getUnprocessed();

    @Query("SELECT * FROM inspection_recordings WHERE recordedAt < :thresholdDate")
    Single<List<InspectionRecording>> getOlderThan(long thresholdDate);

    @Query("DELETE FROM inspection_recordings WHERE id = :id")
    Completable deleteById(String id);
}
