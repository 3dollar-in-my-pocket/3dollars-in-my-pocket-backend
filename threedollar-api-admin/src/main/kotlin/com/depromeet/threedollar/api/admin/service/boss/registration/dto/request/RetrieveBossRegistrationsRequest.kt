package com.depromeet.threedollar.api.admin.service.boss.registration.dto.request

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class RetrieveBossRegistrationsRequest(
    val cursor: String?,

    @field:Max(value = 50, message = "{common.size.min}")
    @field:Min(value = 1, message = "{common.size.min}")
    val size: Int = 20
)
