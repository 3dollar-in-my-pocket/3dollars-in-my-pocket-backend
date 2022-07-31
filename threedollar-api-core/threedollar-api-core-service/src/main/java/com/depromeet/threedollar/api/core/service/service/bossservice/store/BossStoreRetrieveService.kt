package com.depromeet.threedollar.api.core.service.service.bossservice.store

import com.depromeet.threedollar.api.core.service.service.bossservice.category.BossStoreCategoryRetrieveService
import com.depromeet.threedollar.api.core.service.service.bossservice.category.BossStoreCategoryServiceHelper
import com.depromeet.threedollar.api.core.service.service.bossservice.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.common.utils.distance.LookupRadiusDistanceLimiter
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpen
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.feedback.BossStoreFeedbackCountRepository
import org.springframework.stereotype.Service

@Service
class BossStoreRetrieveService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreOpenRepository: BossStoreOpenRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreCategoryRetrieveService: BossStoreCategoryRetrieveService,
) {

    fun getAroundBossStores(
        request: GetAroundBossStoresRequest,
        mapLocation: LocationValue,
        deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
        bossId: String? = null,
    ): List<BossStoreAroundInfoResponse> {
        validateExistCategory(request.categoryId)

        val bossStores: List<BossStore> = bossStoreRepository.findAllNearBossStoresFilterByCategoryId(
            latitude = mapLocation.latitude,
            longitude = mapLocation.longitude,
            maxDistance = LookupRadiusDistanceLimiter.fromKmToKm(request.distanceKm),
            size = request.size,
            categoryId = request.categoryId
        )

        val bossStoreIds = bossStores.map { bossStore -> bossStore.id }

        val categoriesDictionary: Map<String, BossStoreCategoryResponse> = bossStoreCategoryRetrieveService.retrieveBossStoreCategories()
            .associateBy { it.categoryId }

        val bossStoreOpensDictionary: Map<String, BossStoreOpen> = bossStoreOpenRepository.findBossOpenStoresByIds(bossStoreIds = bossStoreIds)
            .associateBy { it.bossStoreId }

        val feedbackTotalCountsDictionary: Map<String, Int> = bossStoreFeedbackCountRepository.getTotalCountsMap(bossStoreIds = bossStoreIds)

        return bossStores.asSequence()
            .filter { bossStore -> bossStore.isNotOwner(bossId = bossId ?: "") }
            .map { bossStore ->
                BossStoreAroundInfoResponse.of(
                    bossStore = bossStore,
                    categories = getCategories(bossStore = bossStore, categoriesDictionary = categoriesDictionary),
                    bossStoreOpen = bossStoreOpensDictionary[bossStore.id],
                    deviceLocation = deviceLocation,
                    totalFeedbacksCounts = feedbackTotalCountsDictionary[bossStore.id]
                )
            }
            .sortedWith(request.orderType.sorted)
            .toList()
    }

    private fun validateExistCategory(categoryId: String?) {
        if (categoryId != null) {
            BossStoreCategoryServiceHelper.validateExistsCategory(bossStoreCategoryRepository, categoryId = categoryId)
        }
    }

    private fun getCategories(bossStore: BossStore, categoriesDictionary: Map<String, BossStoreCategoryResponse>): List<BossStoreCategoryResponse> {
        return bossStore.categoriesIds.mapNotNull { categoryId -> categoriesDictionary[categoryId] }
    }

    fun getBossStore(
        storeId: String,
        deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
    ): BossStoreInfoResponse {
        val bossStore = BossStoreServiceHelper.findBossStoreById(bossStoreRepository, bossStoreId = storeId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            categories = bossStoreCategoryRetrieveService.retrieveBossStoreCategoriesByIds(bossStoreCategoryIds = bossStore.categoriesIds),
            bossStoreOpen = bossStoreOpenRepository.findBossOpenStoreByBossStoreId(bossStoreId = bossStore.id),
            deviceLocation = deviceLocation
        )
    }

    fun getMyBossStore(bossId: String): BossStoreInfoResponse {
        val bossStore = BossStoreServiceHelper.findBossStoreByBossId(bossStoreRepository = bossStoreRepository, bossId = bossId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            categories = bossStoreCategoryRetrieveService.retrieveBossStoreCategoriesByIds(bossStoreCategoryIds = bossStore.categoriesIds),
            bossStoreOpen = bossStoreOpenRepository.findBossOpenStoreByBossStoreId(bossStoreId = bossStore.id),
        )
    }

}
