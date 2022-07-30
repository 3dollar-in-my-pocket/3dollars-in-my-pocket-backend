package com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.response

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.Admin

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
