package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import com.depromeet.threedollar.common.model.TimeInterval
import com.depromeet.threedollar.common.type.DayOfTheWeek
import java.time.LocalTime

data class BossStoreAppearanceDay(
    val dayOfTheWeek: DayOfTheWeek,
    val openingHours: TimeInterval,
    val locationDescription: String,
) {

    companion object {
        fun of(
            dayOfTheWeek: DayOfTheWeek,
            startTime: LocalTime,
            endTime: LocalTime,
            locationDescription: String = "",
        ): BossStoreAppearanceDay {
            return BossStoreAppearanceDay(
                dayOfTheWeek = dayOfTheWeek,
                openingHours = TimeInterval.of(startTime, endTime),
                locationDescription = locationDescription
            )
        }
    }

}
