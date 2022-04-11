package com.depromeet.threedollar.domain.mongo.boss.domain.store

import java.time.LocalTime
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.common.domain.TimeInterval

data class BossStoreAppearanceDay(
    val dayOfTheWeek: DayOfTheWeek,
    val openingHours: TimeInterval,
    val locationDescription: String
) {

    companion object {
        fun of(
            dayOfTheWeek: DayOfTheWeek,
            startTime: LocalTime,
            endTime: LocalTime,
            locationDescription: String = ""
        ): BossStoreAppearanceDay {
            return BossStoreAppearanceDay(
                dayOfTheWeek = dayOfTheWeek,
                openingHours = TimeInterval.of(startTime, endTime),
                locationDescription = locationDescription
            )
        }
    }

}
