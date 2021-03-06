package com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.request

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class RetrieveBossRegistrationsRequest(
    val cursor: String?,

    @field:Min(value = 1, message = "{common.size.min}")
    @field:Max(value = 50, message = "{common.size.max}")
    val size: Int = 20,
)
