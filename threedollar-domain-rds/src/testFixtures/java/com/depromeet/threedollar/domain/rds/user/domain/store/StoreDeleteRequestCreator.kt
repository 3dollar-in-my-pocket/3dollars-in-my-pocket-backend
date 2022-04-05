package com.depromeet.threedollar.domain.rds.user.domain.store

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object StoreDeleteRequestCreator {

    @JvmStatic
    fun create(
            store: Store,
            userId: Long,
            reasonType: DeleteReasonType
    ): StoreDeleteRequest {
        return StoreDeleteRequest.builder()
            .store(store)
            .userId(userId)
            .reason(reasonType)
            .build()
    }

}
