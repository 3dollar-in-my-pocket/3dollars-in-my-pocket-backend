package com.depromeet.threedollar.api.service.visit.dto.response

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.domain.visit.VisitType
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection
import java.time.LocalDate

data class VisitHistoryResponse(
    val visitId: Long,
    val storeId: Long,
    val type: VisitType,
    val dateOfVisit: LocalDate,
    val userId: Long,
    val userName: String
) : AuditingTimeResponse() {

    companion object {
        fun of(projection: VisitHistoryWithUserProjection): VisitHistoryResponse {
            val response = VisitHistoryResponse(
                visitId = projection.visitId,
                storeId = projection.storeId,
                type = projection.type,
                dateOfVisit = projection.dateOfVisit,
                userId = projection.userId,
                userName = projection.userName
            )
            response.setBaseTime(projection.visitCreatedAt, projection.visitUpdatedAt)
            return response
        }
    }

}
