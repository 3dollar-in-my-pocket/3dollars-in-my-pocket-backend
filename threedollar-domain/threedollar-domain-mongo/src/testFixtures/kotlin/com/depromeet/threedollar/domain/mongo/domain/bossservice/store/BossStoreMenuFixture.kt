package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object BossStoreMenuFixture {

    fun create(
        name: String = "푸드트럭 메뉴 이름",
        price: Int = 3000,
        imageUrl: String = "https://store-menu.png",
    ): BossStoreMenu {
        return BossStoreMenu(
            name = name,
            price = price,
            imageUrl = imageUrl
        )
    }

}
