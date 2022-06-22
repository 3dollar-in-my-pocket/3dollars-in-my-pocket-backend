package com.depromeet.threedollar.domain.rds.domain.userservice.store

import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object StoreImageFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        storeId: Long,
        userId: Long,
        url: String,
        status: StoreImageStatus = StoreImageStatus.ACTIVE,
    ): StoreImage {
        return StoreImage.builder()
            .storeId(storeId)
            .userId(userId)
            .url(url)
            .status(status)
            .build()
    }

}
