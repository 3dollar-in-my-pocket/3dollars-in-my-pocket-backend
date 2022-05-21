package com.depromeet.threedollar.domain.redis.domain.boss.category

import org.springframework.stereotype.Repository
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import com.depromeet.threedollar.domain.redis.domain.boss.category.model.BossStoreCategoryCacheModel

@Repository
class BossStoreCategoryCacheRepository(
    private val bossStoreCategoryRepository: StringRedisRepository<BossStoreCategoryKey, List<BossStoreCategoryCacheModel>>,
) {

    fun getBossStoreCategories(): List<BossStoreCategoryCacheModel>? {
        return bossStoreCategoryRepository.get(BossStoreCategoryKey.of())
    }

    fun set(categories: List<BossStoreCategoryCacheModel>) {
        bossStoreCategoryRepository.set(BossStoreCategoryKey.of(), categories)
    }

}
