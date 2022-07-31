package com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.common.utils.distance.LocationDistanceUtils
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpen

data class BossStoreAroundInfoResponse(
    val bossStoreId: String,
    val name: String,
    val location: LocationResponse?,
    val menus: List<BossStoreMenuResponse>,
    val categories: Set<BossStoreCategoryResponse>,
    val openStatus: BossStoreOpenStatusResponse,
    val totalFeedbacksCounts: Int,
    val distance: Int,
) : AuditingTimeResponse() {

    companion object {
        fun of(
            bossStore: BossStore,
            categories: List<BossStoreCategoryResponse>,
            bossStoreOpen: BossStoreOpen?,
            totalFeedbacksCounts: Int?,
            deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
        ): BossStoreAroundInfoResponse {
            val response = BossStoreAroundInfoResponse(
                bossStoreId = bossStore.id,
                name = bossStore.name,
                location = bossStore.location?.let { LocationResponse.of(it) },
                menus = bossStore.menus.map { menu -> BossStoreMenuResponse.of(menu) },
                categories = categories.toSet(),
                openStatus = bossStoreOpen?.let { BossStoreOpenStatusResponse.open(it) }
                    ?: BossStoreOpenStatusResponse.close(),
                totalFeedbacksCounts = totalFeedbacksCounts ?: 0,
                distance = LocationDistanceUtils.getDistanceM(
                    LocationValue.of(bossStore.location?.latitude ?: 0.0,
                        bossStore.location?.longitude ?: 0.0
                    ), deviceLocation)
            )
            response.setAuditingTimeByDocument(bossStore)
            return response
        }
    }

}
