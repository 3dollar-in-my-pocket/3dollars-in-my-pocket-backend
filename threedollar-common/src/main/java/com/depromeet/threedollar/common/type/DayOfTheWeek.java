package com.depromeet.threedollar.common.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DayOfTheWeek {

    MONDAY(DayOfWeek.MONDAY, false),
    TUESDAY(DayOfWeek.TUESDAY, false),
    WEDNESDAY(DayOfWeek.WEDNESDAY, false),
    THURSDAY(DayOfWeek.THURSDAY, false),
    FRIDAY(DayOfWeek.FRIDAY, false),
    SATURDAY(DayOfWeek.SATURDAY, true),
    SUNDAY(DayOfWeek.SUNDAY, true);

    private final DayOfWeek dayOfWeek;
    private final boolean isWeekend;

}
