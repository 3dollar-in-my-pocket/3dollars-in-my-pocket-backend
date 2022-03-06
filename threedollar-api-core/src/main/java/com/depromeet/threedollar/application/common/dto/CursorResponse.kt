package com.depromeet.threedollar.application.common.dto

private const val LAST_CURSOR = -1L

data class CursorResponse<T>(
    val nextCursor: T?,
    val hasMore: Boolean
) {

    companion object {
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
