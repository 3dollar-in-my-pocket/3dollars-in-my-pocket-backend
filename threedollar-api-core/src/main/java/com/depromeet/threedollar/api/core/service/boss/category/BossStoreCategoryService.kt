package com.depromeet.threedollar.api.core.service.boss.category

import com.depromeet.threedollar.api.core.service.boss.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.common.type.CacheType.CacheKey.BOSS_STORE_CATEGORIES
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossStoreCategoryService(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository
) {

    @Cacheable(cacheNames = [BOSS_STORE_CATEGORIES], key = "'ALL'")
    @Transactional(readOnly = true)
    fun getBossStoreCategories(): List<BossStoreCategoryResponse> {
        return bossStoreCategoryRepository.findAll().asSequence()
            .sortedBy { it.sequencePriority }
            .map { BossStoreCategoryResponse.of(it) }
            .toList()
    }

}
