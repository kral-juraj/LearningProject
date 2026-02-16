package com.beekeeper.desktop.calculator;

/**
 * Parameters for Varroa destructor population modeling.
 * Based on peer-reviewed research (Martin 1998, Rosenkranz et al. 2010, Fries et al. 1994).
 *
 * All parameters are configurable to allow users to adjust the model.
 */
public class VarroaParameters {

    // ========== REPRODUCTION RATES ==========

    /** Average viable female offspring per cycle in worker cells (default: 1.3) */
    private double workerOffspring = 1.3;

    /** Average viable female offspring per cycle in drone cells (default: 2.4) */
    private double droneOffspring = 2.4;

    /** Reproductive failure rate in worker cells (default: 0.25 = 25%) */
    private double workerFailureRate = 0.25;

    /** Reproductive failure rate in drone cells (default: 0.125 = 12.5%) */
    private double droneFailureRate = 0.125;

    // ========== LIFE CYCLE DURATION ==========

    /** Length of reproductive cycle in worker cells in days (default: 12) */
    private double workerCycleLength = 12.0;

    /** Length of reproductive cycle in drone cells in days (default: 15) */
    private double droneCycleLength = 15.0;

    /** Phoretic phase duration (on adult bee) in days (default: 5.5) */
    private double phoreticPhaseDays = 5.5;

    // ========== DRONE PREFERENCE ==========

    /** Drone cell preference ratio (8-12x more attractive, default: 10) */
    private double dronePreference = 10.0;

    /** Percentage of brood that is drone brood (default: 0.10 = 10%) */
    private double droneBroodPercentage = 0.10;

    // ========== MORTALITY ==========

    /** Mortality rate per reproductive cycle (default: 0.175 = 17.5%) */
    private double mortalityPerCycle = 0.175;

    /** Phoretic mortality rate per day (default: 0.01 = 1% per day) */
    private double phoreticMortalityPerDay = 0.01;

    // ========== MITE BEHAVIOR ==========

    /** Maximum number of reproductive cycles per mite (default: 2.5) */
    private double maxReproductiveCycles = 2.5;

    /** Percentage of mites that successfully invade cells (default: 0.20 = 20%) */
    private double cellInvasionRate = 0.20;

    // ========== CONSTRUCTORS ==========

    public VarroaParameters() {
        // Default constructor with default values
    }

    /**
     * Copy constructor
     */
    public VarroaParameters(VarroaParameters other) {
        this.workerOffspring = other.workerOffspring;
        this.droneOffspring = other.droneOffspring;
        this.workerFailureRate = other.workerFailureRate;
        this.droneFailureRate = other.droneFailureRate;
        this.workerCycleLength = other.workerCycleLength;
        this.droneCycleLength = other.droneCycleLength;
        this.phoreticPhaseDays = other.phoreticPhaseDays;
        this.dronePreference = other.dronePreference;
        this.droneBroodPercentage = other.droneBroodPercentage;
        this.mortalityPerCycle = other.mortalityPerCycle;
        this.phoreticMortalityPerDay = other.phoreticMortalityPerDay;
        this.maxReproductiveCycles = other.maxReproductiveCycles;
        this.cellInvasionRate = other.cellInvasionRate;
    }

    // ========== FACTORY METHOD ==========

    /**
     * Returns a new instance with default research-based values.
     */
    public static VarroaParameters createDefault() {
        return new VarroaParameters();
    }

    // ========== GETTERS AND SETTERS ==========

    public double getWorkerOffspring() {
        return workerOffspring;
    }

    public void setWorkerOffspring(double workerOffspring) {
        this.workerOffspring = workerOffspring;
    }

    public double getDroneOffspring() {
        return droneOffspring;
    }

    public void setDroneOffspring(double droneOffspring) {
        this.droneOffspring = droneOffspring;
    }

    public double getWorkerFailureRate() {
        return workerFailureRate;
    }

    public void setWorkerFailureRate(double workerFailureRate) {
        this.workerFailureRate = workerFailureRate;
    }

    public double getDroneFailureRate() {
        return droneFailureRate;
    }

    public void setDroneFailureRate(double droneFailureRate) {
        this.droneFailureRate = droneFailureRate;
    }

    public double getWorkerCycleLength() {
        return workerCycleLength;
    }

    public void setWorkerCycleLength(double workerCycleLength) {
        this.workerCycleLength = workerCycleLength;
    }

    public double getDroneCycleLength() {
        return droneCycleLength;
    }

    public void setDroneCycleLength(double droneCycleLength) {
        this.droneCycleLength = droneCycleLength;
    }

    public double getPhoreticPhaseDays() {
        return phoreticPhaseDays;
    }

    public void setPhoreticPhaseDays(double phoreticPhaseDays) {
        this.phoreticPhaseDays = phoreticPhaseDays;
    }

    public double getDronePreference() {
        return dronePreference;
    }

    public void setDronePreference(double dronePreference) {
        this.dronePreference = dronePreference;
    }

    public double getDroneBroodPercentage() {
        return droneBroodPercentage;
    }

    public void setDroneBroodPercentage(double droneBroodPercentage) {
        this.droneBroodPercentage = droneBroodPercentage;
    }

    public double getMortalityPerCycle() {
        return mortalityPerCycle;
    }

    public void setMortalityPerCycle(double mortalityPerCycle) {
        this.mortalityPerCycle = mortalityPerCycle;
    }

    public double getPhoreticMortalityPerDay() {
        return phoreticMortalityPerDay;
    }

    public void setPhoreticMortalityPerDay(double phoreticMortalityPerDay) {
        this.phoreticMortalityPerDay = phoreticMortalityPerDay;
    }

    public double getMaxReproductiveCycles() {
        return maxReproductiveCycles;
    }

    public void setMaxReproductiveCycles(double maxReproductiveCycles) {
        this.maxReproductiveCycles = maxReproductiveCycles;
    }

    public double getCellInvasionRate() {
        return cellInvasionRate;
    }

    public void setCellInvasionRate(double cellInvasionRate) {
        this.cellInvasionRate = cellInvasionRate;
    }
}
