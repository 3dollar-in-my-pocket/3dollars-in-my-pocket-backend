package com.depromeet.threedollar.api.admin.controller.vendor.store

import javax.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.admin.config.interceptor.Auth
import com.depromeet.threedollar.api.admin.service.vendor.store.AdminUserStoreService
import com.depromeet.threedollar.api.admin.service.vendor.store.dto.request.RetrieveLatestStoresRequest
import com.depromeet.threedollar.api.admin.service.vendor.store.dto.request.RetrieveReportedStoresRequest
import com.depromeet.threedollar.api.admin.service.vendor.store.dto.response.ReportedStoreInfoResponse
import com.depromeet.threedollar.api.admin.service.vendor.store.dto.response.StoreInfosWithCursorResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation

@RestController
class AdminUserStoreController(
    private val adminUserStoreService: AdminUserStoreService,
) {

    @ApiOperation("N개 이상 삭제 요청된 노점상 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/user/stores/reported")
    fun retrieveReportedStores(
        @Valid request: RetrieveReportedStoresRequest,
    ): ApiResponse<List<ReportedStoreInfoResponse>> {
        return ApiResponse.success(adminUserStoreService.retrieveReportedStores(request))
    }

    @ApiOperation("최신순으로 노점상 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/user/stores/latest")
    fun retrieveLatestStores(
        @Valid request: RetrieveLatestStoresRequest,
    ): ApiResponse<StoreInfosWithCursorResponse> {
        return ApiResponse.success(adminUserStoreService.retrieveLatestStores(request))
    }

    @ApiOperation("특정 가게를 강제로 삭제합니다")
    @Auth
    @DeleteMapping("/v1/user/store/{storeId}")
    fun deleteStoreByForce(
        @PathVariable storeId: Long,
    ): ApiResponse<String> {
        adminUserStoreService.deleteStoreByForce(storeId)
        return ApiResponse.OK
    }

}
