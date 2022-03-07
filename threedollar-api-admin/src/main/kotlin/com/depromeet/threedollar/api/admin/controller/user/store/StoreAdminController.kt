package com.depromeet.threedollar.api.admin.controller.user.store

import com.depromeet.threedollar.api.admin.config.interceptor.Auth
import com.depromeet.threedollar.api.admin.service.user.store.StoreAdminService
import com.depromeet.threedollar.api.admin.service.user.store.dto.request.RetrieveLatestStoresRequest
import com.depromeet.threedollar.api.admin.service.user.store.dto.request.RetrieveReportedStoresRequest
import com.depromeet.threedollar.api.admin.service.user.store.dto.response.ReportedStoresResponse
import com.depromeet.threedollar.api.admin.service.user.store.dto.response.StoresCursorResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
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
    @GetMapping("/v1/user/stores/reported")
    fun retrieveReportedStores(
        @Valid request: RetrieveReportedStoresRequest
    ): ApiResponse<List<ReportedStoresResponse>> {
        return ApiResponse.success(storeAdminService.retrieveReportedStores(request))
    }

    @ApiOperation("최신순으로 유저 가게 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/user/stores/latest")
    fun retrieveLatestStores(
        @Valid request: RetrieveLatestStoresRequest
    ): ApiResponse<StoresCursorResponse> {
        return ApiResponse.success(storeAdminService.retrieveLatestStores(request))
    }

}
