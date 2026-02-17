package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.HiveActivity;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

/**
 * DAO interface for HiveActivity entities.
 *
 * Use case: Track historical changes and events for hives.
 * Activities include equipment changes (supers, grids), major events
 * (honey harvest, treatment), and modifications to hives.
 */
public interface HiveActivityDao {

    /**
     * Insert a new hive activity.
     *
     * @param activity Activity to insert
     * @return Completable that completes on success
     */
    Completable insert(HiveActivity activity);

    /**
     * Update an existing hive activity.
     *
     * @param activity Activity to update
     * @return Completable that completes on success
     */
    Completable update(HiveActivity activity);

    /**
     * Delete a hive activity.
     *
     * @param activity Activity to delete
     * @return Completable that completes on success
     */
    Completable delete(HiveActivity activity);

    /**
     * Get all activities for a specific hive, ordered by date descending.
     *
     * @param hiveId Hive ID
     * @return Flowable stream of activities
     */
    Flowable<List<HiveActivity>> getByHiveId(String hiveId);

    /**
     * Get a single activity by ID.
     *
     * @param id Activity ID
     * @return Single activity
     */
    Single<HiveActivity> getById(String id);

    /**
     * Get all activities across all hives.
     *
     * @return Flowable stream of all activities
     */
    Flowable<List<HiveActivity>> getAll();
}
