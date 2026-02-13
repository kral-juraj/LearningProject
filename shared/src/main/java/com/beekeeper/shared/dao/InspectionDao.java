package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.Inspection;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Data Access Object for Inspection entities.
 * Platform-specific implementations:
 * - Android: Room-based implementation in app module
 * - Desktop: JDBC-based implementation in desktop module
 */
public interface InspectionDao {

    Completable insert(Inspection inspection);

    Completable insertAll(List<Inspection> inspections);

    Completable update(Inspection inspection);

    Completable delete(Inspection inspection);

    Single<Inspection> getById(String id);

    Flowable<List<Inspection>> getByHiveId(String hiveId);

    Single<List<Inspection>> getByHiveIdAndDateRange(String hiveId, long startDate, long endDate);

    Flowable<List<Inspection>> getRecent(int limit);

    Single<List<Inspection>> getByDateRange(long startDate, long endDate);

    Completable deleteById(String id);

    Single<Integer> getCountByHiveId(String hiveId);
}
