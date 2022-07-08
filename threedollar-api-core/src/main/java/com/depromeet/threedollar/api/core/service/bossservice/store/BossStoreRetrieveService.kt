package com.depromeet.threedollar.api.core.service.bossservice.store

import com.depromeet.threedollar.api.core.service.bossservice.category.BossStoreCategoryService
import com.depromeet.threedollar.api.core.service.bossservice.category.BossStoreCategoryServiceHelper
import com.depromeet.threedollar.api.core.service.bossservice.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreInfoResponse
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
    private val bossStoreCategoryService: BossStoreCategoryService,
) {

    fun getAroundBossStores(
        request: GetAroundBossStoresRequest,
        mapLocation: LocationValue,
        deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
        bossId: String? = null,
    ): List<BossStoreAroundInfoResponse> {
        request.categoryId?.let {
            BossStoreCategoryServiceHelper.validateExistsCategory(bossStoreCategoryRepository, it)
        }

        val bossStores: List<BossStore> = bossStoreRepository.findAllNearBossStoresFilterByCategoryId(
            latitude = mapLocation.latitude,
            longitude = mapLocation.longitude,
            maxDistance = LookupRadiusDistanceLimiter.fromKmToKm(request.distanceKm),
            size = request.size,
            categoryId = request.categoryId
        )

        val categoriesDictionary: Map<String, BossStoreCategoryResponse> = bossStoreCategoryService.retrieveBossStoreCategories().associateBy { it.categoryId }
        val bossStoreOpensDictionary: Map<String, BossStoreOpen> = bossStoreOpenRepository.findBossOpenStoresByIds(bossStoreIds = bossStores.map { bossStore -> bossStore.id })
            .associateBy { it.bossStoreId }

        return bossStores.asSequence()
            .filter { bossStore -> bossStore.isNotOwner(bossId = bossId ?: "") }
            .map { bossStore ->
                BossStoreAroundInfoResponse.of(
                    bossStore = bossStore,
                    categories = getCategories(bossStore, categoriesDictionary),
                    bossStoreOpen = bossStoreOpensDictionary[bossStore.id],
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

    fun getBossStore(
        storeId: String,
        deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
    ): BossStoreInfoResponse {
        val bossStore = BossStoreServiceHelper.findBossStoreById(bossStoreRepository, storeId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            categories = bossStoreCategoryService.retrieveBossStoreCategoriesByIds(bossStore.categoriesIds),
            bossStoreOpen = bossStoreOpenRepository.findBossOpenStoreByBossStoreId(bossStore.id),
            deviceLocation = deviceLocation
        )
    }

    fun getMyBossStore(bossId: String): BossStoreInfoResponse {
        val bossStore = BossStoreServiceHelper.findBossStoreByBossId(bossStoreRepository = bossStoreRepository, bossId = bossId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            categories = bossStoreCategoryService.retrieveBossStoreCategoriesByIds(bossStore.categoriesIds),
            bossStoreOpen = bossStoreOpenRepository.findBossOpenStoreByBossStoreId(bossStore.id),
        )
    }

}
