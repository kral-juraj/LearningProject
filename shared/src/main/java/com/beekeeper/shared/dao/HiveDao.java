package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.Hive;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Data Access Object for Hive entities.
 * Platform-specific implementations:
 * - Android: Room-based implementation in app module
 * - Desktop: JDBC-based implementation in desktop module
 */
public interface HiveDao {

    Completable insert(Hive hive);

    Completable insertAll(List<Hive> hives);

    Completable update(Hive hive);

    Completable delete(Hive hive);

    Single<Hive> getById(String id);

    Flowable<List<Hive>> getByApiaryId(String apiaryId);

    Flowable<List<Hive>> getActiveByApiaryId(String apiaryId);

    Flowable<List<Hive>> getAll();

    Completable deleteById(String id);

    Single<Integer> getCountByApiaryId(String apiaryId);
}
