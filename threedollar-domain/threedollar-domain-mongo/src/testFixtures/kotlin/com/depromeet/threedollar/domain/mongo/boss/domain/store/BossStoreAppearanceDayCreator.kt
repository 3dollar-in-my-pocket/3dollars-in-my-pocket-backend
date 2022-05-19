package com.depromeet.threedollar.domain.mongo.boss.domain.store

import java.time.LocalTime
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.TestFixture
import com.depromeet.threedollar.common.model.TimeInterval

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
