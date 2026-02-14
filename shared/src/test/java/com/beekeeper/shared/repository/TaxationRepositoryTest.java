package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.TaxationDao;
import com.beekeeper.shared.dao.TaxationFrameDao;
import com.beekeeper.shared.entity.Taxation;
import com.beekeeper.shared.entity.TaxationFrame;
import io.reactivex.Completable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TaxationRepository.
 */
class TaxationRepositoryTest {

    @Mock
    private TaxationDao taxationDao;

    @Mock
    private TaxationFrameDao taxationFrameDao;

    private TaxationRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new TaxationRepository(taxationDao, taxationFrameDao);
    }

    @Test
    void testInsertTaxation_generatesIdAndTimestamps() {
        Taxation taxation = new Taxation();
        taxation.setHiveId("hive-1");
        taxation.setTaxationDate(System.currentTimeMillis());

        when(taxationDao.insert(any(Taxation.class))).thenReturn(Completable.complete());

        long before = System.currentTimeMillis();
        repository.insertTaxation(taxation).blockingAwait();
        long after = System.currentTimeMillis();

        assertNotNull(taxation.getId());
        assertTrue(taxation.getCreatedAt() >= before);
        assertTrue(taxation.getUpdatedAt() >= before);
        verify(taxationDao, times(1)).insert(taxation);
    }

    @Test
    void testInsertTaxationWithFrames_callsBothDaos() {
        Taxation taxation = new Taxation();
        taxation.setHiveId("hive-1");
        taxation.setTaxationDate(System.currentTimeMillis());

        TaxationFrame frame1 = new TaxationFrame();
        frame1.setPosition(1);
        TaxationFrame frame2 = new TaxationFrame();
        frame2.setPosition(2);

        List<TaxationFrame> frames = Arrays.asList(frame1, frame2);

        when(taxationDao.insert(any(Taxation.class))).thenReturn(Completable.complete());
        when(taxationFrameDao.insertAll(anyList())).thenReturn(Completable.complete());

        repository.insertTaxationWithFrames(taxation, frames).blockingAwait();

        assertNotNull(taxation.getId());
        verify(taxationDao, times(1)).insert(taxation);
        verify(taxationFrameDao, times(1)).insertAll(frames);
    }

    @Test
    void testUpdateTaxation_updatesTimestamp() {
        Taxation taxation = new Taxation();
        taxation.setId("test-id");
        taxation.setUpdatedAt(1000000000L);

        when(taxationDao.update(any(Taxation.class))).thenReturn(Completable.complete());

        repository.updateTaxation(taxation).blockingAwait();

        assertTrue(taxation.getUpdatedAt() > 1000000000L);
        verify(taxationDao, times(1)).update(taxation);
    }

    @Test
    void testDeleteTaxation_callsDao() {
        Taxation taxation = new Taxation();
        taxation.setId("test-id");

        when(taxationDao.delete(any(Taxation.class))).thenReturn(Completable.complete());

        repository.deleteTaxation(taxation).blockingAwait();

        verify(taxationDao, times(1)).delete(taxation);
    }
}
