package com.beekeeper.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.beekeeper.app.data.local.entity.TaxationFrame;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface TaxationFrameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(TaxationFrame frame);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<TaxationFrame> frames);

    @Update
    Completable update(TaxationFrame frame);

    @Delete
    Completable delete(TaxationFrame frame);

    @Query("SELECT * FROM taxation_frames WHERE id = :id")
    Single<TaxationFrame> getById(String id);

    @Query("SELECT * FROM taxation_frames WHERE taxationId = :taxationId ORDER BY position ASC")
    Flowable<List<TaxationFrame>> getByTaxationId(String taxationId);

    @Query("SELECT * FROM taxation_frames WHERE taxationId = :taxationId ORDER BY position ASC")
    Single<List<TaxationFrame>> getByTaxationIdOnce(String taxationId);

    @Query("DELETE FROM taxation_frames WHERE taxationId = :taxationId")
    Completable deleteByTaxationId(String taxationId);
}
