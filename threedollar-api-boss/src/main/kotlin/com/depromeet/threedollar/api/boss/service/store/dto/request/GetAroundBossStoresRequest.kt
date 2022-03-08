package com.depromeet.threedollar.api.boss.service.store.dto.request

import javax.validation.constraints.PositiveOrZero

data class GetAroundBossStoresRequest(
    @field:PositiveOrZero(message = "{store.distance.positiveOrZero}")
    val distanceKm: Double = -0.1
)