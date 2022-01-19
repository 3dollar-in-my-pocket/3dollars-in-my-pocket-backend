package com.depromeet.threedollar.common.type;

import com.depromeet.threedollar.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DayOfTheWeek implements EnumModel {

    MONDAY(DayOfWeek.MONDAY, false, "월요일"),
    TUESDAY(DayOfWeek.TUESDAY, false, "화요일"),
    WEDNESDAY(DayOfWeek.WEDNESDAY, false, "수요일"),
    THURSDAY(DayOfWeek.THURSDAY, false, "목요일"),
    FRIDAY(DayOfWeek.FRIDAY, false, "금요일"),
    SATURDAY(DayOfWeek.SATURDAY, true, "토요일"),
    SUNDAY(DayOfWeek.SUNDAY, true, "일요일");

    private final DayOfWeek dayOfWeek;
    private final boolean isWeekend;
    private final String description;

    @Override
    public String getKey() {
        return name();
    }

}
