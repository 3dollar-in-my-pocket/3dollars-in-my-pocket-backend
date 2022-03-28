package com.depromeet.threedollar.api.core.service.boss.store.dto.request

import com.depromeet.threedollar.api.core.service.boss.store.dto.type.BossStoreOrderType
import javax.validation.constraints.Max
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class GetAroundBossStoresRequest(
    @field:PositiveOrZero(message = "{store.distance.positiveOrZero}")
    val distanceKm: Double = -0.1,

    val orderType: BossStoreOrderType = BossStoreOrderType.DISTANCE_ASC,

    val categoryId: String?,

    @field:Max(value = 50, message = "{store.limit.max}")
    @field:Positive(message = "{store.limit.positive")
    val limit: Int = 50
)
