package com.beekeeper.desktop.util;

import com.beekeeper.shared.i18n.TranslationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Utility class for providing enum values as ObservableLists for ComboBoxes.
 * Maintains consistency with database enum values.
 * Labels are internationalized using TranslationManager.
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
     * Returns translated labels for feed types.
     * Uses TranslationManager to support multiple languages.
     */
    public static String getFeedTypeLabel(String feedType) {
        if (feedType == null) return "";

        TranslationManager tm = TranslationManager.getInstance();
        switch (feedType) {
            case "SYRUP_1_1": return tm.get("feedtype.syrup_1_1");
            case "SYRUP_3_2": return tm.get("feedtype.syrup_3_2");
            case "FONDANT": return tm.get("feedtype.fondant");
            case "POLLEN_PATTY": return tm.get("feedtype.pollen_patty");
            default: return feedType;
        }
    }

    /**
     * Returns translated labels for event types.
     * Uses TranslationManager to support multiple languages.
     */
    public static String getEventTypeLabel(String eventType) {
        if (eventType == null) return "";

        TranslationManager tm = TranslationManager.getInstance();
        switch (eventType) {
            case "INSPECTION": return tm.get("eventtype.inspection");
            case "FEEDING": return tm.get("eventtype.feeding");
            case "TREATMENT": return tm.get("eventtype.treatment");
            case "HARVEST": return tm.get("eventtype.harvest");
            case "REMINDER": return tm.get("eventtype.reminder");
            default: return eventType;
        }
    }

    /**
     * Returns translated labels for frame types.
     * Uses TranslationManager to support multiple languages.
     */
    public static String getFrameTypeLabel(String frameType) {
        if (frameType == null) return "";

        TranslationManager tm = TranslationManager.getInstance();
        switch (frameType) {
            case "BROOD": return tm.get("frametype.brood");
            case "HONEY": return tm.get("frametype.honey");
            case "FOUNDATION": return tm.get("frametype.foundation");
            case "DRAWN": return tm.get("frametype.drawn");
            case "DARK": return tm.get("frametype.dark");
            default: return frameType;
        }
    }
}
