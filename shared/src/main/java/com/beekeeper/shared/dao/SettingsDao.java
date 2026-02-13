package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.Settings;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Data Access Object for Settings entities.
 * Platform-specific implementations:
 * - Android: Room-based implementation in app module
 * - Desktop: JDBC-based implementation in desktop module
 */
public interface SettingsDao {

    Completable insert(Settings settings);

    Single<Settings> getByKey(String key);

    Single<String> getValue(String key);

    Completable deleteByKey(String key);
}
