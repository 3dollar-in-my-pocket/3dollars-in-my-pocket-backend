package com.depromeet.threedollar.api.adminservice.service.commonservice.auth.dto.request

import javax.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "{auth.token.notBlank}")
    val token: String = "",
)
