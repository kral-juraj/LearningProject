package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.FeedingDao;
import com.beekeeper.shared.entity.Feeding;
import com.beekeeper.shared.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Repository for Feeding entities.
 * Contains business logic for feeding operations.
 * 100% shared between Android and Desktop.
 */
public class FeedingRepository {

    private final FeedingDao feedingDao;

    public FeedingRepository(FeedingDao feedingDao) {
        this.feedingDao = feedingDao;
    }

    public Completable insertFeeding(Feeding feeding) {
        if (feeding.getId() == null || feeding.getId().isEmpty()) {
            feeding.setId(UUID.randomUUID().toString());
        }
        if (feeding.getCreatedAt() == 0) {
            feeding.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        return feedingDao.insert(feeding);
    }

    public Completable updateFeeding(Feeding feeding) {
        return feedingDao.update(feeding);
    }

    public Completable deleteFeeding(Feeding feeding) {
        return feedingDao.delete(feeding);
    }

    public Single<Feeding> getFeedingById(String id) {
        return feedingDao.getById(id);
    }

    public Flowable<List<Feeding>> getFeedingsByHiveId(String hiveId) {
        return feedingDao.getByHiveId(hiveId);
    }

    public Single<List<Feeding>> getFeedingsByHiveIdAndDateRange(String hiveId, long startDate, long endDate) {
        return feedingDao.getByHiveIdAndDateRange(hiveId, startDate, endDate);
    }

    public Single<List<Feeding>> getFeedingsByDateRange(long startDate, long endDate) {
        return feedingDao.getByDateRange(startDate, endDate);
    }

    public Single<Double> getTotalAmountByHiveId(String hiveId, long startDate, long endDate) {
        return feedingDao.getTotalAmountByHiveId(hiveId, startDate, endDate);
    }

    public Completable deleteFeedingById(String id) {
        return feedingDao.deleteById(id);
    }
}
