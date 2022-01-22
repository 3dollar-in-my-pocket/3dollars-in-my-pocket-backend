package com.depromeet.threedollar.domain.user.domain.store

import com.depromeet.threedollar.domain.user.domain.ObjectMother

@ObjectMother
object MenuCreator {

    @JvmStatic
    fun create(
        store: Store,
        name: String,
        price: String,
        category: MenuCategoryType
    ): Menu {
        return Menu.builder()
            .store(store)
            .name(name)
            .price(price)
            .category(category)
            .build()
    }

}
