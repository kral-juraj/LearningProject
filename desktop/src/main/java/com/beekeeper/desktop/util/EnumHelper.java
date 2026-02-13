package com.beekeeper.desktop.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Utility class for providing enum values as ObservableLists for ComboBoxes.
 * Maintains consistency with database enum values.
 */
public class EnumHelper {

    /**
     * Returns feed types for Feeding entity.
     * SYRUP_1_1, SYRUP_3_2, FONDANT, POLLEN_PATTY
     */
    public static ObservableList<String> getFeedTypes() {
        return FXCollections.observableArrayList(
            "SYRUP_1_1",
            "SYRUP_3_2",
            "FONDANT",
            "POLLEN_PATTY"
        );
    }

    /**
     * Returns event types for CalendarEvent entity.
     * INSPECTION, FEEDING, TREATMENT, HARVEST, REMINDER
     */
    public static ObservableList<String> getEventTypes() {
        return FXCollections.observableArrayList(
            "INSPECTION",
            "FEEDING",
            "TREATMENT",
            "HARVEST",
            "REMINDER"
        );
    }

    /**
     * Returns frame types for TaxationFrame entity.
     * BROOD, HONEY, FOUNDATION, DRAWN, DARK
     */
    public static ObservableList<String> getFrameTypes() {
        return FXCollections.observableArrayList(
            "BROOD",
            "HONEY",
            "FOUNDATION",
            "DRAWN",
            "DARK"
        );
    }

    /**
     * Returns user-friendly Slovak labels for feed types.
     */
    public static String getFeedTypeLabel(String feedType) {
        if (feedType == null) return "";

        switch (feedType) {
            case "SYRUP_1_1": return "Sirup 1:1";
            case "SYRUP_3_2": return "Sirup 3:2";
            case "FONDANT": return "Fondán";
            case "POLLEN_PATTY": return "Peľová placka";
            default: return feedType;
        }
    }

    /**
     * Returns user-friendly Slovak labels for event types.
     */
    public static String getEventTypeLabel(String eventType) {
        if (eventType == null) return "";

        switch (eventType) {
            case "INSPECTION": return "Prehliadka";
            case "FEEDING": return "Krmenie";
            case "TREATMENT": return "Ošetrenie";
            case "HARVEST": return "Medobranie";
            case "REMINDER": return "Pripomienka";
            default: return eventType;
        }
    }

    /**
     * Returns user-friendly Slovak labels for frame types.
     */
    public static String getFrameTypeLabel(String frameType) {
        if (frameType == null) return "";

        switch (frameType) {
            case "BROOD": return "Plodový";
            case "HONEY": return "Medový";
            case "FOUNDATION": return "Osnova";
            case "DRAWN": return "Vystavený";
            case "DARK": return "Tmavý";
            default: return frameType;
        }
    }
}
