package com.depromeet.threedollar.api.core.service.boss.category

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.core.service.boss.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.redis.domain.boss.category.BossStoreCategoryCacheModel
import com.depromeet.threedollar.domain.redis.domain.boss.category.BossStoreCategoryCacheRepository

@Service
class BossStoreCategoryService(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository,
) {

    @Transactional(readOnly = true)
    fun retrieveBossStoreCategories(): List<BossStoreCategoryResponse> {
        val cachedCategories = bossStoreCategoryCacheRepository.getAll()
        cachedCategories?.let {
            return cachedCategories.map { cachedCategory -> BossStoreCategoryResponse.of(cachedCategory) }
        }

        val originCategories = bossStoreCategoryRepository.findAll()
            .sortedBy { it.sequencePriority }

        val bossStoreCacheModel = originCategories.map { originCategory ->
            BossStoreCategoryCacheModel.of(
                categoryId = originCategory.id,
                name = originCategory.name,
            )
        }
        bossStoreCategoryCacheRepository.set(bossStoreCacheModel)

        return originCategories.map { originCategory -> BossStoreCategoryResponse.of(originCategory) }
    }

    @Transactional(readOnly = true)
    fun retrieveBossStoreCategoriesByIds(bossStoreCategories: Collection<String>): List<BossStoreCategoryResponse> {
        return retrieveBossStoreCategories().filter { bossStoreCategoryResponse ->
            bossStoreCategories.contains(bossStoreCategoryResponse.categoryId)
        }
    }

}
