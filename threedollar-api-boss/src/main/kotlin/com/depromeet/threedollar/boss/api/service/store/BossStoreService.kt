package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.boss.api.service.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossStoreService(
    private val bossStoreRepository: BossStoreRepository
) {

    @Transactional(readOnly = true)
    fun getNearBossStores(
        mapCoordinate: CoordinateValue,
        distanceKm: Double
    ): List<BossStoreInfoResponse> {
        return bossStoreRepository.findNearBossStores(
            latitude = mapCoordinate.latitude,
            longitude = mapCoordinate.longitude,
            maxDistance = distanceKm
        ).map { BossStoreInfoResponse.of(it) }
    }

}
