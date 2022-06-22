package com.depromeet.threedollar.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateTimeUtils {

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    public static long toTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZONE_ID).toEpochSecond();
    }

    public static LocalDateTime epochToLocalDateTime(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochMilli), ZONE_ID);
    }

}
