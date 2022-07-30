package com.depromeet.threedollar.api.core.common.dto.response

import kotlin.math.ceil

data class PagingResponse(
    val totalPage: Long,
    val totalSize: Long,
) {

    companion object {
        fun of(perSize: Int, totalSize: Long): PagingResponse {
            return PagingResponse(
                totalPage = ceil((totalSize.toDouble() / perSize)).toLong(),
                totalSize = totalSize
            )
        }
    }

}
