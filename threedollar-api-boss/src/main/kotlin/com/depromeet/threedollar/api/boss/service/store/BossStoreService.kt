package com.depromeet.threedollar.api.boss.service.store

import com.depromeet.threedollar.api.boss.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.service.boss.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossDeletedStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossDeletedStoreRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.boss.domain.store.BossStoreOpenTimeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossStoreService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossDeleteBossStoreRepository: BossDeletedStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository
) {

    @Transactional
    fun updateBossStoreInfo(
        bossStoreId: String,
        request: UpdateBossStoreInfoRequest,
        bossId: String
    ) {
        val bossStore = BossStoreServiceUtils.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        BossStoreCategoryServiceUtils.validateExistsCategories(bossStoreCategoryRepository, request.categoriesIds)
        request.let {
            bossStore.updateInfo(it.name, it.imageUrl, it.introduction, it.contactsNumber, it.snsUrl)
            bossStore.updateMenus(request.toMenus())
            bossStore.updateAppearanceDays(request.toAppearanceDays())
            bossStore.updateCategoriesIds(request.categoriesIds)
        }
        bossStoreRepository.save(bossStore)
    }

    @Transactional
    fun deleteBossStoreByBossId(bossId: String) {
        bossStoreRepository.findBossStoreByBossId(bossId)?.let {
            bossDeleteBossStoreRepository.save(BossDeletedStore.of(it))
            bossStoreRepository.delete(it)
        }
    }

    @Transactional(readOnly = true)
    fun getMyBossStore(bossId: String): BossStoreInfoResponse {
        val bossStore = BossStoreServiceUtils.findBossStoreByBossId(bossStoreRepository, bossId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            location = bossStoreLocationRepository.findBossStoreLocationByBossStoreId(bossStore.id)?.location,
            categories = bossStoreCategoryRepository.findCategoriesByIds(bossStore.categoriesIds),
            openStartDateTime = bossStoreOpenTimeRepository.get(bossStore.id)
        )
    }

}
