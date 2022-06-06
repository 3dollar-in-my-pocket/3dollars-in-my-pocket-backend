package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

data class BossStoreMenu(
    val name: String,
    val price: Int,
    val imageUrl: String?,
) {

    companion object {
        fun of(
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

}
