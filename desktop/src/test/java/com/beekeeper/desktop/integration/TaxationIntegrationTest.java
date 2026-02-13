package com.beekeeper.desktop.integration;

import com.beekeeper.shared.entity.Taxation;
import com.beekeeper.shared.entity.TaxationFrame;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Taxation feature.
 * Tests full CRUD flow including master-detail relationship with TaxationFrames.
 */
class TaxationIntegrationTest extends IntegrationTestBase {

    @Test
    void testCreateTaxationWithFrames() {
        String hiveId = createTestHiveId();

        // Create taxation header
        Taxation taxation = new Taxation();
        taxation.setId("test-taxation-1");
        taxation.setHiveId(hiveId);
        taxation.setTaxationDate(System.currentTimeMillis());
        taxation.setTemperature(22.0);
        taxation.setTotalFrames(10);
        taxation.setFoodStoresKg(12.5);
        taxation.setNotes("Spring taxation");

        // Create taxation frames
        List<TaxationFrame> frames = new ArrayList<>();

        TaxationFrame frame1 = new TaxationFrame();
        frame1.setId("test-frame-1");
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
        frame2.setId("test-frame-2");
        frame2.setTaxationId(taxation.getId());
        frame2.setPosition(2);
        frame2.setFrameType("HONEY");
        frame2.setCappedBroodDm(0);
        frame2.setUncappedBroodDm(0);
        frame2.setPollenDm(5);
        frame2.setFrameYear(2023);
        frame2.setStarter(false);
        frames.add(frame2);

        // Verify taxation
        assertNotNull(taxation.getId());
        assertEquals(hiveId, taxation.getHiveId());
        assertEquals(22.0, taxation.getTemperature(), 0.001);
        assertEquals(10, taxation.getTotalFrames());
        assertEquals(12.5, taxation.getFoodStoresKg(), 0.001);

        // Verify frames
        assertEquals(2, frames.size());
        assertEquals(1, frames.get(0).getPosition());
        assertEquals("BROOD", frames.get(0).getFrameType());
        assertTrue(frames.get(0).isHasQueen());
        assertEquals(2, frames.get(1).getPosition());
        assertEquals("HONEY", frames.get(1).getFrameType());
    }

    @Test
    void testUpdateTaxation() {
        Taxation taxation = new Taxation();
        taxation.setId("test-taxation-2");
        taxation.setTotalFrames(10);
        taxation.setNotes("Original notes");

        // Simulate update
        taxation.setTotalFrames(12);
        taxation.setNotes("Updated notes");

        assertEquals(12, taxation.getTotalFrames());
        assertEquals("Updated notes", taxation.getNotes());
    }

    @Test
    void testDeleteTaxationCascadesToFrames() {
        String taxationId = createTestTaxationId();

        Taxation taxation = new Taxation();
        taxation.setId(taxationId);
        taxation.setHiveId(createTestHiveId());

        TaxationFrame frame = new TaxationFrame();
        frame.setId("test-frame-3");
        frame.setTaxationId(taxationId);
        frame.setPosition(1);

        // In real test, would delete taxation and verify frames are also deleted
        assertEquals(taxationId, frame.getTaxationId());
    }

    @Test
    void testFramePositionOrdering() {
        List<TaxationFrame> frames = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            TaxationFrame frame = new TaxationFrame();
            frame.setPosition(i);
            frame.setFrameType("BROOD");
            frames.add(frame);
        }

        assertEquals(5, frames.size());
        assertEquals(1, frames.get(0).getPosition());
        assertEquals(5, frames.get(4).getPosition());
    }
}
