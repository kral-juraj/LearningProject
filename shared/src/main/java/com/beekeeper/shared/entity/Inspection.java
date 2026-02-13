package com.beekeeper.shared.entity;

import java.io.Serializable;

/**
 * Inspection (Prehliadka) entity.
 * Represents a hive inspection record.
 * Foreign key: hiveId references Hive.id
 */
public class Inspection implements Serializable {

    private String id;
    private String hiveId;
    private long inspectionDate;
    private double temperature;

    // Odhad sily
    private int strengthEstimate; // 1-10 scale

    // Zásoby
    private double foodStoresKg;

    // Plod
    private int broodFrames;
    private int cappedBroodDm; // decimeters
    private int uncappedBroodDm;

    // Pel a rámiky
    private int pollenFrames;
    private int totalFrames;

    // Matka
    private boolean queenSeen;
    private String queenNote;

    // Klieštik
    private boolean varroa;
    private int varroaCount;

    // Správanie
    private int aggression; // 1-5 scale
    private String behavior;

    // Poznámky
    private String notes;

    // Audio/Video
    private String recordingId;
    private boolean extractedFromAudio;

    private long createdAt;
    private long updatedAt;

    public Inspection() {
    }

    // Getters and Setters
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

    public long getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(long inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getStrengthEstimate() {
        return strengthEstimate;
    }

    public void setStrengthEstimate(int strengthEstimate) {
        this.strengthEstimate = strengthEstimate;
    }

    public double getFoodStoresKg() {
        return foodStoresKg;
    }

    public void setFoodStoresKg(double foodStoresKg) {
        this.foodStoresKg = foodStoresKg;
    }

    public int getBroodFrames() {
        return broodFrames;
    }

    public void setBroodFrames(int broodFrames) {
        this.broodFrames = broodFrames;
    }

    public int getCappedBroodDm() {
        return cappedBroodDm;
    }

    public void setCappedBroodDm(int cappedBroodDm) {
        this.cappedBroodDm = cappedBroodDm;
    }

    public int getUncappedBroodDm() {
        return uncappedBroodDm;
    }

    public void setUncappedBroodDm(int uncappedBroodDm) {
        this.uncappedBroodDm = uncappedBroodDm;
    }

    public int getPollenFrames() {
        return pollenFrames;
    }

    public void setPollenFrames(int pollenFrames) {
        this.pollenFrames = pollenFrames;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public boolean isQueenSeen() {
        return queenSeen;
    }

    public void setQueenSeen(boolean queenSeen) {
        this.queenSeen = queenSeen;
    }

    public String getQueenNote() {
        return queenNote;
    }

    public void setQueenNote(String queenNote) {
        this.queenNote = queenNote;
    }

    public boolean isVarroa() {
        return varroa;
    }

    public void setVarroa(boolean varroa) {
        this.varroa = varroa;
    }

    public int getVarroaCount() {
        return varroaCount;
    }

    public void setVarroaCount(int varroaCount) {
        this.varroaCount = varroaCount;
    }

    public int getAggression() {
        return aggression;
    }

    public void setAggression(int aggression) {
        this.aggression = aggression;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(String recordingId) {
        this.recordingId = recordingId;
    }

    public boolean isExtractedFromAudio() {
        return extractedFromAudio;
    }

    public void setExtractedFromAudio(boolean extractedFromAudio) {
        this.extractedFromAudio = extractedFromAudio;
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
