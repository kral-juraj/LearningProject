package com.beekeeper.shared.util;

/**
 * Manages application-wide date format settings.
 * Thread-safe singleton providing configurable date/time formatting.
 *
 * Use case: All UI components use this manager to format dates consistently
 * according to user preferences.
 *
 * Default formats match Slovak conventions:
 * - Date: dd.MM.yyyy (31.12.2026)
 * - Time: HH:mm (14:30)
 * - DateTime: dd.MM.yyyy HH:mm (31.12.2026 14:30)
 */
public class DateFormatManager {
    private static DateFormatManager instance;

    private String dateFormat;
    private String timeFormat;
    private String dateTimeFormat;

    // Default formats (Slovak convention)
    private static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm";
    private static final String DEFAULT_DATETIME_FORMAT = "dd.MM.yyyy HH:mm";

    private DateFormatManager() {
        // Initialize with defaults
        this.dateFormat = DEFAULT_DATE_FORMAT;
        this.timeFormat = DEFAULT_TIME_FORMAT;
        this.dateTimeFormat = DEFAULT_DATETIME_FORMAT;
    }

    /**
     * Gets singleton instance of DateFormatManager.
     * Thread-safe lazy initialization.
     *
     * @return DateFormatManager instance
     */
    public static synchronized DateFormatManager getInstance() {
        if (instance == null) {
            instance = new DateFormatManager();
        }
        return instance;
    }

    /**
     * Loads date formats from settings loader (JDBC, Room, etc.).
     * If settings are empty or null, uses default formats.
     *
     * Use case: Called once during application startup in Main.java
     *
     * @param dateFormat Custom date format pattern (e.g., "dd.MM.yyyy")
     * @param timeFormat Custom time format pattern (e.g., "HH:mm")
     * @param dateTimeFormat Custom datetime format pattern (e.g., "dd.MM.yyyy HH:mm")
     */
    public void loadFormats(String dateFormat, String timeFormat, String dateTimeFormat) {
        this.dateFormat = (dateFormat != null && !dateFormat.isEmpty())
            ? dateFormat : DEFAULT_DATE_FORMAT;
        this.timeFormat = (timeFormat != null && !timeFormat.isEmpty())
            ? timeFormat : DEFAULT_TIME_FORMAT;
        this.dateTimeFormat = (dateTimeFormat != null && !dateTimeFormat.isEmpty())
            ? dateTimeFormat : DEFAULT_DATETIME_FORMAT;
    }

    /**
     * Gets current date format pattern.
     *
     * Use case: DateUtils uses this to format date-only values.
     *
     * @return Date format pattern (e.g., "dd.MM.yyyy")
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Gets current time format pattern.
     *
     * Use case: DateUtils uses this to format time-only values.
     *
     * @return Time format pattern (e.g., "HH:mm")
     */
    public String getTimeFormat() {
        return timeFormat;
    }

    /**
     * Gets current datetime format pattern.
     *
     * Use case: DateUtils uses this to format combined date+time values.
     *
     * @return DateTime format pattern (e.g., "dd.MM.yyyy HH:mm")
     */
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * Sets new date format pattern.
     * Used by Settings dialog when user changes format preference.
     *
     * @param dateFormat New date format pattern
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = (dateFormat != null && !dateFormat.isEmpty())
            ? dateFormat : DEFAULT_DATE_FORMAT;
    }

    /**
     * Sets new time format pattern.
     * Used by Settings dialog when user changes format preference.
     *
     * @param timeFormat New time format pattern
     */
    public void setTimeFormat(String timeFormat) {
        this.timeFormat = (timeFormat != null && !timeFormat.isEmpty())
            ? timeFormat : DEFAULT_TIME_FORMAT;
    }

    /**
     * Sets new datetime format pattern.
     * Used by Settings dialog when user changes format preference.
     *
     * @param dateTimeFormat New datetime format pattern
     */
    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = (dateTimeFormat != null && !dateTimeFormat.isEmpty())
            ? dateTimeFormat : DEFAULT_DATETIME_FORMAT;
    }

    /**
     * Resets all formats to defaults.
     *
     * Use case: Settings dialog "Reset to defaults" button.
     */
    public void resetToDefaults() {
        this.dateFormat = DEFAULT_DATE_FORMAT;
        this.timeFormat = DEFAULT_TIME_FORMAT;
        this.dateTimeFormat = DEFAULT_DATETIME_FORMAT;
    }

    /**
     * Validates if a format string is valid SimpleDateFormat pattern.
     *
     * Use case: Settings dialog validates user input before saving.
     *
     * @param formatPattern Format pattern to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidFormat(String formatPattern) {
        if (formatPattern == null || formatPattern.trim().isEmpty()) {
            return false;
        }

        try {
            new java.text.SimpleDateFormat(formatPattern);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
