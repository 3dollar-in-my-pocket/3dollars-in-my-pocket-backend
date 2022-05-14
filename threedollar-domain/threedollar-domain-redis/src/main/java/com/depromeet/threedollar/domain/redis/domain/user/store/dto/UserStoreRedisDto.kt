package com.depromeet.threedollar.domain.redis.domain.user.store.dto

import java.time.LocalDateTime
import com.depromeet.threedollar.common.type.MenuCategoryType

data class UserStoreRedisDto(
    val categories: List<MenuCategoryType>,
    val storeId: Long,
    val latitude: Double,
    val longitude: Double,
    val storeName: String,
    val rating: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {

    companion object {
        @JvmStatic
        fun of(
            categories: List<MenuCategoryType>,
            storeId: Long,
            latitude: Double,
            longitude: Double,
            storeName: String,
            rating: Double,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
        ): UserStoreRedisDto {
            return UserStoreRedisDto(
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

}
