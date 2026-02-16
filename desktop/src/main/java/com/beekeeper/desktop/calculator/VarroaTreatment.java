package com.beekeeper.desktop.calculator;

/**
 * Represents a Varroa treatment or mechanical intervention.
 * Used to model the effect of treatments on mite population over time.
 */
public class VarroaTreatment {
    private String id;
    private long treatmentDate; // Timestamp when treatment is applied
    private String treatmentType; // Type of treatment (e.g., "Tymol", "Kyselina mravčia")
    private double effectiveness; // 0.0 to 1.0 (percentage of mites eliminated)
    private String description; // Optional description

    public VarroaTreatment() {
    }

    public VarroaTreatment(String id, long treatmentDate, String treatmentType,
                           double effectiveness, String description) {
        this.id = id;
        this.treatmentDate = treatmentDate;
        this.treatmentType = treatmentType;
        this.effectiveness = effectiveness;
        this.description = description;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(long treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public double getEffectiveness() {
        return effectiveness;
    }

    public void setEffectiveness(double effectiveness) {
        this.effectiveness = effectiveness;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get effectiveness as percentage (0-100).
     */
    public double getEffectivenessPercent() {
        return effectiveness * 100.0;
    }

    /**
     * Set effectiveness from percentage (0-100).
     */
    public void setEffectivenessPercent(double percent) {
        this.effectiveness = percent / 100.0;
    }
}
