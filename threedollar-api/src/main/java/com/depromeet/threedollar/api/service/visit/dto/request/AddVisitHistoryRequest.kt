package com.depromeet.threedollar.api.service.visit.dto.request

import com.depromeet.threedollar.domain.domain.visit.VisitHistory
import com.depromeet.threedollar.domain.domain.visit.VisitType
import java.time.LocalDate
import javax.validation.constraints.NotNull

data class AddVisitHistoryRequest(
    @get:NotNull
    val storeId: Long,

    @get:NotNull
    val type: VisitType
) {

    fun toEntity(userId: Long, dateOfVisit: LocalDate): VisitHistory {
        return VisitHistory.of(storeId, userId, type, dateOfVisit)
    }

}
