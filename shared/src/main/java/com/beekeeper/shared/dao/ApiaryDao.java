package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.Apiary;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Data Access Object for Apiary entities.
 * Platform-specific implementations:
 * - Android: Room-based implementation in app module
 * - Desktop: JDBC-based implementation in desktop module
 */
public interface ApiaryDao {

    Completable insert(Apiary apiary);

    Completable insertAll(List<Apiary> apiaries);

    Completable update(Apiary apiary);

    Completable delete(Apiary apiary);

    Single<Apiary> getById(String id);

    Flowable<List<Apiary>> getAll();

    Single<List<Apiary>> getAllOnce();

    Completable deleteById(String id);

    Single<Integer> getCount();
}
