package com.depromeet.threedollar.admin.service.store.dto.response

import com.depromeet.threedollar.domain.common.collection.CursorSupporter
import com.depromeet.threedollar.domain.user.domain.store.Store

data class StoresCursorResponse(
    val contents: List<StoreInfoResponse>,
    val nextCursor: Long
) {

    companion object {
        private const val LAST_CURSOR = -1L

        fun of(stores: CursorSupporter<Store>): StoresCursorResponse {
            if (stores.isLastCursor) {
                return newLastCursor(stores.itemsInCurrentCursor)
            }
            return newCursorHasNext(stores.itemsInCurrentCursor, stores.nextCursor.id)
        }

        private fun newLastCursor(stores: List<Store>): StoresCursorResponse {
            return newCursorHasNext(stores, LAST_CURSOR)
        }

        private fun newCursorHasNext(stores: List<Store>, nextCursor: Long): StoresCursorResponse {
            return StoresCursorResponse(stores.map { StoreInfoResponse.of(it) }, nextCursor)
        }

    }

}
