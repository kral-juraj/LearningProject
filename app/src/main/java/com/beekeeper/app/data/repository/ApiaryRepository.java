package com.beekeeper.app.data.repository;

import com.beekeeper.app.data.local.dao.ApiaryDao;
import com.beekeeper.app.data.local.entity.Apiary;
import com.beekeeper.app.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

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
}
