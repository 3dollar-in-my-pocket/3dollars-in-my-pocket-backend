package com.depromeet.threedollar.domain.rds.domain.userservice.store

import com.depromeet.threedollar.domain.rds.domain.TestFixture

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
