package com.depromeet.threedollar.api.core.service.boss.store

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.core.service.boss.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.core.service.boss.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.boss.feedback.BossStoreFeedbackCountRepository
import com.depromeet.threedollar.domain.redis.domain.boss.store.BossStoreOpenTimeRepository
import kotlin.math.min

private const val MAX_DISTANCE_KM = 2.0

@Service
class BossStoreCommonService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository
) {

    @Transactional(readOnly = true)
    fun getAroundBossStores(
        request: GetAroundBossStoresRequest,
        mapLocation: LocationValue,
        deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
        bossId: String? = null
    ): List<BossStoreAroundInfoResponse> {
        request.categoryId?.let {
            BossStoreCategoryServiceUtils.validateExistsCategory(bossStoreCategoryRepository, it)
        }

        val storeLocations: List<BossStoreLocation> = bossStoreLocationRepository.findAllNearBossStoreLocations(
            latitude = mapLocation.latitude,
            longitude = mapLocation.longitude,
            maxDistance = min(request.distanceKm, MAX_DISTANCE_KM),
            size = request.size
        )

        val locationsDictionary: Map<String, BossStoreLocation> = storeLocations.associateBy { it.bossStoreId }
        val bossStores: List<BossStore> = bossStoreRepository.findAllByIdByCategory(bossStoreIds = storeLocations.map { it.bossStoreId }, categoryId = request.categoryId)
        val categoriesDictionary: Map<String, BossStoreCategory> = bossStoreCategoryRepository.findAll().associateBy { it.id }

        return bossStores.asSequence()
            .filter { it.isNotOwner(bossId ?: "") }
            .map {
                BossStoreAroundInfoResponse.of(
                    bossStore = it,
                    categories = getCategory(it, categoriesDictionary),
                    openStartDateTime = bossStoreOpenTimeRepository.get(it.id),
                    location = locationsDictionary[it.id]?.location,
                    deviceLocation = deviceLocation,
                    totalFeedbacksCounts = bossStoreFeedbackCountRepository.getTotalCounts(it.id)
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
        deviceLocation: LocationValue = LocationValue.of(0.0, 0.0)
    ): BossStoreInfoResponse {
        val bossStore = BossStoreCommonServiceUtils.findBossStoreById(bossStoreRepository, storeId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            location = bossStoreLocationRepository.findBossStoreLocationByBossStoreId(bossStore.id)?.location,
            categories = bossStoreCategoryRepository.findAllCategoriesByIds(bossStore.categoriesIds),
            openStartDateTime = bossStoreOpenTimeRepository.get(bossStore.id),
            deviceLocation = deviceLocation
        )
    }

}
