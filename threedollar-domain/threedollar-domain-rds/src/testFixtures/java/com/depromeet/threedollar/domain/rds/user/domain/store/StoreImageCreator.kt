package com.depromeet.threedollar.domain.rds.user.domain.store

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object StoreImageCreator {

    @JvmStatic
    fun create(
        storeId: Long,
        userId: Long,
        url: String
    ): StoreImage {
        return StoreImage.builder()
            .storeId(storeId)
            .userId(userId)
            .url(url)
            .build()
    }

}
