package com.beekeeper.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.beekeeper.app.data.local.entity.Feeding;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface FeedingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Feeding feeding);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<Feeding> feedings);

    @Update
    Completable update(Feeding feeding);

    @Delete
    Completable delete(Feeding feeding);

    @Query("SELECT * FROM feedings WHERE id = :id")
    Single<Feeding> getById(String id);

    @Query("SELECT * FROM feedings WHERE hiveId = :hiveId ORDER BY feedingDate DESC")
    Flowable<List<Feeding>> getByHiveId(String hiveId);

    @Query("SELECT * FROM feedings WHERE hiveId = :hiveId AND feedingDate BETWEEN :startDate AND :endDate ORDER BY feedingDate DESC")
    Single<List<Feeding>> getByHiveIdAndDateRange(String hiveId, long startDate, long endDate);

    @Query("SELECT * FROM feedings WHERE feedingDate BETWEEN :startDate AND :endDate ORDER BY feedingDate DESC")
    Single<List<Feeding>> getByDateRange(long startDate, long endDate);

    @Query("DELETE FROM feedings WHERE id = :id")
    Completable deleteById(String id);

    @Query("SELECT SUM(amountKg) FROM feedings WHERE hiveId = :hiveId AND feedingDate BETWEEN :startDate AND :endDate")
    Single<Double> getTotalAmountByHiveId(String hiveId, long startDate, long endDate);
}
