package com.depromeet.threedollar.api.core.service.foodtruck.store

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.core.service.foodtruck.category.BossStoreCategoryService
import com.depromeet.threedollar.api.core.service.foodtruck.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.core.service.foodtruck.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.api.core.service.foodtruck.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.foodtruck.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.foodtruck.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.common.utils.distance.LookupRadiusDistanceLimiter
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.boss.feedback.BossStoreFeedbackCountRepository
import com.depromeet.threedollar.domain.redis.domain.boss.store.BossStoreOpenTimeRepository

@Service
class BossStoreCommonService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreCategoryService: BossStoreCategoryService,
) {

    @Transactional(readOnly = true)
    fun getAroundBossStores(
        request: GetAroundBossStoresRequest,
        mapLocation: LocationValue,
        deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
        bossId: String? = null,
    ): List<BossStoreAroundInfoResponse> {
        request.categoryId?.let {
            BossStoreCategoryServiceUtils.validateExistsCategory(bossStoreCategoryRepository, it)
        }

        val bossStores: List<BossStore> = bossStoreRepository.findAllNearBossStoresFilterByCategoryId(
            latitude = mapLocation.latitude,
            longitude = mapLocation.longitude,
            maxDistance = LookupRadiusDistanceLimiter.fromKmToKm(request.distanceKm),
            size = request.size,
            categoryId = request.categoryId
        )

        val categoriesDictionary: Map<String, BossStoreCategoryResponse> = bossStoreCategoryService.retrieveBossStoreCategories().associateBy { it.categoryId }

        return bossStores.asSequence()
            .filter { it.isNotOwner(bossId ?: "") }
            .map {
                BossStoreAroundInfoResponse.of(
                    bossStore = it,
                    categories = getCategories(it, categoriesDictionary),
                    openStartDateTime = bossStoreOpenTimeRepository.get(it.id),
                    deviceLocation = deviceLocation,
                    totalFeedbacksCounts = bossStoreFeedbackCountRepository.getTotalCounts(it.id)
                )
            }
            .sortedWith(request.orderType.sorted)
            .toList()
    }

    private fun getCategories(bossStore: BossStore, categoriesDictionary: Map<String, BossStoreCategoryResponse>): List<BossStoreCategoryResponse> {
        return bossStore.categoriesIds.mapNotNull { categoriesDictionary[it] }
    }

    @Transactional(readOnly = true)
    fun getBossStore(
        storeId: String,
        deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
    ): BossStoreInfoResponse {
        val bossStore = BossStoreCommonServiceUtils.findBossStoreById(bossStoreRepository, storeId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            categories = bossStoreCategoryService.retrieveBossStoreCategoriesByIds(bossStore.categoriesIds),
            openStartDateTime = bossStoreOpenTimeRepository.get(bossStore.id),
            deviceLocation = deviceLocation
        )
    }

}
