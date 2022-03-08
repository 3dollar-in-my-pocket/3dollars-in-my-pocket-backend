package com.depromeet.threedollar.api.boss.controller.store

import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.boss.config.resolver.BossId
import com.depromeet.threedollar.api.boss.config.interceptor.Auth
import com.depromeet.threedollar.api.boss.config.resolver.MapCoordinate
import com.depromeet.threedollar.api.boss.service.store.BossStoreOpenService
import com.depromeet.threedollar.api.boss.service.store.BossStoreRetrieveService
import com.depromeet.threedollar.api.boss.service.store.BossStoreService
import com.depromeet.threedollar.api.boss.service.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.boss.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.boss.service.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.CoordinateValue
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class BossStoreController(
    private val bossStoreRetrieveService: BossStoreRetrieveService,
    private val bossStoreOpenService: BossStoreOpenService,
    private val bossStoreService: BossStoreService
) {

    @ApiOperation("특정 거리 안에 위치한 가게 목록을 조회합니다.", notes = "distanceKm=1: 반경 1km 이내의 가게 목록을 조회 (최대 2km 제한 중)")
    @GetMapping("/v1/boss/stores/around")
    fun getAroundBossStores(
        @MapCoordinate mapCoordinate: CoordinateValue,
        @Valid request: GetAroundBossStoresRequest
    ): ApiResponse<List<BossStoreInfoResponse>> {
        return ApiResponse.success(bossStoreRetrieveService.getAroundBossStores(mapCoordinate, request))
    }

    @ApiOperation("[인증] 가게를 영업 정보를 시작/갱신합니다.", notes = "30분간 갱신하지 않으면 자동 영업 종료 처리되니 주기적으로 갱신해야 합니다.")
    @Auth
    @PutMapping("/v1/boss/store/{bossStoreId}/open")
    fun openBossStore(
        @PathVariable bossStoreId: String,
        @BossId bossId: String,
        @MapCoordinate mapCoordinate: CoordinateValue
    ): ApiResponse<String> {
        bossStoreOpenService.openBossStore(bossStoreId, bossId, mapCoordinate)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("[인증] 가게를 강제로 영업 종료합니다")
    @Auth
    @PutMapping("/v1/boss/store/{bossStoreId}/close")
    fun closeBossStore(
        @PathVariable bossStoreId: String,
        @BossId bossId: String
    ): ApiResponse<String> {
        bossStoreOpenService.closeBossStore(bossStoreId, bossId)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("[인증] 사장님 자신이 운영중인 가게를 조회합니다.")
    @Auth
    @GetMapping("/v1/boss/store/my-store")
    fun getMyBossStore(
        @BossId bossId: String
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreRetrieveService.getMyBossStore(bossId))
    }

    @ApiOperation("특정 가게의 상세 정보를 조회합니다")
    @GetMapping("/v1/boss/store/{bossStoreId}")
    fun getBossStoreDetail(
        @PathVariable bossStoreId: String
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreRetrieveService.getBossStore(bossStoreId))
    }

    @ApiOperation("[인증] 사장님 자신의 가게의 정보를 수정합니다")
    @Auth
    @PutMapping("/v1/boss/store/my-store/{bossStoreId}")
    fun updateBossStoreInfo(
        @PathVariable bossStoreId: String,
        @Valid @RequestBody request: UpdateBossStoreInfoRequest,
        @BossId bossId: String
    ): ApiResponse<String> {
        bossStoreService.updateBossStoreInfo(bossStoreId, request, bossId)
        return ApiResponse.SUCCESS
    }

}