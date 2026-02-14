package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.ApiaryDao;
import com.beekeeper.shared.entity.Apiary;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ApiaryRepository.
 * Tests business logic with mocked DAO.
 */
class ApiaryRepositoryTest {

    @Mock
    private ApiaryDao apiaryDao;

    private ApiaryRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new ApiaryRepository(apiaryDao);
    }

    @Test
    void testInsertApiary_generatesIdWhenMissing() {
        // Given: Apiary without ID
        Apiary apiary = new Apiary();
        apiary.setName("Test Včelnica");

        when(apiaryDao.insert(any(Apiary.class))).thenReturn(Completable.complete());

        // When: Insert apiary
        repository.insertApiary(apiary).blockingAwait();

        // Then: ID should be generated
        assertNotNull(apiary.getId());
        assertFalse(apiary.getId().isEmpty());
    }

    @Test
    void testInsertApiary_keepsExistingId() {
        // Given: Apiary with existing ID
        Apiary apiary = new Apiary();
        apiary.setId("existing-id");
        apiary.setName("Test Včelnica");

        when(apiaryDao.insert(any(Apiary.class))).thenReturn(Completable.complete());

        // When: Insert apiary
        repository.insertApiary(apiary).blockingAwait();

        // Then: ID should remain unchanged
        assertEquals("existing-id", apiary.getId());
    }

    @Test
    void testInsertApiary_setsCreatedAtTimestamp() {
        // Given: Apiary without createdAt
        Apiary apiary = new Apiary();
        apiary.setName("Test Včelnica");

        when(apiaryDao.insert(any(Apiary.class))).thenReturn(Completable.complete());

        long before = System.currentTimeMillis();

        // When: Insert apiary
        repository.insertApiary(apiary).blockingAwait();

        long after = System.currentTimeMillis();

        // Then: createdAt should be set to current timestamp
        assertTrue(apiary.getCreatedAt() >= before);
        assertTrue(apiary.getCreatedAt() <= after);
    }

    @Test
    void testInsertApiary_setsUpdatedAtTimestamp() {
        // Given: Apiary
        Apiary apiary = new Apiary();
        apiary.setName("Test Včelnica");

        when(apiaryDao.insert(any(Apiary.class))).thenReturn(Completable.complete());

        long before = System.currentTimeMillis();

        // When: Insert apiary
        repository.insertApiary(apiary).blockingAwait();

        long after = System.currentTimeMillis();

        // Then: updatedAt should be set to current timestamp
        assertTrue(apiary.getUpdatedAt() >= before);
        assertTrue(apiary.getUpdatedAt() <= after);
    }

    @Test
    void testInsertApiary_callsDaoInsert() {
        // Given: Apiary
        Apiary apiary = new Apiary();
        apiary.setName("Test Včelnica");

        when(apiaryDao.insert(any(Apiary.class))).thenReturn(Completable.complete());

        // When: Insert apiary
        repository.insertApiary(apiary).blockingAwait();

        // Then: DAO insert should be called once
        verify(apiaryDao, times(1)).insert(apiary);
    }

    @Test
    void testUpdateApiary_updatesTimestamp() {
        // Given: Existing apiary
        Apiary apiary = new Apiary();
        apiary.setId("test-id");
        apiary.setName("Test Včelnica");
        apiary.setCreatedAt(1000000000L);
        apiary.setUpdatedAt(1000000000L);

        when(apiaryDao.update(any(Apiary.class))).thenReturn(Completable.complete());

        long before = System.currentTimeMillis();

        // When: Update apiary
        repository.updateApiary(apiary).blockingAwait();

        long after = System.currentTimeMillis();

        // Then: updatedAt should be updated to current timestamp
        assertTrue(apiary.getUpdatedAt() >= before);
        assertTrue(apiary.getUpdatedAt() <= after);
        assertTrue(apiary.getUpdatedAt() > apiary.getCreatedAt());
    }

    @Test
    void testUpdateApiary_callsDaoUpdate() {
        // Given: Apiary
        Apiary apiary = new Apiary();
        apiary.setId("test-id");
        apiary.setName("Updated Včelnica");

        when(apiaryDao.update(any(Apiary.class))).thenReturn(Completable.complete());

        // When: Update apiary
        repository.updateApiary(apiary).blockingAwait();

        // Then: DAO update should be called once
        verify(apiaryDao, times(1)).update(apiary);
    }

    @Test
    void testDeleteApiary_callsDaoDelete() {
        // Given: Apiary
        Apiary apiary = new Apiary();
        apiary.setId("test-id");

        when(apiaryDao.delete(any(Apiary.class))).thenReturn(Completable.complete());

        // When: Delete apiary
        repository.deleteApiary(apiary).blockingAwait();

        // Then: DAO delete should be called once
        verify(apiaryDao, times(1)).delete(apiary);
    }

    @Test
    void testGetApiaryById_callsDaoGetById() {
        // Given: Apiary ID
        String id = "test-id";
        Apiary apiary = new Apiary();
        apiary.setId(id);
        apiary.setName("Test Včelnica");

        when(apiaryDao.getById(id)).thenReturn(Single.just(apiary));

        // When: Get apiary by ID
        Apiary result = repository.getApiaryById(id).blockingGet();

        // Then: Should return the apiary
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test Včelnica", result.getName());
        verify(apiaryDao, times(1)).getById(id);
    }

    @Test
    void testGetAllApiaries_callsDaoGetAll() {
        // Given: Multiple apiaries
        Apiary apiary1 = new Apiary();
        apiary1.setId("id1");
        apiary1.setName("Včelnica 1");

        Apiary apiary2 = new Apiary();
        apiary2.setId("id2");
        apiary2.setName("Včelnica 2");

        List<Apiary> apiaries = Arrays.asList(apiary1, apiary2);

        when(apiaryDao.getAll()).thenReturn(Flowable.just(apiaries));

        // When: Get all apiaries
        List<Apiary> result = repository.getAllApiaries().blockingFirst();

        // Then: Should return all apiaries
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Včelnica 1", result.get(0).getName());
        assertEquals("Včelnica 2", result.get(1).getName());
        verify(apiaryDao, times(1)).getAll();
    }

    @Test
    void testGetAllApiariesOnce_callsDaoGetAllOnce() {
        // Given: Multiple apiaries
        Apiary apiary1 = new Apiary();
        apiary1.setId("id1");
        Apiary apiary2 = new Apiary();
        apiary2.setId("id2");

        List<Apiary> apiaries = Arrays.asList(apiary1, apiary2);

        when(apiaryDao.getAllOnce()).thenReturn(Single.just(apiaries));

        // When: Get all apiaries once
        List<Apiary> result = repository.getAllApiariesOnce().blockingGet();

        // Then: Should return all apiaries
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(apiaryDao, times(1)).getAllOnce();
    }

    @Test
    void testGetApiaryCount_callsDaoGetCount() {
        // Given: Count
        when(apiaryDao.getCount()).thenReturn(Single.just(5));

        // When: Get apiary count
        int count = repository.getApiaryCount().blockingGet();

        // Then: Should return count
        assertEquals(5, count);
        verify(apiaryDao, times(1)).getCount();
    }
}
