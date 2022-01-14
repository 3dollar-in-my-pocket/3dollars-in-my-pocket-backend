package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.common.type.DayOfTheWeek
import java.time.LocalTime

class BossStoreAppearanceDay(
    val day: DayOfTheWeek,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val address: String = ""
)
