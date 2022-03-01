package com.depromeet.threedollar.admin.service.admin.dto.response

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.user.domain.admin.Admin

data class AdminInfoResponse(
    val email: String,
    val name: String,
) : AuditingTimeResponse() {

    companion object {
        fun of(admin: Admin): AdminInfoResponse {
            val response = AdminInfoResponse(admin.email, admin.name)
            response.setBaseTime(admin)
            return response
        }
    }

}
