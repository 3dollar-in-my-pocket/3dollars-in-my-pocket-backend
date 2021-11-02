package com.depromeet.threedollar.admin.service.store.dto.request

import javax.validation.constraints.Min

data class RetrieveReportedStoresRequest(
    @field:Min(value = 1, message = "minCount를 1이상 입력해주세요")
    val minCount: Int = 0,

    @field:Min(value = 1, message = "page를 1이상 입력해주세요")
    val page: Long = 0,

    @field:Min(value = 1, message = "1이상 size를 입력해주세요")
    val size: Int = 0
)
