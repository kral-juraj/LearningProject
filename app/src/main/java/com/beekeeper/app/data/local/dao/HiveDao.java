package com.beekeeper.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.beekeeper.app.data.local.entity.Hive;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface HiveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Hive hive);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<Hive> hives);

    @Update
    Completable update(Hive hive);

    @Delete
    Completable delete(Hive hive);

    @Query("SELECT * FROM hives WHERE id = :id")
    Single<Hive> getById(String id);

    @Query("SELECT * FROM hives WHERE apiaryId = :apiaryId ORDER BY name ASC")
    Flowable<List<Hive>> getByApiaryId(String apiaryId);

    @Query("SELECT * FROM hives WHERE apiaryId = :apiaryId AND active = 1 ORDER BY name ASC")
    Flowable<List<Hive>> getActiveByApiaryId(String apiaryId);

    @Query("SELECT * FROM hives ORDER BY name ASC")
    Flowable<List<Hive>> getAll();

    @Query("DELETE FROM hives WHERE id = :id")
    Completable deleteById(String id);

    @Query("SELECT COUNT(*) FROM hives WHERE apiaryId = :apiaryId")
    Single<Integer> getCountByApiaryId(String apiaryId);
}
