package com.beekeeper.shared.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for date/time formatting and calculations.
 * Uses DateFormatManager for user-configurable date formats.
 *
 * Use case: Consistent date formatting across entire application
 * according to user preferences (e.g., dd.MM.yyyy vs MM/dd/yyyy).
 */
public class DateUtils {

    private static final Locale LOCALE = new Locale("sk", "SK");

    /**
     * Formats timestamp as date string using user-configured format.
     *
     * Use case: Display dates in TableView columns (31.12.2026).
     *
     * @param timestamp Unix timestamp in milliseconds
     * @return Formatted date string (e.g., "31.12.2026")
     */
    public static String formatDate(long timestamp) {
        DateFormatManager dfm = DateFormatManager.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dfm.getDateFormat(), LOCALE);
        return sdf.format(new Date(timestamp));
    }

    /**
     * Formats timestamp as datetime string using user-configured format.
     *
     * Use case: Display full datetime in lists (31.12.2026 14:30).
     *
     * @param timestamp Unix timestamp in milliseconds
     * @return Formatted datetime string (e.g., "31.12.2026 14:30")
     */
    public static String formatDateTime(long timestamp) {
        DateFormatManager dfm = DateFormatManager.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dfm.getDateTimeFormat(), LOCALE);
        return sdf.format(new Date(timestamp));
    }

    /**
     * Formats timestamp as time string using user-configured format.
     *
     * Use case: Display time-only values (14:30).
     *
     * @param timestamp Unix timestamp in milliseconds
     * @return Formatted time string (e.g., "14:30")
     */
    public static String formatTime(long timestamp) {
        DateFormatManager dfm = DateFormatManager.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dfm.getTimeFormat(), LOCALE);
        return sdf.format(new Date(timestamp));
    }

    /**
     * Gets current timestamp in milliseconds.
     *
     * @return Current Unix timestamp
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Adds specified number of days to timestamp.
     *
     * Use case: Calculate future dates (e.g., queen rearing milestones).
     *
     * @param timestamp Starting timestamp
     * @param days Number of days to add
     * @return New timestamp
     */
    public static long addDays(long timestamp, int days) {
        return timestamp + (days * 24L * 60L * 60L * 1000L);
    }

    /**
     * Calculates number of days between two timestamps.
     *
     * Use case: Show age of inspection, days since last feeding, etc.
     *
     * @param startTimestamp Start timestamp
     * @param endTimestamp End timestamp
     * @return Number of days between timestamps
     */
    public static int daysBetween(long startTimestamp, long endTimestamp) {
        return (int) ((endTimestamp - startTimestamp) / (24L * 60L * 60L * 1000L));
    }
}
