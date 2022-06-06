package com.depromeet.threedollar.api.boss.service.store

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.boss.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.boss.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.service.bossservice.category.BossStoreCategoryService
import com.depromeet.threedollar.api.core.service.bossservice.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossDeletedStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossDeletedStoreRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.store.BossStoreOpenTimeRepository

@Service
class BossStoreService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossDeleteBossStoreRepository: BossDeletedStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
    private val bossStoreCategoryService: BossStoreCategoryService,
) {

    @Transactional
    fun updateBossStoreInfo(
        bossStoreId: String,
        request: UpdateBossStoreInfoRequest,
        bossId: String,
    ) {
        val bossStore = BossStoreServiceUtils.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        BossStoreCategoryServiceUtils.validateExistsCategories(bossStoreCategoryRepository, request.categoriesIds)
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

    @Transactional
    fun patchBossStoreInfo(
        bossStoreId: String,
        request: PatchBossStoreInfoRequest,
        bossId: String,
    ) {
        val bossStore = BossStoreServiceUtils.findBossStoreByIdAndBossId(bossStoreRepository, bossStoreId = bossStoreId, bossId = bossId)
        request.let {
            it.categoriesIds?.let { categoriesIds ->
                BossStoreCategoryServiceUtils.validateExistsCategories(bossStoreCategoryRepository, categoriesIds)
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

    @Transactional
    fun deleteBossStoreByBossId(bossId: String) {
        val bossStore: BossStore? = bossStoreRepository.findBossStoreByBossId(bossId = bossId)
        bossStore?.let {
            bossDeleteBossStoreRepository.save(BossDeletedStore.of(it))
            bossStoreRepository.delete(it)
        }
    }

    @Transactional(readOnly = true)
    fun getMyBossStore(bossId: String): BossStoreInfoResponse {
        val bossStore = BossStoreServiceUtils.findBossStoreByBossId(bossStoreRepository = bossStoreRepository, bossId = bossId)
        return BossStoreInfoResponse.of(
            bossStore = bossStore,
            categories = bossStoreCategoryService.retrieveBossStoreCategoriesByIds(bossStore.categoriesIds),
            openStartDateTime = bossStoreOpenTimeRepository.get(bossStore.id)
        )
    }

}
