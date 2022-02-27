package com.depromeet.threedollar.boss.api.service.store.dto.request

import javax.validation.constraints.PositiveOrZero

data class GetAroundBossStoresRequest(
    @PositiveOrZero(message = "{store.distance.positiveOrZero}")
    val distanceKm: Double
)
