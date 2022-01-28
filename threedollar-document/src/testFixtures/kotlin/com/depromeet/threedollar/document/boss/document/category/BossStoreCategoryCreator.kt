package com.depromeet.threedollar.document.boss.document.category

import com.depromeet.threedollar.document.TestFixture

@TestFixture
object BossStoreCategoryCreator {

    fun create(title: String): BossStoreCategory {
        return BossStoreCategory(
            title = title,
            sequencePriority = 0
        )
    }

}
