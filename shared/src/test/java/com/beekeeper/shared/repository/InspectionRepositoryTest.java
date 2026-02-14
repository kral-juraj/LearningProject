package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.InspectionDao;
import com.beekeeper.shared.entity.Inspection;
import io.reactivex.Completable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for InspectionRepository.
 */
class InspectionRepositoryTest {

    @Mock
    private InspectionDao inspectionDao;

    private InspectionRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new InspectionRepository(inspectionDao);
    }

    @Test
    void testInsertInspection_generatesIdAndTimestamps() {
        Inspection inspection = new Inspection();
        inspection.setHiveId("hive-1");
        inspection.setInspectionDate(System.currentTimeMillis());

        when(inspectionDao.insert(any(Inspection.class))).thenReturn(Completable.complete());

        long before = System.currentTimeMillis();
        repository.insertInspection(inspection).blockingAwait();
        long after = System.currentTimeMillis();

        assertNotNull(inspection.getId());
        assertTrue(inspection.getCreatedAt() >= before);
        assertTrue(inspection.getUpdatedAt() >= before);
        verify(inspectionDao, times(1)).insert(inspection);
    }

    @Test
    void testUpdateInspection_updatesTimestamp() {
        Inspection inspection = new Inspection();
        inspection.setId("test-id");
        inspection.setUpdatedAt(1000000000L);

        when(inspectionDao.update(any(Inspection.class))).thenReturn(Completable.complete());

        repository.updateInspection(inspection).blockingAwait();

        assertTrue(inspection.getUpdatedAt() > 1000000000L);
        verify(inspectionDao, times(1)).update(inspection);
    }

    @Test
    void testDeleteInspection_callsDao() {
        Inspection inspection = new Inspection();
        inspection.setId("test-id");

        when(inspectionDao.delete(any(Inspection.class))).thenReturn(Completable.complete());

        repository.deleteInspection(inspection).blockingAwait();

        verify(inspectionDao, times(1)).delete(inspection);
    }
}
