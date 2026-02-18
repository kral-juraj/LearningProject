package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.ApiaryDao;
import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Repository for Apiary entities.
 * Contains business logic for apiary operations.
 * 100% shared between Android and Desktop.
 */
public class ApiaryRepository {

    private final ApiaryDao apiaryDao;

    public ApiaryRepository(ApiaryDao apiaryDao) {
        this.apiaryDao = apiaryDao;
    }

    public Completable insertApiary(Apiary apiary) {
        if (apiary.getId() == null || apiary.getId().isEmpty()) {
            apiary.setId(UUID.randomUUID().toString());
        }
        if (apiary.getCreatedAt() == 0) {
            apiary.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        apiary.setUpdatedAt(DateUtils.getCurrentTimestamp());
        return apiaryDao.insert(apiary);
    }

    public Completable updateApiary(Apiary apiary) {
        apiary.setUpdatedAt(DateUtils.getCurrentTimestamp());
        return apiaryDao.update(apiary);
    }

    public Completable deleteApiary(Apiary apiary) {
        return apiaryDao.delete(apiary);
    }

    public Single<Apiary> getApiaryById(String id) {
        return apiaryDao.getById(id);
    }

    public Flowable<List<Apiary>> getAllApiaries() {
        return apiaryDao.getAll();
    }

    public Single<List<Apiary>> getAllApiariesOnce() {
        return apiaryDao.getAllOnce();
    }

    public Single<Integer> getApiaryCount() {
        return apiaryDao.getCount();
    }

    /**
     * Update display order for multiple apiaries (used for drag-and-drop reordering).
     * Sets updatedAt timestamp for all apiaries and delegates to DAO insertAll().
     *
     * Use case: After user drags an apiary to a new position, update all display orders.
     *
     * @param apiaries List of apiaries with updated displayOrder values
     * @return Completable that completes when all updates are done
     */
    public Completable updateApiaryOrder(List<Apiary> apiaries) {
        long now = DateUtils.getCurrentTimestamp();
        for (Apiary apiary : apiaries) {
            apiary.setUpdatedAt(now);
        }
        return apiaryDao.insertAll(apiaries);
    }
}
