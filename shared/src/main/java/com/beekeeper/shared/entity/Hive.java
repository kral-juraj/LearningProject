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

    // Extended hive details
    private String frameType;           // B, LANGSTROTH, DADANT, ZANDER, FOUNDATION
    private int frameCount;             // Total number of frames in hive
    private boolean insulated;          // Zateplený
    private boolean highBottomBoard;    // Vysoké dno (true) vs nízke dno (false)
    private boolean hasQueenExcluder;   // Materská mriežka
    private boolean hasPropolisTrap;    // Propolisová mriežka
    private boolean hasEntranceReducer; // Výklz
    private boolean hasPollenTrap;      // Peľochyt
    private boolean hasTopInsulation;   // Stropná uteplivka
    private boolean hasFoil;            // Fólia

    // Frame condition tracking (detailné sledovanie stavu rámikov)
    private int darkFrames;             // Tmavé plásty
    private int lightFrames;            // Svetlé plásty
    private int newFrames;              // Nové plásty
    private int foundationFrames;       // Stavebné rámiky (s medzistienkou v rámiku)
    private int emptyFrames;            // Voľná stavba (bez medzistienky)
    private int foundationSheets;       // Voľné medzistienky (neprerámkované)

    // Queen behavior and colony health indicators
    private String aggression;          // Agresivita včiel (LOW/MEDIUM/HIGH)
    private boolean chalkBrood;         // Vápenaťenie plodu
    private boolean droneCells;         // Trudica (nadmerné trudičie bunky)
    private boolean droneLaying;        // Trudokľadná matka

    // Display order for sorting hives within apiary
    private int displayOrder;           // Poradie zobrazovania (pre drag-and-drop)

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

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public boolean isInsulated() {
        return insulated;
    }

    public void setInsulated(boolean insulated) {
        this.insulated = insulated;
    }

    public boolean isHighBottomBoard() {
        return highBottomBoard;
    }

    public void setHighBottomBoard(boolean highBottomBoard) {
        this.highBottomBoard = highBottomBoard;
    }

    public boolean isHasQueenExcluder() {
        return hasQueenExcluder;
    }

    public void setHasQueenExcluder(boolean hasQueenExcluder) {
        this.hasQueenExcluder = hasQueenExcluder;
    }

    public boolean isHasPropolisTrap() {
        return hasPropolisTrap;
    }

    public void setHasPropolisTrap(boolean hasPropolisTrap) {
        this.hasPropolisTrap = hasPropolisTrap;
    }

    public int getDarkFrames() {
        return darkFrames;
    }

    public void setDarkFrames(int darkFrames) {
        this.darkFrames = darkFrames;
    }

    public int getLightFrames() {
        return lightFrames;
    }

    public void setLightFrames(int lightFrames) {
        this.lightFrames = lightFrames;
    }

    public int getNewFrames() {
        return newFrames;
    }

    public void setNewFrames(int newFrames) {
        this.newFrames = newFrames;
    }

    public int getFoundationFrames() {
        return foundationFrames;
    }

    public void setFoundationFrames(int foundationFrames) {
        this.foundationFrames = foundationFrames;
    }

    public int getEmptyFrames() {
        return emptyFrames;
    }

    public void setEmptyFrames(int emptyFrames) {
        this.emptyFrames = emptyFrames;
    }

    public boolean isHasEntranceReducer() {
        return hasEntranceReducer;
    }

    public void setHasEntranceReducer(boolean hasEntranceReducer) {
        this.hasEntranceReducer = hasEntranceReducer;
    }

    public boolean isHasPollenTrap() {
        return hasPollenTrap;
    }

    public void setHasPollenTrap(boolean hasPollenTrap) {
        this.hasPollenTrap = hasPollenTrap;
    }

    public boolean isHasTopInsulation() {
        return hasTopInsulation;
    }

    public void setHasTopInsulation(boolean hasTopInsulation) {
        this.hasTopInsulation = hasTopInsulation;
    }

    public boolean isHasFoil() {
        return hasFoil;
    }

    public void setHasFoil(boolean hasFoil) {
        this.hasFoil = hasFoil;
    }

    public int getFoundationSheets() {
        return foundationSheets;
    }

    public void setFoundationSheets(int foundationSheets) {
        this.foundationSheets = foundationSheets;
    }

    public String getAggression() {
        return aggression;
    }

    public void setAggression(String aggression) {
        this.aggression = aggression;
    }

    public boolean isChalkBrood() {
        return chalkBrood;
    }

    public void setChalkBrood(boolean chalkBrood) {
        this.chalkBrood = chalkBrood;
    }

    public boolean isDroneCells() {
        return droneCells;
    }

    public void setDroneCells(boolean droneCells) {
        this.droneCells = droneCells;
    }

    public boolean isDroneLaying() {
        return droneLaying;
    }

    public void setDroneLaying(boolean droneLaying) {
        this.droneLaying = droneLaying;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
