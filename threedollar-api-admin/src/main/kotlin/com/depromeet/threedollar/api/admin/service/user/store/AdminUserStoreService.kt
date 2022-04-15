package com.depromeet.threedollar.api.admin.service.user.store

import com.depromeet.threedollar.api.admin.service.user.store.dto.request.RetrieveLatestStoresRequest
import com.depromeet.threedollar.api.admin.service.user.store.dto.request.RetrieveReportedStoresRequest
import com.depromeet.threedollar.api.admin.service.user.store.dto.response.ReportedStoreInfoResponse
import com.depromeet.threedollar.api.admin.service.user.store.dto.response.StoreInfosWithCursorResponse
import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminUserStoreService(
    private val storeRepository: StoreRepository
) {

    @Transactional(readOnly = true)
    fun retrieveReportedStores(request: RetrieveReportedStoresRequest): List<ReportedStoreInfoResponse> {
        return storeRepository.findStoresByMoreThanReportCntWithPagination(
            request.minCount,
            request.page - 1,
            request.size
        ).map { ReportedStoreInfoResponse.of(it) }
    }

    @Transactional(readOnly = true)
    fun retrieveLatestStores(request: RetrieveLatestStoresRequest): StoreInfosWithCursorResponse {
        val storesWithNextCursor = storeRepository.findAllUsingCursor(request.cursor, request.size + 1)
        val storesCursor = CursorPagingSupporter.of(storesWithNextCursor, request.size)
        return StoreInfosWithCursorResponse.of(storesCursor)
    }

}