package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.Taxation;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Data Access Object for Taxation entities.
 * Platform-specific implementations:
 * - Android: Room-based implementation in app module
 * - Desktop: JDBC-based implementation in desktop module
 */
public interface TaxationDao {

    Completable insert(Taxation taxation);

    Completable insertAll(List<Taxation> taxations);

    Completable update(Taxation taxation);

    Completable delete(Taxation taxation);

    Single<Taxation> getById(String id);

    Flowable<List<Taxation>> getByHiveId(String hiveId);

    Single<List<Taxation>> getByHiveIdAndDateRange(String hiveId, long startDate, long endDate);

    Completable deleteById(String id);
}
