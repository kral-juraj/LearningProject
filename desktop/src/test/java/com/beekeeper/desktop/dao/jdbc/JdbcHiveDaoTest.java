package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.entity.Hive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JdbcHiveDao focusing on new displayOrder and health indicator fields.
 *
 * Tests the persistence and retrieval of:
 * - displayOrder field for drag-and-drop ordering
 * - aggression level (LOW/MEDIUM/HIGH)
 * - chalkBrood, droneCells, droneLaying health indicators
 */
class JdbcHiveDaoTest {

    private JdbcHiveDao dao;
    private String testApiaryId;

    @BeforeEach
    void setUp() throws Exception {
        // Use file-based test database (in-memory doesn't persist connections properly)
        String testDbPath = System.getProperty("java.io.tmpdir") + "/test_hive_dao_" + System.currentTimeMillis() + ".db";
        DatabaseManager.initialize(testDbPath);
        dao = new JdbcHiveDao();

        // Create test apiary (required for foreign key constraint)
        testApiaryId = UUID.randomUUID().toString();
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        Apiary apiary = new Apiary();
        apiary.setId(testApiaryId);
        apiary.setName("Test Apiary");
        apiary.setLocation("Test Location");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryDao.insert(apiary).blockingAwait();
    }

    /**
     * Test: Insert hive with displayOrder field.
     *
     * Use case: User creates new hive, should be assigned displayOrder for sorting.
     * Expected: Hive persisted with displayOrder value.
     */
    @Test
    void testInsertHiveWithDisplayOrder() {
        Hive hive = createTestHive("Test Hive");
        hive.setDisplayOrder(5);

        dao.insert(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertEquals(5, retrieved.getDisplayOrder());
    }

    /**
     * Test: Insert hive with aggression level.
     *
     * Use case: Beekeeper records colony aggression during inspection.
     * Expected: Aggression level persisted correctly.
     */
    @Test
    void testInsertHiveWithAggression() {
        Hive hive = createTestHive("Aggressive Colony");
        hive.setAggression("HIGH");

        dao.insert(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertEquals("HIGH", retrieved.getAggression());
    }

    /**
     * Test: Insert hive with health indicators.
     *
     * Use case: Beekeeper identifies chalk brood and drone cells during inspection.
     * Expected: All health indicators persisted correctly.
     */
    @Test
    void testInsertHiveWithHealthIndicators() {
        Hive hive = createTestHive("Sick Colony");
        hive.setChalkBrood(true);
        hive.setDroneCells(true);
        hive.setDroneLaying(false);

        dao.insert(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertTrue(retrieved.isChalkBrood());
        assertTrue(retrieved.isDroneCells());
        assertFalse(retrieved.isDroneLaying());
    }

    /**
     * Test: Update hive displayOrder.
     *
     * Use case: User drags hive to new position, displayOrder updated.
     * Expected: DisplayOrder changes persisted.
     */
    @Test
    void testUpdateHiveDisplayOrder() {
        Hive hive = createTestHive("Test Hive");
        hive.setDisplayOrder(0);
        dao.insert(hive).blockingAwait();

        hive.setDisplayOrder(10);
        dao.update(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertEquals(10, retrieved.getDisplayOrder());
    }

    /**
     * Test: Retrieve hives sorted by displayOrder.
     *
     * Use case: Display hives in user-defined order after drag-and-drop.
     * Expected: Hives returned sorted by displayOrder ascending, then by name.
     */
    @Test
    void testGetByApiaryIdSortedByDisplayOrder() {
        // Create hives with different displayOrder values
        Hive hive1 = createTestHive("Ležan");
        hive1.setDisplayOrder(2);
        dao.insert(hive1).blockingAwait();

        Hive hive2 = createTestHive("Oddielok");
        hive2.setDisplayOrder(0);
        dao.insert(hive2).blockingAwait();

        Hive hive3 = createTestHive("Úľ 3");
        hive3.setDisplayOrder(1);
        dao.insert(hive3).blockingAwait();

        List<Hive> hives = dao.getByApiaryId(testApiaryId).blockingFirst();

        assertEquals(3, hives.size());
        assertEquals("Oddielok", hives.get(0).getName()); // displayOrder = 0
        assertEquals("Úľ 3", hives.get(1).getName());     // displayOrder = 1
        assertEquals("Ležan", hives.get(2).getName());    // displayOrder = 2
    }

    /**
     * Test: Batch update hive order using insertAll.
     *
     * Use case: After drag-and-drop, update multiple hives' displayOrder at once.
     * Expected: All hives updated with new displayOrder values.
     */
    @Test
    void testInsertAllUpdatesDisplayOrder() {
        // Create 3 hives
        List<Hive> hives = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Hive hive = createTestHive("Hive " + i);
            hive.setDisplayOrder(i);
            hives.add(hive);
        }
        dao.insertAll(hives).blockingAwait();

        // Reorder: swap first and last
        hives.get(0).setDisplayOrder(2);
        hives.get(2).setDisplayOrder(0);
        dao.insertAll(hives).blockingAwait();

        List<Hive> retrieved = dao.getByApiaryId(testApiaryId).blockingFirst();
        assertEquals("Hive 2", retrieved.get(0).getName()); // Now first
        assertEquals("Hive 1", retrieved.get(1).getName()); // Still middle
        assertEquals("Hive 0", retrieved.get(2).getName()); // Now last
    }

    /**
     * Test: Update aggression level.
     *
     * Use case: Colony becomes more aggressive over time, beekeeper updates level.
     * Expected: Aggression level updated correctly.
     */
    @Test
    void testUpdateAggression() {
        Hive hive = createTestHive("Test Hive");
        hive.setAggression("LOW");
        dao.insert(hive).blockingAwait();

        hive.setAggression("HIGH");
        dao.update(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertEquals("HIGH", retrieved.getAggression());
    }

    /**
     * Test: Update health indicators.
     *
     * Use case: Chalk brood clears up after treatment, beekeeper updates status.
     * Expected: Health indicators updated correctly.
     */
    @Test
    void testUpdateHealthIndicators() {
        Hive hive = createTestHive("Test Hive");
        hive.setChalkBrood(true);
        hive.setDroneCells(false);
        dao.insert(hive).blockingAwait();

        hive.setChalkBrood(false);
        hive.setDroneCells(true);
        dao.update(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertFalse(retrieved.isChalkBrood());
        assertTrue(retrieved.isDroneCells());
    }

    /**
     * Test: Insert hive with null aggression.
     *
     * Use case: Beekeeper doesn't record aggression level.
     * Expected: NULL stored in database, retrieved as null.
     */
    @Test
    void testInsertHiveWithNullAggression() {
        Hive hive = createTestHive("Test Hive");
        hive.setAggression(null);

        dao.insert(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertNull(retrieved.getAggression());
    }

    /**
     * Test: Default values for new fields.
     *
     * Use case: Create hive without setting health indicators.
     * Expected: All boolean health indicators default to false, displayOrder to 0.
     */
    @Test
    void testDefaultValuesForNewFields() {
        Hive hive = createTestHive("Test Hive");
        // Don't set aggression, health indicators, or displayOrder

        dao.insert(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertNull(retrieved.getAggression());
        assertFalse(retrieved.isChalkBrood());
        assertFalse(retrieved.isDroneCells());
        assertFalse(retrieved.isDroneLaying());
        assertEquals(0, retrieved.getDisplayOrder());
    }

    /**
     * Test: Insert hive with hasVarroaScreen field.
     *
     * Use case: Beekeeper adds Varroa screen to hive for mite monitoring.
     * Expected: hasVarroaScreen persisted correctly.
     */
    @Test
    void testInsertHiveWithVarroaScreen() {
        Hive hive = createTestHive("Test Hive");
        hive.setHasVarroaScreen(true);

        dao.insert(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertTrue(retrieved.isHasVarroaScreen());
    }

    /**
     * Test: Update hasVarroaScreen field.
     *
     * Use case: Beekeeper adds or removes Varroa screen from hive.
     * Expected: hasVarroaScreen updated correctly.
     */
    @Test
    void testUpdateVarroaScreen() {
        Hive hive = createTestHive("Test Hive");
        hive.setHasVarroaScreen(false);
        dao.insert(hive).blockingAwait();

        hive.setHasVarroaScreen(true);
        dao.update(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertTrue(retrieved.isHasVarroaScreen());
    }

    /**
     * Test: Default value for hasVarroaScreen.
     *
     * Use case: Create hive without setting hasVarroaScreen.
     * Expected: hasVarroaScreen defaults to false.
     */
    @Test
    void testDefaultValueForVarroaScreen() {
        Hive hive = createTestHive("Test Hive");
        // Don't set hasVarroaScreen

        dao.insert(hive).blockingAwait();

        Hive retrieved = dao.getById(hive.getId()).blockingGet();
        assertFalse(retrieved.isHasVarroaScreen());
    }

    // Helper method to create test hive
    private Hive createTestHive(String name) {
        Hive hive = new Hive();
        hive.setId(UUID.randomUUID().toString());
        hive.setApiaryId(testApiaryId);
        hive.setName(name);
        hive.setType("VERTICAL");
        hive.setActive(true);
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        return hive;
    }
}
