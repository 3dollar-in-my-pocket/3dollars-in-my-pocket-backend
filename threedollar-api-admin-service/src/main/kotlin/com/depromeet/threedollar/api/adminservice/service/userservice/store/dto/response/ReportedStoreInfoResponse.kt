package com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.response

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreType
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithReportedCountProjection

data class ReportedStoreInfoResponse(
    val storeId: Long,
    val storeName: String,
    val latitude: Double,
    val longitude: Double,
    val type: StoreType,
    val rating: Double,
    val reportsCount: Long,
) : AuditingTimeResponse() {

    companion object {
        fun of(projection: StoreWithReportedCountProjection): ReportedStoreInfoResponse {
            val response = ReportedStoreInfoResponse(
                projection.storeId,
                projection.storeName,
                projection.latitude,
                projection.longitude,
                projection.type,
                projection.rating,
                projection.reportsCount
            )
            response.setAuditingTime(projection.storeCreatedAt, projection.storeUpdatedAt)
            return response
        }
    }

}
