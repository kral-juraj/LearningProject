package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.InspectionDao;
import com.beekeeper.shared.entity.Inspection;
import com.beekeeper.shared.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Repository for Inspection entities.
 * Contains business logic for inspection operations.
 * 100% shared between Android and Desktop.
 */
public class InspectionRepository {

    private final InspectionDao inspectionDao;

    public InspectionRepository(InspectionDao inspectionDao) {
        this.inspectionDao = inspectionDao;
    }

    public Completable insertInspection(Inspection inspection) {
        if (inspection.getId() == null || inspection.getId().isEmpty()) {
            inspection.setId(UUID.randomUUID().toString());
        }
        if (inspection.getCreatedAt() == 0) {
            inspection.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        inspection.setUpdatedAt(DateUtils.getCurrentTimestamp());
        return inspectionDao.insert(inspection);
    }

    public Completable updateInspection(Inspection inspection) {
        inspection.setUpdatedAt(DateUtils.getCurrentTimestamp());
        return inspectionDao.update(inspection);
    }

    public Completable deleteInspection(Inspection inspection) {
        return inspectionDao.delete(inspection);
    }

    public Single<Inspection> getInspectionById(String id) {
        return inspectionDao.getById(id);
    }

    public Flowable<List<Inspection>> getInspectionsByHiveId(String hiveId) {
        return inspectionDao.getByHiveId(hiveId);
    }

    public Single<List<Inspection>> getInspectionsByHiveIdAndDateRange(String hiveId, long startDate, long endDate) {
        return inspectionDao.getByHiveIdAndDateRange(hiveId, startDate, endDate);
    }

    public Flowable<List<Inspection>> getRecentInspections(int limit) {
        return inspectionDao.getRecent(limit);
    }

    public Single<Integer> getInspectionCountByHiveId(String hiveId) {
        return inspectionDao.getCountByHiveId(hiveId);
    }
}
