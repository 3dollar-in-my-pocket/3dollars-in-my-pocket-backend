package com.depromeet.threedollar.api.core.service.bossservice.store.dto.type

import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreAroundInfoResponse

enum class BossStoreOrderType(
    val description: String,
    val sorted: Comparator<BossStoreAroundInfoResponse>,
) {

    DISTANCE_ASC("가까운 거리순", Comparator.comparing<BossStoreAroundInfoResponse, Int> { it.distance }),
    TOTAL_FEEDBACKS_COUNTS_DESC("총 피드백가 많은 순", Comparator.comparing<BossStoreAroundInfoResponse, Int> { it.totalFeedbacksCounts }.reversed())

}
