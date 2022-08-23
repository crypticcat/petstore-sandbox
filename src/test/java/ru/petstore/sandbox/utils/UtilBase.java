package ru.petstore.sandbox.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UtilBase {
    public static String getExpireDateInString(String pattern, String zoneId, int plusHours) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        Date dateNow = new Date();
        ZonedDateTime expireDate = ZonedDateTime.ofInstant(dateNow.toInstant(), ZoneId.of(zoneId))
                .plusHours(plusHours);
        return expireDate.format(formatter);
    }
}
