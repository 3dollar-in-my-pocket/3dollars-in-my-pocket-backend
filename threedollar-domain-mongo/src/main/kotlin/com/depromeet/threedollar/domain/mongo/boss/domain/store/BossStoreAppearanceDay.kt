package com.depromeet.threedollar.domain.mongo.boss.domain.store

import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.common.domain.TimeInterval

data class BossStoreAppearanceDay(
        val dayOfTheWeek: DayOfTheWeek,
        val openingHours: TimeInterval,
        val locationDescription: String = ""
)
