package com.depromeet.threedollar.domain.rds.domain.userservice.store

import com.depromeet.threedollar.common.type.UserMenuCategoryType
import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object MenuFixture {

    @JvmStatic
    fun create(
        store: Store,
        name: String,
        price: String,
        category: UserMenuCategoryType,
    ): Menu {
        return Menu.builder()
            .store(store)
            .name(name)
            .price(price)
            .category(category)
            .build()
    }

}
