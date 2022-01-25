package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.boss.api.service.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.document.boss.document.store.BossStore
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import org.springframework.data.repository.findByIdOrNull
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

    @Transactional(readOnly = true)
    fun getMyBossStore(
        bossId: String
    ): BossStoreInfoResponse {
        return BossStoreInfoResponse.of(findBossStoreByBossId(bossStoreRepository, bossId))
    }

}

fun findBossStoreByBossId(bossStoreRepository: BossStoreRepository, bossId: String): BossStore {
    return bossStoreRepository.findBossStoreByBossId(bossId)
        ?: throw NotFoundException("사장님 (${bossId})이 운영중인 가게가 존재하지 않습니다")
}

fun validateExistsBossStore(bossStoreRepository: BossStoreRepository, bossStoreId: String) {
    bossStoreRepository.findByIdOrNull(bossStoreId)
        ?: throw NotFoundException("해당하는 ($bossStoreId) 가게는 존재하지 않습니다")
}
