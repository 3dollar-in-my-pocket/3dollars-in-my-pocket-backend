package com.depromeet.threedollar.api.boss.service.store

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.boss.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.boss.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.service.foodtruck.category.BossStoreCategoryService
import com.depromeet.threedollar.api.core.service.foodtruck.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.core.service.foodtruck.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.BossDeletedStore
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.BossDeletedStoreRepository
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.boss.store.BossStoreOpenTimeRepository

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
            bossStore.updateMenus(request.toMenus())
            bossStore.updateAppearanceDays(request.toAppearanceDays())
            bossStore.updateCategoriesIds(request.categoriesIds)
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
            bossStore.patchInfo(
                name = it.name,
                imageUrl = it.imageUrl,
                introduction = it.introduction,
                contactsNumber = it.contactsNumber,
                snsUrl = it.snsUrl,
            )
            bossStore.patchMenus(it.toMenus())
            bossStore.patchAppearanceDays(it.toAppearanceDays())
            it.categoriesIds?.let { categoriesIds ->
                BossStoreCategoryServiceUtils.validateExistsCategories(bossStoreCategoryRepository, categoriesIds)
                bossStore.updateCategoriesIds(categoriesIds)
            }
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
            categories = bossStoreCategoryService.retrieveBossStoreCategoriesByIds(bossStore.categoriesIds),
            openStartDateTime = bossStoreOpenTimeRepository.get(bossStore.id)
        )
    }

}
