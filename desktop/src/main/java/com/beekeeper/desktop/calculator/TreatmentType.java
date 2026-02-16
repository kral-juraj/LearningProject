package com.beekeeper.desktop.calculator;

import com.beekeeper.shared.i18n.TranslationManager;

/**
 * Predefined Varroa treatment types with typical effectiveness values.
 * Based on research and practical beekeeping experience.
 *
 * Display names, descriptions, and detailed info are loaded from TranslationManager
 * to support internationalization.
 */
public enum TreatmentType {
    // Chemical treatments
    THYMOL("thymol", 0.70),
    FORMIC_ACID("formic_acid", 0.90),
    OXALIC_ACID("oxalic_acid", 0.95),
    AMITRAZ("amitraz", 0.92),
    FLUMETHRIN("flumethrin", 0.90),

    // Mechanical interventions (NO chemistry)
    DRONE_BROOD_REMOVAL("drone_brood_removal", 0.70),
    VARROA_TRAP("varroa_trap", 0.65),
    QUEEN_CAGING("queen_caging", 0.50),
    BROOD_BREAK_BIO("brood_break_bio", 0.30),
    BROOD_BREAK_FULL("brood_break_full", 0.90),

    // Passive biotechnical methods
    TRAP_COMB("trap_comb", 0.30),
    SCREENED_BOTTOM("screened_bottom", 0.10),
    CUSTOM("custom", 0.80);

    private final String key;
    private final double defaultEffectiveness;

    TreatmentType(String key, double defaultEffectiveness) {
        this.key = key;
        this.defaultEffectiveness = defaultEffectiveness;
    }

    /**
     * Get translated display name for this treatment type.
     * Uses TranslationManager to support multiple languages.
     *
     * @return Translated display name (e.g., "Tymol" in SK, "Thymol" in EN)
     */
    public String getDisplayName() {
        TranslationManager tm = TranslationManager.getInstance();
        return tm.get("treatment." + key + ".name");
    }

    /**
     * Get short translated description.
     *
     * @return Short description (e.g., "Éterický olej, organické ošetrenie")
     */
    public String getDescription() {
        TranslationManager tm = TranslationManager.getInstance();
        return tm.get("treatment." + key + ".desc");
    }

    /**
     * Get detailed translated information about this treatment.
     * Contains effectiveness, timing, application instructions, advantages, disadvantages.
     *
     * @return Detailed multi-line description
     */
    public String getDetailedInfo() {
        TranslationManager tm = TranslationManager.getInstance();
        return tm.get("treatment." + key + ".info");
    }

    public double getDefaultEffectiveness() {
        return defaultEffectiveness;
    }

    /**
     * Get effectiveness as percentage (0-100).
     */
    public double getDefaultEffectivenessPercent() {
        return defaultEffectiveness * 100.0;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    /**
     * Find TreatmentType by display name.
     * Searches all enum values and compares translated display names.
     *
     * @param displayName Translated display name to search for
     * @return Matching TreatmentType, or CUSTOM if not found
     */
    public static TreatmentType fromDisplayName(String displayName) {
        for (TreatmentType type : values()) {
            if (type.getDisplayName().equals(displayName)) {
                return type;
            }
        }
        return CUSTOM;
    }
}
