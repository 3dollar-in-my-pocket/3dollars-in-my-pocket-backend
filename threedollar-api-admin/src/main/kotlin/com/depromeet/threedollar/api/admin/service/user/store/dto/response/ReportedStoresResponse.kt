package com.depromeet.threedollar.api.admin.service.user.store.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreType
import com.depromeet.threedollar.domain.rds.user.domain.store.projection.StoreWithReportedCountProjection

data class ReportedStoresResponse(
    val storeId: Long,
    val storeName: String,
    val latitude: Double,
    val longitude: Double,
    val type: StoreType,
    val rating: Double,
    val reportsCount: Long,
) : AuditingTimeResponse() {

    companion object {
        fun of(projection: StoreWithReportedCountProjection): ReportedStoresResponse {
            val response = ReportedStoresResponse(
                projection.storeId,
                projection.storeName,
                projection.latitude,
                projection.longitude,
                projection.type,
                projection.rating,
                projection.reportsCount
            )
            response.setBaseTime(projection.storeCreatedAt, projection.storeUpdatedAt)
            return response
        }
    }

}
