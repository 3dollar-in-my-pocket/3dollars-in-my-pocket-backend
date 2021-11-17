package com.depromeet.threedollar.admin.service.store.dto.response

import com.depromeet.threedollar.common.collection.ScrollPaginationCollection
import com.depromeet.threedollar.domain.domain.store.Store

data class StoreScrollResponse(
    val contents: List<StoreInfoResponse>,
    val nextCursor: Long
) {

    companion object {
        private const val LAST_CURSOR = -1L

        fun of(scrollCollection: ScrollPaginationCollection<Store>): StoreScrollResponse {
            if (scrollCollection.isLastScroll) {
                return newLastScroll(scrollCollection.itemsInCurrentScroll)
            }
            return newScrollHasNext(scrollCollection.itemsInCurrentScroll, scrollCollection.nextCursor.id)
        }

        private fun newLastScroll(stores: List<Store>): StoreScrollResponse {
            return newScrollHasNext(stores, LAST_CURSOR)
        }

        private fun newScrollHasNext(stores: List<Store>, nextCursor: Long): StoreScrollResponse {
            return StoreScrollResponse(stores.map { StoreInfoResponse.of(it) }, nextCursor)
        }

    }

}
