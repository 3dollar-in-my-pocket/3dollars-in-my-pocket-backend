package com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UpdateMyAdminInfoRequest(
    @field:Size(max = 30, message = "{admin.name.size}")
    @field:NotBlank(message = "{admin.name.notBlank}")
    val name: String,
)
