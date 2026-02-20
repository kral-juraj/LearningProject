package com.beekeeper.shared.entity;

/**
 * Enum representing types of frames used in beehives.
 *
 * Use case: Track different frame standards used by beekeepers.
 * All frames have 420 mm width, heights vary by type.
 */
public enum FrameType {
    // B (Brda)
    B("B", "hive.frame.b", "420×275 mm (11.5 dm²)"),

    // Langstroth variants
    LANGSTROTH_1_2("LANGSTROTH_1_2", "hive.frame.langstroth_1_2", "420×135 mm (5.7 dm²)"),
    LANGSTROTH_2_3("LANGSTROTH_2_3", "hive.frame.langstroth_2_3", "420×176 mm (7.4 dm²)"),
    LANGSTROTH_3_4("LANGSTROTH_3_4", "hive.frame.langstroth_3_4", "420×203 mm (8.5 dm²)"),
    LANGSTROTH_1_1("LANGSTROTH_1_1", "hive.frame.langstroth_1_1", "420×232 mm (9.7 dm²)"),
    LANGSTROTH_JUMBO("LANGSTROTH_JUMBO", "hive.frame.langstroth_jumbo", "420×285 mm (12.0 dm²)"),
    LANGSTROTH("LANGSTROTH", "hive.frame.langstroth", "420×232 mm (9.7 dm²)"),  // Alias for 1/1

    // Dadant variants
    DADANT_PLODISKO("DADANT_PLODISKO", "hive.frame.dadant_plodisko", "420×300 mm (12.6 dm²)"),
    DADANT_MEDNIK_1_2("DADANT_MEDNIK_1_2", "hive.frame.dadant_mednik_1_2", "420×145 mm (6.1 dm²)"),
    DADANT_BLATT("DADANT_BLATT", "hive.frame.dadant_blatt", "420×470 mm (19.7 dm²)"),
    DADANT("DADANT", "hive.frame.dadant", "420×300 mm (12.6 dm²)"),  // Alias for plodisko

    // Zander variants
    ZANDER_CELY("ZANDER_CELY", "hive.frame.zander_cely", "420×220 mm (9.2 dm²)"),
    ZANDER_2_3("ZANDER_2_3", "hive.frame.zander_2_3", "420×159 mm (6.7 dm²)"),
    ZANDER_1_2("ZANDER_1_2", "hive.frame.zander_1_2", "420×110 mm (4.6 dm²)"),
    ZANDER_1_5("ZANDER_1_5", "hive.frame.zander_1_5", "420×337 mm (14.2 dm²)"),
    ZANDER("ZANDER", "hive.frame.zander", "420×220 mm (9.2 dm²)");  // Alias for celý

    private final String code;
    private final String translationKey;
    private final String dimensions;

    FrameType(String code, String translationKey, String dimensions) {
        this.code = code;
        this.translationKey = translationKey;
        this.dimensions = dimensions;
    }

    public String getCode() {
        return code;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getDimensions() {
        return dimensions;
    }

    /**
     * Get FrameType from code string.
     *
     * @param code String code (e.g., "LANGSTROTH_1_1")
     * @return FrameType enum value, or null if not found
     */
    public static FrameType fromCode(String code) {
        if (code == null) return null;
        for (FrameType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
