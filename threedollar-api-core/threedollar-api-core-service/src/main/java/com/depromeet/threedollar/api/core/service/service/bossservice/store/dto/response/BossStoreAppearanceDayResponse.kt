package com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response

import com.depromeet.threedollar.common.model.TimeInterval
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDay

data class BossStoreAppearanceDayResponse(
    val dayOfTheWeek: DayOfTheWeek,
    val openingHours: TimeInterval,
    val locationDescription: String,
) {

    companion object {
        fun of(appearanceDay: BossStoreAppearanceDay): BossStoreAppearanceDayResponse {
            return BossStoreAppearanceDayResponse(
                dayOfTheWeek = appearanceDay.dayOfTheWeek,
                openingHours = appearanceDay.openingHours,
                locationDescription = appearanceDay.locationDescription
            )
        }
    }

}
