package com.depromeet.threedollar.domain.rds.user.domain.store

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object StoreImageCreator {

    @JvmOverloads
    @JvmStatic
    fun create(
        storeId: Long,
        userId: Long,
        url: String,
        status: StoreImageStatus = StoreImageStatus.ACTIVE
    ): StoreImage {
        return StoreImage.builder()
            .storeId(storeId)
            .userId(userId)
            .url(url)
            .status(status)
            .build()
    }

}
