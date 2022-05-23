package com.depromeet.threedollar.api.admin.service.vendor.store.dto.response

import com.depromeet.threedollar.api.core.common.dto.CursorResponse
import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter
import com.depromeet.threedollar.domain.rds.vendor.domain.store.Store

data class StoreInfosWithCursorResponse(
    val contents: List<StoreInfoResponse>,
    val cursor: CursorResponse<Long>,
) {

    companion object {
        fun of(storesCursor: CursorPagingSupporter<Store>): StoreInfosWithCursorResponse {
            val stores = storesCursor.currentCursorItems.map { StoreInfoResponse.of(it) }
            if (storesCursor.hasNext()) {
                return StoreInfosWithCursorResponse(stores, CursorResponse.of(storesCursor.nextCursor.id))
            }
            return StoreInfosWithCursorResponse(stores, CursorResponse.newLastCursor())
        }
    }

}
