package com.depromeet.threedollar.api.boss.service.store

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.boss.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.boss.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.service.boss.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossDeletedStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossDeletedStoreRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.boss.store.BossStoreOpenTimeRepository

@Service
class BossStoreService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossDeleteBossStoreRepository: BossDeletedStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository
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
        bossId: String
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
            categories = bossStoreCategoryRepository.findAllCategoriesByIds(bossStore.categoriesIds),
            openStartDateTime = bossStoreOpenTimeRepository.get(bossStore.id)
        )
    }

}
