package com.depromeet.threedollar.api.admin.service.vendor.store

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.admin.service.vendor.store.dto.request.RetrieveLatestStoresRequest
import com.depromeet.threedollar.api.admin.service.vendor.store.dto.request.RetrieveReportedStoresRequest
import com.depromeet.threedollar.api.admin.service.vendor.store.dto.response.ReportedStoreInfoResponse
import com.depromeet.threedollar.api.admin.service.vendor.store.dto.response.StoreInfosWithCursorResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreRepository

@Service
class AdminUserStoreService(
    private val storeRepository: StoreRepository,
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

    @Transactional
    fun deleteStoreByForce(storeId: Long) {
        val store = storeRepository.findStoreById(storeId)
            ?: throw NotFoundException("해당하는 가게($storeId)는 존재하지 않습니다", ErrorCode.NOTFOUND_STORE)
        store.deleteByAdmin()
    }

}
