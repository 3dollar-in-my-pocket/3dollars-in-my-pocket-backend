package com.depromeet.threedollar.domain.user.domain.store

import com.depromeet.threedollar.domain.user.domain.ObjectMother
import com.depromeet.threedollar.domain.user.domain.store.MenuCreator.create

@ObjectMother
object StoreCreator {

    @JvmOverloads
    @JvmStatic
    fun create(
        userId: Long,
        storeName: String,
        latitude: Double = 36.0,
        longitude: Double = 126.0,
        rating: Double = 0.0
    ): Store {
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(StoreType.STORE)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
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
        storeType: StoreType = StoreType.STORE
    ): Store {
        val store = Store.builder()
            .userId(userId)
            .name(storeName)
            .type(storeType)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
            .build()
        store.addMenus(listOf(create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)))
        return store
    }

    @JvmOverloads
    @JvmStatic
    fun createDeleted(
        userId: Long,
        storeName: String,
        latitude: Double = 34.0,
        longitude: Double = 126.0
    ): Store {
        val store = create(userId, storeName, latitude, longitude)
        store.delete()
        return store
    }

    @JvmOverloads
    @JvmStatic
    fun createDeletedWithDefaultMenu(
        userId: Long,
        storeName: String,
        latitude: Double = 34.0,
        longitude: Double = 126.0
    ): Store {
        val store = createDeleted(userId, storeName, latitude, longitude)
        store.addMenus(listOf(create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)))
        return store
    }

    @JvmOverloads
    @JvmStatic
    fun createWithDefaultMenuAndPromotion(
        userId: Long,
        storeName: String,
        latitude: Double,
        longitude: Double,
        promotion: StorePromotion,
        storeType: StoreType = StoreType.CONVENIENCE_STORE,
        rating: Double = 0.0
    ): Store {
        val store = Store.builder()
            .userId(userId)
            .name(storeName)
            .type(storeType)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
            .promotion(promotion)
            .build()
        store.addMenus(listOf(create(store, "메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)))
        return store
    }

}
