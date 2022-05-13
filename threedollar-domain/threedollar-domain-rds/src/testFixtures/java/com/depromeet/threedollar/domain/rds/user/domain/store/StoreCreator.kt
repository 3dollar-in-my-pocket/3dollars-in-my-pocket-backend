package com.depromeet.threedollar.domain.rds.user.domain.store

import com.depromeet.threedollar.common.type.MenuCategoryType
import com.depromeet.threedollar.domain.rds.user.domain.TestFixture
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCreator.create

@TestFixture
object StoreCreator {

    @JvmOverloads
    @JvmStatic
    fun create(
        userId: Long,
        storeName: String,
        latitude: Double = 36.0,
        longitude: Double = 126.0,
        rating: Double = 0.0,
        status: StoreStatus = StoreStatus.ACTIVE
    ): Store {
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(StoreType.STORE)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
            .status(status)
            .build()
    }

    @JvmOverloads
    @JvmStatic
    fun createWithDefaultMenu(
        userId: Long,
        storeName: String,
        latitude: Double = 34.0,
        longitude: Double = 126.0,
        rating: Double = 0.0,
        storeType: StoreType = StoreType.STORE,
        status: StoreStatus = StoreStatus.ACTIVE
    ): Store {
        val store = Store.builder()
            .userId(userId)
            .name(storeName)
            .type(storeType)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
            .status(status)
            .build()
        store.addMenus(listOf(create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)))
        return store
    }

    @JvmOverloads
    @JvmStatic
    fun createDefault(
        userId: Long,
        storeName: String,
        status: StoreStatus = StoreStatus.ACTIVE
    ): Store {
        return create(
            userId = userId,
            storeName = storeName,
            latitude = 34.0,
            longitude = 126.0,
            rating = 0.0,
            status = status
        )
    }

    @JvmOverloads
    @JvmStatic
    fun createDefaultWithMenu(
        userId: Long,
        storeName: String,
        status: StoreStatus = StoreStatus.ACTIVE
    ): Store {
        val store = createDefault(
            userId = userId,
            storeName = storeName,
            status = status
        )
        store.addMenus(listOf(create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)))
        return store
    }

}
