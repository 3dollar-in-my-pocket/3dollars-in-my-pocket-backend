package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.boss.api.service.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import com.depromeet.threedollar.redis.boss.domain.store.BossStoreOpenInfoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossStoreRetrieveService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenInfoRepository: BossStoreOpenInfoRepository
) {

    @Transactional(readOnly = true)
    fun getNearBossStores(mapCoordinate: CoordinateValue, distanceKm: Double): List<BossStoreInfoResponse> {
        val bossStores = bossStoreRepository.findNearBossStores(
            latitude = mapCoordinate.latitude,
            longitude = mapCoordinate.longitude,
            maxDistance = distanceKm
        )
        val categoriesMap = bossStoreCategoryRepository.findAll()
            .map { it.id to it }
            .toMap()

        val openStatusMap = bossStoreOpenInfoRepository.findAllById(bossStores.map { it.id })
            .map { it.bossStoreId to it }
            .toMap()

        return bossStores.map { it ->
            BossStoreInfoResponse.of(
                bossStore = it,
                categories = it.categoriesIds.asSequence()
                    .filter { categoriesMap.containsKey(it) }
                    .map { categoriesMap[it]!! }
                    .toList(),
                bossStoreOpenInfo = openStatusMap[it.id]
            )
        }
    }

    @Transactional(readOnly = true)
    fun getMyBossStore(bossId: String): BossStoreInfoResponse {
        val bossStore = BossStoreServiceUtils.findBossStoreByBossId(bossStoreRepository, bossId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            categories = bossStoreCategoryRepository.findCategoriesByIds(bossStore.categoriesIds),
            bossStoreOpenInfo = bossStoreOpenInfoRepository.findByIdOrNull(bossStore.id)
        )
    }

    @Transactional(readOnly = true)
    fun getBossStore(storeId: String): BossStoreInfoResponse {
        val bossStore = BossStoreServiceUtils.findBossStoreById(bossStoreRepository, storeId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            categories = bossStoreCategoryRepository.findCategoriesByIds(bossStore.categoriesIds),
            bossStoreOpenInfo = bossStoreOpenInfoRepository.findByIdOrNull(bossStore.id)
        )
    }

}
