package com.depromeet.threedollar.admin.service.store

import com.depromeet.threedollar.admin.service.store.dto.request.RetrieveLatestStoresRequest
import com.depromeet.threedollar.admin.service.store.dto.request.RetrieveReportedStoresRequest
import com.depromeet.threedollar.admin.service.store.dto.response.ReportedStoresResponse
import com.depromeet.threedollar.admin.service.store.dto.response.StoreScrollResponse
import com.depromeet.threedollar.domain.domain.store.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StoreAdminService(
    private val storeRepository: StoreRepository
) {

    @Transactional(readOnly = true)
    fun retrieveReportedStores(request: RetrieveReportedStoresRequest): List<ReportedStoresResponse> {
        return storeRepository.findStoresByMoreThanReportCntWithPagination(
            request.minCount,
            request.page - 1,
            request.size
        )
            .map { ReportedStoresResponse.of(it) }
            .toList()
    }

    @Transactional(readOnly = true)
    fun retrieveLatestStores(request: RetrieveLatestStoresRequest): StoreScrollResponse {
        val stores = storeRepository.findAllWithScroll(request.cursor, request.size + 1)
        if (stores.size <= request.size) {
            return StoreScrollResponse.lastCursor(stores)
        }
        return StoreScrollResponse.of(
            stores.subList(0, request.size),
            stores[request.size - 1].id
        )
    }

}
