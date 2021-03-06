package com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.common.utils.distance.LocationDistanceUtils
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpen

data class BossStoreInfoResponse(
    val bossStoreId: String,
    val name: String,
    val location: LocationResponse?,
    val imageUrl: String?,
    val introduction: String?,
    val contactsNumber: String?,
    val snsUrl: String?,
    val menus: List<BossStoreMenuResponse>,
    val appearanceDays: Set<BossStoreAppearanceDayResponse>,
    val categories: Set<BossStoreCategoryResponse>,
    val openStatus: BossStoreOpenStatusResponse,
    val distance: Int,
) : AuditingTimeResponse() {

    companion object {
        fun of(
            bossStore: BossStore,
            categories: List<BossStoreCategoryResponse>,
            bossStoreOpen: BossStoreOpen?,
            deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
        ): BossStoreInfoResponse {
            val response = BossStoreInfoResponse(
                bossStoreId = bossStore.id,
                name = bossStore.name,
                location = bossStore.location?.let { LocationResponse.of(it) },
                imageUrl = bossStore.imageUrl,
                introduction = bossStore.introduction,
                contactsNumber = bossStore.contactsNumber?.getNumberWithSeparator(),
                snsUrl = bossStore.snsUrl,
                menus = bossStore.menus.map { menu -> BossStoreMenuResponse.of(menu) },
                appearanceDays = bossStore.appearanceDays.asSequence()
                    .map { appearanceDay -> BossStoreAppearanceDayResponse.of(appearanceDay) }
                    .toSet(),
                categories = categories.toSet(),
                openStatus = bossStoreOpen?.let { BossStoreOpenStatusResponse.open(it) }
                    ?: BossStoreOpenStatusResponse.close(),
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
