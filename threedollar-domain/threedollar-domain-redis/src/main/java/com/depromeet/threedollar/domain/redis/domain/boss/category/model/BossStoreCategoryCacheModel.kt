package com.depromeet.threedollar.domain.redis.domain.boss.category.model

data class BossStoreCategoryCacheModel(
    val categoryId: String,
    val name: String,
    val sequencePriority: Int,
) {

    companion object {
        fun of(categoryId: String, name: String, sequencePriority: Int): BossStoreCategoryCacheModel {
            return BossStoreCategoryCacheModel(
                categoryId = categoryId,
                name = name,
                sequencePriority = sequencePriority
            )
        }
    }

}
