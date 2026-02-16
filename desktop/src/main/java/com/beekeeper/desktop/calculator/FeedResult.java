package com.beekeeper.desktop.calculator;

/**
 * Result object for feed calculator.
 * Contains calculated amounts of feed, sugar, water, and preparation instructions.
 */
public class FeedResult {
    private double neededKg;
    private double neededLiters;
    private double sugarKg;
    private double waterLiters;
    private String instructions;

    public FeedResult() {
    }

    public FeedResult(double neededKg, double neededLiters, double sugarKg,
                      double waterLiters, String instructions) {
        this.neededKg = neededKg;
        this.neededLiters = neededLiters;
        this.sugarKg = sugarKg;
        this.waterLiters = waterLiters;
        this.instructions = instructions;
    }

    // Getters and Setters
    public double getNeededKg() {
        return neededKg;
    }

    public void setNeededKg(double neededKg) {
        this.neededKg = neededKg;
    }

    public double getNeededLiters() {
        return neededLiters;
    }

    public void setNeededLiters(double neededLiters) {
        this.neededLiters = neededLiters;
    }

    public double getSugarKg() {
        return sugarKg;
    }

    public void setSugarKg(double sugarKg) {
        this.sugarKg = sugarKg;
    }

    public double getWaterLiters() {
        return waterLiters;
    }

    public void setWaterLiters(double waterLiters) {
        this.waterLiters = waterLiters;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
