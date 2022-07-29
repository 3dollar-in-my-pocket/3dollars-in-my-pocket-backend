package com.depromeet.threedollar.domain.redis.domain.bossservice.category

data class BossStoreCategoryCacheModel(
    val categoryId: String,
    val name: String,
    val imageUrl: String,
) {

    companion object {
        fun of(categoryId: String, name: String, imageUrl: String): BossStoreCategoryCacheModel {
            return BossStoreCategoryCacheModel(
                categoryId = categoryId,
                name = name,
                imageUrl = imageUrl,
            )
        }
    }

}
