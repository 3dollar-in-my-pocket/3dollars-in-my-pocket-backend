package com.depromeet.threedollar.domain.user.domain.storedelete

import com.depromeet.threedollar.domain.user.domain.ObjectMother
import com.depromeet.threedollar.domain.user.domain.store.Store

@ObjectMother
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
