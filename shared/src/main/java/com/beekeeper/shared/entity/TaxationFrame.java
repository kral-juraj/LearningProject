package com.beekeeper.shared.entity;

import java.io.Serializable;

/**
 * TaxationFrame (Rámik taxácie) entity.
 * Represents detailed information about a single frame during taxation.
 * Foreign key: taxationId references Taxation.id
 */
public class TaxationFrame implements Serializable {

    private String id;
    private String taxationId;
    private int position; // position in hive (1-25)

    // Plod
    private int cappedBroodDm;      // Zavíčkovaný plod
    private int uncappedBroodDm;    // Otvorený plod

    // Peľ
    private int pollenDm;

    // Zásoby (med)
    private int cappedStoresDm;     // Zavíčkované zásoby
    private int uncappedStoresDm;   // Nezavíčkované zásoby

    // Typ rámika
    private String frameType; // BROOD, HONEY, FOUNDATION, DRAWN, DARK
    private int frameYear;
    private boolean isStarter; // stavebný rámik

    // Špeciálne označenia
    private boolean hasQueen;
    private boolean hasCage;
    private boolean hasNucBox; // opačnenec
    private String notes;

    public TaxationFrame() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxationId() {
        return taxationId;
    }

    public void setTaxationId(String taxationId) {
        this.taxationId = taxationId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public int getPollenDm() {
        return pollenDm;
    }

    public void setPollenDm(int pollenDm) {
        this.pollenDm = pollenDm;
    }

    public int getCappedStoresDm() {
        return cappedStoresDm;
    }

    public void setCappedStoresDm(int cappedStoresDm) {
        this.cappedStoresDm = cappedStoresDm;
    }

    public int getUncappedStoresDm() {
        return uncappedStoresDm;
    }

    public void setUncappedStoresDm(int uncappedStoresDm) {
        this.uncappedStoresDm = uncappedStoresDm;
    }

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    public int getFrameYear() {
        return frameYear;
    }

    public void setFrameYear(int frameYear) {
        this.frameYear = frameYear;
    }

    public boolean isStarter() {
        return isStarter;
    }

    public void setStarter(boolean starter) {
        isStarter = starter;
    }

    public boolean isHasQueen() {
        return hasQueen;
    }

    public void setHasQueen(boolean hasQueen) {
        this.hasQueen = hasQueen;
    }

    public boolean isHasCage() {
        return hasCage;
    }

    public void setHasCage(boolean hasCage) {
        this.hasCage = hasCage;
    }

    public boolean isHasNucBox() {
        return hasNucBox;
    }

    public void setHasNucBox(boolean hasNucBox) {
        this.hasNucBox = hasNucBox;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
