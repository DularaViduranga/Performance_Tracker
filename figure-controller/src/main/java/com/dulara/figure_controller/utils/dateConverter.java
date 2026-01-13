package com.dulara.figure_controller.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class dateConverter {
    private static final DateTimeFormatter INPUT =
            DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);

    // Mon Dec 01 2025  ->  01-DEC-25
    public static String toOracleFormat(String input) {
        return LocalDate.parse(input.trim(), INPUT)
                .format(DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH))
                .toUpperCase();
    }

    // Sun Nov 30 2025  ->  2025-11-30
    public static String toIsoFormat(String input) {
        return LocalDate.parse(input.trim(), INPUT)
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
