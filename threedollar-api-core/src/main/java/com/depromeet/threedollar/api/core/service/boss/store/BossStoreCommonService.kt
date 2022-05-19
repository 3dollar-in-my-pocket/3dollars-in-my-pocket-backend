package com.depromeet.threedollar.api.core.service.boss.store

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.core.service.boss.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.core.service.boss.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.common.utils.distance.LookupRadiusDistanceLimiter
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.boss.feedback.BossStoreFeedbackCountRepository
import com.depromeet.threedollar.domain.redis.domain.boss.store.BossStoreOpenTimeRepository

@Service
class BossStoreCommonService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository,
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

        val categoriesDictionary: Map<String, BossStoreCategory> = bossStoreCategoryRepository.findAll().associateBy { it.id }

        return bossStores.asSequence()
            .filter { it.isNotOwner(bossId ?: "") }
            .map {
                BossStoreAroundInfoResponse.of(
                    bossStore = it,
                    categories = getCategory(it, categoriesDictionary),
                    openStartDateTime = bossStoreOpenTimeRepository.get(it.id),
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
        deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
    ): BossStoreInfoResponse {
        val bossStore = BossStoreCommonServiceUtils.findBossStoreById(bossStoreRepository, storeId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            categories = bossStoreCategoryRepository.findAllCategoriesByIds(bossStore.categoriesIds),
            openStartDateTime = bossStoreOpenTimeRepository.get(bossStore.id),
            deviceLocation = deviceLocation
        )
    }

}
