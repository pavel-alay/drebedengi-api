package com.alay.drebedengi.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {
    private static final DateTimeFormatter DREBEDENGI_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DREBEDENGI_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String dateToString(LocalDate date) {
        return date.format(DREBEDENGI_DATE);
    }

    public static String dateToString(LocalDateTime time) {
        return time.format(DREBEDENGI_DATE_TIME);
    }

    public static LocalDate stringToDate(String date) {
        return LocalDate.parse(date, DREBEDENGI_DATE);
    }

    public static LocalDateTime stringToDateTime(String date) {
        return LocalDateTime.parse(date, DREBEDENGI_DATE_TIME);
    }
}
