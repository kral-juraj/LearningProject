package com.beekeeper.desktop.integration;

import com.beekeeper.desktop.dao.jdbc.JdbcApiaryDao;
import com.beekeeper.desktop.dao.jdbc.JdbcHiveDao;
import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.repository.ApiaryRepository;
import com.beekeeper.shared.repository.HiveRepository;
import com.beekeeper.shared.viewmodel.HiveViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for new hive health indicator fields.
 *
 * Tests the complete workflow for:
 * - Aggression level tracking (LOW/MEDIUM/HIGH)
 * - Chalk brood detection
 * - Drone cells monitoring
 * - Drone-laying queen detection
 *
 * Verifies persistence through all layers (ViewModel → Repository → DAO → Database).
 */
class HiveHealthIndicatorsIntegrationTest {

    private JdbcHiveDao hiveDao;
    private HiveRepository hiveRepository;
    private HiveViewModel hiveViewModel;
    private String testApiaryId;

    @BeforeEach
    void setUp() throws Exception {
        // Use file-based test database (in-memory doesn't persist connections properly)
        String testDbPath = System.getProperty("java.io.tmpdir") + "/test_health_" + System.currentTimeMillis() + ".db";
        DatabaseManager.initialize(testDbPath);

        // Setup DAOs, repositories, and ViewModel
        hiveDao = new JdbcHiveDao();
        hiveRepository = new HiveRepository(hiveDao);
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        hiveViewModel = new HiveViewModel(hiveRepository, schedulerProvider);

        // Create test apiary
        testApiaryId = createTestApiary();
    }

    /**
     * Test: Create hive with all health indicators.
     *
     * Use case: Beekeeper creates new hive record and sets initial health status.
     * Expected: All health fields persisted and retrieved correctly.
     */
    @Test
    void testCreateHiveWithHealthIndicators() {
        Hive hive = createBasicHive("Sick Colony");
        hive.setAggression("HIGH");
        hive.setChalkBrood(true);
        hive.setDroneCells(true);
        hive.setDroneLaying(false);

        hiveViewModel.createHive(hive);

        // Wait for async operation
        waitForAsync();

        // Retrieve and verify
        Hive retrieved = hiveDao.getById(hive.getId()).blockingGet();
        assertEquals("HIGH", retrieved.getAggression());
        assertTrue(retrieved.isChalkBrood());
        assertTrue(retrieved.isDroneCells());
        assertFalse(retrieved.isDroneLaying());
    }

    /**
     * Test: Update aggression level after inspection.
     *
     * Use case: Colony becomes more aggressive, beekeeper updates record.
     * Expected: Aggression level updated correctly.
     */
    @Test
    void testUpdateAggressionLevel() {
        // Create hive with low aggression
        Hive hive = createBasicHive("Calm Colony");
        hive.setAggression("LOW");
        hiveViewModel.createHive(hive);
        waitForAsync();

        // Update to high aggression
        Hive retrieved = hiveDao.getById(hive.getId()).blockingGet();
        retrieved.setAggression("HIGH");
        hiveViewModel.updateHive(retrieved);
        waitForAsync();

        // Verify update
        Hive updated = hiveDao.getById(hive.getId()).blockingGet();
        assertEquals("HIGH", updated.getAggression());
    }

    /**
     * Test: Track chalk brood treatment progress.
     *
     * Use case: Beekeeper detects chalk brood, treats colony, marks as resolved.
     * Expected: ChalkBrood flag can be toggled.
     */
    @Test
    void testChalkBroodTreatmentProgress() {
        // Create hive with chalk brood
        Hive hive = createBasicHive("Colony 1");
        hive.setChalkBrood(true);
        hiveViewModel.createHive(hive);
        waitForAsync();

        // Mark as treated (cleared)
        Hive retrieved = hiveDao.getById(hive.getId()).blockingGet();
        assertTrue(retrieved.isChalkBrood());

        retrieved.setChalkBrood(false);
        hiveViewModel.updateHive(retrieved);
        waitForAsync();

        // Verify cleared
        Hive updated = hiveDao.getById(hive.getId()).blockingGet();
        assertFalse(updated.isChalkBrood());
    }

    /**
     * Test: Identify drone-laying queen.
     *
     * Use case: Beekeeper suspects drone-laying queen (too many drone cells, no worker brood).
     * Expected: Can mark hive with droneLaying flag.
     */
    @Test
    void testIdentifyDroneLayingQueen() {
        // Create hive with normal queen
        Hive hive = createBasicHive("Healthy Colony");
        hive.setDroneLaying(false);
        hive.setDroneCells(false);
        hiveViewModel.createHive(hive);
        waitForAsync();

        // Update after identifying drone-laying queen
        Hive retrieved = hiveDao.getById(hive.getId()).blockingGet();
        retrieved.setDroneLaying(true);
        retrieved.setDroneCells(true);  // Usually correlated
        hiveViewModel.updateHive(retrieved);
        waitForAsync();

        // Verify
        Hive updated = hiveDao.getById(hive.getId()).blockingGet();
        assertTrue(updated.isDroneLaying());
        assertTrue(updated.isDroneCells());
    }

    /**
     * Test: Create hive with null aggression.
     *
     * Use case: Beekeeper doesn't assess aggression level initially.
     * Expected: NULL stored and retrieved correctly.
     */
    @Test
    void testCreateHiveWithNullAggression() {
        Hive hive = createBasicHive("New Colony");
        hive.setAggression(null);
        hiveViewModel.createHive(hive);
        waitForAsync();

        Hive retrieved = hiveDao.getById(hive.getId()).blockingGet();
        assertNull(retrieved.getAggression());
    }

    /**
     * Test: All aggression levels (LOW, MEDIUM, HIGH).
     *
     * Use case: Verify all aggression levels can be stored and retrieved.
     * Expected: Each level persisted correctly.
     */
    @Test
    void testAllAggressionLevels() {
        String[] levels = {"LOW", "MEDIUM", "HIGH"};

        for (String level : levels) {
            Hive hive = createBasicHive("Colony " + level);
            hive.setAggression(level);
            hiveViewModel.createHive(hive);
            waitForAsync();

            Hive retrieved = hiveDao.getById(hive.getId()).blockingGet();
            assertEquals(level, retrieved.getAggression(),
                "Aggression level " + level + " should persist correctly");
        }
    }

    /**
     * Test: Complex scenario - multiple health issues.
     *
     * Use case: Colony has multiple problems (aggressive, chalk brood, drone cells).
     * Expected: All indicators tracked independently.
     */
    @Test
    void testMultipleHealthIssues() {
        Hive hive = createBasicHive("Problem Colony");
        hive.setAggression("HIGH");
        hive.setChalkBrood(true);
        hive.setDroneCells(true);
        hive.setDroneLaying(false);  // Not drone-laying, just too many drone cells
        hiveViewModel.createHive(hive);
        waitForAsync();

        Hive retrieved = hiveDao.getById(hive.getId()).blockingGet();
        assertEquals("HIGH", retrieved.getAggression());
        assertTrue(retrieved.isChalkBrood());
        assertTrue(retrieved.isDroneCells());
        assertFalse(retrieved.isDroneLaying());
    }

    /**
     * Test: Progressive deterioration tracking.
     *
     * Use case: Colony health worsens over time, beekeeper tracks changes.
     * Expected: Multiple updates persist correctly.
     */
    @Test
    void testProgressiveDeteriorationTracking() {
        // Initial state - healthy
        Hive hive = createBasicHive("Declining Colony");
        hive.setAggression("LOW");
        hive.setChalkBrood(false);
        hive.setDroneCells(false);
        hiveViewModel.createHive(hive);
        waitForAsync();

        // Week 1 - slight issues
        Hive retrieved = hiveDao.getById(hive.getId()).blockingGet();
        retrieved.setAggression("MEDIUM");
        retrieved.setDroneCells(true);
        hiveViewModel.updateHive(retrieved);
        waitForAsync();

        Hive week1 = hiveDao.getById(hive.getId()).blockingGet();
        assertEquals("MEDIUM", week1.getAggression());
        assertTrue(week1.isDroneCells());
        assertFalse(week1.isChalkBrood());

        // Week 2 - worsening
        week1.setAggression("HIGH");
        week1.setChalkBrood(true);
        hiveViewModel.updateHive(week1);
        waitForAsync();

        Hive week2 = hiveDao.getById(hive.getId()).blockingGet();
        assertEquals("HIGH", week2.getAggression());
        assertTrue(week2.isDroneCells());
        assertTrue(week2.isChalkBrood());
    }

    /**
     * Test: Filter hives by health status.
     *
     * Use case: Beekeeper wants to find all hives with chalk brood.
     * Expected: Can query and filter hives by health indicators.
     */
    @Test
    void testFilterHivesByHealthStatus() {
        // Create 5 hives with different health statuses
        createHiveWithHealth("Healthy 1", "LOW", false, false, false);
        createHiveWithHealth("Sick 1", "HIGH", true, false, false);
        createHiveWithHealth("Healthy 2", "LOW", false, false, false);
        createHiveWithHealth("Sick 2", "MEDIUM", true, true, false);
        createHiveWithHealth("Healthy 3", "MEDIUM", false, false, false);

        waitForAsync();

        // Retrieve all hives
        List<Hive> allHives = hiveDao.getByApiaryId(testApiaryId).blockingFirst();
        assertEquals(5, allHives.size());

        // Filter by chalk brood
        long chalkBroodCount = allHives.stream()
            .filter(Hive::isChalkBrood)
            .count();
        assertEquals(2, chalkBroodCount, "Should have 2 hives with chalk brood");

        // Filter by high aggression
        long highAggressionCount = allHives.stream()
            .filter(h -> "HIGH".equals(h.getAggression()))
            .count();
        assertEquals(1, highAggressionCount, "Should have 1 highly aggressive hive");
    }

    /**
     * Test: Update preserves other fields.
     *
     * Use case: Updating health indicators should not affect other hive data.
     * Expected: Name, type, equipment fields unchanged.
     */
    @Test
    void testUpdateHealthPreservesOtherFields() {
        Hive hive = createBasicHive("Test Colony");
        hive.setType("HORIZONTAL");
        hive.setQueenId("Q2024-001");
        hive.setQueenYear(2024);
        hive.setFrameCount(10);
        hive.setAggression("LOW");
        hiveViewModel.createHive(hive);
        waitForAsync();

        // Update only health indicators
        Hive retrieved = hiveDao.getById(hive.getId()).blockingGet();
        retrieved.setAggression("HIGH");
        retrieved.setChalkBrood(true);
        hiveViewModel.updateHive(retrieved);
        waitForAsync();

        // Verify other fields preserved
        Hive updated = hiveDao.getById(hive.getId()).blockingGet();
        assertEquals("HIGH", updated.getAggression());
        assertTrue(updated.isChalkBrood());
        assertEquals("HORIZONTAL", updated.getType());
        assertEquals("Q2024-001", updated.getQueenId());
        assertEquals(2024, updated.getQueenYear());
        assertEquals(10, updated.getFrameCount());
    }

    // Helper methods

    private String createTestApiary() {
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);

        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName("Test Apiary");
        apiary.setLocation("Test Location");
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());

        apiaryRepository.insertApiary(apiary).blockingAwait();
        return apiary.getId();
    }

    private Hive createBasicHive(String name) {
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

    private void createHiveWithHealth(String name, String aggression, boolean chalkBrood,
                                     boolean droneCells, boolean droneLaying) {
        Hive hive = createBasicHive(name);
        hive.setAggression(aggression);
        hive.setChalkBrood(chalkBrood);
        hive.setDroneCells(droneCells);
        hive.setDroneLaying(droneLaying);
        hiveViewModel.createHive(hive);
    }

    private void waitForAsync() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }
    }
}
