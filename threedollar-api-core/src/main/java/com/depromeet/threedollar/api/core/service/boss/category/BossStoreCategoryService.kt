package com.depromeet.threedollar.api.core.service.boss.category

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.core.service.boss.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.redis.domain.boss.category.BossStoreCategoryCacheRepository
import com.depromeet.threedollar.domain.redis.domain.boss.category.model.BossStoreCategoryCacheModel

@Service
class BossStoreCategoryService(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository,
) {

    @Transactional(readOnly = true)
    fun retrieveBossStoreCategories(): List<BossStoreCategoryResponse> {
        val cachedCategories = bossStoreCategoryCacheRepository.getBossStoreCategories()
        cachedCategories?.let {
            return cachedCategories.map { BossStoreCategoryResponse.of(it) }
        }
        val originCategories = bossStoreCategoryRepository.findAll().sortedBy { it.sequencePriority }
        bossStoreCategoryCacheRepository.set(originCategories.map {
            BossStoreCategoryCacheModel.of(
                categoryId = it.id,
                name = it.name,
                sequencePriority = it.sequencePriority
            )
        })
        return originCategories.map { BossStoreCategoryResponse.of(it) }
    }

    @Transactional(readOnly = true)
    fun retrieveBossStoreCategoriesByIds(bossStoreCategories: Collection<String>): List<BossStoreCategoryResponse> {
        return retrieveBossStoreCategories().filter { bossStoreCategories.contains(it.categoryId) }
    }

}
