package com.depromeet.threedollar.domain.redis.domain.vendor.store

import java.time.LocalDateTime
import com.depromeet.threedollar.common.type.UserMenuCategoryType

data class VendorStoreCacheModel(
    val categories: List<UserMenuCategoryType>,
    val storeId: Long,
    val latitude: Double,
    val longitude: Double,
    val storeName: String,
    val rating: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {

    companion object {
        @JvmStatic
        fun of(
            categories: List<UserMenuCategoryType>,
            storeId: Long,
            latitude: Double,
            longitude: Double,
            storeName: String,
            rating: Double,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
        ): VendorStoreCacheModel {
            return VendorStoreCacheModel(
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
