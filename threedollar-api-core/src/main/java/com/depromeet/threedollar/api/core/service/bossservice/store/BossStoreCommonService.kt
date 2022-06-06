package com.depromeet.threedollar.api.core.service.bossservice.store

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.core.service.bossservice.category.BossStoreCategoryService
import com.depromeet.threedollar.api.core.service.bossservice.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.core.service.bossservice.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.common.utils.distance.LookupRadiusDistanceLimiter
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.feedback.BossStoreFeedbackCountRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.store.BossStoreOpenTimeRepository

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
            .filter { bossStore -> bossStore.isNotOwner(bossId = bossId ?: "") }
            .map { bossStore ->
                BossStoreAroundInfoResponse.of(
                    bossStore = bossStore,
                    categories = getCategories(bossStore, categoriesDictionary),
                    openStartDateTime = bossStoreOpenTimeRepository.get(bossStore.id),
                    deviceLocation = deviceLocation,
                    totalFeedbacksCounts = bossStoreFeedbackCountRepository.getTotalCounts(bossStore.id)
                )
            }
            .sortedWith(request.orderType.sorted)
            .toList()
    }

    private fun getCategories(bossStore: BossStore, categoriesDictionary: Map<String, BossStoreCategoryResponse>): List<BossStoreCategoryResponse> {
        return bossStore.categoriesIds.mapNotNull { categoryId -> categoriesDictionary[categoryId] }
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
