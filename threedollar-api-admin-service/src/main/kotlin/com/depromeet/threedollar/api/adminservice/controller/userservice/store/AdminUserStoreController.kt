package com.depromeet.threedollar.api.adminservice.controller.userservice.store

import com.depromeet.threedollar.api.adminservice.config.interceptor.Auth
import com.depromeet.threedollar.api.adminservice.service.userservice.store.AdminUserStoreService
import com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.request.RetrieveLatestStoresRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.request.RetrieveReportedStoresRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.response.ReportedStoreInfoResponse
import com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.response.StoreInfosWithCursorResponse
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AdminUserStoreController(
    private val adminUserStoreService: AdminUserStoreService,
) {

    @ApiOperation("N개 이상 삭제 요청된 유저 가게 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/user/stores/reported")
    fun retrieveReportedStores(
        @Valid request: RetrieveReportedStoresRequest,
    ): ApiResponse<List<ReportedStoreInfoResponse>> {
        return ApiResponse.success(adminUserStoreService.retrieveReportedStores(request))
    }

    @ApiOperation("최신순으로 유저 가게 목록을 조회합니다")
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
