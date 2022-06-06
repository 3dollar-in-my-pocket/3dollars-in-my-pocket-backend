package com.depromeet.threedollar.domain.rds.user.domain.store

import com.depromeet.threedollar.common.type.UserMenuCategoryType
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Menu
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store
import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object MenuCreator {

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
