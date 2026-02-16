package com.beekeeper.desktop.calculator;

/**
 * Represents a milestone in queen rearing timeline.
 * Each milestone has a name, day offset from start, date, description, and color coding.
 */
public class QueenMilestone {
    private String name;
    private int dayOffset; // D+0, D+5, D+7, etc.
    private long date; // Actual date in milliseconds
    private String description;
    private String color; // "green", "yellow", "orange", "red"

    public QueenMilestone() {
    }

    public QueenMilestone(String name, int dayOffset, long date, String description, String color) {
        this.name = name;
        this.dayOffset = dayOffset;
        this.date = date;
        this.description = description;
        this.color = color;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDayOffset() {
        return dayOffset;
    }

    public void setDayOffset(int dayOffset) {
        this.dayOffset = dayOffset;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Returns formatted day offset (e.g., "D+0", "D+7").
     */
    public String getFormattedDayOffset() {
        return "D+" + dayOffset;
    }
}
