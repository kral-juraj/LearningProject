package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.entity.Apiary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JdbcApiaryDao focusing on new extended apiary fields.
 *
 * Tests the persistence and retrieval of:
 * - displayOrder field for drag-and-drop ordering
 * - registrationNumber field for official apiary registration
 * - address field for detailed location
 * - description field for notes
 */
class JdbcApiaryDaoTest {

    private JdbcApiaryDao dao;

    @BeforeEach
    void setUp() throws Exception {
        // Use file-based test database (in-memory doesn't persist connections properly)
        String testDbPath = System.getProperty("java.io.tmpdir") + "/test_apiary_dao_" + System.currentTimeMillis() + ".db";
        DatabaseManager.initialize(testDbPath);
        dao = new JdbcApiaryDao();
    }

    /**
     * Test: Insert apiary with extended fields.
     *
     * Use case: User creates apiary with all optional details filled in.
     * Expected: All fields (displayOrder, registrationNumber, address, description) persisted correctly.
     */
    @Test
    void testInsertApiaryWithExtendedFields() {
        Apiary apiary = createTestApiary("Test Včelnica");
        apiary.setDisplayOrder(5);
        apiary.setRegistrationNumber("REG-12345");
        apiary.setAddress("Hlavná 123, Bratislava");
        apiary.setDescription("Testovacia včelnica s poznámkami");

        dao.insert(apiary).blockingAwait();

        Apiary retrieved = dao.getById(apiary.getId()).blockingGet();
        assertEquals(5, retrieved.getDisplayOrder());
        assertEquals("REG-12345", retrieved.getRegistrationNumber());
        assertEquals("Hlavná 123, Bratislava", retrieved.getAddress());
        assertEquals("Testovacia včelnica s poznámkami", retrieved.getDescription());
    }

    /**
     * Test: Insert apiary with null optional fields.
     *
     * Use case: User creates apiary without filling in optional fields.
     * Expected: NULL values stored and retrieved correctly.
     */
    @Test
    void testInsertApiaryWithNullOptionalFields() {
        Apiary apiary = createTestApiary("Test Včelnica");
        apiary.setRegistrationNumber(null);
        apiary.setAddress(null);
        apiary.setDescription(null);

        dao.insert(apiary).blockingAwait();

        Apiary retrieved = dao.getById(apiary.getId()).blockingGet();
        assertNull(retrieved.getRegistrationNumber());
        assertNull(retrieved.getAddress());
        assertNull(retrieved.getDescription());
        assertEquals(0, retrieved.getDisplayOrder()); // Default value
    }

    /**
     * Test: Update apiary with extended fields.
     *
     * Use case: User edits apiary to add/update optional details.
     * Expected: All extended fields updated correctly.
     */
    @Test
    void testUpdateApiaryWithExtendedFields() {
        Apiary apiary = createTestApiary("Test Včelnica");
        apiary.setRegistrationNumber("REG-OLD");
        apiary.setAddress("Stará adresa");
        apiary.setDescription("Starý popis");
        apiary.setDisplayOrder(0);
        dao.insert(apiary).blockingAwait();

        // Update all extended fields
        apiary.setRegistrationNumber("REG-NEW");
        apiary.setAddress("Nová adresa");
        apiary.setDescription("Nový popis");
        apiary.setDisplayOrder(10);
        dao.update(apiary).blockingAwait();

        Apiary retrieved = dao.getById(apiary.getId()).blockingGet();
        assertEquals("REG-NEW", retrieved.getRegistrationNumber());
        assertEquals("Nová adresa", retrieved.getAddress());
        assertEquals("Nový popis", retrieved.getDescription());
        assertEquals(10, retrieved.getDisplayOrder());
    }

    /**
     * Test: Update apiary displayOrder only.
     *
     * Use case: User drags apiary to new position, only displayOrder changed.
     * Expected: DisplayOrder updated, other fields unchanged.
     */
    @Test
    void testUpdateApiaryDisplayOrder() {
        Apiary apiary = createTestApiary("Test Včelnica");
        apiary.setDisplayOrder(0);
        apiary.setRegistrationNumber("REG-12345");
        dao.insert(apiary).blockingAwait();

        apiary.setDisplayOrder(5);
        dao.update(apiary).blockingAwait();

        Apiary retrieved = dao.getById(apiary.getId()).blockingGet();
        assertEquals(5, retrieved.getDisplayOrder());
        assertEquals("REG-12345", retrieved.getRegistrationNumber()); // Unchanged
    }

    /**
     * Test: Retrieve apiaries sorted by displayOrder.
     *
     * Use case: Display apiaries in user-defined order after drag-and-drop.
     * Expected: Apiaries returned sorted by displayOrder ascending, then by name.
     */
    @Test
    void testGetAllSortedByDisplayOrder() {
        // Create apiaries with different displayOrder values
        Apiary apiary1 = createTestApiary("Včelnica A");
        apiary1.setDisplayOrder(2);
        dao.insert(apiary1).blockingAwait();

        Apiary apiary2 = createTestApiary("Včelnica B");
        apiary2.setDisplayOrder(0);
        dao.insert(apiary2).blockingAwait();

        Apiary apiary3 = createTestApiary("Včelnica C");
        apiary3.setDisplayOrder(1);
        dao.insert(apiary3).blockingAwait();

        List<Apiary> apiaries = dao.getAll().blockingFirst();

        assertEquals(3, apiaries.size());
        assertEquals("Včelnica B", apiaries.get(0).getName()); // displayOrder = 0
        assertEquals("Včelnica C", apiaries.get(1).getName()); // displayOrder = 1
        assertEquals("Včelnica A", apiaries.get(2).getName()); // displayOrder = 2
    }

    /**
     * Test: Batch update apiary order using insertAll.
     *
     * Use case: After drag-and-drop, update multiple apiaries' displayOrder at once.
     * Expected: All apiaries updated with new displayOrder values.
     */
    @Test
    void testInsertAllUpdatesDisplayOrder() {
        // Create 3 apiaries
        List<Apiary> apiaries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Apiary apiary = createTestApiary("Včelnica " + i);
            apiary.setDisplayOrder(i);
            apiaries.add(apiary);
        }

        // Insert all
        for (Apiary apiary : apiaries) {
            dao.insert(apiary).blockingAwait();
        }

        // Reorder: swap first and last
        apiaries.get(0).setDisplayOrder(2);
        apiaries.get(2).setDisplayOrder(0);
        dao.insertAll(apiaries).blockingAwait();

        List<Apiary> retrieved = dao.getAll().blockingFirst();
        assertEquals("Včelnica 2", retrieved.get(0).getName()); // Now first
        assertEquals("Včelnica 1", retrieved.get(1).getName()); // Still middle
        assertEquals("Včelnica 0", retrieved.get(2).getName()); // Now last
    }

    /**
     * Test: insertAll uses UPDATE to avoid CASCADE DELETE.
     *
     * Use case: Reordering apiaries should NOT delete associated hives.
     * Expected: insertAll uses UPDATE internally (verified by no errors when hives exist).
     *
     * Note: Full verification requires integration test with hives table.
     */
    @Test
    void testInsertAllUsesUpdateToAvoidCascadeDelete() {
        // Create apiary
        Apiary apiary = createTestApiary("Test Včelnica");
        apiary.setDisplayOrder(0);
        dao.insert(apiary).blockingAwait();

        // Update displayOrder using insertAll
        apiary.setDisplayOrder(5);
        List<Apiary> apiaries = new ArrayList<>();
        apiaries.add(apiary);

        // This should use UPDATE, not INSERT OR REPLACE
        // If it used INSERT OR REPLACE, child hives would be deleted (tested in integration test)
        dao.insertAll(apiaries).blockingAwait();

        Apiary retrieved = dao.getById(apiary.getId()).blockingGet();
        assertEquals(5, retrieved.getDisplayOrder());
    }

    /**
     * Test: Default values for new fields.
     *
     * Use case: Create apiary without setting extended fields.
     * Expected: displayOrder defaults to 0, other fields default to null.
     */
    @Test
    void testDefaultValuesForNewFields() {
        Apiary apiary = createTestApiary("Test Včelnica");
        // Don't set displayOrder, registrationNumber, address, description

        dao.insert(apiary).blockingAwait();

        Apiary retrieved = dao.getById(apiary.getId()).blockingGet();
        assertEquals(0, retrieved.getDisplayOrder());
        assertNull(retrieved.getRegistrationNumber());
        assertNull(retrieved.getAddress());
        assertNull(retrieved.getDescription());
    }

    /**
     * Test: Sorting by displayOrder, then by name.
     *
     * Use case: Multiple apiaries with same displayOrder should be sorted alphabetically.
     * Expected: Primary sort by displayOrder, secondary sort by name.
     */
    @Test
    void testGetAllSortsByDisplayOrderThenName() {
        // Create apiaries with same displayOrder
        Apiary apiary1 = createTestApiary("Včelnica Z");
        apiary1.setDisplayOrder(1);
        dao.insert(apiary1).blockingAwait();

        Apiary apiary2 = createTestApiary("Včelnica A");
        apiary2.setDisplayOrder(1);
        dao.insert(apiary2).blockingAwait();

        Apiary apiary3 = createTestApiary("Včelnica M");
        apiary3.setDisplayOrder(1);
        dao.insert(apiary3).blockingAwait();

        List<Apiary> apiaries = dao.getAll().blockingFirst();

        assertEquals(3, apiaries.size());
        // All have displayOrder = 1, so sorted alphabetically
        assertEquals("Včelnica A", apiaries.get(0).getName());
        assertEquals("Včelnica M", apiaries.get(1).getName());
        assertEquals("Včelnica Z", apiaries.get(2).getName());
    }

    // Helper method to create test apiary
    private Apiary createTestApiary(String name) {
        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName(name);
        apiary.setLocation("Test Location");
        apiary.setLatitude(48.1486);
        apiary.setLongitude(17.1077);
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        return apiary;
    }
}
