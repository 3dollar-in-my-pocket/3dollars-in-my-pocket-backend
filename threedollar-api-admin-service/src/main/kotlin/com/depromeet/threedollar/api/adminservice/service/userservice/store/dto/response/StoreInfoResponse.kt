package com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.response

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse
import com.depromeet.threedollar.common.type.UserMenuCategoryType
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store

data class StoreInfoResponse(
    val storeId: Long,
    val latitude: Double,
    val longitude: Double,
    val storeName: String,
    val rating: Double,
    val categories: List<UserMenuCategoryType>,
) : AuditingTimeResponse() {

    companion object {
        fun of(store: Store): StoreInfoResponse {
            val response = StoreInfoResponse(
                store.id,
                store.latitude,
                store.longitude,
                store.name,
                store.rating,
                store.menuCategoriesSortedByCounts
            )
            response.setAuditingTimeByEntity(store)
            return response
        }
    }

}
