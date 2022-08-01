package com.depromeet.threedollar.domain.redis.domain.userservice.store

import com.depromeet.threedollar.common.type.UserMenuCategoryType
import com.depromeet.threedollar.domain.redis.TestFixture
import java.time.LocalDateTime

@TestFixture
object UserStoreCacheModelFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        storeId: Long,
        latitude: Double = 34.0,
        longitude: Double = 128.5,
        categories: List<UserMenuCategoryType> = listOf(UserMenuCategoryType.BUNGEOPPANG),
        storeName: String = "가게 이름",
        rating: Double = 3.5,
        createdAt: LocalDateTime = LocalDateTime.of(2022, 1, 1, 0, 0),
        updatedAt: LocalDateTime = LocalDateTime.of(2022, 1, 1, 0, 0),
    ): UserStoreCacheModel {
        return UserStoreCacheModel(
            categories = categories,
            storeId = storeId,
            latitude = latitude,
            longitude = longitude,
            storeName = storeName,
            rating = rating,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

}
