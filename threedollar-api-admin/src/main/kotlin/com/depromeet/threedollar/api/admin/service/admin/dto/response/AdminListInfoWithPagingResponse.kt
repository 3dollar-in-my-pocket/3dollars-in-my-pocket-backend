package com.depromeet.threedollar.api.admin.service.admin.dto.response

import com.depromeet.threedollar.api.core.common.dto.PagingResponse
import com.depromeet.threedollar.domain.rds.vendor.domain.admin.Admin

data class AdminListInfoWithPagingResponse(
    val admins: List<AdminInfoResponse>,
    val page: PagingResponse,
) {

    companion object {
        fun of(admins: List<Admin>, perSize: Int, totalSize: Long): AdminListInfoWithPagingResponse {
            return AdminListInfoWithPagingResponse(
                admins = admins.map { AdminInfoResponse.of(it) },
                page = PagingResponse.of(totalSize = totalSize, perSize = perSize)
            )
        }
    }

}
