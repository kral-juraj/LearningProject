package com.beekeeper.shared.entity;

/**
 * Enum representing types of hive activities/events.
 *
 * Use case: Track history of changes and events for each hive.
 * Categories:
 * - Basic: Adding/removing supers, grids, insulation
 * - Main events: Honey harvest, treatment, swarming, splits
 * - Other: Hive relocation, bottom board replacement, repairs
 */
public enum HiveActivityType {
    // Sorted alphabetically by translation key for better UX in dropdowns
    ADDED_INSULATION("ADDED_INSULATION", "hive.activity.added_insulation"),
    ADDED_PROPOLIS_TRAP("ADDED_PROPOLIS_TRAP", "hive.activity.added_propolis_trap"),
    ADDED_QUEEN_EXCLUDER("ADDED_QUEEN_EXCLUDER", "hive.activity.added_queen_excluder"),
    ADDED_SUPER("ADDED_SUPER", "hive.activity.added_super"),
    CHANGED_BOTTOM_BOARD("CHANGED_BOTTOM_BOARD", "hive.activity.changed_bottom_board"),
    COMBINED("COMBINED", "hive.activity.combined"),
    HONEY_HARVEST("HONEY_HARVEST", "hive.activity.honey_harvest"),
    OTHER("OTHER", "hive.activity.other"),
    RELOCATED("RELOCATED", "hive.activity.relocated"),
    REMOVED_INSULATION("REMOVED_INSULATION", "hive.activity.removed_insulation"),
    REMOVED_PROPOLIS_TRAP("REMOVED_PROPOLIS_TRAP", "hive.activity.removed_propolis_trap"),
    REMOVED_QUEEN_EXCLUDER("REMOVED_QUEEN_EXCLUDER", "hive.activity.removed_queen_excluder"),
    REMOVED_SUPER("REMOVED_SUPER", "hive.activity.removed_super"),
    REPAIRED("REPAIRED", "hive.activity.repaired"),
    REQUEENED("REQUEENED", "hive.activity.requeened"),
    SPLIT_CREATED("SPLIT_CREATED", "hive.activity.split_created"),
    SWARMED("SWARMED", "hive.activity.swarmed"),
    TREATMENT("TREATMENT", "hive.activity.treatment");

    private final String code;
    private final String translationKey;

    HiveActivityType(String code, String translationKey) {
        this.code = code;
        this.translationKey = translationKey;
    }

    public String getCode() {
        return code;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    /**
     * Get HiveActivityType from code string.
     *
     * @param code String code (e.g., "ADDED_SUPER")
     * @return HiveActivityType enum value, or null if not found
     */
    public static HiveActivityType fromCode(String code) {
        if (code == null) return null;
        for (HiveActivityType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
