package com.depromeet.threedollar.api.admin.service.admin.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.rds.vendor.domain.admin.Admin

data class AdminInfoResponse(
    val email: String,
    val name: String,
) : AuditingTimeResponse() {

    companion object {
        fun of(admin: Admin): AdminInfoResponse {
            val response = AdminInfoResponse(admin.email, admin.name)
            response.setAuditingTimeByEntity(admin)
            return response
        }
    }

}
