package com.beekeeper.desktop.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumHelperTest {

    @Test
    void testGetFeedTypes() {
        var feedTypes = EnumHelper.getFeedTypes();
        assertNotNull(feedTypes);
        assertEquals(4, feedTypes.size());
        assertTrue(feedTypes.contains("SYRUP_1_1"));
        assertTrue(feedTypes.contains("SYRUP_3_2"));
        assertTrue(feedTypes.contains("FONDANT"));
        assertTrue(feedTypes.contains("POLLEN_PATTY"));
    }

    @Test
    void testGetEventTypes() {
        var eventTypes = EnumHelper.getEventTypes();
        assertNotNull(eventTypes);
        assertEquals(5, eventTypes.size());
        assertTrue(eventTypes.contains("INSPECTION"));
        assertTrue(eventTypes.contains("FEEDING"));
        assertTrue(eventTypes.contains("TREATMENT"));
        assertTrue(eventTypes.contains("HARVEST"));
        assertTrue(eventTypes.contains("REMINDER"));
    }

    @Test
    void testGetFrameTypes() {
        var frameTypes = EnumHelper.getFrameTypes();
        assertNotNull(frameTypes);
        assertEquals(5, frameTypes.size());
        assertTrue(frameTypes.contains("BROOD"));
        assertTrue(frameTypes.contains("HONEY"));
        assertTrue(frameTypes.contains("FOUNDATION"));
        assertTrue(frameTypes.contains("DRAWN"));
        assertTrue(frameTypes.contains("DARK"));
    }

    @Test
    void testGetFeedTypeLabel() {
        // Labels are now internationalized, check they're not null/empty
        assertNotNull(EnumHelper.getFeedTypeLabel("SYRUP_1_1"));
        assertFalse(EnumHelper.getFeedTypeLabel("SYRUP_1_1").isEmpty());
        assertNotNull(EnumHelper.getFeedTypeLabel("SYRUP_3_2"));
        assertFalse(EnumHelper.getFeedTypeLabel("SYRUP_3_2").isEmpty());
        assertNotNull(EnumHelper.getFeedTypeLabel("FONDANT"));
        assertFalse(EnumHelper.getFeedTypeLabel("FONDANT").isEmpty());
        assertNotNull(EnumHelper.getFeedTypeLabel("POLLEN_PATTY"));
        assertFalse(EnumHelper.getFeedTypeLabel("POLLEN_PATTY").isEmpty());
        assertEquals("", EnumHelper.getFeedTypeLabel(null));
        assertEquals("UNKNOWN", EnumHelper.getFeedTypeLabel("UNKNOWN"));
    }

    @Test
    void testGetEventTypeLabel() {
        // Labels are now internationalized, check they're not null/empty
        assertNotNull(EnumHelper.getEventTypeLabel("INSPECTION"));
        assertFalse(EnumHelper.getEventTypeLabel("INSPECTION").isEmpty());
        assertNotNull(EnumHelper.getEventTypeLabel("FEEDING"));
        assertFalse(EnumHelper.getEventTypeLabel("FEEDING").isEmpty());
        assertNotNull(EnumHelper.getEventTypeLabel("TREATMENT"));
        assertFalse(EnumHelper.getEventTypeLabel("TREATMENT").isEmpty());
        assertNotNull(EnumHelper.getEventTypeLabel("HARVEST"));
        assertFalse(EnumHelper.getEventTypeLabel("HARVEST").isEmpty());
        assertNotNull(EnumHelper.getEventTypeLabel("REMINDER"));
        assertFalse(EnumHelper.getEventTypeLabel("REMINDER").isEmpty());
        assertEquals("", EnumHelper.getEventTypeLabel(null));
        assertEquals("UNKNOWN", EnumHelper.getEventTypeLabel("UNKNOWN"));
    }

    @Test
    void testGetFrameTypeLabel() {
        // Labels are now internationalized, check they're not null/empty
        assertNotNull(EnumHelper.getFrameTypeLabel("BROOD"));
        assertFalse(EnumHelper.getFrameTypeLabel("BROOD").isEmpty());
        assertNotNull(EnumHelper.getFrameTypeLabel("HONEY"));
        assertFalse(EnumHelper.getFrameTypeLabel("HONEY").isEmpty());
        assertNotNull(EnumHelper.getFrameTypeLabel("FOUNDATION"));
        assertFalse(EnumHelper.getFrameTypeLabel("FOUNDATION").isEmpty());
        assertNotNull(EnumHelper.getFrameTypeLabel("DRAWN"));
        assertFalse(EnumHelper.getFrameTypeLabel("DRAWN").isEmpty());
        assertNotNull(EnumHelper.getFrameTypeLabel("DARK"));
        assertFalse(EnumHelper.getFrameTypeLabel("DARK").isEmpty());
        assertEquals("", EnumHelper.getFrameTypeLabel(null));
        assertEquals("UNKNOWN", EnumHelper.getFrameTypeLabel("UNKNOWN"));
    }
}
