package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.TaxationDao;
import com.beekeeper.shared.dao.TaxationFrameDao;
import com.beekeeper.shared.entity.Taxation;
import com.beekeeper.shared.entity.TaxationFrame;
import com.beekeeper.shared.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Repository for Taxation entities.
 * Contains business logic for taxation operations.
 * 100% shared between Android and Desktop.
 */
public class TaxationRepository {

    private final TaxationDao taxationDao;
    private final TaxationFrameDao taxationFrameDao;

    public TaxationRepository(TaxationDao taxationDao, TaxationFrameDao taxationFrameDao) {
        this.taxationDao = taxationDao;
        this.taxationFrameDao = taxationFrameDao;
    }

    public Completable insertTaxation(Taxation taxation) {
        if (taxation.getId() == null || taxation.getId().isEmpty()) {
            taxation.setId(UUID.randomUUID().toString());
        }
        if (taxation.getCreatedAt() == 0) {
            taxation.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        taxation.setUpdatedAt(DateUtils.getCurrentTimestamp());
        return taxationDao.insert(taxation);
    }

    public Completable insertTaxationWithFrames(Taxation taxation, List<TaxationFrame> frames) {
        if (taxation.getId() == null || taxation.getId().isEmpty()) {
            taxation.setId(UUID.randomUUID().toString());
        }
        if (taxation.getCreatedAt() == 0) {
            taxation.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        taxation.setUpdatedAt(DateUtils.getCurrentTimestamp());

        // Set taxationId for all frames
        String taxationId = taxation.getId();
        for (TaxationFrame frame : frames) {
            if (frame.getId() == null || frame.getId().isEmpty()) {
                frame.setId(UUID.randomUUID().toString());
            }
            frame.setTaxationId(taxationId);
        }

        return taxationDao.insert(taxation)
                .andThen(taxationFrameDao.insertAll(frames));
    }

    public Completable updateTaxation(Taxation taxation) {
        taxation.setUpdatedAt(DateUtils.getCurrentTimestamp());
        return taxationDao.update(taxation);
    }

    public Completable deleteTaxation(Taxation taxation) {
        return taxationDao.delete(taxation);
    }

    public Single<Taxation> getTaxationById(String id) {
        return taxationDao.getById(id);
    }

    public Flowable<List<Taxation>> getTaxationsByHiveId(String hiveId) {
        return taxationDao.getByHiveId(hiveId);
    }

    public Single<List<Taxation>> getTaxationsByHiveIdAndDateRange(String hiveId, long startDate, long endDate) {
        return taxationDao.getByHiveIdAndDateRange(hiveId, startDate, endDate);
    }

    public Flowable<List<Taxation>> getTaxationsByApiaryId(String apiaryId) {
        return taxationDao.getByApiaryId(apiaryId);
    }

    // TaxationFrame operations
    public Completable insertFrame(TaxationFrame frame) {
        if (frame.getId() == null || frame.getId().isEmpty()) {
            frame.setId(UUID.randomUUID().toString());
        }
        return taxationFrameDao.insert(frame);
    }

    public Completable insertFrames(List<TaxationFrame> frames) {
        for (TaxationFrame frame : frames) {
            if (frame.getId() == null || frame.getId().isEmpty()) {
                frame.setId(UUID.randomUUID().toString());
            }
        }
        return taxationFrameDao.insertAll(frames);
    }

    public Completable updateFrame(TaxationFrame frame) {
        return taxationFrameDao.update(frame);
    }

    public Completable deleteFrame(TaxationFrame frame) {
        return taxationFrameDao.delete(frame);
    }

    public Flowable<List<TaxationFrame>> getFramesByTaxationId(String taxationId) {
        return taxationFrameDao.getByTaxationId(taxationId);
    }

    public Single<List<TaxationFrame>> getFramesByTaxationIdOnce(String taxationId) {
        return taxationFrameDao.getByTaxationIdOnce(taxationId);
    }
}
