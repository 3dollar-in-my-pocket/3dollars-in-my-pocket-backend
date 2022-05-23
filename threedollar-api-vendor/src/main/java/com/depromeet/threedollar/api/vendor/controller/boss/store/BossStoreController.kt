package com.depromeet.threedollar.api.vendor.controller.boss.store

import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.config.resolver.DeviceLocation
import com.depromeet.threedollar.api.core.config.resolver.MapLocation
import com.depromeet.threedollar.api.core.service.foodtruck.store.BossStoreCommonService
import com.depromeet.threedollar.api.core.service.foodtruck.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.foodtruck.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.foodtruck.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.api.vendor.config.interceptor.Auth
import com.depromeet.threedollar.common.model.LocationValue
import io.swagger.annotations.ApiOperation

@RestController
class BossStoreController(
    private val bossStoreCommonService: BossStoreCommonService,
) {

    @ApiOperation("주변의 푸드트럭 목록을 조회합니다")
    @GetMapping("/v1/boss/stores/around")
    fun retrieveAroundBossStores(
        @Valid request: GetAroundBossStoresRequest,
        @MapLocation mapLocation: LocationValue,
        @DeviceLocation(required = false) deviceLocation: LocationValue,
    ): ApiResponse<List<BossStoreAroundInfoResponse>> {
        return ApiResponse.success(bossStoreCommonService.getAroundBossStores(
            request = request,
            mapLocation = mapLocation,
            deviceLocation = deviceLocation
        ))
    }

    @ApiOperation("특정 푸드트럭의 상세 정보를 조회합니다")
    @Auth
    @GetMapping("/v1/boss/store/{bossStoreId}")
    fun getBossStoreDetail(
        @PathVariable bossStoreId: String,
        @DeviceLocation(required = false) deviceLocation: LocationValue,
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreCommonService.getBossStore(bossStoreId, deviceLocation))
    }

}
