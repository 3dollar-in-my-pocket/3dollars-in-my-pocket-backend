package com.depromeet.threedollar.application.common.dto

data class CursorResponse<T>(
    val nextCursor: T?,
    val hasMore: Boolean
) {

    companion object {
        private const val LAST_CURSOR = -1L
        fun <T> of(nextCursor: T?): CursorResponse<T> {
            return CursorResponse(
                nextCursor = nextCursor,
                hasMore = nextCursor != null
            )
        }

        fun newLastCursor(): CursorResponse<Long> {
            return CursorResponse(
                nextCursor = LAST_CURSOR,
                hasMore = false
            )
        }
    }

}
