package com.depromeet.threedollar.domain.redis.domain.bossservice.category

import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.springframework.stereotype.Repository

@Repository
class BossStoreCategoryCacheRepository(
    private val bossStoreCategoryRepository: StringRedisRepository<BossStoreCategoriesCacheKey, List<BossStoreCategoryCacheModel>>,
) {

    fun getAll(): List<BossStoreCategoryCacheModel>? {
        return bossStoreCategoryRepository.get(BossStoreCategoriesCacheKey.of())
    }

    fun set(categories: List<BossStoreCategoryCacheModel>) {
        bossStoreCategoryRepository.set(BossStoreCategoriesCacheKey.of(), categories)
    }

}
