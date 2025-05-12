package com.cooknote.backend.global.utils.format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy. MM. dd.");

    public static String format(LocalDate date) {
        if (date == null) return "";
        return date.format(DEFAULT_FORMATTER);
    }
}
