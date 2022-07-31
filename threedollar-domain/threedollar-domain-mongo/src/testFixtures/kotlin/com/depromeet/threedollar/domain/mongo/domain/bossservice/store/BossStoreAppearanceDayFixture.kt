package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import com.depromeet.threedollar.common.model.TimeInterval
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.TestFixture
import java.time.LocalTime

@TestFixture
class BossStoreAppearanceDayFixture {

    companion object {
        fun create(
            dayOfTheWeek: DayOfTheWeek,
            startTime: LocalTime = LocalTime.of(8, 0),
            endTime: LocalTime = LocalTime.of(19, 0),
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
