package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.document.common.document.TimeInterval

data class BossStoreAppearanceDay(
    val dayOfTheWeek: DayOfTheWeek,
    val openingHours: TimeInterval,
    val locationDescription: String = ""
)
