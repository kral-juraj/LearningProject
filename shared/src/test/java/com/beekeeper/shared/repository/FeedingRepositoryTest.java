package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.FeedingDao;
import com.beekeeper.shared.entity.Feeding;
import io.reactivex.Completable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FeedingRepository.
 */
class FeedingRepositoryTest {

    @Mock
    private FeedingDao feedingDao;

    private FeedingRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new FeedingRepository(feedingDao);
    }

    @Test
    void testInsertFeeding_generatesIdAndTimestamps() {
        Feeding feeding = new Feeding();
        feeding.setHiveId("hive-1");
        feeding.setFeedingDate(System.currentTimeMillis());

        when(feedingDao.insert(any(Feeding.class))).thenReturn(Completable.complete());

        long before = System.currentTimeMillis();
        repository.insertFeeding(feeding).blockingAwait();
        long after = System.currentTimeMillis();

        assertNotNull(feeding.getId());
        assertTrue(feeding.getCreatedAt() >= before);
        verify(feedingDao, times(1)).insert(feeding);
    }

    @Test
    void testUpdateFeeding_callsDao() {
        Feeding feeding = new Feeding();
        feeding.setId("test-id");

        when(feedingDao.update(any(Feeding.class))).thenReturn(Completable.complete());

        repository.updateFeeding(feeding).blockingAwait();

        verify(feedingDao, times(1)).update(feeding);
    }

    @Test
    void testDeleteFeeding_callsDao() {
        Feeding feeding = new Feeding();
        feeding.setId("test-id");

        when(feedingDao.delete(any(Feeding.class))).thenReturn(Completable.complete());

        repository.deleteFeeding(feeding).blockingAwait();

        verify(feedingDao, times(1)).delete(feeding);
    }
}
