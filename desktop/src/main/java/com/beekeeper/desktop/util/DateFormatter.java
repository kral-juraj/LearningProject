package com.beekeeper.desktop.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Centrálny formátovač dátumov pre celú aplikáciu.
 *
 * Všetky dátumy v aplikácii sa zobrazujú v jednotnom formáte: dd.MM.yyyy (napr. 31.12.2026)
 *
 * Use case: Konzistentné zobrazenie dátumov v tabuľkách, dialógoch, kalkulačkách.
 * Budúcnosť: User preferences pre výber formátu dátumu.
 */
public class DateFormatter {

    /**
     * Predvolený formát dátumu pre celú aplikáciu: dd.MM.yyyy
     * Príklad: 31.12.2026
     */
    private static final DateTimeFormatter DEFAULT_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("sk", "SK"));

    /**
     * Formát dátumu s časom: dd.MM.yyyy HH:mm
     * Príklad: 31.12.2026 14:30
     */
    private static final DateTimeFormatter DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", new Locale("sk", "SK"));

    /**
     * Formátuje LocalDate do reťazca podľa predvoleného formátu.
     *
     * @param date Dátum na formátovanie
     * @return Reťazec vo formáte dd.MM.yyyy (napr. "31.12.2026")
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DEFAULT_FORMAT);
    }

    /**
     * Formátuje timestamp (milliseconds) do reťazca podľa predvoleného formátu.
     *
     * @param timestamp Timestamp v milisekundách od epoch
     * @return Reťazec vo formáte dd.MM.yyyy (napr. "31.12.2026")
     */
    public static String format(long timestamp) {
        LocalDate date = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return format(date);
    }

    /**
     * Formátuje timestamp s časom do reťazca.
     *
     * @param timestamp Timestamp v milisekundách od epoch
     * @return Reťazec vo formáte dd.MM.yyyy HH:mm (napr. "31.12.2026 14:30")
     */
    public static String formatWithTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .format(DATETIME_FORMAT);
    }

    /**
     * Vráti predvolený DateTimeFormatter pre použitie v JavaFX komponentoch.
     *
     * @return DateTimeFormatter s formátom dd.MM.yyyy
     */
    public static DateTimeFormatter getDefaultFormatter() {
        return DEFAULT_FORMAT;
    }

    /**
     * Vráti DateTimeFormatter s časom pre použitie v JavaFX komponentoch.
     *
     * @return DateTimeFormatter s formátom dd.MM.yyyy HH:mm
     */
    public static DateTimeFormatter getDateTimeFormatter() {
        return DATETIME_FORMAT;
    }
}
