package com.depromeet.threedollar.domain.rds.user.domain.storeimage

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture
import com.depromeet.threedollar.domain.rds.user.domain.store.Store
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage

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
