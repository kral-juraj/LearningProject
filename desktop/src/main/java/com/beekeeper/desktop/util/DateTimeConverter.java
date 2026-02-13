package com.beekeeper.desktop.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Utility class for converting between LocalDate/time components and Unix timestamps.
 * Used for datetime fields in dialogs (DatePicker + hour/minute TextFields).
 */
public class DateTimeConverter {

    /**
     * Converts LocalDate and time components to Unix timestamp (milliseconds).
     */
    public static long toTimestamp(LocalDate date, int hour, int minute) {
        if (date == null) {
            return 0;
        }

        ZonedDateTime zdt = date.atTime(hour, minute).atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    /**
     * Extracts LocalDate from Unix timestamp.
     */
    public static LocalDate toLocalDate(long timestamp) {
        if (timestamp == 0) {
            return null;
        }

        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Extracts hour (0-23) from Unix timestamp.
     */
    public static int extractHour(long timestamp) {
        if (timestamp == 0) {
            return 0;
        }

        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .getHour();
    }

    /**
     * Extracts minute (0-59) from Unix timestamp.
     */
    public static int extractMinute(long timestamp) {
        if (timestamp == 0) {
            return 0;
        }

        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .getMinute();
    }
}
