package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.document.common.document.TimeInterval

class BossStoreAppearanceDay(
    val day: DayOfTheWeek,
    val openTime: TimeInterval,
    val locationDescription: String = ""
)
