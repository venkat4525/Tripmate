package com.tripmate.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Utility for parsing, formatting, and computing dates across TripMate.
 */
public class DateUtil {

    private static final String INPUT_FORMAT = "yyyy-MM-dd";
    private static final String DISPLAY_FORMAT = "MMM dd, yyyy";
    private static final String DATETIME_DISPLAY_FORMAT = "MMM dd, yyyy hh:mm a";

    /**
     * Parses a date string from an HTML5 date input (yyyy-MM-dd).
     */
    public static Date parseInputDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return new SimpleDateFormat(INPUT_FORMAT).parse(dateStr.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Formats a Date to yyyy-MM-dd for HTML5 date input fields.
     */
    public static String formatInputDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(INPUT_FORMAT).format(date);
    }

    /**
     * Formats a Date for friendly display (e.g. "Jul 10, 2026").
     */
    public static String formatDisplayDate(Date date) {
        if (date == null) return "N/A";
        return new SimpleDateFormat(DISPLAY_FORMAT).format(date);
    }

    /**
     * Formats a Date and Time for friendly display (e.g. "Jul 10, 2026 10:00 AM").
     */
    public static String formatDisplayDateTime(Date date) {
        if (date == null) return "N/A";
        return new SimpleDateFormat(DATETIME_DISPLAY_FORMAT).format(date);
    }

    /**
     * Calculates the number of days between two dates (inclusive).
     */
    public static long calculateDurationDays(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) return 0;
        long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
    }
}
