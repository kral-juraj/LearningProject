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
}
