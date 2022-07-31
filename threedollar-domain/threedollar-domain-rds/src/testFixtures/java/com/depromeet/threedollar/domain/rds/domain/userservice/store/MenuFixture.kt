package com.depromeet.threedollar.domain.rds.domain.userservice.store

import com.depromeet.threedollar.common.type.UserMenuCategoryType
import com.depromeet.threedollar.common.type.UserMenuCategoryType.BUNGEOPPANG
import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object MenuFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        store: Store,
        name: String = "팥 붕어빵",
        price: String = "3개에 천원",
        category: UserMenuCategoryType = BUNGEOPPANG,
    ): Menu {
        return Menu.builder()
            .store(store)
            .name(name)
            .price(price)
            .category(category)
            .build()
    }

}
