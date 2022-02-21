package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.boss.api.service.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategory
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.store.BossStore
import com.depromeet.threedollar.document.boss.document.store.BossStoreLocationRepository
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import com.depromeet.threedollar.redis.boss.domain.store.BossStoreOpenInfoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.min

@Service
class BossStoreRetrieveService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenInfoRepository: BossStoreOpenInfoRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository
) {

    @Transactional(readOnly = true)
    fun getNearBossStores(mapCoordinate: CoordinateValue, distanceKm: Double): List<BossStoreInfoResponse> {
        val storeLocations = bossStoreLocationRepository.findNearBossStoreLocations(
            latitude = mapCoordinate.latitude,
            longitude = mapCoordinate.longitude,
            maxDistance = min(distanceKm, MAX_DISTANCE_KM)
        )

        val locationsDictionary = storeLocations.associateBy { it.bossStoreId }

        val bossStores = bossStoreRepository.findAllById(storeLocations.map { it.bossStoreId })

        val categoriesDictionary = bossStoreCategoryRepository.findAll().associateBy { it.id }

        val openInfoDictionary =
            bossStoreOpenInfoRepository.findAllById(bossStores.map { it.id }).associateBy { it.bossStoreId }

        return bossStores.map {
            BossStoreInfoResponse.of(
                bossStore = it,
                categories = getCategory(it, categoriesDictionary),
                bossStoreOpenInfo = openInfoDictionary[it.id],
                location = locationsDictionary[it.id]?.location
            )
        }
    }

    private fun getCategory(bossStore: BossStore, categoriesDictionary: Map<String, BossStoreCategory>): List<BossStoreCategory> {
        return bossStore.categoriesIds.asSequence()
            .filter { categoriesDictionary.containsKey(it) }
            .map { categoriesDictionary[it]!! }
            .toList()
    }

    @Transactional(readOnly = true)
    fun getMyBossStore(bossId: String): BossStoreInfoResponse {
        val bossStore = BossStoreServiceUtils.findBossStoreByBossId(bossStoreRepository, bossId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            location = bossStoreLocationRepository.findBossStoreLocationByBossStoreId(bossStore.id)?.location,
            categories = bossStoreCategoryRepository.findCategoriesByIds(bossStore.categoriesIds),
            bossStoreOpenInfo = bossStoreOpenInfoRepository.findByIdOrNull(bossStore.id)
        )
    }

    @Transactional(readOnly = true)
    fun getBossStore(storeId: String): BossStoreInfoResponse {
        val bossStore = BossStoreServiceUtils.findBossStoreById(bossStoreRepository, storeId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            location = bossStoreLocationRepository.findBossStoreLocationByBossStoreId(bossStore.id)?.location,
            categories = bossStoreCategoryRepository.findCategoriesByIds(bossStore.categoriesIds),
            bossStoreOpenInfo = bossStoreOpenInfoRepository.findByIdOrNull(bossStore.id)
        )
    }

    companion object {
        private const val MAX_DISTANCE_KM = 2.0
    }

}
