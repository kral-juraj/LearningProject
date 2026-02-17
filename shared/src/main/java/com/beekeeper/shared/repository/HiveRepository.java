package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.HiveDao;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Repository for Hive entities.
 * Contains business logic for hive operations.
 * 100% shared between Android and Desktop.
 */
public class HiveRepository {

    private final HiveDao hiveDao;

    public HiveRepository(HiveDao hiveDao) {
        this.hiveDao = hiveDao;
    }

    public Completable insertHive(Hive hive) {
        if (hive.getId() == null || hive.getId().isEmpty()) {
            hive.setId(UUID.randomUUID().toString());
        }
        if (hive.getCreatedAt() == 0) {
            hive.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        hive.setUpdatedAt(DateUtils.getCurrentTimestamp());
        return hiveDao.insert(hive);
    }

    public Completable updateHive(Hive hive) {
        hive.setUpdatedAt(DateUtils.getCurrentTimestamp());
        return hiveDao.update(hive);
    }

    public Completable deleteHive(Hive hive) {
        return hiveDao.delete(hive);
    }

    public Single<Hive> getHiveById(String id) {
        return hiveDao.getById(id);
    }

    public Flowable<List<Hive>> getHivesByApiaryId(String apiaryId) {
        return hiveDao.getByApiaryId(apiaryId);
    }

    public Flowable<List<Hive>> getActiveHivesByApiaryId(String apiaryId) {
        return hiveDao.getActiveByApiaryId(apiaryId);
    }

    public Flowable<List<Hive>> getAllHives() {
        return hiveDao.getAll();
    }

    public Single<Integer> getHiveCountByApiaryId(String apiaryId) {
        return hiveDao.getCountByApiaryId(apiaryId);
    }

    /**
     * Update display order for multiple hives.
     * Used for drag-and-drop reordering.
     *
     * @param hives List of hives with updated displayOrder values
     * @return Completable
     */
    public Completable updateHiveOrder(List<Hive> hives) {
        for (Hive hive : hives) {
            hive.setUpdatedAt(DateUtils.getCurrentTimestamp());
        }
        return hiveDao.insertAll(hives);  // Uses INSERT OR REPLACE
    }
}
