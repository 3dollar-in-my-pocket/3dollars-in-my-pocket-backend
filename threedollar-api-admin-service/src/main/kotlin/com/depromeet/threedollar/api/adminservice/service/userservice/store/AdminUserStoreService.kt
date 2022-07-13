package com.depromeet.threedollar.api.adminservice.service.userservice.store

import com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.request.RetrieveLatestStoresRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.request.RetrieveReportedStoresRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.response.ReportedStoreInfoResponse
import com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.response.StoreInfosWithCursorResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.rds.core.support.CursorPagingSupporter
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminUserStoreService(
    private val storeRepository: StoreRepository,
) {

    @Transactional(readOnly = true)
    fun retrieveReportedStores(request: RetrieveReportedStoresRequest): List<ReportedStoreInfoResponse> {
        val storesMoreThanReportCount = storeRepository.findStoresByMoreThanReportCntWithPagination(
            request.minCount,
            request.page - 1,
            request.size
        )
        return storesMoreThanReportCount.map { store -> ReportedStoreInfoResponse.of(store) }
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
            ?: throw NotFoundException("해당하는 가게($storeId)는 존재하지 않습니다", ErrorCode.E404_NOT_EXISTS_STORE)
        store.deleteByAdmin()
    }

}
