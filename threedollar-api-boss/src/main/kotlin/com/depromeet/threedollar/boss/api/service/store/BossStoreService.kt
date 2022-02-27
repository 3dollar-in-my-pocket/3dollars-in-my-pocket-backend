package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.boss.api.service.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.boss.api.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import com.depromeet.threedollar.document.boss.document.store.BossDeletedStore
import com.depromeet.threedollar.document.boss.document.store.BossDeletedStoreRepository
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
        val bossStore = BossStoreServiceUtils.findBossStoreByBossId(bossStoreRepository, bossId)
        bossDeleteBossStoreRepository.save(BossDeletedStore.of(bossStore))
        bossStoreRepository.delete(bossStore)
    }

}
