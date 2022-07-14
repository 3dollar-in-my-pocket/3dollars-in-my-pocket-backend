package com.depromeet.threedollar.domain.redis.domain.bossservice.category

import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.springframework.stereotype.Repository

@Repository
class BossStoreCategoryCacheRepositoryImpl(
    private val bossStoreCategoryRepository: StringRedisRepository<BossStoreCategoriesCacheKey, List<BossStoreCategoryCacheModel>>,
) : BossStoreCategoryCacheRepository {

    override fun getCache(): List<BossStoreCategoryCacheModel>? {
        return bossStoreCategoryRepository.get(BossStoreCategoriesCacheKey.of())
    }

    override fun setCache(categories: List<BossStoreCategoryCacheModel>) {
        bossStoreCategoryRepository.set(BossStoreCategoriesCacheKey.of(), categories)
    }

    override fun cleanCache() {
        bossStoreCategoryRepository.del(BossStoreCategoriesCacheKey.of())
    }

}
