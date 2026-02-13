package com.beekeeper.shared.entity;

import java.io.Serializable;

/**
 * Feeding (Krmenie) entity.
 * Represents a hive feeding record.
 * Foreign key: hiveId references Hive.id
 */
public class Feeding implements Serializable {

    private String id;
    private String hiveId;
    private long feedingDate;

    // Hmotnosť
    private double weightBefore; // kg
    private double weightAfter; // kg

    // Krmivo
    private String feedType; // SYRUP_1_1, SYRUP_3_2, FONDANT, POLLEN_PATTY
    private double amountKg;

    private String notes;
    private long createdAt;

    public Feeding() {
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

    public long getFeedingDate() {
        return feedingDate;
    }

    public void setFeedingDate(long feedingDate) {
        this.feedingDate = feedingDate;
    }

    public double getWeightBefore() {
        return weightBefore;
    }

    public void setWeightBefore(double weightBefore) {
        this.weightBefore = weightBefore;
    }

    public double getWeightAfter() {
        return weightAfter;
    }

    public void setWeightAfter(double weightAfter) {
        this.weightAfter = weightAfter;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public double getAmountKg() {
        return amountKg;
    }

    public void setAmountKg(double amountKg) {
        this.amountKg = amountKg;
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
}
