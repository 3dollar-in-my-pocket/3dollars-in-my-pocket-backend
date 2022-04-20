package com.depromeet.threedollar.domain.mongo.boss.domain.store

import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.TestFixture
import com.depromeet.threedollar.domain.mongo.common.domain.TimeInterval
import java.time.LocalTime

@TestFixture
class BossStoreAppearanceDayCreator {

    companion object {
        fun create(
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
