package com.depromeet.threedollar.api.core.service.boss.category.dto.response

import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory

data class BossStoreCategoryResponse(
    val categoryId: String,
    val name: String
) {

    companion object {
        fun of(bossStoreCategory: BossStoreCategory): BossStoreCategoryResponse {
            return BossStoreCategoryResponse(
                categoryId = bossStoreCategory.id,
                name = bossStoreCategory.name
            )
        }
    }

}
