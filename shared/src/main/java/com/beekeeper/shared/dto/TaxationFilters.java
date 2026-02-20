package com.beekeeper.shared.dto;

import java.io.Serializable;

/**
 * Data Transfer Object for taxation filtering criteria.
 *
 * Use case: Beekeeper wants to filter taxations by various criteria:
 * - Date range (from-to)
 * - Hive name (partial match)
 * - Free space availability
 * - Presence of brood, starter frames
 * - Temperature, frame count ranges
 * - Colony strength
 *
 * All fields are nullable. Null means "no filter applied" for that criterion.
 */
public class TaxationFilters implements Serializable {
    private static final long serialVersionUID = 1L;

    // Date range
    private Long dateFrom;  // Timestamp in milliseconds
    private Long dateTo;    // Timestamp in milliseconds

    // Hive identification
    private String hiveNameFilter;  // Partial, case-insensitive match

    // Free space (dm²)
    private Integer minFreeSpaceDm;
    private Integer maxFreeSpaceDm;

    // Brood presence
    private Boolean hasBrood;  // True = has brood, False = no brood, null = any

    // Starter frames (queen cells)
    private Boolean hasStarterFrames;

    // Free space percentage (% of total capacity)
    private Integer minFreeSpacePercent;
    private Integer maxFreeSpacePercent;

    // Capped stores percentage (% of total capacity)
    private Integer minCappedStoresPercent;
    private Integer maxCappedStoresPercent;

    // Temperature range (°C)
    private Double minTemperature;
    private Double maxTemperature;

    // Frame count range
    private Integer minFrameCount;
    private Integer maxFrameCount;

    // Colony strength (1-10 scale)
    private Integer minStrength;
    private Integer maxStrength;

    // Brood range (dm²) - total capped + uncapped brood
    private Integer minBroodDm;
    private Integer maxBroodDm;

    // Stores range (dm²) - total capped + uncapped stores
    private Integer minStoresDm;
    private Integer maxStoresDm;

    // Pollen range (dm²)
    private Integer minPollenDm;
    private Integer maxPollenDm;

    // Constructors
    public TaxationFilters() {
    }

    // Getters and Setters

    public Long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Long getDateTo() {
        return dateTo;
    }

    public void setDateTo(Long dateTo) {
        this.dateTo = dateTo;
    }

    public String getHiveNameFilter() {
        return hiveNameFilter;
    }

    public void setHiveNameFilter(String hiveNameFilter) {
        this.hiveNameFilter = hiveNameFilter;
    }

    public Integer getMinFreeSpaceDm() {
        return minFreeSpaceDm;
    }

    public void setMinFreeSpaceDm(Integer minFreeSpaceDm) {
        this.minFreeSpaceDm = minFreeSpaceDm;
    }

    public Integer getMaxFreeSpaceDm() {
        return maxFreeSpaceDm;
    }

    public void setMaxFreeSpaceDm(Integer maxFreeSpaceDm) {
        this.maxFreeSpaceDm = maxFreeSpaceDm;
    }

    public Boolean getHasBrood() {
        return hasBrood;
    }

    public void setHasBrood(Boolean hasBrood) {
        this.hasBrood = hasBrood;
    }

    public Boolean getHasStarterFrames() {
        return hasStarterFrames;
    }

    public void setHasStarterFrames(Boolean hasStarterFrames) {
        this.hasStarterFrames = hasStarterFrames;
    }

    public Integer getMinFreeSpacePercent() {
        return minFreeSpacePercent;
    }

    public void setMinFreeSpacePercent(Integer minFreeSpacePercent) {
        this.minFreeSpacePercent = minFreeSpacePercent;
    }

    public Integer getMaxFreeSpacePercent() {
        return maxFreeSpacePercent;
    }

    public void setMaxFreeSpacePercent(Integer maxFreeSpacePercent) {
        this.maxFreeSpacePercent = maxFreeSpacePercent;
    }

    public Integer getMinCappedStoresPercent() {
        return minCappedStoresPercent;
    }

    public void setMinCappedStoresPercent(Integer minCappedStoresPercent) {
        this.minCappedStoresPercent = minCappedStoresPercent;
    }

    public Integer getMaxCappedStoresPercent() {
        return maxCappedStoresPercent;
    }

    public void setMaxCappedStoresPercent(Integer maxCappedStoresPercent) {
        this.maxCappedStoresPercent = maxCappedStoresPercent;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Integer getMinFrameCount() {
        return minFrameCount;
    }

    public void setMinFrameCount(Integer minFrameCount) {
        this.minFrameCount = minFrameCount;
    }

    public Integer getMaxFrameCount() {
        return maxFrameCount;
    }

    public void setMaxFrameCount(Integer maxFrameCount) {
        this.maxFrameCount = maxFrameCount;
    }

    public Integer getMinStrength() {
        return minStrength;
    }

    public void setMinStrength(Integer minStrength) {
        this.minStrength = minStrength;
    }

    public Integer getMaxStrength() {
        return maxStrength;
    }

    public void setMaxStrength(Integer maxStrength) {
        this.maxStrength = maxStrength;
    }

    public Integer getMinBroodDm() {
        return minBroodDm;
    }

    public void setMinBroodDm(Integer minBroodDm) {
        this.minBroodDm = minBroodDm;
    }

    public Integer getMaxBroodDm() {
        return maxBroodDm;
    }

    public void setMaxBroodDm(Integer maxBroodDm) {
        this.maxBroodDm = maxBroodDm;
    }

    public Integer getMinStoresDm() {
        return minStoresDm;
    }

    public void setMinStoresDm(Integer minStoresDm) {
        this.minStoresDm = minStoresDm;
    }

    public Integer getMaxStoresDm() {
        return maxStoresDm;
    }

    public void setMaxStoresDm(Integer maxStoresDm) {
        this.maxStoresDm = maxStoresDm;
    }

    public Integer getMinPollenDm() {
        return minPollenDm;
    }

    public void setMinPollenDm(Integer minPollenDm) {
        this.minPollenDm = minPollenDm;
    }

    public Integer getMaxPollenDm() {
        return maxPollenDm;
    }

    public void setMaxPollenDm(Integer maxPollenDm) {
        this.maxPollenDm = maxPollenDm;
    }

    /**
     * Checks if any filter is active.
     *
     * @return true if at least one filter is set
     */
    public boolean hasAnyFilter() {
        return dateFrom != null
                || dateTo != null
                || hiveNameFilter != null
                || minFreeSpaceDm != null
                || maxFreeSpaceDm != null
                || hasBrood != null
                || hasStarterFrames != null
                || minFreeSpacePercent != null
                || maxFreeSpacePercent != null
                || minCappedStoresPercent != null
                || maxCappedStoresPercent != null
                || minTemperature != null
                || maxTemperature != null
                || minFrameCount != null
                || maxFrameCount != null
                || minStrength != null
                || maxStrength != null
                || minBroodDm != null
                || maxBroodDm != null
                || minStoresDm != null
                || maxStoresDm != null
                || minPollenDm != null
                || maxPollenDm != null;
    }

    @Override
    public String toString() {
        return "TaxationFilters{" +
                "dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", hiveNameFilter='" + hiveNameFilter + '\'' +
                ", minFreeSpaceDm=" + minFreeSpaceDm +
                ", maxFreeSpaceDm=" + maxFreeSpaceDm +
                ", hasBrood=" + hasBrood +
                ", hasStarterFrames=" + hasStarterFrames +
                ", minFreeSpacePercent=" + minFreeSpacePercent +
                ", maxFreeSpacePercent=" + maxFreeSpacePercent +
                ", minCappedStoresPercent=" + minCappedStoresPercent +
                ", maxCappedStoresPercent=" + maxCappedStoresPercent +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                ", minFrameCount=" + minFrameCount +
                ", maxFrameCount=" + maxFrameCount +
                ", minStrength=" + minStrength +
                ", maxStrength=" + maxStrength +
                '}';
    }
}
