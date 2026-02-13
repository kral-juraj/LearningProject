package com.beekeeper.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.beekeeper.app.data.local.entity.Taxation;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface TaxationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Taxation taxation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<Taxation> taxations);

    @Update
    Completable update(Taxation taxation);

    @Delete
    Completable delete(Taxation taxation);

    @Query("SELECT * FROM taxations WHERE id = :id")
    Single<Taxation> getById(String id);

    @Query("SELECT * FROM taxations WHERE hiveId = :hiveId ORDER BY taxationDate DESC")
    Flowable<List<Taxation>> getByHiveId(String hiveId);

    @Query("SELECT * FROM taxations WHERE hiveId = :hiveId AND taxationDate BETWEEN :startDate AND :endDate ORDER BY taxationDate DESC")
    Single<List<Taxation>> getByHiveIdAndDateRange(String hiveId, long startDate, long endDate);

    @Query("DELETE FROM taxations WHERE id = :id")
    Completable deleteById(String id);
}
