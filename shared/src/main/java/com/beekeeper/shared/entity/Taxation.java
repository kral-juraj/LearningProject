package com.beekeeper.shared.entity;

import java.io.Serializable;

/**
 * Taxation (Taxácia) entity.
 * Represents a detailed hive assessment with frame-by-frame analysis.
 * Foreign key: hiveId references Hive.id
 */
public class Taxation implements Serializable {

    private String id;
    private String hiveId;
    private long taxationDate;
    private double temperature;
    private int totalFrames;
    private double foodStoresKg;
    private String notes;
    private long createdAt;
    private long updatedAt;

    // Agregované dáta zo všetkých rámikov
    private int totalPollenDm;
    private int totalCappedStoresDm;
    private int totalUncappedStoresDm;
    private int totalCappedBroodDm;
    private int totalUncappedBroodDm;
    private int totalStarterFrames;     // Počet stavebných rámikov

    // Ďalšie informácie (nie v DB, ale načítané cez JOIN)
    private String hiveName;

    public Taxation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHiveId() {
        return hiveId;
    }

    public void setHiveId(String hiveId) {
        this.hiveId = hiveId;
    }

    public long getTaxationDate() {
        return taxationDate;
    }

    public void setTaxationDate(long taxationDate) {
        this.taxationDate = taxationDate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public double getFoodStoresKg() {
        return foodStoresKg;
    }

    public void setFoodStoresKg(double foodStoresKg) {
        this.foodStoresKg = foodStoresKg;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getTotalPollenDm() {
        return totalPollenDm;
    }

    public void setTotalPollenDm(int totalPollenDm) {
        this.totalPollenDm = totalPollenDm;
    }

    public int getTotalCappedStoresDm() {
        return totalCappedStoresDm;
    }

    public void setTotalCappedStoresDm(int totalCappedStoresDm) {
        this.totalCappedStoresDm = totalCappedStoresDm;
    }

    public int getTotalUncappedStoresDm() {
        return totalUncappedStoresDm;
    }

    public void setTotalUncappedStoresDm(int totalUncappedStoresDm) {
        this.totalUncappedStoresDm = totalUncappedStoresDm;
    }

    public int getTotalCappedBroodDm() {
        return totalCappedBroodDm;
    }

    public void setTotalCappedBroodDm(int totalCappedBroodDm) {
        this.totalCappedBroodDm = totalCappedBroodDm;
    }

    public int getTotalUncappedBroodDm() {
        return totalUncappedBroodDm;
    }

    public void setTotalUncappedBroodDm(int totalUncappedBroodDm) {
        this.totalUncappedBroodDm = totalUncappedBroodDm;
    }

    public int getTotalStarterFrames() {
        return totalStarterFrames;
    }

    public void setTotalStarterFrames(int totalStarterFrames) {
        this.totalStarterFrames = totalStarterFrames;
    }

    public String getHiveName() {
        return hiveName;
    }

    public void setHiveName(String hiveName) {
        this.hiveName = hiveName;
    }
}
