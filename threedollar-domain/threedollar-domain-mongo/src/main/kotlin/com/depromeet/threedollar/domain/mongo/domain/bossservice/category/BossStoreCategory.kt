package com.depromeet.threedollar.domain.mongo.domain.bossservice.category

import org.springframework.data.mongodb.core.mapping.Document
import com.depromeet.threedollar.domain.mongo.common.model.BaseDocument

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
