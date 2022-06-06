package com.depromeet.threedollar.api.core.service.bossservice.category.dto.response

import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheModel

data class BossStoreCategoryResponse(
    val categoryId: String,
    val name: String,
) {

    companion object {
        fun of(bossStoreCategory: BossStoreCategory): BossStoreCategoryResponse {
            return BossStoreCategoryResponse(
                categoryId = bossStoreCategory.id,
                name = bossStoreCategory.name
            )
        }

        fun of(bossStoreCategory: BossStoreCategoryCacheModel): BossStoreCategoryResponse {
            return BossStoreCategoryResponse(
                categoryId = bossStoreCategory.categoryId,
                name = bossStoreCategory.name
            )
        }
    }

}
