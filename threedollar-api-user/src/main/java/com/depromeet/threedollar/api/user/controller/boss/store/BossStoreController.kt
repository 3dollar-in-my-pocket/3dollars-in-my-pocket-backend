package com.depromeet.threedollar.api.user.controller.boss.store

import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.boss.store.BossStoreCommonService
import com.depromeet.threedollar.api.core.service.boss.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.api.user.config.interceptor.Auth
import com.depromeet.threedollar.api.core.config.resolver.GeoCoordinate
import com.depromeet.threedollar.api.core.config.resolver.MapCoordinate
import com.depromeet.threedollar.common.model.CoordinateValue
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class BossStoreController(
    private val bossStoreCommonService: BossStoreCommonService
) {

    @ApiOperation("주변의 사장님 가게 목록을 조회합니다")
    @GetMapping("/v1/boss/stores/around")
    fun retrieveAroundBossStores(
            @Valid request: GetAroundBossStoresRequest,
            @MapCoordinate mapCoordinate: CoordinateValue,
            @GeoCoordinate(required = false) geoCoordinate: CoordinateValue
    ): ApiResponse<List<BossStoreAroundInfoResponse>> {
        return ApiResponse.success(bossStoreCommonService.getAroundBossStores(
            request = request,
            mapCoordinate = mapCoordinate,
            geoCoordinate = geoCoordinate
        ))
    }

    @ApiOperation("특정 사장님 가게의 상세 정보를 조회합니다")
    @Auth
    @GetMapping("/v1/boss/store/{bossStoreId}")
    fun getBossStoreDetail(
        @PathVariable bossStoreId: String,
        @GeoCoordinate(required = false) geoCoordinate: CoordinateValue
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreCommonService.getBossStore(bossStoreId, geoCoordinate))
    }

}
