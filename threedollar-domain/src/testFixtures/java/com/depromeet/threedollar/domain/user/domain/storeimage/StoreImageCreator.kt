package com.depromeet.threedollar.domain.user.domain.storeimage

import com.depromeet.threedollar.domain.user.domain.ObjectMother
import com.depromeet.threedollar.domain.user.domain.store.Store

@ObjectMother
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
