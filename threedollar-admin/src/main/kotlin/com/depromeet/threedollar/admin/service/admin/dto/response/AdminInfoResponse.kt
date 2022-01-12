package com.depromeet.threedollar.admin.service.admin.dto.response

import com.depromeet.threedollar.domain.user.domain.admin.Admin

data class AdminInfoResponse(
    val email: String,
    val name: String,
) {

    companion object {
        fun of(admin: Admin): AdminInfoResponse {
            return AdminInfoResponse(admin.email, admin.name)
        }
    }

}
