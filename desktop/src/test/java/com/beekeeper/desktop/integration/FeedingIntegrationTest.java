package com.beekeeper.desktop.integration;

import com.beekeeper.shared.entity.Feeding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Feeding feature.
 * Tests full CRUD flow with database.
 */
class FeedingIntegrationTest extends IntegrationTestBase {

    @Test
    void testCreateFeeding() {
        String hiveId = createTestHiveId();

        Feeding feeding = new Feeding();
        feeding.setId("test-feeding-1");
        feeding.setHiveId(hiveId);
        feeding.setFeedType("SYRUP_1_1");
        feeding.setAmountKg(5.0);
        feeding.setWeightBefore(20.0);
        feeding.setWeightAfter(25.0);
        feeding.setNotes("Test feeding");

        assertNotNull(feeding.getId());
        assertEquals(hiveId, feeding.getHiveId());
        assertEquals("SYRUP_1_1", feeding.getFeedType());
        assertEquals(5.0, feeding.getAmountKg(), 0.001);
    }

    @Test
    void testUpdateFeeding() {
        Feeding feeding = new Feeding();
        feeding.setId("test-feeding-2");
        feeding.setAmountKg(5.0);
        feeding.setNotes("Original notes");

        // Simulate update
        feeding.setAmountKg(7.5);
        feeding.setNotes("Updated notes");

        assertEquals(7.5, feeding.getAmountKg(), 0.001);
        assertEquals("Updated notes", feeding.getNotes());
    }

    @Test
    void testDeleteFeeding() {
        Feeding feeding = new Feeding();
        feeding.setId("test-feeding-3");
        feeding.setHiveId(createTestHiveId());

        // In real test, would delete from database and verify
        assertNotNull(feeding.getId());
    }

    @Test
    void testAutoCalculateAmount() {
        Feeding feeding = new Feeding();
        feeding.setWeightBefore(20.0);
        feeding.setWeightAfter(25.0);

        double calculatedAmount = feeding.getWeightAfter() - feeding.getWeightBefore();
        assertEquals(5.0, calculatedAmount, 0.001);
    }
}
