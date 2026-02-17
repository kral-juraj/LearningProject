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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for hive drag-and-drop reordering functionality.
 *
 * Tests the complete workflow:
 * 1. Create apiary and multiple hives
 * 2. Simulate drag-and-drop reordering
 * 3. Update displayOrder for all affected hives
 * 4. Verify persistence in database
 * 5. Verify retrieval in correct order
 */
class HiveDragAndDropIntegrationTest {

    private JdbcHiveDao hiveDao;
    private HiveRepository hiveRepository;
    private HiveViewModel hiveViewModel;
    private String testApiaryId;

    @BeforeEach
    void setUp() throws Exception {
        // Use file-based test database (in-memory doesn't persist connections properly)
        String testDbPath = System.getProperty("java.io.tmpdir") + "/test_drag_drop_" + System.currentTimeMillis() + ".db";
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
     * Test: Complete drag-and-drop workflow.
     *
     * Use case: Beekeeper has 5 hives, drags "Ležan" (index 4) to position 1 (after "Oddielok").
     * Expected: All hives reordered correctly, persisted, and retrieved in new order.
     */
    @Test
    void testCompleteDragAndDropWorkflow() {
        // Create 5 hives with initial order
        List<Hive> hives = new ArrayList<>();
        hives.add(createHive("Oddielok", 0));
        hives.add(createHive("Úľ 1", 1));
        hives.add(createHive("Úľ 2", 2));
        hives.add(createHive("Úľ 3", 3));
        hives.add(createHive("Ležan", 4));

        // Insert all hives
        for (Hive hive : hives) {
            hiveDao.insert(hive).blockingAwait();
        }

        // Verify initial order
        List<Hive> initialOrder = hiveDao.getByApiaryId(testApiaryId).blockingFirst();
        assertEquals("Oddielok", initialOrder.get(0).getName());
        assertEquals("Ležan", initialOrder.get(4).getName());

        // Simulate drag-and-drop: Move "Ležan" from index 4 to index 1
        Hive draggedHive = hives.remove(4);  // Remove "Ležan"
        hives.add(1, draggedHive);           // Insert after "Oddielok"

        // Update displayOrder values
        for (int i = 0; i < hives.size(); i++) {
            hives.get(i).setDisplayOrder(i);
        }

        // Save new order via ViewModel
        hiveViewModel.updateHiveOrder(new ArrayList<>(hives));

        // Wait for async operation to complete
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Verify new order persisted
        List<Hive> newOrder = hiveDao.getByApiaryId(testApiaryId).blockingFirst();
        assertEquals(5, newOrder.size());
        assertEquals("Oddielok", newOrder.get(0).getName());
        assertEquals("Ležan", newOrder.get(1).getName());      // Moved here
        assertEquals("Úľ 1", newOrder.get(2).getName());
        assertEquals("Úľ 2", newOrder.get(3).getName());
        assertEquals("Úľ 3", newOrder.get(4).getName());

        // Verify displayOrder values
        for (int i = 0; i < newOrder.size(); i++) {
            assertEquals(i, newOrder.get(i).getDisplayOrder(),
                "displayOrder should match position in list");
        }
    }

    /**
     * Test: Move hive to beginning.
     *
     * Use case: Beekeeper drags last hive to first position.
     * Expected: Hive becomes first, all others shift down.
     */
    @Test
    void testMoveHiveToBeginning() {
        // Create 3 hives
        List<Hive> hives = new ArrayList<>();
        hives.add(createHive("Hive A", 0));
        hives.add(createHive("Hive B", 1));
        hives.add(createHive("Hive C", 2));

        for (Hive hive : hives) {
            hiveDao.insert(hive).blockingAwait();
        }

        // Move "Hive C" to beginning
        Hive draggedHive = hives.remove(2);
        hives.add(0, draggedHive);

        for (int i = 0; i < hives.size(); i++) {
            hives.get(i).setDisplayOrder(i);
        }

        hiveViewModel.updateHiveOrder(new ArrayList<>(hives));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Verify
        List<Hive> newOrder = hiveDao.getByApiaryId(testApiaryId).blockingFirst();
        assertEquals("Hive C", newOrder.get(0).getName());
        assertEquals("Hive A", newOrder.get(1).getName());
        assertEquals("Hive B", newOrder.get(2).getName());
    }

    /**
     * Test: Move hive to end.
     *
     * Use case: Beekeeper drags first hive to last position.
     * Expected: Hive becomes last, all others shift up.
     */
    @Test
    void testMoveHiveToEnd() {
        // Create 3 hives
        List<Hive> hives = new ArrayList<>();
        hives.add(createHive("Hive A", 0));
        hives.add(createHive("Hive B", 1));
        hives.add(createHive("Hive C", 2));

        for (Hive hive : hives) {
            hiveDao.insert(hive).blockingAwait();
        }

        // Move "Hive A" to end
        Hive draggedHive = hives.remove(0);
        hives.add(draggedHive);

        for (int i = 0; i < hives.size(); i++) {
            hives.get(i).setDisplayOrder(i);
        }

        hiveViewModel.updateHiveOrder(new ArrayList<>(hives));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Verify
        List<Hive> newOrder = hiveDao.getByApiaryId(testApiaryId).blockingFirst();
        assertEquals("Hive B", newOrder.get(0).getName());
        assertEquals("Hive C", newOrder.get(1).getName());
        assertEquals("Hive A", newOrder.get(2).getName());
    }

    /**
     * Test: Adjacent hive swap.
     *
     * Use case: Beekeeper swaps two adjacent hives.
     * Expected: Only those two hives change positions.
     */
    @Test
    void testAdjacentHiveSwap() {
        // Create 4 hives
        List<Hive> hives = new ArrayList<>();
        hives.add(createHive("Hive A", 0));
        hives.add(createHive("Hive B", 1));
        hives.add(createHive("Hive C", 2));
        hives.add(createHive("Hive D", 3));

        for (Hive hive : hives) {
            hiveDao.insert(hive).blockingAwait();
        }

        // Swap "Hive B" and "Hive C"
        Hive temp = hives.get(1);
        hives.set(1, hives.get(2));
        hives.set(2, temp);

        for (int i = 0; i < hives.size(); i++) {
            hives.get(i).setDisplayOrder(i);
        }

        hiveViewModel.updateHiveOrder(new ArrayList<>(hives));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Verify
        List<Hive> newOrder = hiveDao.getByApiaryId(testApiaryId).blockingFirst();
        assertEquals("Hive A", newOrder.get(0).getName());
        assertEquals("Hive C", newOrder.get(1).getName());  // Swapped
        assertEquals("Hive B", newOrder.get(2).getName());  // Swapped
        assertEquals("Hive D", newOrder.get(3).getName());
    }

    /**
     * Test: Single hive (no reordering possible).
     *
     * Use case: Apiary has only one hive.
     * Expected: No error, hive remains at position 0.
     */
    @Test
    void testSingleHiveNoReordering() {
        // Create single hive
        List<Hive> hives = new ArrayList<>();
        hives.add(createHive("Only Hive", 0));

        hiveDao.insert(hives.get(0)).blockingAwait();

        // "Reorder" single hive (no-op)
        hiveViewModel.updateHiveOrder(new ArrayList<>(hives));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Verify
        List<Hive> order = hiveDao.getByApiaryId(testApiaryId).blockingFirst();
        assertEquals(1, order.size());
        assertEquals("Only Hive", order.get(0).getName());
        assertEquals(0, order.get(0).getDisplayOrder());
    }

    /**
     * Test: Multiple reordering operations.
     *
     * Use case: User performs multiple drag-and-drops in succession.
     * Expected: Each operation persists correctly, final order is accurate.
     */
    @Test
    void testMultipleReorderingOperations() {
        // Create 3 hives
        List<Hive> hives = new ArrayList<>();
        hives.add(createHive("Hive A", 0));
        hives.add(createHive("Hive B", 1));
        hives.add(createHive("Hive C", 2));

        for (Hive hive : hives) {
            hiveDao.insert(hive).blockingAwait();
        }

        // First reorder: A, C, B
        Hive temp = hives.remove(2);
        hives.add(1, temp);
        for (int i = 0; i < hives.size(); i++) {
            hives.get(i).setDisplayOrder(i);
        }
        hiveViewModel.updateHiveOrder(new ArrayList<>(hives));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Second reorder: C, A, B
        temp = hives.remove(1);
        hives.add(0, temp);
        for (int i = 0; i < hives.size(); i++) {
            hives.get(i).setDisplayOrder(i);
        }
        hiveViewModel.updateHiveOrder(new ArrayList<>(hives));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Verify final order
        List<Hive> finalOrder = hiveDao.getByApiaryId(testApiaryId).blockingFirst();
        assertEquals("Hive C", finalOrder.get(0).getName());
        assertEquals("Hive A", finalOrder.get(1).getName());
        assertEquals("Hive B", finalOrder.get(2).getName());
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

    private Hive createHive(String name, int displayOrder) {
        Hive hive = new Hive();
        hive.setId(UUID.randomUUID().toString());
        hive.setApiaryId(testApiaryId);
        hive.setName(name);
        hive.setType("VERTICAL");
        hive.setDisplayOrder(displayOrder);
        hive.setActive(true);
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        return hive;
    }
}
