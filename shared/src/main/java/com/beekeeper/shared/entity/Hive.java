package com.beekeeper.shared.entity;

import java.io.Serializable;

/**
 * Hive (Úľ) entity.
 * Represents a beehive within an apiary.
 * Foreign key: apiaryId references Apiary.id
 */
public class Hive implements Serializable {

    private String id;
    private String apiaryId;
    private String name;
    private String type; // VERTICAL, HORIZONTAL, NUKE
    private String queenId;
    private int queenYear;
    private String queenColor;
    private boolean active;
    private String notes;
    private long createdAt;
    private long updatedAt;

    public Hive() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiaryId() {
        return apiaryId;
    }

    public void setApiaryId(String apiaryId) {
        this.apiaryId = apiaryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQueenId() {
        return queenId;
    }

    public void setQueenId(String queenId) {
        this.queenId = queenId;
    }

    public int getQueenYear() {
        return queenYear;
    }

    public void setQueenYear(int queenYear) {
        this.queenYear = queenYear;
    }

    public String getQueenColor() {
        return queenColor;
    }

    public void setQueenColor(String queenColor) {
        this.queenColor = queenColor;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
