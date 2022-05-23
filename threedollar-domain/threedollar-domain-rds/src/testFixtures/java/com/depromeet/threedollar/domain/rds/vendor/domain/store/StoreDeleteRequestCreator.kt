package com.depromeet.threedollar.domain.rds.vendor.domain.store

import com.depromeet.threedollar.domain.rds.vendor.domain.TestFixture

@TestFixture
object StoreDeleteRequestCreator {

    @JvmStatic
    fun create(
        store: Store,
        userId: Long,
        reasonType: DeleteReasonType,
    ): StoreDeleteRequest {
        return StoreDeleteRequest.builder()
            .store(store)
            .userId(userId)
            .reason(reasonType)
            .build()
    }

}
