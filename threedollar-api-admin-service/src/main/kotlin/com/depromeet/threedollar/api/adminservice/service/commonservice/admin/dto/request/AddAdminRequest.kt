package com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.Admin

data class AddAdminRequest(
    @field:Size(min = 1, max = 50, message = "{admin.email.size}")
    @field:Email(message = "{admin.email.email}")
    val email: String = "",

    @field:Size(min = 1, max = 30, message = "{admin.name.size}")
    @field:NotBlank(message = "{admin.name.notBlank}")
    val name: String = "",
) {

    fun toEntity(adminId: Long): Admin {
        return Admin.newInstance(email, name, adminId)
    }

}