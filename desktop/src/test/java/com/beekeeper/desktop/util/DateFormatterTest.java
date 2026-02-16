package com.beekeeper.desktop.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DateFormatter.
 *
 * Tests verify correct date formatting:
 * - LocalDate → String (dd.MM.yyyy)
 * - Timestamp → String (dd.MM.yyyy)
 * - Timestamp → String with time (dd.MM.yyyy HH:mm)
 */
class DateFormatterTest {

    /**
     * Test: Formátovanie LocalDate
     *
     * Use case: Tabuľka zobrazuje dátum vo formáte dd.MM.yyyy
     *
     * Očakávaný výsledok: "31.12.2026"
     */
    @Test
    void testFormatLocalDate() {
        LocalDate date = LocalDate.of(2026, 12, 31);
        String formatted = DateFormatter.format(date);

        assertEquals("31.12.2026", formatted);
    }

    /**
     * Test: Formátovanie timestamp
     *
     * Use case: Kalkulačka zobrazuje míľníky s dátumami
     *
     * Očakávaný výsledok: Správny formát dd.MM.yyyy
     */
    @Test
    void testFormatTimestamp() {
        LocalDate date = LocalDate.of(2025, 5, 1);
        long timestamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        String formatted = DateFormatter.format(timestamp);

        assertEquals("01.05.2025", formatted);
    }

    /**
     * Test: Null LocalDate
     *
     * Use case: Nepovinný dátum nie je vyplnený
     *
     * Očakávaný výsledok: Prázdny reťazec
     */
    @Test
    void testFormatNullDate() {
        String formatted = DateFormatter.format((LocalDate) null);

        assertEquals("", formatted);
    }

    /**
     * Test: Formátovanie s časom
     *
     * Use case: Zobrazenie dátumu a času v detailných info
     *
     * Očakávaný výsledok: "31.12.2026 14:30"
     */
    @Test
    void testFormatWithTime() {
        LocalDate date = LocalDate.of(2026, 12, 31);
        long timestamp = date.atTime(14, 30)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        String formatted = DateFormatter.formatWithTime(timestamp);

        assertEquals("31.12.2026 14:30", formatted);
    }

    /**
     * Test: Konzistentný formát pre rôzne mesiace
     *
     * Use case: Overiť že jednociferné dni a mesiace majú vedúcu nulu
     *
     * Očakávaný výsledok: "01.01.2025", "09.05.2025"
     */
    @Test
    void testFormatWithLeadingZeros() {
        LocalDate date1 = LocalDate.of(2025, 1, 1);
        LocalDate date2 = LocalDate.of(2025, 5, 9);

        assertEquals("01.01.2025", DateFormatter.format(date1));
        assertEquals("09.05.2025", DateFormatter.format(date2));
    }

    /**
     * Test: Formatter instance je dostupný
     *
     * Use case: JavaFX komponenty potrebujú priamy prístup k formatteru
     *
     * Očakávaný výsledok: Formatter nie je null
     */
    @Test
    void testGetDefaultFormatter() {
        assertNotNull(DateFormatter.getDefaultFormatter());
    }

    /**
     * Test: DateTime formatter instance je dostupný
     *
     * Use case: Komponenty potrebujú formátovať dátum s časom
     *
     * Očakávaný výsledok: Formatter nie je null
     */
    @Test
    void testGetDateTimeFormatter() {
        assertNotNull(DateFormatter.getDateTimeFormatter());
    }
}
