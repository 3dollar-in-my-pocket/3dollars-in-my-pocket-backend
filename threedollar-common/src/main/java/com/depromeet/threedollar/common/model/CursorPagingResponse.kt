package com.depromeet.threedollar.common.model

data class CursorPagingResponse<T>(
    val nextCursor: T?,
    val hasMore: Boolean
) {

    companion object {
        private const val LAST_CURSOR = -1L
        fun <T> of(nextCursor: T?): CursorPagingResponse<T> {
            return CursorPagingResponse(
                nextCursor = nextCursor,
                hasMore = nextCursor != null
            )
        }

        fun newLastCursor(): CursorPagingResponse<Long> {
            return CursorPagingResponse(
                nextCursor = LAST_CURSOR,
                hasMore = false
            )
        }
    }

}
