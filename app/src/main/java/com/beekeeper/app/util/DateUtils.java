package com.beekeeper.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final Locale LOCALE = new Locale("sk", "SK");

    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_DATE, LOCALE);
        return sdf.format(new Date(timestamp));
    }

    public static String formatDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_FULL, LOCALE);
        return sdf.format(new Date(timestamp));
    }

    public static String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_TIME, LOCALE);
        return sdf.format(new Date(timestamp));
    }

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public static long addDays(long timestamp, int days) {
        return timestamp + (days * 24L * 60L * 60L * 1000L);
    }

    public static int daysBetween(long startTimestamp, long endTimestamp) {
        return (int) ((endTimestamp - startTimestamp) / (24L * 60L * 60L * 1000L));
    }
}
