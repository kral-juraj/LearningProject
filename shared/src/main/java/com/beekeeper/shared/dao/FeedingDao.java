package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.Feeding;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Data Access Object for Feeding entities.
 * Platform-specific implementations:
 * - Android: Room-based implementation in app module
 * - Desktop: JDBC-based implementation in desktop module
 */
public interface FeedingDao {

    Completable insert(Feeding feeding);

    Completable insertAll(List<Feeding> feedings);

    Completable update(Feeding feeding);

    Completable delete(Feeding feeding);

    Single<Feeding> getById(String id);

    Flowable<List<Feeding>> getByHiveId(String hiveId);

    Single<List<Feeding>> getByHiveIdAndDateRange(String hiveId, long startDate, long endDate);

    Single<List<Feeding>> getByDateRange(long startDate, long endDate);

    Completable deleteById(String id);

    Single<Double> getTotalAmountByHiveId(String hiveId, long startDate, long endDate);
}
