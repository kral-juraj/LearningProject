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
        assertEquals("Sirup 1:1", EnumHelper.getFeedTypeLabel("SYRUP_1_1"));
        assertEquals("Sirup 3:2", EnumHelper.getFeedTypeLabel("SYRUP_3_2"));
        assertEquals("Fondán", EnumHelper.getFeedTypeLabel("FONDANT"));
        assertEquals("Peľová placka", EnumHelper.getFeedTypeLabel("POLLEN_PATTY"));
        assertEquals("", EnumHelper.getFeedTypeLabel(null));
        assertEquals("UNKNOWN", EnumHelper.getFeedTypeLabel("UNKNOWN"));
    }

    @Test
    void testGetEventTypeLabel() {
        assertEquals("Prehliadka", EnumHelper.getEventTypeLabel("INSPECTION"));
        assertEquals("Krmenie", EnumHelper.getEventTypeLabel("FEEDING"));
        assertEquals("Ošetrenie", EnumHelper.getEventTypeLabel("TREATMENT"));
        assertEquals("Medobranie", EnumHelper.getEventTypeLabel("HARVEST"));
        assertEquals("Pripomienka", EnumHelper.getEventTypeLabel("REMINDER"));
        assertEquals("", EnumHelper.getEventTypeLabel(null));
        assertEquals("UNKNOWN", EnumHelper.getEventTypeLabel("UNKNOWN"));
    }

    @Test
    void testGetFrameTypeLabel() {
        assertEquals("Plodový", EnumHelper.getFrameTypeLabel("BROOD"));
        assertEquals("Medový", EnumHelper.getFrameTypeLabel("HONEY"));
        assertEquals("Osnova", EnumHelper.getFrameTypeLabel("FOUNDATION"));
        assertEquals("Vystavený", EnumHelper.getFrameTypeLabel("DRAWN"));
        assertEquals("Tmavý", EnumHelper.getFrameTypeLabel("DARK"));
        assertEquals("", EnumHelper.getFrameTypeLabel(null));
        assertEquals("UNKNOWN", EnumHelper.getFrameTypeLabel("UNKNOWN"));
    }
}
