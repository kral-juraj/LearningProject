package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.InspectionRecording;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Data Access Object for InspectionRecording entities.
 * Platform-specific implementations:
 * - Android: Room-based implementation in app module
 * - Desktop: JDBC-based implementation in desktop module
 */
public interface InspectionRecordingDao {

    Completable insert(InspectionRecording recording);

    Completable update(InspectionRecording recording);

    Completable delete(InspectionRecording recording);

    Single<InspectionRecording> getById(String id);

    Single<InspectionRecording> getByInspectionId(String inspectionId);

    Single<List<InspectionRecording>> getUnprocessed();

    Single<List<InspectionRecording>> getOlderThan(long thresholdDate);

    Completable deleteById(String id);
}
