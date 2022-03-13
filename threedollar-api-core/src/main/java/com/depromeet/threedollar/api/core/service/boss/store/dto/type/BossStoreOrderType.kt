package com.depromeet.threedollar.api.core.service.boss.store.dto.type

import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreInfoResponse

enum class BossStoreOrderType(
    val description: String,
    val sorted: Comparator<BossStoreInfoResponse>
) {

    DISTANCE_ASC("가까운 거리순", Comparator.comparing<BossStoreInfoResponse, Int> { it.distance }.reversed())

}
