package com.beekeeper.desktop.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeConverterTest {

    @Test
    void testToTimestamp() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        long timestamp = DateTimeConverter.toTimestamp(date, 14, 30);

        assertTrue(timestamp > 0, "Timestamp should be positive");
    }

    @Test
    void testToTimestampWithNullDate() {
        long timestamp = DateTimeConverter.toTimestamp(null, 14, 30);
        assertEquals(0, timestamp, "Timestamp should be 0 for null date");
    }

    @Test
    void testToLocalDate() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        long timestamp = DateTimeConverter.toTimestamp(date, 14, 30);

        LocalDate result = DateTimeConverter.toLocalDate(timestamp);
        assertEquals(date, result, "Date should match after roundtrip");
    }

    @Test
    void testToLocalDateWithZeroTimestamp() {
        LocalDate result = DateTimeConverter.toLocalDate(0);
        assertNull(result, "Result should be null for timestamp 0");
    }

    @Test
    void testExtractHour() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        long timestamp = DateTimeConverter.toTimestamp(date, 14, 30);

        int hour = DateTimeConverter.extractHour(timestamp);
        assertEquals(14, hour, "Hour should be 14");
    }

    @Test
    void testExtractMinute() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        long timestamp = DateTimeConverter.toTimestamp(date, 14, 30);

        int minute = DateTimeConverter.extractMinute(timestamp);
        assertEquals(30, minute, "Minute should be 30");
    }

    @Test
    void testExtractHourWithZeroTimestamp() {
        int hour = DateTimeConverter.extractHour(0);
        assertEquals(0, hour, "Hour should be 0 for timestamp 0");
    }

    @Test
    void testExtractMinuteWithZeroTimestamp() {
        int minute = DateTimeConverter.extractMinute(0);
        assertEquals(0, minute, "Minute should be 0 for timestamp 0");
    }

    @Test
    void testRoundtripAtMidnight() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        long timestamp = DateTimeConverter.toTimestamp(date, 0, 0);

        assertEquals(0, DateTimeConverter.extractHour(timestamp), "Hour should be 0 at midnight");
        assertEquals(0, DateTimeConverter.extractMinute(timestamp), "Minute should be 0 at midnight");
    }

    @Test
    void testRoundtripAtEndOfDay() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        long timestamp = DateTimeConverter.toTimestamp(date, 23, 59);

        assertEquals(23, DateTimeConverter.extractHour(timestamp), "Hour should be 23");
        assertEquals(59, DateTimeConverter.extractMinute(timestamp), "Minute should be 59");
    }
}
