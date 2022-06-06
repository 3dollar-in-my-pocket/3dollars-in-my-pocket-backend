package com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.response

import com.depromeet.threedollar.api.core.common.dto.CursorResponse
import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store

data class StoreInfosWithCursorResponse(
    val contents: List<StoreInfoResponse>,
    val cursor: CursorResponse<Long>,
) {

    companion object {
        fun of(storesCursor: CursorPagingSupporter<Store>): StoreInfosWithCursorResponse {
            val stores = storesCursor.currentCursorItems.map { store -> StoreInfoResponse.of(store) }
            if (storesCursor.hasNext()) {
                return StoreInfosWithCursorResponse(stores, CursorResponse.of(storesCursor.nextCursor.id))
            }
            return StoreInfosWithCursorResponse(stores, CursorResponse.newLastCursor())
        }
    }

}
