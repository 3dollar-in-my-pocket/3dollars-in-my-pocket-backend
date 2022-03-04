package com.depromeet.threedollar.admin.controller.store

import com.depromeet.threedollar.admin.config.interceptor.Auth
import com.depromeet.threedollar.admin.service.store.StoreAdminService
import com.depromeet.threedollar.admin.service.store.dto.request.RetrieveLatestStoresRequest
import com.depromeet.threedollar.admin.service.store.dto.request.RetrieveReportedStoresRequest
import com.depromeet.threedollar.admin.service.store.dto.response.ReportedStoresResponse
import com.depromeet.threedollar.admin.service.store.dto.response.StoresCursorResponse
import com.depromeet.threedollar.application.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class StoreAdminController(
    private val storeAdminService: StoreAdminService
) {

    @ApiOperation("N개 이상 삭제 요청된 유저 가게 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/stores/reported")
    fun retrieveReportedStores(
        @Valid request: RetrieveReportedStoresRequest
    ): ApiResponse<List<ReportedStoresResponse>> {
        return ApiResponse.success(storeAdminService.retrieveReportedStores(request))
    }

    @ApiOperation("최신순으로 유저 가게 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/stores/latest")
    fun retrieveLatestStores(
        @Valid request: RetrieveLatestStoresRequest
    ): ApiResponse<StoresCursorResponse> {
        return ApiResponse.success(storeAdminService.retrieveLatestStores(request))
    }

}
