package com.beekeeper.desktop.calculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Result object for Varroa mite projection calculations.
 * Contains initial conditions, projection data points, and treatment recommendations.
 */
public class VarroaProjection {
    private int initialCount;
    private long measurementDate;
    private int projectionDays;
    private List<DataPoint> dataPoints;
    private String recommendation;
    private String status; // "OK", "VAROVANIE", "KRITICKÉ"
    private long criticalDate; // When action is needed

    public VarroaProjection() {
        this.dataPoints = new ArrayList<>();
    }

    public VarroaProjection(int initialCount, long measurementDate, int projectionDays) {
        this.initialCount = initialCount;
        this.measurementDate = measurementDate;
        this.projectionDays = projectionDays;
        this.dataPoints = new ArrayList<>();
    }

    // Getters and Setters
    public int getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(int initialCount) {
        this.initialCount = initialCount;
    }

    public long getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(long measurementDate) {
        this.measurementDate = measurementDate;
    }

    public int getProjectionDays() {
        return projectionDays;
    }

    public void setProjectionDays(int projectionDays) {
        this.projectionDays = projectionDays;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCriticalDate() {
        return criticalDate;
    }

    public void setCriticalDate(long criticalDate) {
        this.criticalDate = criticalDate;
    }

    /**
     * Represents a single data point in the projection timeline.
     */
    public static class DataPoint {
        private int day;
        private int count;
        private long date;

        public DataPoint(int day, int count, long date) {
            this.day = day;
            this.count = count;
            this.date = date;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }
    }
}
