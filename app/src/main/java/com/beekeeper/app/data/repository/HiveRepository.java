package com.beekeeper.app.data.repository;

import com.beekeeper.app.data.local.dao.HiveDao;
import com.beekeeper.app.data.local.entity.Hive;
import com.beekeeper.app.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

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
}
