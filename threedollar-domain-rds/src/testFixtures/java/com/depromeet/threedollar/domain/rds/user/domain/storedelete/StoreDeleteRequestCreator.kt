package com.depromeet.threedollar.domain.rds.user.domain.storedelete

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture
import com.depromeet.threedollar.domain.rds.user.domain.store.DeleteReasonType
import com.depromeet.threedollar.domain.rds.user.domain.store.Store
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequest

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
