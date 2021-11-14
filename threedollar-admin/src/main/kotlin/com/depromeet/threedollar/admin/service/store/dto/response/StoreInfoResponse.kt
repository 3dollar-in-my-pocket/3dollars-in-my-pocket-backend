package com.depromeet.threedollar.admin.service.store.dto.response

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType
import com.depromeet.threedollar.domain.domain.store.Store

data class StoreInfoResponse(
    val storeId: Long,
    val latitude: Double,
    val longitude: Double,
    val storeName: String,
    val rating: Double,
    val categories: List<MenuCategoryType>
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
            response.setBaseTime(store)
            return response
        }
    }

}
