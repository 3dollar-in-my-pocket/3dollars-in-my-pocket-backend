package com.depromeet.threedollar.api.controller.visit.dto.request

import javax.validation.constraints.NotNull

data class RetrieveVisitHistoryRequest(
    @get: NotNull
    val storeId: Long
)
