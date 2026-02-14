package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.HiveDao;
import com.beekeeper.shared.entity.Hive;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HiveRepository.
 * Tests business logic with mocked DAO.
 */
class HiveRepositoryTest {

    @Mock
    private HiveDao hiveDao;

    private HiveRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new HiveRepository(hiveDao);
    }

    @Test
    void testInsertHive_generatesIdWhenMissing() {
        Hive hive = new Hive();
        hive.setName("L1");

        when(hiveDao.insert(any(Hive.class))).thenReturn(Completable.complete());

        repository.insertHive(hive).blockingAwait();

        assertNotNull(hive.getId());
        assertFalse(hive.getId().isEmpty());
    }

    @Test
    void testInsertHive_setsTimestamps() {
        Hive hive = new Hive();
        hive.setName("L1");

        when(hiveDao.insert(any(Hive.class))).thenReturn(Completable.complete());

        long before = System.currentTimeMillis();
        repository.insertHive(hive).blockingAwait();
        long after = System.currentTimeMillis();

        assertTrue(hive.getCreatedAt() >= before);
        assertTrue(hive.getCreatedAt() <= after);
        assertTrue(hive.getUpdatedAt() >= before);
        assertTrue(hive.getUpdatedAt() <= after);
    }

    @Test
    void testInsertHive_callsDaoInsert() {
        Hive hive = new Hive();
        hive.setName("L1");

        when(hiveDao.insert(any(Hive.class))).thenReturn(Completable.complete());

        repository.insertHive(hive).blockingAwait();

        verify(hiveDao, times(1)).insert(hive);
    }

    @Test
    void testUpdateHive_updatesTimestamp() {
        Hive hive = new Hive();
        hive.setId("test-id");
        hive.setName("L1");
        hive.setCreatedAt(1000000000L);
        hive.setUpdatedAt(1000000000L);

        when(hiveDao.update(any(Hive.class))).thenReturn(Completable.complete());

        long before = System.currentTimeMillis();
        repository.updateHive(hive).blockingAwait();
        long after = System.currentTimeMillis();

        assertTrue(hive.getUpdatedAt() >= before);
        assertTrue(hive.getUpdatedAt() <= after);
        assertTrue(hive.getUpdatedAt() > hive.getCreatedAt());
    }

    @Test
    void testDeleteHive_callsDaoDelete() {
        Hive hive = new Hive();
        hive.setId("test-id");

        when(hiveDao.delete(any(Hive.class))).thenReturn(Completable.complete());

        repository.deleteHive(hive).blockingAwait();

        verify(hiveDao, times(1)).delete(hive);
    }

    @Test
    void testGetHiveById_callsDaoGetById() {
        String id = "test-id";
        Hive hive = new Hive();
        hive.setId(id);

        when(hiveDao.getById(id)).thenReturn(Single.just(hive));

        Hive result = repository.getHiveById(id).blockingGet();

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(hiveDao, times(1)).getById(id);
    }

    @Test
    void testGetHivesByApiaryId_callsDaoGetByApiaryId() {
        String apiaryId = "apiary-1";

        when(hiveDao.getByApiaryId(apiaryId)).thenReturn(Flowable.just(Arrays.asList()));

        repository.getHivesByApiaryId(apiaryId).blockingFirst();

        verify(hiveDao, times(1)).getByApiaryId(apiaryId);
    }
}
