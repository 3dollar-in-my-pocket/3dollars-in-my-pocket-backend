package com.depromeet.threedollar.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateTimeUtils {

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    public static long toTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZONE_ID).toEpochSecond();
    }

    public static LocalDateTime epochToLocalDateTime(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochMilli), ZONE_ID);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE_ID);
    }

}
