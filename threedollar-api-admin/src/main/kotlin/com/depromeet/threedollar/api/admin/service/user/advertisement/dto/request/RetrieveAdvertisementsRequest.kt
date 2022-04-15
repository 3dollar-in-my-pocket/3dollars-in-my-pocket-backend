package com.depromeet.threedollar.api.admin.service.user.advertisement.dto.request

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class RetrieveAdvertisementsRequest(
    @field:Min(value = 1, message = "{common.size.min}")
    @field:Max(value = 30, message = "{common.size.max}")
    val size: Long = 1,

    @field:Min(value = 1, message = "{common.page.min}")
    val page: Int = 10
)
