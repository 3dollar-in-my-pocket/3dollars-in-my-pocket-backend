package com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenu

data class BossStoreMenuResponse(
    val name: String,
    val price: Int,
    val imageUrl: String?,
) {

    companion object {
        fun of(menu: BossStoreMenu): BossStoreMenuResponse {
            return BossStoreMenuResponse(
                name = menu.name,
                price = menu.price,
                imageUrl = menu.imageUrl,
            )
        }
    }

}
