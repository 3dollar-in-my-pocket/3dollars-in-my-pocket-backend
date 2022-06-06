package com.depromeet.threedollar.api.admin.service.userservice.store.dto.request

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class RetrieveReportedStoresRequest(
    @field:Min(value = 1, message = "{store.reported.minCount.min}")
    val minCount: Int = 1,

    @field:Min(value = 1, message = "{common.page.min}")
    val page: Long = 1,

    @field:Min(value = 1, message = "{common.size.min}")
    @field:Max(value = 100, message = "{common.size.max}")
    val size: Int = 10,
)
