package com.wimix.automation.core.utils;

import org.joda.time.DateTime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.wimix.automation.core.configuration.SentryConfig.getDeviceTimeZone;

public class DateTimeUtils {

    public static String getDateTimeString(String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, new Locale("en"));
        ZonedDateTime zonedDateTimeLocal = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        return zonedDateTimeLocal.format(dateTimeFormatter);
    }

    public static String getStartOfCurrentDay() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(getDeviceTimeZone()));
        int offsetSeconds = zonedDateTime.getOffset().getTotalSeconds();
        LocalDate date = LocalDate.from(zonedDateTime);
        return date.atStartOfDay().minusSeconds(offsetSeconds).format(dateTimeFormatter);
    }

    public static String getCurrentTime() {
        return DateTime.now().toString("HH:mm", Locale.forLanguageTag("en"));
    }

    public static String getCurrentDateAndYear() {
        return DateTime.now().toString("dd MMM yyyy", Locale.forLanguageTag("en"));
    }

    public static String getCurrentDateAndTime() {
        return DateTime.now().toString("dd MMM yyyy HH:mm", Locale.forLanguageTag("en"));
    }

    public static String getCurrentDate() {
        return DateTime.now().toString("E, dd MMM", Locale.forLanguageTag("en"));
    }

    public static String getCurrentYear() {
        return DateTime.now().toString("yyyy", Locale.forLanguageTag("en"));
    }

    public static String getZonedDateAndTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        return zonedDateTime.format(dateTimeFormatter);
    }
}