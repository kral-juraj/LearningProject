package com.beekeeper.shared.entity;

import java.io.Serializable;

/**
 * HiveActivity entity represents a historical record of changes and events for a hive.
 *
 * Use case: Track equipment changes (supers, grids), major events (harvest, treatment),
 * and modifications to the hive throughout the season.
 *
 * Foreign key: hiveId references Hive.id
 */
public class HiveActivity implements Serializable {

    private String id;
    private String hiveId;              // Foreign key to Hive
    private String activityType;        // HiveActivityType enum code
    private long activityDate;          // Timestamp of activity
    private String description;         // User notes about the activity
    private String oldValue;            // Previous value (e.g., "5 frames")
    private String newValue;            // New value (e.g., "8 frames")
    private long createdAt;
    private long updatedAt;

    public HiveActivity() {
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

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public long getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(long activityDate) {
        this.activityDate = activityDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
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
