package com.beekeeper.desktop.integration;

import com.beekeeper.desktop.dao.jdbc.JdbcApiaryDao;
import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.repository.ApiaryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Apiary CRUD operations.
 * Tests full database lifecycle with in-memory SQLite.
 */
class ApiaryIntegrationTest extends IntegrationTestBase {

    @Test
    void testCreateAndReadApiary() {
        // Given: DAO and repository
        JdbcApiaryDao dao = new JdbcApiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);

        // When: Create apiary
        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Včelnica");
        apiary.setLocation("Bratislava");
        apiary.setLatitude(48.1486);
        apiary.setLongitude(17.1077);
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());

        repository.insertApiary(apiary).blockingAwait();

        // Then: Should be able to read it back
        Apiary retrieved = repository.getApiaryById(apiary.getId()).blockingGet();

        assertNotNull(retrieved);
        assertEquals(apiary.getId(), retrieved.getId());
        assertEquals("Test Včelnica", retrieved.getName());
        assertEquals("Bratislava", retrieved.getLocation());
        assertEquals(48.1486, retrieved.getLatitude(), 0.0001);
        assertEquals(17.1077, retrieved.getLongitude(), 0.0001);
        assertTrue(retrieved.getCreatedAt() > 0);
        assertTrue(retrieved.getUpdatedAt() > 0);
    }

    @Test
    void testCreateMultipleApiaries() {
        // Given: DAO and repository
        JdbcApiaryDao dao = new JdbcApiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);

        // When: Create multiple apiaries
        for (int i = 1; i <= 3; i++) {
            Apiary apiary = new Apiary();
            apiary.setId(UUID.randomUUID().toString());
            apiary.setName("Včelnica " + i);
            apiary.setLocation("Lokalita " + i);
            apiary.setCreatedAt(System.currentTimeMillis());
            apiary.setUpdatedAt(System.currentTimeMillis());
            repository.insertApiary(apiary).blockingAwait();
        }

        // Then: Should be able to retrieve all
        List<Apiary> apiaries = repository.getAllApiaries().blockingFirst();

        assertNotNull(apiaries);
        assertEquals(3, apiaries.size());
        assertEquals("Včelnica 1", apiaries.get(0).getName());
        assertEquals("Včelnica 2", apiaries.get(1).getName());
        assertEquals("Včelnica 3", apiaries.get(2).getName());
    }

    @Test
    void testUpdateApiary() throws InterruptedException {
        // Given: Existing apiary
        JdbcApiaryDao dao = new JdbcApiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);

        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Original Name");
        apiary.setLocation("Original Location");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());

        repository.insertApiary(apiary).blockingAwait();

        // When: Update apiary
        long updatedAtBefore = apiary.getUpdatedAt();
        Thread.sleep(10); // Ensure timestamp changes

        apiary.setName("Updated Name");
        apiary.setLocation("Updated Location");
        repository.updateApiary(apiary).blockingAwait();

        // Then: Changes should be persisted
        Apiary retrieved = repository.getApiaryById(apiary.getId()).blockingGet();

        assertNotNull(retrieved);
        assertEquals("Updated Name", retrieved.getName());
        assertEquals("Updated Location", retrieved.getLocation());
        assertTrue(retrieved.getUpdatedAt() > updatedAtBefore);
    }

    @Test
    void testDeleteApiary() {
        // Given: Existing apiary
        JdbcApiaryDao dao = new JdbcApiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);

        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("To Be Deleted");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());

        repository.insertApiary(apiary).blockingAwait();

        // Verify it exists
        List<Apiary> beforeDelete = repository.getAllApiaries().blockingFirst();
        assertEquals(1, beforeDelete.size());

        // When: Delete apiary
        repository.deleteApiary(apiary).blockingAwait();

        // Then: Should be removed from database
        List<Apiary> afterDelete = repository.getAllApiaries().blockingFirst();
        assertEquals(0, afterDelete.size());
    }

    @Test
    void testGetApiaryCount() {
        // Given: Multiple apiaries
        JdbcApiaryDao dao = new JdbcApiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);

        for (int i = 1; i <= 5; i++) {
            Apiary apiary = new Apiary();
            apiary.setId(UUID.randomUUID().toString());
            apiary.setName("Včelnica " + i);
            apiary.setCreatedAt(System.currentTimeMillis());
            apiary.setUpdatedAt(System.currentTimeMillis());
            repository.insertApiary(apiary).blockingAwait();
        }

        // When: Get count
        int count = repository.getApiaryCount().blockingGet();

        // Then: Should return correct count
        assertEquals(5, count);
    }

    @Test
    void testRepositoryGeneratesIdAndTimestamps() {
        // Given: Apiary without ID or timestamps
        JdbcApiaryDao dao = new JdbcApiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);

        Apiary apiary = new Apiary();
        apiary.setName("Auto ID Včelnica");
        // No ID, no timestamps set

        long before = System.currentTimeMillis();

        // When: Insert via repository
        repository.insertApiary(apiary).blockingAwait();

        long after = System.currentTimeMillis();

        // Then: Repository should generate ID and timestamps
        assertNotNull(apiary.getId());
        assertFalse(apiary.getId().isEmpty());
        assertTrue(apiary.getCreatedAt() >= before);
        assertTrue(apiary.getCreatedAt() <= after);
        assertTrue(apiary.getUpdatedAt() >= before);
        assertTrue(apiary.getUpdatedAt() <= after);

        // Verify in database
        Apiary retrieved = repository.getApiaryById(apiary.getId()).blockingGet();
        assertNotNull(retrieved);
        assertEquals(apiary.getId(), retrieved.getId());
    }
}
