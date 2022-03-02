package com.depromeet.threedollar.admin.service.store.dto.response

import com.depromeet.threedollar.application.common.dto.CursorResponse
import com.depromeet.threedollar.domain.common.support.CursorPagingSupporter
import com.depromeet.threedollar.domain.user.domain.store.Store

data class StoresCursorResponse(
    val contents: List<StoreInfoResponse>,
    val cursor: CursorResponse<Long>
) {

    companion object {
        fun of(storesCursor: CursorPagingSupporter<Store>): StoresCursorResponse {
            val stores = storesCursor.itemsInCurrentCursor.map { StoreInfoResponse.of(it) }
            if (storesCursor.hasNext()) {
                return StoresCursorResponse(stores, CursorResponse.of(storesCursor.nextCursor.id))
            }
            return StoresCursorResponse(stores, CursorResponse.newLastCursor())
        }
    }

}
