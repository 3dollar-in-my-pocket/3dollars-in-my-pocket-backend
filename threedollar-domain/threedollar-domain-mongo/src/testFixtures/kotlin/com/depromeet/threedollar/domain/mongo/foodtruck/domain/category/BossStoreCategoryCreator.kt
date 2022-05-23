package com.depromeet.threedollar.domain.mongo.foodtruck.domain.category

import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object BossStoreCategoryCreator {

    fun create(
        title: String,
        sequencePriority: Int = 0,
    ): BossStoreCategory {
        return BossStoreCategory(
            name = title,
            sequencePriority = sequencePriority
        )
    }

}
