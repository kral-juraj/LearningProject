package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.dao.TaxationDao;
import com.beekeeper.shared.dao.TaxationFrameDao;
import com.beekeeper.shared.entity.Taxation;
import com.beekeeper.shared.entity.TaxationFrame;
import com.beekeeper.shared.repository.TaxationRepository;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TaxationViewModel.
 * Tests business logic with mocked DAOs and real Repository.
 *
 * CRITICAL: Tests the bug fix where updateTaxationWithFrames must preserve hiveId.
 */
class TaxationViewModelTest {

    @Mock
    private TaxationDao taxationDao;

    @Mock
    private TaxationFrameDao taxationFrameDao;

    private TaxationViewModel viewModel;

    // Test scheduler that executes immediately (synchronous)
    private final SchedulerProvider testScheduler = new SchedulerProvider() {
        @Override
        public Scheduler io() {
            return Schedulers.trampoline();
        }

        @Override
        public Scheduler mainThread() {
            return Schedulers.trampoline();
        }

        @Override
        public Scheduler computation() {
            return Schedulers.trampoline();
        }
    };

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Use real repository with mocked DAOs
        TaxationRepository repository = new TaxationRepository(taxationDao, taxationFrameDao);
        viewModel = new TaxationViewModel(repository, testScheduler);
    }

    @Test
    void testLoadTaxationsByHiveId_callsDAO() {
        // Given
        when(taxationDao.getByHiveId(anyString())).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.loadTaxationsByHiveId("hive-123");

        // Then
        verify(taxationDao, times(1)).getByHiveId("hive-123");
    }

    @Test
    void testLoadTaxationsByApiaryId_callsDAO() {
        // Given
        when(taxationDao.getByApiaryId(anyString())).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.loadTaxationsByApiaryId("apiary-456");

        // Then
        verify(taxationDao, times(1)).getByApiaryId("apiary-456");
    }

    @Test
    void testCreateTaxationWithFrames_callsBothDAOs() {
        // Given
        Taxation taxation = new Taxation();
        taxation.setHiveId("hive-123");
        taxation.setTaxationDate(System.currentTimeMillis());

        TaxationFrame frame1 = new TaxationFrame();
        frame1.setPosition(1);
        frame1.setCappedBroodDm(40);

        TaxationFrame frame2 = new TaxationFrame();
        frame2.setPosition(2);
        frame2.setPollenDm(10);

        List<TaxationFrame> frames = Arrays.asList(frame1, frame2);

        when(taxationDao.insert(any(Taxation.class))).thenReturn(Completable.complete());
        when(taxationFrameDao.insertAll(anyList())).thenReturn(Completable.complete());
        when(taxationDao.getByHiveId(anyString())).thenReturn(Flowable.just(Arrays.asList()));
        when(taxationDao.getByApiaryId(anyString())).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.createTaxationWithFrames(taxation, frames);

        // Then
        verify(taxationDao, times(1)).insert(any(Taxation.class));
        verify(taxationFrameDao, times(1)).insertAll(anyList());
    }

    @Test
    void testCreateTaxationWithFrames_calculatesAggregates() {
        // Given
        Taxation taxation = new Taxation();
        taxation.setHiveId("hive-123");

        TaxationFrame frame1 = new TaxationFrame();
        frame1.setPosition(1);
        frame1.setCappedBroodDm(40);
        frame1.setUncappedBroodDm(20);
        frame1.setPollenDm(10);
        frame1.setCappedStoresDm(15);
        frame1.setUncappedStoresDm(5);
        frame1.setStarter(true);

        TaxationFrame frame2 = new TaxationFrame();
        frame2.setPosition(2);
        frame2.setCappedBroodDm(30);
        frame2.setUncappedBroodDm(10);
        frame2.setPollenDm(5);
        frame2.setCappedStoresDm(10);
        frame2.setUncappedStoresDm(8);
        frame2.setStarter(false);

        List<TaxationFrame> frames = Arrays.asList(frame1, frame2);

        when(taxationDao.insert(any(Taxation.class))).thenAnswer(invocation -> {
            Taxation t = invocation.getArgument(0);
            // Verify aggregates were calculated
            assertEquals(70, t.getTotalCappedBroodDm()); // 40 + 30
            assertEquals(30, t.getTotalUncappedBroodDm()); // 20 + 10
            assertEquals(15, t.getTotalPollenDm()); // 10 + 5
            assertEquals(25, t.getTotalCappedStoresDm()); // 15 + 10
            assertEquals(13, t.getTotalUncappedStoresDm()); // 5 + 8
            assertEquals(1, t.getTotalStarterFrames()); // Only frame1 is starter
            return Completable.complete();
        });
        when(taxationFrameDao.insertAll(anyList())).thenReturn(Completable.complete());

        // When
        viewModel.createTaxationWithFrames(taxation, frames);

        // Then: Assert was called in mock answer
        verify(taxationDao, times(1)).insert(any(Taxation.class));
    }

    @Test
    void testUpdateTaxation_callsDAO() {
        // Given
        Taxation taxation = new Taxation();
        taxation.setId("tax-123");
        taxation.setHiveId("hive-456");
        taxation.setNotes("Updated notes");

        when(taxationDao.update(any(Taxation.class))).thenReturn(Completable.complete());
        when(taxationDao.getByHiveId(anyString())).thenReturn(Flowable.just(Arrays.asList()));
        when(taxationDao.getByApiaryId(anyString())).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.updateTaxation(taxation);

        // Then
        verify(taxationDao, times(1)).update(taxation);
    }

    @Test
    void testUpdateTaxationWithFrames_preservesHiveId() {
        // Given: CRITICAL TEST - This verifies the bug fix
        Taxation taxation = new Taxation();
        taxation.setId("tax-123");
        taxation.setHiveId("hive-ORIGINAL"); // Original hiveId must be preserved
        taxation.setNotes("Updated notes");

        TaxationFrame frame1 = new TaxationFrame();
        frame1.setId("frame-1");
        frame1.setPosition(1);

        List<TaxationFrame> frames = Arrays.asList(frame1);

        when(taxationDao.update(any(Taxation.class))).thenAnswer(invocation -> {
            Taxation t = invocation.getArgument(0);
            // CRITICAL: Verify hiveId was NOT changed to null or wrong value
            assertEquals("hive-ORIGINAL", t.getHiveId());
            return Completable.complete();
        });
        when(taxationFrameDao.getByTaxationIdOnce(anyString())).thenReturn(Single.just(Arrays.asList()));
        when(taxationFrameDao.insertAll(anyList())).thenReturn(Completable.complete());
        when(taxationDao.getByHiveId(anyString())).thenReturn(Flowable.just(Arrays.asList()));
        when(taxationDao.getByApiaryId(anyString())).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.updateTaxationWithFrames(taxation, frames);

        // Then
        verify(taxationDao, times(1)).update(argThat(t ->
            "hive-ORIGINAL".equals(t.getHiveId())
        ));
    }

    @Test
    void testUpdateTaxationWithFrames_deletesOldFrames() {
        // Given
        Taxation taxation = new Taxation();
        taxation.setId("tax-123");
        taxation.setHiveId("hive-456");

        // Old frames in database
        TaxationFrame oldFrame1 = new TaxationFrame();
        oldFrame1.setId("old-frame-1");
        TaxationFrame oldFrame2 = new TaxationFrame();
        oldFrame2.setId("old-frame-2");
        List<TaxationFrame> oldFrames = Arrays.asList(oldFrame1, oldFrame2);

        // New frames from dialog
        TaxationFrame newFrame1 = new TaxationFrame();
        newFrame1.setId("new-frame-1");
        newFrame1.setPosition(1);
        List<TaxationFrame> newFrames = Arrays.asList(newFrame1);

        when(taxationDao.update(any(Taxation.class))).thenReturn(Completable.complete());
        when(taxationFrameDao.getByTaxationIdOnce(anyString())).thenReturn(Single.just(oldFrames));
        when(taxationFrameDao.delete(any(TaxationFrame.class))).thenReturn(Completable.complete());
        when(taxationFrameDao.insertAll(anyList())).thenReturn(Completable.complete());
        when(taxationDao.getByHiveId(anyString())).thenReturn(Flowable.just(Arrays.asList()));
        when(taxationDao.getByApiaryId(anyString())).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.updateTaxationWithFrames(taxation, newFrames);

        // Then: Old frames should be deleted
        verify(taxationFrameDao, times(2)).delete(any(TaxationFrame.class)); // 2 old frames
        verify(taxationFrameDao, times(1)).insertAll(newFrames); // 1 new frame
    }

    @Test
    void testUpdateTaxationWithFrames_recalculatesAggregates() {
        // Given
        Taxation taxation = new Taxation();
        taxation.setId("tax-123");
        taxation.setHiveId("hive-456");

        TaxationFrame frame1 = new TaxationFrame();
        frame1.setPosition(1);
        frame1.setCappedBroodDm(50);
        frame1.setUncappedBroodDm(25);
        frame1.setPollenDm(12);
        frame1.setStarter(true);

        List<TaxationFrame> frames = Arrays.asList(frame1);

        when(taxationDao.update(any(Taxation.class))).thenAnswer(invocation -> {
            Taxation t = invocation.getArgument(0);
            // Verify aggregates were recalculated
            assertEquals(50, t.getTotalCappedBroodDm());
            assertEquals(25, t.getTotalUncappedBroodDm());
            assertEquals(12, t.getTotalPollenDm());
            assertEquals(1, t.getTotalStarterFrames());
            return Completable.complete();
        });
        when(taxationFrameDao.getByTaxationIdOnce(anyString())).thenReturn(Single.just(Arrays.asList()));
        when(taxationFrameDao.insertAll(anyList())).thenReturn(Completable.complete());

        // When
        viewModel.updateTaxationWithFrames(taxation, frames);

        // Then: Assert was called in mock answer
        verify(taxationDao, times(1)).update(any(Taxation.class));
    }

    @Test
    void testDeleteTaxation_callsDAO() {
        // Given
        Taxation taxation = new Taxation();
        taxation.setId("tax-123");

        when(taxationDao.delete(any(Taxation.class))).thenReturn(Completable.complete());
        when(taxationDao.getByHiveId(anyString())).thenReturn(Flowable.just(Arrays.asList()));
        when(taxationDao.getByApiaryId(anyString())).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.deleteTaxation(taxation);

        // Then
        verify(taxationDao, times(1)).delete(taxation);
    }

    @Test
    void testLoadFramesByTaxationId_callsDAO() {
        // Given
        when(taxationFrameDao.getByTaxationId(anyString())).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.loadFramesByTaxationId("tax-123");

        // Then
        verify(taxationFrameDao, times(1)).getByTaxationId("tax-123");
    }
}
