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

    /**
     * Test: Create and read apiary with extended fields.
     *
     * Use case: User creates apiary with all optional details filled in.
     * Expected: All extended fields (displayOrder, registrationNumber, address, description) persisted correctly.
     */
    @Test
    void testCreateAndReadApiaryWithExtendedFields() {
        // Given: DAO and repository
        JdbcApiaryDao dao = new JdbcApiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);

        // When: Create apiary with extended fields
        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Včelnica");
        apiary.setLocation("Bratislava");
        apiary.setLatitude(48.1486);
        apiary.setLongitude(17.1077);
        apiary.setDisplayOrder(5);
        apiary.setRegistrationNumber("REG-12345");
        apiary.setAddress("Hlavná 123, Bratislava");
        apiary.setDescription("Testovacia včelnica s poznámkami");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());

        repository.insertApiary(apiary).blockingAwait();

        // Then: Should be able to read it back with all extended fields
        Apiary retrieved = repository.getApiaryById(apiary.getId()).blockingGet();

        assertNotNull(retrieved);
        assertEquals(apiary.getId(), retrieved.getId());
        assertEquals("Test Včelnica", retrieved.getName());
        assertEquals(5, retrieved.getDisplayOrder());
        assertEquals("REG-12345", retrieved.getRegistrationNumber());
        assertEquals("Hlavná 123, Bratislava", retrieved.getAddress());
        assertEquals("Testovacia včelnica s poznámkami", retrieved.getDescription());
    }

    /**
     * Test: Update apiary order via repository.
     *
     * Use case: User drags apiaries to reorder them.
     * Expected: All apiaries updated with new displayOrder values, retrievable in new order.
     */
    @Test
    void testUpdateApiaryOrder() {
        // Given: DAO and repository
        JdbcApiaryDao dao = new JdbcApiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);

        // Create 3 apiaries with initial order
        Apiary apiary1 = new Apiary();
        apiary1.setId(UUID.randomUUID().toString());
        apiary1.setName("Včelnica 1");
        apiary1.setDisplayOrder(0);
        apiary1.setCreatedAt(System.currentTimeMillis());
        apiary1.setUpdatedAt(System.currentTimeMillis());
        repository.insertApiary(apiary1).blockingAwait();

        Apiary apiary2 = new Apiary();
        apiary2.setId(UUID.randomUUID().toString());
        apiary2.setName("Včelnica 2");
        apiary2.setDisplayOrder(1);
        apiary2.setCreatedAt(System.currentTimeMillis());
        apiary2.setUpdatedAt(System.currentTimeMillis());
        repository.insertApiary(apiary2).blockingAwait();

        Apiary apiary3 = new Apiary();
        apiary3.setId(UUID.randomUUID().toString());
        apiary3.setName("Včelnica 3");
        apiary3.setDisplayOrder(2);
        apiary3.setCreatedAt(System.currentTimeMillis());
        apiary3.setUpdatedAt(System.currentTimeMillis());
        repository.insertApiary(apiary3).blockingAwait();

        // When: Reorder apiaries (swap first and last)
        apiary1.setDisplayOrder(2);
        apiary3.setDisplayOrder(0);

        List<Apiary> apiaries = java.util.Arrays.asList(apiary1, apiary2, apiary3);
        repository.updateApiaryOrder(apiaries).blockingAwait();

        // Then: Should be retrievable in new order
        List<Apiary> retrieved = repository.getAllApiaries().blockingFirst();

        assertEquals(3, retrieved.size());
        assertEquals("Včelnica 3", retrieved.get(0).getName()); // Now first (displayOrder = 0)
        assertEquals("Včelnica 2", retrieved.get(1).getName()); // Still middle (displayOrder = 1)
        assertEquals("Včelnica 1", retrieved.get(2).getName()); // Now last (displayOrder = 2)
    }

    /**
     * Test: Apiary ordering in getAll.
     *
     * Use case: Display apiaries in user-defined order.
     * Expected: Apiaries sorted by displayOrder ascending, then by name.
     */
    @Test
    void testApiaryOrderingInGetAll() {
        // Given: DAO and repository
        JdbcApiaryDao dao = new JdbcApiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);

        // Create apiaries with different display orders
        for (int i = 3; i >= 1; i--) { // Insert in reverse order
            Apiary apiary = new Apiary();
            apiary.setId(UUID.randomUUID().toString());
            apiary.setName("Včelnica " + i);
            apiary.setDisplayOrder(i);
            apiary.setCreatedAt(System.currentTimeMillis());
            apiary.setUpdatedAt(System.currentTimeMillis());
            repository.insertApiary(apiary).blockingAwait();
        }

        // When: Get all apiaries
        List<Apiary> apiaries = repository.getAllApiaries().blockingFirst();

        // Then: Should be sorted by displayOrder
        assertEquals(3, apiaries.size());
        assertEquals("Včelnica 1", apiaries.get(0).getName());
        assertEquals("Včelnica 2", apiaries.get(1).getName());
        assertEquals("Včelnica 3", apiaries.get(2).getName());
    }
}
