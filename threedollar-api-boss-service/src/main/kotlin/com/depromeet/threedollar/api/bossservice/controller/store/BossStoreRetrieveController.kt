package com.depromeet.threedollar.api.bossservice.controller.store

import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.bossservice.config.interceptor.Auth
import com.depromeet.threedollar.api.bossservice.config.resolver.BossId
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.config.resolver.MapLocation
import com.depromeet.threedollar.api.core.service.bossservice.store.BossStoreRetrieveService
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.LocationValue
import io.swagger.annotations.ApiOperation

@RestController
class BossStoreRetrieveController(
    private val bossStoreRetrieveService: BossStoreRetrieveService,
) {

    @ApiOperation("특정 거리 안에 위치한 가게 목록을 조회합니다.", notes = "distanceKm=1: 반경 1km 이내의 가게 목록을 조회 (최대 2km 제한 중)")
    @Auth(optional = true)
    @GetMapping("/v1/boss/stores/around")
    fun getAroundBossStores(
        @MapLocation mapLocation: LocationValue,
        @Valid request: GetAroundBossStoresRequest,
        @BossId bossId: String?,
    ): ApiResponse<List<BossStoreAroundInfoResponse>> {
        return ApiResponse.success(bossStoreRetrieveService.getAroundBossStores(
            request = request,
            mapLocation = mapLocation,
            bossId = bossId
        ))
    }

    @ApiOperation("[인증] 사장님 자신이 운영중인 가게를 조회합니다.")
    @Auth
    @GetMapping("/v1/boss/store/me")
    fun getMyBossStore(
        @BossId bossId: String,
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreRetrieveService.getMyBossStore(bossId))
    }

    @ApiOperation("특정 가게의 상세 정보를 조회합니다")
    @Auth
    @GetMapping("/v1/boss/store/{bossStoreId}")
    fun getBossStoreDetail(
        @PathVariable bossStoreId: String,
        @MapLocation(required = false) mapLocation: LocationValue,
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreRetrieveService.getBossStore(bossStoreId, mapLocation))
    }

}
