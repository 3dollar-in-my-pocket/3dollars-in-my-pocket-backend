package com.depromeet.threedollar.api.admin.service.commonservice.admin.dto.request

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class RetrieveAdminsWithPagingRequest(
    @field:Min(value = 1, message = "{common.page.min}")
    val page: Long = 1,

    @field:Min(value = 1, message = "{common.size.min}")
    @field:Max(value = 100, message = "{common.size.max}")
    val size: Int = 10,
)
