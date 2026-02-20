package com.beekeeper.shared.util;

import com.beekeeper.shared.dto.TaxationFilters;
import com.beekeeper.shared.entity.Taxation;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for filtering taxations and calculating derived metrics.
 *
 * Use case: Beekeeper wants to filter taxations by complex criteria including
 * calculated values like free space and colony strength.
 *
 * Key calculations:
 * - Free space = (totalFrames × frameSizeDm) - (pollen + stores + brood)
 * - Colony strength = (totalCappedBroodDm + totalUncappedBroodDm) / 40
 */
public class TaxationFilterUtil {

    /**
     * Frame sizes in dm² (decimeters squared) by hive type.
     *
     * Physical dimensions (width × height in mm):
     * All frames have 420 mm width, height varies by type.
     *
     * Langstroth:
     * - LANGSTROTH_1_2: 420 × 135 mm = 56,700 mm² = 5.67 dm²
     * - LANGSTROTH_2_3: 420 × 176 mm = 73,920 mm² = 7.39 dm² (najčastejšie medník)
     * - LANGSTROTH_3_4: 420 × 203 mm = 85,260 mm² = 8.53 dm²
     * - LANGSTROTH_1_1: 420 × 232 mm = 97,440 mm² = 9.74 dm² (original)
     * - LANGSTROTH_JUMBO: 420 × 285 mm = 119,700 mm² = 11.97 dm²
     *
     * Dadant:
     * - DADANT_PLODISKO: 420 × 300 mm = 126,000 mm² = 12.6 dm²
     * - DADANT_MEDNIK_1_2: 420 × 145 mm = 60,900 mm² = 6.09 dm²
     * - DADANT_BLATT: 420 × 470 mm = 197,400 mm² = 19.74 dm²
     *
     * Zander:
     * - ZANDER_CELY: 420 × 220 mm = 92,400 mm² = 9.24 dm²
     * - ZANDER_2_3: 420 × 159 mm = 66,780 mm² = 6.68 dm²
     * - ZANDER_1_2: 420 × 110 mm = 46,200 mm² = 4.62 dm²
     * - ZANDER_1_5: 420 × 337 mm = 141,540 mm² = 14.15 dm²
     *
     * B (Brda):
     * - B: 420 × 275 mm = 115,500 mm² = 11.55 dm²
     *
     * Note: 1 dm² = 100 cm² = 10,000 mm²
     * Values stored × 10 for integer precision (e.g., 115 represents 11.5 dm²)
     */
    private static final Map<String, Integer> FRAME_SIZE_DM_X10 = new HashMap<>();

    static {
        // B (Brda)
        FRAME_SIZE_DM_X10.put("B", 115);

        // Langstroth variants
        FRAME_SIZE_DM_X10.put("LANGSTROTH_1_2", 57);      // 5.67 dm² × 10 ≈ 57
        FRAME_SIZE_DM_X10.put("LANGSTROTH_2_3", 74);      // 7.39 dm² × 10 ≈ 74
        FRAME_SIZE_DM_X10.put("LANGSTROTH_3_4", 85);      // 8.53 dm² × 10 ≈ 85
        FRAME_SIZE_DM_X10.put("LANGSTROTH_1_1", 97);      // 9.74 dm² × 10 ≈ 97
        FRAME_SIZE_DM_X10.put("LANGSTROTH_JUMBO", 120);   // 11.97 dm² × 10 ≈ 120
        FRAME_SIZE_DM_X10.put("LANGSTROTH", 97);          // Alias for 1/1 (backward compatibility)

        // Dadant variants
        FRAME_SIZE_DM_X10.put("DADANT_PLODISKO", 126);    // 12.6 dm² × 10 = 126
        FRAME_SIZE_DM_X10.put("DADANT_MEDNIK_1_2", 61);   // 6.09 dm² × 10 ≈ 61
        FRAME_SIZE_DM_X10.put("DADANT_BLATT", 197);       // 19.74 dm² × 10 ≈ 197
        FRAME_SIZE_DM_X10.put("DADANT", 126);             // Alias for plodisko (backward compatibility)

        // Zander variants
        FRAME_SIZE_DM_X10.put("ZANDER_CELY", 92);         // 9.24 dm² × 10 ≈ 92
        FRAME_SIZE_DM_X10.put("ZANDER_2_3", 67);          // 6.68 dm² × 10 ≈ 67
        FRAME_SIZE_DM_X10.put("ZANDER_1_2", 46);          // 4.62 dm² × 10 ≈ 46
        FRAME_SIZE_DM_X10.put("ZANDER_1_5", 142);         // 14.15 dm² × 10 ≈ 142
        FRAME_SIZE_DM_X10.put("ZANDER", 92);              // Alias for celý (backward compatibility)
    }

    /**
     * Checks if a taxation matches the given filters.
     *
     * Use case: ViewModel applies filters to list of taxations.
     *
     * @param taxation Taxation entity to check
     * @param hiveType Hive type (B, LANGSTROTH, DADANT, ZANDER) - required for free space calculation
     * @param filters Filter criteria (null fields are ignored)
     * @return true if taxation matches all active filters
     */
    public static boolean matches(Taxation taxation, String hiveType, TaxationFilters filters) {
        if (filters == null || !filters.hasAnyFilter()) {
            return true;  // No filters = match all
        }

        // Date range filter
        if (filters.getDateFrom() != null) {
            if (taxation.getTaxationDate() < filters.getDateFrom()) {
                return false;
            }
        }
        if (filters.getDateTo() != null) {
            if (taxation.getTaxationDate() > filters.getDateTo()) {
                return false;
            }
        }

        // Hive name filter (partial, case-insensitive)
        if (filters.getHiveNameFilter() != null && !filters.getHiveNameFilter().trim().isEmpty()) {
            String filterText = filters.getHiveNameFilter().toLowerCase().trim();
            String hiveName = taxation.getHiveName() != null ? taxation.getHiveName().toLowerCase() : "";
            if (!hiveName.contains(filterText)) {
                return false;
            }
        }

        // Free space filter
        if (filters.getMinFreeSpaceDm() != null || filters.getMaxFreeSpaceDm() != null) {
            int freeSpace = calculateFreeSpaceDm(taxation, hiveType);
            if (freeSpace == -1) {
                return false;  // Unknown frame type = exclude
            }
            if (filters.getMinFreeSpaceDm() != null && freeSpace < filters.getMinFreeSpaceDm()) {
                return false;
            }
            if (filters.getMaxFreeSpaceDm() != null && freeSpace > filters.getMaxFreeSpaceDm()) {
                return false;
            }
        }

        // Has brood filter
        if (filters.getHasBrood() != null) {
            boolean hasBrood = hasBrood(taxation);
            if (filters.getHasBrood() != hasBrood) {
                return false;
            }
        }

        // Has starter frames filter
        if (filters.getHasStarterFrames() != null) {
            boolean hasStarters = taxation.getTotalStarterFrames() > 0;
            if (filters.getHasStarterFrames() != hasStarters) {
                return false;
            }
        }

        // Free space percentage filter
        if (filters.getMinFreeSpacePercent() != null || filters.getMaxFreeSpacePercent() != null) {
            int freeSpacePercent = calculateFreeSpacePercent(taxation, hiveType);
            if (freeSpacePercent == -1) {
                return false;  // Unknown frame type = exclude
            }
            if (filters.getMinFreeSpacePercent() != null && freeSpacePercent < filters.getMinFreeSpacePercent()) {
                return false;
            }
            if (filters.getMaxFreeSpacePercent() != null && freeSpacePercent > filters.getMaxFreeSpacePercent()) {
                return false;
            }
        }

        // Capped stores percentage filter
        if (filters.getMinCappedStoresPercent() != null || filters.getMaxCappedStoresPercent() != null) {
            int cappedStoresPercent = calculateCappedStoresPercent(taxation, hiveType);
            if (cappedStoresPercent == -1) {
                return false;  // Unknown frame type = exclude
            }
            if (filters.getMinCappedStoresPercent() != null && cappedStoresPercent < filters.getMinCappedStoresPercent()) {
                return false;
            }
            if (filters.getMaxCappedStoresPercent() != null && cappedStoresPercent > filters.getMaxCappedStoresPercent()) {
                return false;
            }
        }

        // Temperature range filter
        if (filters.getMinTemperature() != null) {
            if (taxation.getTemperature() < filters.getMinTemperature()) {
                return false;
            }
        }
        if (filters.getMaxTemperature() != null) {
            if (taxation.getTemperature() > filters.getMaxTemperature()) {
                return false;
            }
        }

        // Frame count range filter
        if (filters.getMinFrameCount() != null) {
            if (taxation.getTotalFrames() < filters.getMinFrameCount()) {
                return false;
            }
        }
        if (filters.getMaxFrameCount() != null) {
            if (taxation.getTotalFrames() > filters.getMaxFrameCount()) {
                return false;
            }
        }

        // Colony strength range filter
        if (filters.getMinStrength() != null || filters.getMaxStrength() != null) {
            int strength = calculateColonyStrength(taxation);
            if (filters.getMinStrength() != null && strength < filters.getMinStrength()) {
                return false;
            }
            if (filters.getMaxStrength() != null && strength > filters.getMaxStrength()) {
                return false;
            }
        }

        // Brood range filter (total capped + uncapped)
        if (filters.getMinBroodDm() != null || filters.getMaxBroodDm() != null) {
            int totalBrood = taxation.getTotalCappedBroodDm() + taxation.getTotalUncappedBroodDm();
            if (filters.getMinBroodDm() != null && totalBrood < filters.getMinBroodDm()) {
                return false;
            }
            if (filters.getMaxBroodDm() != null && totalBrood > filters.getMaxBroodDm()) {
                return false;
            }
        }

        // Stores range filter (total capped + uncapped)
        if (filters.getMinStoresDm() != null || filters.getMaxStoresDm() != null) {
            int totalStores = taxation.getTotalCappedStoresDm() + taxation.getTotalUncappedStoresDm();
            if (filters.getMinStoresDm() != null && totalStores < filters.getMinStoresDm()) {
                return false;
            }
            if (filters.getMaxStoresDm() != null && totalStores > filters.getMaxStoresDm()) {
                return false;
            }
        }

        // Pollen range filter
        if (filters.getMinPollenDm() != null || filters.getMaxPollenDm() != null) {
            int pollen = taxation.getTotalPollenDm();
            if (filters.getMinPollenDm() != null && pollen < filters.getMinPollenDm()) {
                return false;
            }
            if (filters.getMaxPollenDm() != null && pollen > filters.getMaxPollenDm()) {
                return false;
            }
        }

        return true;  // All filters passed
    }

    /**
     * Calculates free space in dm² for a taxation.
     *
     * Formula: Free Space = (totalFrames × frameSizeDm) - (pollen + stores + brood)
     *
     * Use case: Beekeeper wants to identify hives with available space for expansion
     * or hives that are running out of space and need supering.
     *
     * @param taxation Taxation entity
     * @param hiveType Hive type (B, LANGSTROTH, DADANT, ZANDER)
     * @return Free space in dm², or -1 if hiveType is unknown or totalFrames is null
     */
    public static int calculateFreeSpaceDm(Taxation taxation, String hiveType) {
        if (hiveType == null || taxation.getTotalFrames() == 0) {
            return -1;
        }

        Integer frameSizeDmX10 = FRAME_SIZE_DM_X10.get(hiveType.toUpperCase());
        if (frameSizeDmX10 == null) {
            return -1;  // Unknown frame type
        }

        // Calculate capacity: frames × size (divide by 10 to get dm²)
        int totalCapacityDm = (taxation.getTotalFrames() * frameSizeDmX10) / 10;

        int occupiedDm = 0;
        occupiedDm += taxation.getTotalPollenDm();
        occupiedDm += taxation.getTotalCappedStoresDm();
        occupiedDm += taxation.getTotalUncappedStoresDm();
        occupiedDm += taxation.getTotalCappedBroodDm();
        occupiedDm += taxation.getTotalUncappedBroodDm();

        return totalCapacityDm - occupiedDm;
    }

    /**
     * Calculates colony strength on a scale of 1-10.
     *
     * Formula: Strength = (totalCappedBroodDm + totalUncappedBroodDm) / 40
     *
     * Use case: Beekeeper wants to assess colony health and compare hive performance.
     *
     * Scale interpretation:
     * - 1-2: Very weak (needs attention)
     * - 3-4: Weak (monitor closely)
     * - 5-6: Average
     * - 7-8: Strong
     * - 9-10: Very strong (ready for splits/honey production)
     *
     * @param taxation Taxation entity
     * @return Strength value 1-10, or 0 if no brood data available
     */
    public static int calculateColonyStrength(Taxation taxation) {
        int totalBroodDm = taxation.getTotalCappedBroodDm() + taxation.getTotalUncappedBroodDm();

        if (totalBroodDm == 0) {
            return 0;  // No brood = strength unknown
        }

        int strength = totalBroodDm / 40;
        return Math.min(10, Math.max(1, strength));  // Clamp to 1-10
    }

    /**
     * Checks if taxation has any brood (capped or uncapped).
     *
     * @param taxation Taxation entity
     * @return true if brood is present
     */
    public static boolean hasBrood(Taxation taxation) {
        return (taxation.getTotalCappedBroodDm() + taxation.getTotalUncappedBroodDm()) > 0;
    }

    /**
     * Gets the frame size in dm² for a given hive type.
     *
     * @param hiveType Hive type (B, LANGSTROTH, DADANT, ZANDER)
     * @return Frame size in dm² (as integer, e.g., 4 for 4.2 dm²), or -1 if unknown
     */
    public static int getFrameSizeDm(String hiveType) {
        if (hiveType == null) {
            return -1;
        }
        Integer sizeX10 = FRAME_SIZE_DM_X10.get(hiveType.toUpperCase());
        return sizeX10 != null ? sizeX10 / 10 : -1;
    }

    /**
     * Calculates free space as percentage of total hive capacity.
     *
     * Formula: (freeSpaceDm / totalCapacityDm) × 100
     *
     * Use case: Filter hives with low free space (< 20%) that need expansion.
     * Percentage is more flexible than absolute dm² values - works across all hive sizes.
     *
     * @param taxation Taxation entity
     * @param hiveType Hive type (B, LANGSTROTH, DADANT, ZANDER)
     * @return Free space percentage (0-100), or -1 if hiveType is unknown
     */
    public static int calculateFreeSpacePercent(Taxation taxation, String hiveType) {
        if (hiveType == null || taxation.getTotalFrames() == 0) {
            return -1;
        }

        Integer frameSizeDmX10 = FRAME_SIZE_DM_X10.get(hiveType.toUpperCase());
        if (frameSizeDmX10 == null) {
            return -1;  // Unknown frame type
        }

        int totalCapacityDm = (taxation.getTotalFrames() * frameSizeDmX10) / 10;
        if (totalCapacityDm == 0) {
            return -1;
        }

        int freeSpaceDm = calculateFreeSpaceDm(taxation, hiveType);
        if (freeSpaceDm == -1) {
            return -1;
        }

        // Calculate percentage, handle negative values
        int percent = (freeSpaceDm * 100) / totalCapacityDm;
        return Math.max(-100, Math.min(100, percent));  // Clamp to -100 to 100
    }

    /**
     * Calculates capped stores as percentage of total hive capacity.
     *
     * Formula: (cappedStoresDm / totalCapacityDm) × 100
     *
     * Use case: Identify hives ready for honey extraction (> 30% capped stores).
     * Percentage adapts to hive size - 30% means different dm² in LANGSTROTH vs DADANT.
     *
     * @param taxation Taxation entity
     * @param hiveType Hive type (B, LANGSTROTH, DADANT, ZANDER)
     * @return Capped stores percentage (0-100+), or -1 if hiveType is unknown
     */
    public static int calculateCappedStoresPercent(Taxation taxation, String hiveType) {
        if (hiveType == null || taxation.getTotalFrames() == 0) {
            return -1;
        }

        Integer frameSizeDmX10 = FRAME_SIZE_DM_X10.get(hiveType.toUpperCase());
        if (frameSizeDmX10 == null) {
            return -1;  // Unknown frame type
        }

        int totalCapacityDm = (taxation.getTotalFrames() * frameSizeDmX10) / 10;
        if (totalCapacityDm == 0) {
            return -1;
        }

        int cappedStoresDm = taxation.getTotalCappedStoresDm();
        int percent = (cappedStoresDm * 100) / totalCapacityDm;
        return Math.max(0, Math.min(200, percent));  // Clamp to 0-200 (allow > 100% for validation errors)
    }
}
