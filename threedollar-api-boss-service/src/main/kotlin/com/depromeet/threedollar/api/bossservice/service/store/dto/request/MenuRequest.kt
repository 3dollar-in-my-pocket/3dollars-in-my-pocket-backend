package com.depromeet.threedollar.api.bossservice.service.store.dto.request

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenu
import org.hibernate.validator.constraints.URL
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

data class MenuRequest(
    @field:Size(max = 20, message = "{store.menu.name.size}")
    val name: String,

    @field:PositiveOrZero(message = "{store.menu.price.positiveOrZero}")
    val price: Int = -1,

    @field:URL(message = "{store.imageUrl.url}")
    val imageUrl: String?,
) {

    fun toMenu(): BossStoreMenu {
        return BossStoreMenu.of(
            name = name,
            price = price,
            imageUrl = imageUrl
        )
    }

}
