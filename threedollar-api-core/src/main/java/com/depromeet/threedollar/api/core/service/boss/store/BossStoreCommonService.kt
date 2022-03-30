package com.depromeet.threedollar.api.core.service.boss.store

import com.depromeet.threedollar.api.core.service.boss.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.core.service.boss.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.boss.domain.feedback.BossStoreFeedbackCountRedisRepository
import com.depromeet.threedollar.domain.redis.boss.domain.store.BossStoreOpenRedisRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.min

private const val MAX_DISTANCE_KM = 2.0

@Service
class BossStoreCommonService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenRedisRepository: BossStoreOpenRedisRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository,
    private val bossStoreFeedbackCountRedisRepository: BossStoreFeedbackCountRedisRepository
) {

    @Transactional(readOnly = true)
    fun getAroundBossStores(
        request: GetAroundBossStoresRequest,
        mapCoordinate: CoordinateValue,
        geoCoordinate: CoordinateValue = CoordinateValue.of(0.0, 0.0)
    ): List<BossStoreAroundInfoResponse> {
        request.categoryId?.let {
            BossStoreCategoryServiceUtils.validateExistsCategory(bossStoreCategoryRepository, it)
        }

        val storeLocations: List<BossStoreLocation> = bossStoreLocationRepository.findNearBossStoreLocations(
            latitude = mapCoordinate.latitude,
            longitude = mapCoordinate.longitude,
            maxDistance = min(request.distanceKm, MAX_DISTANCE_KM),
            limit = request.limit
        )

        val locationsDictionary: Map<String, BossStoreLocation> = storeLocations.associateBy { it.bossStoreId }
        val bossStores: List<BossStore> = bossStoreRepository.findAllByIdByCategory(bossStoreIds = storeLocations.map { it.bossStoreId }, categoryId = request.categoryId)
        val categoriesDictionary: Map<String, BossStoreCategory> = bossStoreCategoryRepository.findAll().associateBy { it.id }

        return bossStores.asSequence()
            .map {
                BossStoreAroundInfoResponse.of(
                    bossStore = it,
                    categories = getCategory(it, categoriesDictionary),
                    openStartDateTime = bossStoreOpenRedisRepository.get(it.id),
                    location = locationsDictionary[it.id]?.location,
                    geoCoordinate = geoCoordinate,
                    totalFeedbacksCounts = bossStoreFeedbackCountRedisRepository.getTotalCounts(it.id)
                )
            }
            .sortedWith(request.orderType.sorted)
            .toList()
    }

    private fun getCategory(bossStore: BossStore, categoriesDictionary: Map<String, BossStoreCategory>): List<BossStoreCategory> {
        return bossStore.categoriesIds.mapNotNull { categoriesDictionary[it] }
    }

    @Transactional(readOnly = true)
    fun getBossStore(
        storeId: String,
        geoCoordinate: CoordinateValue = CoordinateValue.of(0.0, 0.0)
    ): BossStoreInfoResponse {
        val bossStore = BossStoreCommonServiceUtils.findBossStoreById(bossStoreRepository, storeId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            location = bossStoreLocationRepository.findBossStoreLocationByBossStoreId(bossStore.id)?.location,
            categories = bossStoreCategoryRepository.findCategoriesByIds(bossStore.categoriesIds),
            openStartDateTime = bossStoreOpenRedisRepository.get(bossStore.id),
            geoCoordinate = geoCoordinate
        )
    }

}
