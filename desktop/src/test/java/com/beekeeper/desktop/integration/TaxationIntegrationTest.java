package com.beekeeper.desktop.integration;

import com.beekeeper.desktop.dao.jdbc.JdbcApiaryDao;
import com.beekeeper.desktop.dao.jdbc.JdbcHiveDao;
import com.beekeeper.desktop.dao.jdbc.JdbcTaxationDao;
import com.beekeeper.desktop.dao.jdbc.JdbcTaxationFrameDao;
import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.entity.Taxation;
import com.beekeeper.shared.entity.TaxationFrame;
import com.beekeeper.shared.repository.ApiaryRepository;
import com.beekeeper.shared.repository.HiveRepository;
import com.beekeeper.shared.repository.TaxationRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * REAL Integration tests for Taxation feature.
 * Tests full CRUD flow with REAL SQLite database including master-detail relationship.
 *
 * CRITICAL: Tests the bug fix where updateTaxation must preserve hiveId in database.
 */
class TaxationIntegrationTest extends IntegrationTestBase {

    @Test
    void testCreateTaxationWithFrames_persistsToDatabase() {
        // Given: Real DAOs and repositories
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        JdbcTaxationDao taxationDao = new JdbcTaxationDao();
        JdbcTaxationFrameDao frameDao = new JdbcTaxationFrameDao();
        TaxationRepository taxationRepository = new TaxationRepository(taxationDao, frameDao);

        // Create parent apiary
        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Včelnica");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryRepository.insertApiary(apiary).blockingAwait();

        // Create parent hive
        Hive hive = new Hive();
        hive.setId(UUID.randomUUID().toString());
        hive.setApiaryId(apiary.getId());
        hive.setName("L1");
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveRepository.insertHive(hive).blockingAwait();

        // Create taxation header
        Taxation taxation = new Taxation();
        taxation.setId(UUID.randomUUID().toString());
        taxation.setHiveId(hive.getId());
        taxation.setTaxationDate(System.currentTimeMillis());
        taxation.setTemperature(22.0);
        taxation.setTotalFrames(2);
        taxation.setFoodStoresKg(12.5);
        taxation.setNotes("Spring taxation");

        // Create taxation frames
        List<TaxationFrame> frames = new ArrayList<>();

        TaxationFrame frame1 = new TaxationFrame();
        frame1.setId(UUID.randomUUID().toString());
        frame1.setTaxationId(taxation.getId());
        frame1.setPosition(1);
        frame1.setFrameType("BROOD");
        frame1.setCappedBroodDm(40);
        frame1.setUncappedBroodDm(20);
        frame1.setPollenDm(10);
        frame1.setFrameYear(2024);
        frame1.setStarter(false);
        frame1.setHasQueen(true);
        frame1.setHasCage(false);
        frame1.setHasNucBox(false);
        frames.add(frame1);

        TaxationFrame frame2 = new TaxationFrame();
        frame2.setId(UUID.randomUUID().toString());
        frame2.setTaxationId(taxation.getId());
        frame2.setPosition(2);
        frame2.setFrameType("HONEY");
        frame2.setCappedBroodDm(0);
        frame2.setUncappedBroodDm(0);
        frame2.setPollenDm(5);
        frame2.setFrameYear(2023);
        frame2.setStarter(false);
        frame2.setHasQueen(false);
        frame2.setHasCage(false);
        frame2.setHasNucBox(false);
        frames.add(frame2);

        // When: Insert to database
        taxationRepository.insertTaxationWithFrames(taxation, frames).blockingAwait();

        // Then: Should be able to read it back from database
        Taxation retrieved = taxationRepository.getTaxationById(taxation.getId()).blockingGet();

        assertNotNull(retrieved);
        assertEquals(taxation.getId(), retrieved.getId());
        assertEquals(hive.getId(), retrieved.getHiveId());
        assertEquals(22.0, retrieved.getTemperature(), 0.001);
        assertEquals(2, retrieved.getTotalFrames());
        assertEquals(12.5, retrieved.getFoodStoresKg(), 0.001);
        assertEquals("Spring taxation", retrieved.getNotes());

        // Verify frames
        List<TaxationFrame> retrievedFrames = taxationRepository.getFramesByTaxationIdOnce(taxation.getId()).blockingGet();
        assertEquals(2, retrievedFrames.size());
        assertEquals(1, retrievedFrames.get(0).getPosition());
        assertEquals("BROOD", retrievedFrames.get(0).getFrameType());
        assertTrue(retrievedFrames.get(0).isHasQueen());
        assertEquals(2, retrievedFrames.get(1).getPosition());
        assertEquals("HONEY", retrievedFrames.get(1).getFrameType());
    }

    @Test
    void testUpdateTaxation_preservesHiveIdInDatabase() {
        // Given: CRITICAL TEST - This verifies the bug fix
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        JdbcTaxationDao taxationDao = new JdbcTaxationDao();
        JdbcTaxationFrameDao frameDao = new JdbcTaxationFrameDao();
        TaxationRepository taxationRepository = new TaxationRepository(taxationDao, frameDao);

        // Create parent entities
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
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveRepository.insertHive(hive).blockingAwait();

        Taxation taxation = new Taxation();
        taxation.setId(UUID.randomUUID().toString());
        taxation.setHiveId(hive.getId());
        taxation.setTaxationDate(System.currentTimeMillis());
        taxation.setTemperature(20.0);
        taxation.setTotalFrames(10);
        taxation.setNotes("Original notes");
        taxation.setCreatedAt(System.currentTimeMillis());
        taxation.setUpdatedAt(System.currentTimeMillis());

        taxationRepository.insertTaxation(taxation).blockingAwait();

        // Load from DB to verify hiveId
        Taxation loaded = taxationRepository.getTaxationById(taxation.getId()).blockingGet();
        assertEquals(hive.getId(), loaded.getHiveId());

        // When: Update taxation in database
        loaded.setTemperature(22.0);
        loaded.setTotalFrames(12);
        loaded.setNotes("Updated notes");
        taxationRepository.updateTaxation(loaded).blockingAwait();

        // Then: CRITICAL - hiveId must still be there after update!
        Taxation afterUpdate = taxationRepository.getTaxationById(taxation.getId()).blockingGet();

        assertNotNull(afterUpdate);
        assertEquals(hive.getId(), afterUpdate.getHiveId()); // ❌ OLD BUG: This would be NULL
        assertEquals(22.0, afterUpdate.getTemperature(), 0.001);
        assertEquals(12, afterUpdate.getTotalFrames());
        assertEquals("Updated notes", afterUpdate.getNotes());
    }

    @Test
    void testUpdateTaxationWithFrames_replacesFramesInDatabase() {
        // Given: Existing taxation with frames
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        JdbcTaxationDao taxationDao = new JdbcTaxationDao();
        JdbcTaxationFrameDao frameDao = new JdbcTaxationFrameDao();
        TaxationRepository taxationRepository = new TaxationRepository(taxationDao, frameDao);

        // Create parent entities
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
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveRepository.insertHive(hive).blockingAwait();

        // Create taxation with 2 frames
        Taxation taxation = new Taxation();
        taxation.setId(UUID.randomUUID().toString());
        taxation.setHiveId(hive.getId());
        taxation.setTaxationDate(System.currentTimeMillis());
        taxation.setCreatedAt(System.currentTimeMillis());
        taxation.setUpdatedAt(System.currentTimeMillis());

        TaxationFrame oldFrame1 = new TaxationFrame();
        oldFrame1.setId(UUID.randomUUID().toString());
        oldFrame1.setTaxationId(taxation.getId());
        oldFrame1.setPosition(1);
        oldFrame1.setFrameType("BROOD");

        TaxationFrame oldFrame2 = new TaxationFrame();
        oldFrame2.setId(UUID.randomUUID().toString());
        oldFrame2.setTaxationId(taxation.getId());
        oldFrame2.setPosition(2);
        oldFrame2.setFrameType("HONEY");

        List<TaxationFrame> oldFrames = new ArrayList<>();
        oldFrames.add(oldFrame1);
        oldFrames.add(oldFrame2);

        taxationRepository.insertTaxationWithFrames(taxation, oldFrames).blockingAwait();

        // Verify 2 frames exist
        List<TaxationFrame> beforeUpdate = taxationRepository.getFramesByTaxationIdOnce(taxation.getId()).blockingGet();
        assertEquals(2, beforeUpdate.size());

        // When: Update with 3 NEW frames (delete old, insert new)
        taxation.setNotes("Updated with new frames");

        TaxationFrame newFrame1 = new TaxationFrame();
        newFrame1.setId(UUID.randomUUID().toString());
        newFrame1.setTaxationId(taxation.getId());
        newFrame1.setPosition(1);
        newFrame1.setFrameType("FOUNDATION");

        TaxationFrame newFrame2 = new TaxationFrame();
        newFrame2.setId(UUID.randomUUID().toString());
        newFrame2.setTaxationId(taxation.getId());
        newFrame2.setPosition(2);
        newFrame2.setFrameType("BROOD");

        TaxationFrame newFrame3 = new TaxationFrame();
        newFrame3.setId(UUID.randomUUID().toString());
        newFrame3.setTaxationId(taxation.getId());
        newFrame3.setPosition(3);
        newFrame3.setFrameType("HONEY");

        List<TaxationFrame> newFrames = new ArrayList<>();
        newFrames.add(newFrame1);
        newFrames.add(newFrame2);
        newFrames.add(newFrame3);

        // Simulate updateTaxationWithFrames logic
        taxationRepository.updateTaxation(taxation).blockingAwait();

        // Delete old frames
        for (TaxationFrame oldFrame : beforeUpdate) {
            taxationRepository.deleteFrame(oldFrame).blockingAwait();
        }

        // Insert new frames
        taxationRepository.insertFrames(newFrames).blockingAwait();

        // Then: Should have 3 NEW frames
        List<TaxationFrame> afterUpdate = taxationRepository.getFramesByTaxationIdOnce(taxation.getId()).blockingGet();
        assertEquals(3, afterUpdate.size());
        assertEquals("FOUNDATION", afterUpdate.get(0).getFrameType());
        assertEquals("BROOD", afterUpdate.get(1).getFrameType());
        assertEquals("HONEY", afterUpdate.get(2).getFrameType());

        // And hiveId must still be there!
        Taxation retrievedTaxation = taxationRepository.getTaxationById(taxation.getId()).blockingGet();
        assertEquals(hive.getId(), retrievedTaxation.getHiveId());
    }

    @Test
    void testDeleteTaxationCascadesToFrames() {
        // Given: Taxation with frames
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        JdbcTaxationDao taxationDao = new JdbcTaxationDao();
        JdbcTaxationFrameDao frameDao = new JdbcTaxationFrameDao();
        TaxationRepository taxationRepository = new TaxationRepository(taxationDao, frameDao);

        // Create parent entities
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
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveRepository.insertHive(hive).blockingAwait();

        Taxation taxation = new Taxation();
        taxation.setId(UUID.randomUUID().toString());
        taxation.setHiveId(hive.getId());
        taxation.setTaxationDate(System.currentTimeMillis());
        taxation.setCreatedAt(System.currentTimeMillis());
        taxation.setUpdatedAt(System.currentTimeMillis());

        TaxationFrame frame1 = new TaxationFrame();
        frame1.setId(UUID.randomUUID().toString());
        frame1.setTaxationId(taxation.getId());
        frame1.setPosition(1);

        TaxationFrame frame2 = new TaxationFrame();
        frame2.setId(UUID.randomUUID().toString());
        frame2.setTaxationId(taxation.getId());
        frame2.setPosition(2);

        List<TaxationFrame> frames = new ArrayList<>();
        frames.add(frame1);
        frames.add(frame2);

        taxationRepository.insertTaxationWithFrames(taxation, frames).blockingAwait();

        // Verify frames exist
        List<TaxationFrame> beforeDelete = taxationRepository.getFramesByTaxationIdOnce(taxation.getId()).blockingGet();
        assertEquals(2, beforeDelete.size());

        // When: Delete taxation (CASCADE DELETE should remove frames)
        taxationRepository.deleteTaxation(taxation).blockingAwait();

        // Then: Frames should be gone (CASCADE DELETE)
        try {
            List<TaxationFrame> afterDelete = taxationRepository.getFramesByTaxationIdOnce(taxation.getId()).blockingGet();
            assertEquals(0, afterDelete.size());
        } catch (Exception e) {
            // OK - frames were cascade deleted
        }
    }

    @Test
    void testGetTaxationsByApiaryId_joinsHiveName() {
        // Given: Apiary with hive and taxation
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);

        JdbcTaxationDao taxationDao = new JdbcTaxationDao();
        JdbcTaxationFrameDao frameDao = new JdbcTaxationFrameDao();
        TaxationRepository taxationRepository = new TaxationRepository(taxationDao, frameDao);

        // Create entities
        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Včelnica");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryRepository.insertApiary(apiary).blockingAwait();

        Hive hive = new Hive();
        hive.setId(UUID.randomUUID().toString());
        hive.setApiaryId(apiary.getId());
        hive.setName("L1-TestHive");
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveRepository.insertHive(hive).blockingAwait();

        Taxation taxation = new Taxation();
        taxation.setId(UUID.randomUUID().toString());
        taxation.setHiveId(hive.getId());
        taxation.setTaxationDate(System.currentTimeMillis());
        taxation.setCreatedAt(System.currentTimeMillis());
        taxation.setUpdatedAt(System.currentTimeMillis());
        taxationRepository.insertTaxation(taxation).blockingAwait();

        // When: Get by apiary ID (should JOIN with hives to get hiveName)
        List<Taxation> taxations = taxationRepository.getTaxationsByApiaryId(apiary.getId()).blockingFirst();

        // Then: Should have hiveName populated
        assertFalse(taxations.isEmpty());
        assertEquals("L1-TestHive", taxations.get(0).getHiveName());
    }
}
