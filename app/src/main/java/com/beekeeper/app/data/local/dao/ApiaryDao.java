package com.beekeeper.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.beekeeper.app.data.local.entity.Apiary;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ApiaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Apiary apiary);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<Apiary> apiaries);

    @Update
    Completable update(Apiary apiary);

    @Delete
    Completable delete(Apiary apiary);

    @Query("SELECT * FROM apiaries WHERE id = :id")
    Single<Apiary> getById(String id);

    @Query("SELECT * FROM apiaries ORDER BY name ASC")
    Flowable<List<Apiary>> getAll();

    @Query("SELECT * FROM apiaries ORDER BY name ASC")
    Single<List<Apiary>> getAllOnce();

    @Query("DELETE FROM apiaries WHERE id = :id")
    Completable deleteById(String id);

    @Query("SELECT COUNT(*) FROM apiaries")
    Single<Integer> getCount();
}
