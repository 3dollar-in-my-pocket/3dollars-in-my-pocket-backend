package com.depromeet.threedollar.admin.controller.store

import com.depromeet.threedollar.admin.service.store.StoreAdminService
import com.depromeet.threedollar.admin.service.store.dto.request.RetrieveLatestStoresRequest
import com.depromeet.threedollar.admin.service.store.dto.request.RetrieveReportedStoresRequest
import com.depromeet.threedollar.admin.service.store.dto.response.ReportedStoresResponse
import com.depromeet.threedollar.admin.service.store.dto.response.StoresCursorResponse
import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class StoreAdminController(
    private val storeAdminService: StoreAdminService
) {

    @GetMapping("/admin/v1/stores/reported")
    fun retrieveReportedStores(
        @Valid request: RetrieveReportedStoresRequest
    ): ApiResponse<List<ReportedStoresResponse>> {
        return ApiResponse.success(storeAdminService.retrieveReportedStores(request))
    }

    @GetMapping("/admin/v1/stores/latest")
    fun retrieveLatestStores(
        @Valid request: RetrieveLatestStoresRequest
    ): ApiResponse<StoresCursorResponse> {
        return ApiResponse.success(storeAdminService.retrieveLatestStores(request))
    }

}
