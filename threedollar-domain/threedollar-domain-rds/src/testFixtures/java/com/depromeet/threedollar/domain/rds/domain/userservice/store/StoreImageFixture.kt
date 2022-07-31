package com.depromeet.threedollar.domain.rds.domain.userservice.store

import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object StoreImageFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        storeId: Long,
        userId: Long = 200000L,
        url: String = "https://store-image.png",
        status: StoreImageStatus = StoreImageStatus.ACTIVE,
    ): StoreImage {
        return StoreImage.builder()
            .storeId(storeId)
            .userId(userId)
            .url(url)
            .status(status)
            .build()
    }

    @JvmOverloads
    @JvmStatic
    fun createDeleted(
        storeId: Long,
        userId: Long = 200000L,
        url: String = "https://store-image.png",
    ): StoreImage {
        return StoreImage.builder()
            .storeId(storeId)
            .userId(userId)
            .url(url)
            .status(StoreImageStatus.INACTIVE)
            .build()
    }

}
