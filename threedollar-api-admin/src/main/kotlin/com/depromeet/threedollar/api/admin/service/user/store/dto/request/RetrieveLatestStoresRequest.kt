package com.depromeet.threedollar.api.admin.service.user.store.dto.request

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class RetrieveLatestStoresRequest(
    @field:Min(value = 1, message = "{common.size.min}")
    @field:Max(value = 100, message = "{common.size.max}")
    val size: Int = 0,

    val cursor: Long?
)