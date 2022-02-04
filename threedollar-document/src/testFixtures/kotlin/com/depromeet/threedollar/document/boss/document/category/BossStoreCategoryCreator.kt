package com.depromeet.threedollar.document.boss.document.category

import com.depromeet.threedollar.document.TestFixture

@TestFixture
object BossStoreCategoryCreator {

    fun create(
        title: String,
        sequencePriority: Int = 0
    ): BossStoreCategory {
        return BossStoreCategory(
            name = title,
            sequencePriority = sequencePriority
        )
    }

}
