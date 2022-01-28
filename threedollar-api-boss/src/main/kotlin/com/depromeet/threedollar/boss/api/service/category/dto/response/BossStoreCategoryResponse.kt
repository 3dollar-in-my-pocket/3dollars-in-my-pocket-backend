package com.depromeet.threedollar.boss.api.service.category.dto.response

import com.depromeet.threedollar.document.boss.document.category.BossStoreCategory

data class BossStoreCategoryResponse(
    val categoryId: String,
    val title: String
) {

    companion object {
        fun of(bossStoreCategory: BossStoreCategory): BossStoreCategoryResponse {
            return BossStoreCategoryResponse(
                categoryId = bossStoreCategory.id,
                title = bossStoreCategory.title
            )
        }
    }

}
