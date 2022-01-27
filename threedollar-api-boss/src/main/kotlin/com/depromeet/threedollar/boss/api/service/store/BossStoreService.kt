package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.boss.api.service.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossStoreService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
) {

    @Transactional(readOnly = true)
    fun getNearBossStores(
        mapCoordinate: CoordinateValue,
        distanceKm: Double
    ): List<BossStoreInfoResponse> {
        val bossStores = bossStoreRepository.findNearBossStores(
            latitude = mapCoordinate.latitude,
            longitude = mapCoordinate.longitude,
            maxDistance = distanceKm
        )
        val categoriesMap = bossStoreCategoryRepository.findAll()
            .map { it.id to it.title }
            .toMap()

        return bossStores.map { it ->
            BossStoreInfoResponse.of(it, it.categoriesIds.asSequence()
                .filter { categoriesMap.containsKey(it) }
                .map { categoriesMap[it] ?: "" }
                .toList()
            )
        }
    }

    @Transactional(readOnly = true)
    fun getMyBossStore(
        bossId: String
    ): BossStoreInfoResponse {
        val bossStore = BossStoreServiceUtils.findBossStoreByBossId(bossStoreRepository, bossId)
        val categories = bossStoreCategoryRepository.findAllById(bossStore.categoriesIds)
            .map { it.title }
        return BossStoreInfoResponse.of(bossStore, categories)
    }

}
