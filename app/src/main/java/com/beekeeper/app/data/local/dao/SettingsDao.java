package com.beekeeper.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.beekeeper.app.data.local.entity.Settings;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Settings settings);

    @Query("SELECT * FROM settings WHERE key = :key")
    Single<Settings> getByKey(String key);

    @Query("SELECT value FROM settings WHERE key = :key")
    Single<String> getValue(String key);

    @Query("DELETE FROM settings WHERE key = :key")
    Completable deleteByKey(String key);
}
