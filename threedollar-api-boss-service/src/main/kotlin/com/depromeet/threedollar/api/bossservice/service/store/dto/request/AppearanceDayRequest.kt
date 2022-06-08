package com.depromeet.threedollar.api.bossservice.service.store.dto.request

import java.time.LocalTime
import javax.validation.constraints.Size
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDay

data class AppearanceDayRequest(
    val dayOfTheWeek: DayOfTheWeek,
    val startTime: LocalTime,
    val endTime: LocalTime,

    @field:Size(max = 20, message = "{store.appearanceDay.locationDescription.size}")
    val locationDescription: String = "",
) {

    fun toAppearanceDay(): BossStoreAppearanceDay {
        return BossStoreAppearanceDay.of(
            dayOfTheWeek = dayOfTheWeek,
            startTime = startTime,
            endTime = endTime,
            locationDescription = locationDescription
        )
    }

}
