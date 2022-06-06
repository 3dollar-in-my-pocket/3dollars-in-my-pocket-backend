package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object BossStoreMenuCreator {

    fun create(
        name: String,
        price: Int,
        imageUrl: String?,
    ): BossStoreMenu {
        return BossStoreMenu(
            name = name,
            price = price,
            imageUrl = imageUrl
        )
    }

}
