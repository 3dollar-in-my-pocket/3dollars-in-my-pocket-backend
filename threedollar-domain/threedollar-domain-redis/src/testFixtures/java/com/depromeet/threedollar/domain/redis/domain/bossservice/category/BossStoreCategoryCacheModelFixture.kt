package com.depromeet.threedollar.domain.redis.domain.bossservice.category

import com.depromeet.threedollar.domain.redis.TestFixture

@TestFixture
object BossStoreCategoryCacheModelFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        categoryId: String,
        name: String = "사장님 푸드트럭 가게 이름",
        imageUrl: String = "https://image-url.png",
    ): BossStoreCategoryCacheModel {
        return BossStoreCategoryCacheModel(
            categoryId = categoryId,
            name = name,
            imageUrl = imageUrl,
        )
    }

}
