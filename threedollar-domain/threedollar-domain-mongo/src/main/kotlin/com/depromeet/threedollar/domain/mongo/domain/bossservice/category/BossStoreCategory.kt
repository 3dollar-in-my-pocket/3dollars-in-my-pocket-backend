package com.depromeet.threedollar.domain.mongo.domain.bossservice.category

import com.depromeet.threedollar.domain.mongo.core.model.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_store_category_v1")
class BossStoreCategory(
    val name: String,
    val sequencePriority: Int,
) : BaseDocument() {

    companion object {
        fun of(
            name: String,
            sequencePriority: Int,
        ): BossStoreCategory {
            return BossStoreCategory(
                name = name,
                sequencePriority = sequencePriority
            )
        }
    }

}
