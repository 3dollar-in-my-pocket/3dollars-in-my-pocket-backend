package com.depromeet.threedollar.domain.mongo.domain.bossservice.category

import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object BossStoreCategoryFixture {

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
