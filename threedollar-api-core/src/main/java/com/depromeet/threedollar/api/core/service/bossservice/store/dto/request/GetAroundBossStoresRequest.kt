package com.depromeet.threedollar.api.core.service.bossservice.store.dto.request

import com.depromeet.threedollar.api.core.service.bossservice.store.dto.type.BossStoreOrderType
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.PositiveOrZero

data class GetAroundBossStoresRequest(
    @field:PositiveOrZero(message = "{store.distance.positiveOrZero}")
    val distanceKm: Double = -0.1,

    val orderType: BossStoreOrderType = BossStoreOrderType.DISTANCE_ASC,

    val categoryId: String?,

    @field:Min(value = 1, message = "{common.size.min}")
    @field:Max(value = 30, message = "{common.size.max}")
    val size: Int = 30,
)
