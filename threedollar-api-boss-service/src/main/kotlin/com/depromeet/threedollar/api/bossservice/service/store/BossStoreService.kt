package com.depromeet.threedollar.api.bossservice.service.store

import com.depromeet.threedollar.api.bossservice.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.service.service.bossservice.category.BossStoreCategoryServiceHelper
import com.depromeet.threedollar.api.core.service.service.bossservice.store.BossStoreServiceHelper
import com.depromeet.threedollar.domain.mongo.config.mongo.MongoTransactional
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossDeletedStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossDeletedStoreRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossStoreService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossDeleteBossStoreRepository: BossDeletedStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
) {

    fun updateBossStoreInfo(
        bossStoreId: String,
        request: UpdateBossStoreInfoRequest,
        bossId: String,
    ) {
        val bossStore = BossStoreServiceHelper.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        BossStoreCategoryServiceHelper.validateExistsCategories(bossStoreCategoryRepository, request.categoriesIds)
        request.let {
            bossStore.updateInfo(
                name = it.name,
                imageUrl = it.imageUrl,
                introduction = it.introduction,
                contactsNumber = it.contactsNumber,
                snsUrl = it.snsUrl
            )
            bossStore.updateMenus(it.toMenus())
            bossStore.updateAppearanceDays(it.toAppearanceDays())
            bossStore.updateCategoriesIds(it.categoriesIds)
        }
        bossStoreRepository.save(bossStore)
    }

    fun patchBossStoreInfo(
        bossStoreId: String,
        request: PatchBossStoreInfoRequest,
        bossId: String,
    ) {
        val bossStore = BossStoreServiceHelper.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        request.let {
            it.categoriesIds?.let { categoriesIds ->
                BossStoreCategoryServiceHelper.validateExistsCategories(bossStoreCategoryRepository, categoriesIds)
                bossStore.updateCategoriesIds(categoriesIds)
            }

            bossStore.patchInfo(
                name = it.name,
                imageUrl = it.imageUrl,
                introduction = it.introduction,
                contactsNumber = it.contactsNumber,
                snsUrl = it.snsUrl,
            )
            bossStore.patchMenus(it.toMenus())
            bossStore.patchAppearanceDays(it.toAppearanceDays())
        }
        bossStoreRepository.save(bossStore)
    }

    @MongoTransactional
    @Transactional
    fun deleteBossStoreByBossId(bossId: String) {
        val bossStore: BossStore? = bossStoreRepository.findBossStoreByBossId(bossId = bossId)
        bossStore?.let {
            bossDeleteBossStoreRepository.save(BossDeletedStore.of(it))
            bossStoreRepository.delete(it)
        }
    }

}
