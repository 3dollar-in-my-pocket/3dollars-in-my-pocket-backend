package com.depromeet.threedollar.admin.controller.store

import com.depromeet.threedollar.admin.service.store.StoreAdminService
import com.depromeet.threedollar.admin.service.store.dto.request.RetrieveReportedStoresRequest
import com.depromeet.threedollar.admin.service.store.dto.response.ReportedStoresResponse
import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StoreAdminController(
    private val storeAdminService: StoreAdminService
) {

    @GetMapping("/admin/v1/stores/report")
    fun retrieveReportedStores(request: RetrieveReportedStoresRequest): ApiResponse<List<ReportedStoresResponse>> {
        return ApiResponse.success(storeAdminService.retrieveReportedStores(request))
    }

}
