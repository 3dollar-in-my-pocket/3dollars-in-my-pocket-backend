package com.depromeet.threedollar.domain.rds.user.domain.store

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object StoreImageCreator {

    @JvmStatic
    fun create(
            store: Store,
            userId: Long,
            url: String
    ): StoreImage {
        return StoreImage.builder()
            .store(store)
            .userId(userId)
            .url(url)
            .build()
    }

}
