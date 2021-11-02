package com.depromeet.threedollar.admin.service.store.dto.request

import javax.validation.constraints.Min

data class RetrieveLatestStoresRequest(
    @field:Min(value = 1, message = "size를 1이상 입력해주세요")
    val size: Int = 0,

    val cursor: Long?
)
