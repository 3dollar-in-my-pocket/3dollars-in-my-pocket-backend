package com.depromeet.threedollar.api.core.service.foodtruck.category

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.core.service.foodtruck.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.BossStoreCategoryRepository
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
            return cachedCategories.map { BossStoreCategoryResponse.of(it) }
        }
        val originCategories = bossStoreCategoryRepository.findAll().sortedBy { it.sequencePriority }
        bossStoreCategoryCacheRepository.set(originCategories.map {
            BossStoreCategoryCacheModel.of(
                categoryId = it.id,
                name = it.name,
            )
        })
        return originCategories.map { BossStoreCategoryResponse.of(it) }
    }

    @Transactional(readOnly = true)
    fun retrieveBossStoreCategoriesByIds(bossStoreCategories: Collection<String>): List<BossStoreCategoryResponse> {
        return retrieveBossStoreCategories().filter { bossStoreCategories.contains(it.categoryId) }
    }

}
