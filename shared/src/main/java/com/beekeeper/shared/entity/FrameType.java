package com.beekeeper.shared.entity;

/**
 * Enum representing types of frames used in beehives.
 *
 * Use case: Track different frame standards used by beekeepers.
 */
public enum FrameType {
    B("B", "hive.frame.b", "39×24 cm"),                        // Bratislavský
    LANGSTROTH("LANGSTROTH", "hive.frame.langstroth", "44.8×23.2 cm"),
    DADANT("DADANT", "hive.frame.dadant", "42×26.5 cm"),      // Dadant-Blatt
    ZANDER("ZANDER", "hive.frame.zander", "42×22 cm");

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
     * @param code String code (e.g., "LANGSTROTH")
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
