package com.beekeeper.shared.util;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DateUtils utility class.
 */
class DateUtilsTest {

    private static final Locale LOCALE = new Locale("sk", "SK");

    @Test
    void testFormatDate() throws ParseException {
        // Parse date to get platform-independent timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", LOCALE);
        Date date = sdf.parse("14.02.2025 10:30");
        long timestamp = date.getTime();

        String formatted = DateUtils.formatDate(timestamp);

        // Should format as "14.02.2025"
        assertEquals("14.02.2025", formatted);
    }

    @Test
    void testFormatDateTime() throws ParseException {
        // Parse date to get platform-independent timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", LOCALE);
        Date date = sdf.parse("14.02.2025 10:30");
        long timestamp = date.getTime();

        String formatted = DateUtils.formatDateTime(timestamp);

        // Should format as "14.02.2025 10:30"
        assertEquals("14.02.2025 10:30", formatted);
    }

    @Test
    void testFormatTime() throws ParseException {
        // Parse date to get platform-independent timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", LOCALE);
        Date date = sdf.parse("14.02.2025 10:30");
        long timestamp = date.getTime();

        String formatted = DateUtils.formatTime(timestamp);

        // Should format as "10:30"
        assertEquals("10:30", formatted);
    }

    @Test
    void testGetCurrentTimestamp() {
        long before = System.currentTimeMillis();
        long timestamp = DateUtils.getCurrentTimestamp();
        long after = System.currentTimeMillis();

        // Timestamp should be between before and after
        assertTrue(timestamp >= before);
        assertTrue(timestamp <= after);
    }

    @Test
    void testAddDays() {
        long timestamp = System.currentTimeMillis();
        long oneDayLater = DateUtils.addDays(timestamp, 1);

        // Should add 24 hours (86400000 milliseconds)
        assertEquals(timestamp + 86400000L, oneDayLater);
    }

    @Test
    void testAddDaysNegative() {
        long timestamp = System.currentTimeMillis();
        long oneDayEarlier = DateUtils.addDays(timestamp, -1);

        // Should subtract 24 hours
        assertEquals(timestamp - 86400000L, oneDayEarlier);
    }

    @Test
    void testDaysBetween() {
        long start = System.currentTimeMillis();
        long end = start + (3 * 86400000L); // 3 days later

        int days = DateUtils.daysBetween(start, end);

        assertEquals(3, days);
    }

    @Test
    void testDaysBetweenSameDay() {
        long timestamp = System.currentTimeMillis();

        int days = DateUtils.daysBetween(timestamp, timestamp);

        assertEquals(0, days);
    }

    @Test
    void testDaysBetweenNegative() {
        long start = System.currentTimeMillis();
        long end = start - (2 * 86400000L); // 2 days earlier

        int days = DateUtils.daysBetween(start, end);

        assertEquals(-2, days);
    }
}
