package com.beekeeper.shared.entity;

/**
 * Enum representing types of beehives.
 *
 * Use case: Categorize hives by their physical structure.
 */
public enum HiveType {
    VERTICAL("VERTICAL", "hive.type.vertical"),      // Nádstavkový úľ
    HORIZONTAL("HORIZONTAL", "hive.type.horizontal"), // Ležiak
    NUKE("NUKE", "hive.type.nuke"),                  // Oddielka / Nucleus
    MATING_NUC("MATING_NUC", "hive.type.mating_nuc"); // Oplodňovačik

    private final String code;
    private final String translationKey;

    HiveType(String code, String translationKey) {
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
     * Get HiveType from code string.
     *
     * @param code String code (e.g., "VERTICAL")
     * @return HiveType enum value, or null if not found
     */
    public static HiveType fromCode(String code) {
        if (code == null) return null;
        for (HiveType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
