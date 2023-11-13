package ru.practicum.shareit.util;

import java.time.format.DateTimeFormatter;

public class Formatter {
    public static final String PATTERN_FOR_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SS";
    public static final DateTimeFormatter FORMATTER_FOR_DATETIME = DateTimeFormatter.ofPattern(PATTERN_FOR_DATETIME);

    public static final String PATTERN_FOR_BOOKING = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateTimeFormatter FORMATTER_FOR_BOOKING = DateTimeFormatter.ofPattern(PATTERN_FOR_BOOKING);
}
