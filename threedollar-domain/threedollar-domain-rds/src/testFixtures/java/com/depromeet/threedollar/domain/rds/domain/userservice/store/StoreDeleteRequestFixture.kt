package com.depromeet.threedollar.domain.rds.domain.userservice.store

import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object StoreDeleteRequestFixture {

    @JvmStatic
    fun create(
        store: Store,
        userId: Long,
        reasonType: DeleteReasonType = DeleteReasonType.NOSTORE,
    ): StoreDeleteRequest {
        return StoreDeleteRequest.builder()
            .store(store)
            .userId(userId)
            .reason(reasonType)
            .build()
    }

}
