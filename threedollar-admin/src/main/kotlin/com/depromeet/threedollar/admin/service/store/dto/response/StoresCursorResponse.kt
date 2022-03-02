package com.depromeet.threedollar.admin.service.store.dto.response

import com.depromeet.threedollar.common.model.CursorPagingResponse
import com.depromeet.threedollar.domain.common.collection.CursorSupporter
import com.depromeet.threedollar.domain.user.domain.store.Store

data class StoresCursorResponse(
    val contents: List<StoreInfoResponse>,
    val cursor: CursorPagingResponse<Long>
) {

    companion object {
        fun of(storesCursor: CursorSupporter<Store>): StoresCursorResponse {
            val stores = storesCursor.itemsInCurrentCursor.map { StoreInfoResponse.of(it) }
            if (storesCursor.isLastCursor) {
                return StoresCursorResponse(stores, CursorPagingResponse.newLastCursor())
            }
            return StoresCursorResponse(stores, CursorPagingResponse.of(storesCursor.nextCursor.id))
        }
    }

}
