package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.TaxationFrame;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Data Access Object for TaxationFrame entities.
 * Platform-specific implementations:
 * - Android: Room-based implementation in app module
 * - Desktop: JDBC-based implementation in desktop module
 */
public interface TaxationFrameDao {

    Completable insert(TaxationFrame frame);

    Completable insertAll(List<TaxationFrame> frames);

    Completable update(TaxationFrame frame);

    Completable delete(TaxationFrame frame);

    Single<TaxationFrame> getById(String id);

    Flowable<List<TaxationFrame>> getByTaxationId(String taxationId);

    Single<List<TaxationFrame>> getByTaxationIdOnce(String taxationId);

    Completable deleteByTaxationId(String taxationId);
}
