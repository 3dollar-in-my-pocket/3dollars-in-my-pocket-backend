package com.depromeet.threedollar.api.core.service.boss.store

import com.depromeet.threedollar.api.core.service.boss.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.boss.domain.store.BossStoreOpenInfo
import com.depromeet.threedollar.domain.redis.boss.domain.store.BossStoreOpenInfoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.min

private const val MAX_DISTANCE_KM = 2.0

@Service
class BossStoreCommonService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenInfoRepository: BossStoreOpenInfoRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository
) {

    @Transactional(readOnly = true)
    fun getAroundBossStores(mapCoordinate: CoordinateValue, request: GetAroundBossStoresRequest): List<BossStoreInfoResponse> {
        val storeLocations: List<BossStoreLocation> = bossStoreLocationRepository.findNearBossStoreLocations(
            latitude = mapCoordinate.latitude,
            longitude = mapCoordinate.longitude,
            maxDistance = min(request.distanceKm, MAX_DISTANCE_KM)
        )

        val locationsDictionary: Map<String, BossStoreLocation> = storeLocations.associateBy { it.bossStoreId }
        val bossStores: Iterable<BossStore> = bossStoreRepository.findAllById(storeLocations.map { it.bossStoreId })
        val categoriesDictionary: Map<String, BossStoreCategory> = bossStoreCategoryRepository.findAll().associateBy { it.id }
        val openInfoDictionary: Map<String, BossStoreOpenInfo> = bossStoreOpenInfoRepository.findAllById(bossStores.map { it.id }).associateBy { it.bossStoreId }

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
        return bossStore.categoriesIds.mapNotNull { categoriesDictionary[it] }
    }

    @Transactional(readOnly = true)
    fun getBossStore(storeId: String): BossStoreInfoResponse {
        val bossStore = BossStoreCommonServiceUtils.findBossStoreById(bossStoreRepository, storeId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            location = bossStoreLocationRepository.findBossStoreLocationByBossStoreId(bossStore.id)?.location,
            categories = bossStoreCategoryRepository.findCategoriesByIds(bossStore.categoriesIds),
            bossStoreOpenInfo = bossStoreOpenInfoRepository.findByIdOrNull(bossStore.id)
        )
    }

}
