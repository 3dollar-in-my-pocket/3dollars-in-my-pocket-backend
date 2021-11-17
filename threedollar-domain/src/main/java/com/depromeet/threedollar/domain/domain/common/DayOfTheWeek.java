package com.depromeet.threedollar.domain.domain.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DayOfTheWeek {

    MONDAY(DayOfWeek.MONDAY),
    TUESDAY(DayOfWeek.TUESDAY),
    WEDNESDAY(DayOfWeek.WEDNESDAY),
    THURSDAY(DayOfWeek.THURSDAY),
    FRIDAY(DayOfWeek.FRIDAY),
    SATURDAY(DayOfWeek.SATURDAY),
    SUNDAY(DayOfWeek.SUNDAY);

    private final DayOfWeek dayOfWeek;

}
