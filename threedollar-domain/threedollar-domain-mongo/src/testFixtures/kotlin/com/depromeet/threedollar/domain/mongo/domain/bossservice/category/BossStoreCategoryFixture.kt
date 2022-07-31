package com.depromeet.threedollar.domain.mongo.domain.bossservice.category

import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object BossStoreCategoryFixture {

    fun create(
        title: String = "푸드트럭 카테고리 명",
        sequencePriority: Int = 0,
        imageUrl: String = "http://default-category-image.png",
    ): BossStoreCategory {
        return BossStoreCategory(
            name = title,
            sequencePriority = sequencePriority,
            imageUrl = imageUrl,
        )
    }

}
