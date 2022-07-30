package com.depromeet.threedollar.api.core.service.service.bossservice.category

import com.depromeet.threedollar.api.core.service.service.bossservice.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheModel
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheRepository
import org.springframework.stereotype.Service

@Service
class BossStoreCategoryService(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository,
) {

    fun retrieveBossStoreCategories(): List<BossStoreCategoryResponse> {
        val cachedCategories = bossStoreCategoryCacheRepository.getCache()
        if (cachedCategories != null) {
            return cachedCategories.map { cachedCategory -> BossStoreCategoryResponse.of(cachedCategory) }
        }

        val originCategories = bossStoreCategoryRepository.findAll()
            .sortedBy { it.sequencePriority }

        val bossStoreCacheModel = originCategories.map { originCategory ->
            BossStoreCategoryCacheModel.of(
                categoryId = originCategory.id,
                name = originCategory.name,
                imageUrl = originCategory.imageUrl,
            )
        }
        bossStoreCategoryCacheRepository.setCache(bossStoreCacheModel)

        return originCategories.map { originCategory ->
            BossStoreCategoryResponse.of(originCategory)
        }
    }

    fun retrieveBossStoreCategoriesByIds(bossStoreCategoryIds: Collection<String>): List<BossStoreCategoryResponse> {
        val bossStoreCategories: List<BossStoreCategoryResponse> = retrieveBossStoreCategories()
        return bossStoreCategories.filter { bossStoreCategoryResponse ->
            bossStoreCategoryIds.contains(bossStoreCategoryResponse.categoryId)
        }
    }

}
