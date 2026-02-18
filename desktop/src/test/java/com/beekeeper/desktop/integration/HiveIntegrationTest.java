package com.beekeeper.desktop.integration;

import com.beekeeper.desktop.dao.jdbc.JdbcApiaryDao;
import com.beekeeper.desktop.dao.jdbc.JdbcHiveDao;
import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.repository.ApiaryRepository;
import com.beekeeper.shared.repository.HiveRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Hive CRUD operations.
 * Tests full database lifecycle including foreign key constraints.
 */
class HiveIntegrationTest extends IntegrationTestBase {

    @Test
    void testCreateAndReadHive() {
        // Given: Apiary and hive DAO/repository
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        // Create parent apiary first
        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Včelnica");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryRepository.insertApiary(apiary).blockingAwait();

        // When: Create hive
        Hive hive = new Hive();
        hive.setId(UUID.randomUUID().toString());
        hive.setApiaryId(apiary.getId());
        hive.setName("L1");
        hive.setType("LANGSTROTH");
        hive.setQueenYear(2024);
        hive.setActive(true);
        hive.setNotes("Test úľ");
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());

        hiveRepository.insertHive(hive).blockingAwait();

        // Then: Should be able to read it back
        Hive retrieved = hiveRepository.getHiveById(hive.getId()).blockingGet();

        assertNotNull(retrieved);
        assertEquals(hive.getId(), retrieved.getId());
        assertEquals(apiary.getId(), retrieved.getApiaryId());
        assertEquals("L1", retrieved.getName());
        assertEquals("LANGSTROTH", retrieved.getType());
        assertEquals(2024, retrieved.getQueenYear());
        assertTrue(retrieved.isActive());
        assertEquals("Test úľ", retrieved.getNotes());
    }

    @Test
    void testCreateMultipleHivesInApiary() {
        // Given: Apiary and DAOs
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        // Create apiary
        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Včelnica");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryRepository.insertApiary(apiary).blockingAwait();

        // When: Create 3 hives in apiary
        for (int i = 1; i <= 3; i++) {
            Hive hive = new Hive();
            hive.setId(UUID.randomUUID().toString());
            hive.setApiaryId(apiary.getId());
            hive.setName("L" + i);
            hive.setType("LANGSTROTH");
            hive.setActive(true);
            hive.setCreatedAt(System.currentTimeMillis());
            hive.setUpdatedAt(System.currentTimeMillis());
            hiveRepository.insertHive(hive).blockingAwait();
        }

        // Then: Should retrieve all 3 hives for this apiary
        List<Hive> hives = hiveRepository.getHivesByApiaryId(apiary.getId()).blockingFirst();

        assertNotNull(hives);
        assertEquals(3, hives.size());
        assertEquals("L1", hives.get(0).getName());
        assertEquals("L2", hives.get(1).getName());
        assertEquals("L3", hives.get(2).getName());
    }

    @Test
    void testUpdateHive() {
        // Given: Existing hive
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Včelnica");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryRepository.insertApiary(apiary).blockingAwait();

        Hive hive = new Hive();
        hive.setId(UUID.randomUUID().toString());
        hive.setApiaryId(apiary.getId());
        hive.setName("L1");
        hive.setQueenYear(2023);
        hive.setActive(true);
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveRepository.insertHive(hive).blockingAwait();

        // When: Update hive
        hive.setName("L1-Updated");
        hive.setQueenYear(2024);
        hive.setActive(false);
        hive.setNotes("Updated notes");
        hiveRepository.updateHive(hive).blockingAwait();

        // Then: Changes should be persisted
        Hive retrieved = hiveRepository.getHiveById(hive.getId()).blockingGet();

        assertNotNull(retrieved);
        assertEquals("L1-Updated", retrieved.getName());
        assertEquals(2024, retrieved.getQueenYear());
        assertFalse(retrieved.isActive());
        assertEquals("Updated notes", retrieved.getNotes());
    }

    @Test
    void testDeleteHive() {
        // Given: Existing hive
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Včelnica");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryRepository.insertApiary(apiary).blockingAwait();

        Hive hive = new Hive();
        hive.setId(UUID.randomUUID().toString());
        hive.setApiaryId(apiary.getId());
        hive.setName("To Delete");
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveRepository.insertHive(hive).blockingAwait();

        // Verify it exists
        List<Hive> beforeDelete = hiveRepository.getHivesByApiaryId(apiary.getId()).blockingFirst();
        assertEquals(1, beforeDelete.size());

        // When: Delete hive
        hiveRepository.deleteHive(hive).blockingAwait();

        // Then: Should be removed from database
        List<Hive> afterDelete = hiveRepository.getHivesByApiaryId(apiary.getId()).blockingFirst();
        assertEquals(0, afterDelete.size());
    }

    @Test
    void testCascadeDeleteApiaryRemovesHives() {
        // Given: Apiary with multiple hives
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Včelnica na zmazanie");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryRepository.insertApiary(apiary).blockingAwait();

        // Create 3 hives
        for (int i = 1; i <= 3; i++) {
            Hive hive = new Hive();
            hive.setId(UUID.randomUUID().toString());
            hive.setApiaryId(apiary.getId());
            hive.setName("L" + i);
            hive.setCreatedAt(System.currentTimeMillis());
            hive.setUpdatedAt(System.currentTimeMillis());
            hiveRepository.insertHive(hive).blockingAwait();
        }

        // Verify hives exist
        List<Hive> hivesBeforeDelete = hiveRepository.getHivesByApiaryId(apiary.getId()).blockingFirst();
        assertEquals(3, hivesBeforeDelete.size());

        // When: Delete apiary (CASCADE DELETE should remove hives)
        apiaryRepository.deleteApiary(apiary).blockingAwait();

        // Then: All hives should be removed (CASCADE DELETE)
        List<Hive> hivesAfterDelete = hiveRepository.getHivesByApiaryId(apiary.getId()).blockingFirst();
        assertEquals(0, hivesAfterDelete.size());
    }

    /**
     * Test: Create and read hive with hasVarroaScreen field.
     *
     * Use case: Beekeeper adds Varroa screen to hive for mite monitoring.
     * Expected: hasVarroaScreen persisted correctly through full stack.
     */
    @Test
    void testCreateAndReadHiveWithVarroaScreen() {
        // Given: DAO and repository
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);
        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        // Create test apiary
        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Včelnica");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryRepository.insertApiary(apiary).blockingAwait();

        // When: Create hive with hasVarroaScreen = true
        Hive hive = new Hive();
        hive.setId(UUID.randomUUID().toString());
        hive.setApiaryId(apiary.getId());
        hive.setName("Test Hive");
        hive.setType("VERTICAL");
        hive.setActive(true);
        hive.setHasVarroaScreen(true);
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveRepository.insertHive(hive).blockingAwait();

        // Then: Should be able to read it back with hasVarroaScreen = true
        Hive retrieved = hiveRepository.getHiveById(hive.getId()).blockingGet();

        assertNotNull(retrieved);
        assertEquals(hive.getId(), retrieved.getId());
        assertTrue(retrieved.isHasVarroaScreen());
    }

    /**
     * Test: Update hive hasVarroaScreen field.
     *
     * Use case: Beekeeper adds or removes Varroa screen from hive.
     * Expected: hasVarroaScreen updated correctly through full stack.
     */
    @Test
    void testUpdateHiveVarroaScreen() {
        // Given: DAO and repository
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);
        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        // Create test apiary
        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Včelnica");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryRepository.insertApiary(apiary).blockingAwait();

        // Create hive without Varroa screen
        Hive hive = new Hive();
        hive.setId(UUID.randomUUID().toString());
        hive.setApiaryId(apiary.getId());
        hive.setName("Test Hive");
        hive.setType("VERTICAL");
        hive.setActive(true);
        hive.setHasVarroaScreen(false);
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveRepository.insertHive(hive).blockingAwait();

        // When: Update to add Varroa screen
        hive.setHasVarroaScreen(true);
        hiveRepository.updateHive(hive).blockingAwait();

        // Then: Changes should be persisted
        Hive retrieved = hiveRepository.getHiveById(hive.getId()).blockingGet();

        assertNotNull(retrieved);
        assertTrue(retrieved.isHasVarroaScreen());
    }

}
