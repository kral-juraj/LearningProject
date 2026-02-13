package com.beekeeper.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.beekeeper.app.data.local.entity.Inspection;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface InspectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Inspection inspection);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<Inspection> inspections);

    @Update
    Completable update(Inspection inspection);

    @Delete
    Completable delete(Inspection inspection);

    @Query("SELECT * FROM inspections WHERE id = :id")
    Single<Inspection> getById(String id);

    @Query("SELECT * FROM inspections WHERE hiveId = :hiveId ORDER BY inspectionDate DESC")
    Flowable<List<Inspection>> getByHiveId(String hiveId);

    @Query("SELECT * FROM inspections WHERE hiveId = :hiveId AND inspectionDate BETWEEN :startDate AND :endDate ORDER BY inspectionDate DESC")
    Single<List<Inspection>> getByHiveIdAndDateRange(String hiveId, long startDate, long endDate);

    @Query("SELECT * FROM inspections ORDER BY inspectionDate DESC LIMIT :limit")
    Flowable<List<Inspection>> getRecent(int limit);

    @Query("SELECT * FROM inspections WHERE inspectionDate BETWEEN :startDate AND :endDate ORDER BY inspectionDate DESC")
    Single<List<Inspection>> getByDateRange(long startDate, long endDate);

    @Query("DELETE FROM inspections WHERE id = :id")
    Completable deleteById(String id);

    @Query("SELECT COUNT(*) FROM inspections WHERE hiveId = :hiveId")
    Single<Integer> getCountByHiveId(String hiveId);
}
