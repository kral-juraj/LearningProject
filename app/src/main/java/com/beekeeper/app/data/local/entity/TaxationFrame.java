package com.beekeeper.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "taxation_frames",
    foreignKeys = @ForeignKey(
        entity = Taxation.class,
        parentColumns = "id",
        childColumns = "taxationId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("taxationId")}
)
public class TaxationFrame {

    @PrimaryKey
    @NonNull
    private String id;

    private String taxationId;
    private int position; // position in hive (1-25)

    // Plod
    private int cappedBroodDm;
    private int uncappedBroodDm;

    // Pel
    private int pollenDm;

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

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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
