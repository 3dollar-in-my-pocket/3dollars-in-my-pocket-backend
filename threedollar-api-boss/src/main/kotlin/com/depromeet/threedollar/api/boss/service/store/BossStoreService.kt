package com.depromeet.threedollar.api.boss.service.store

import com.depromeet.threedollar.api.boss.service.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.boss.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossDeletedStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossDeletedStoreRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossStoreService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossDeleteBossStoreRepository: BossDeletedStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository
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

}
