package com.depromeet.threedollar.api.boss.controller.store

import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.boss.config.interceptor.Auth
import com.depromeet.threedollar.api.boss.config.resolver.BossId
import com.depromeet.threedollar.api.boss.service.store.BossStoreOpenService
import com.depromeet.threedollar.api.boss.service.store.BossStoreService
import com.depromeet.threedollar.api.boss.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.boss.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.config.resolver.MapLocation
import com.depromeet.threedollar.api.core.service.foodtruck.store.BossStoreCommonService
import com.depromeet.threedollar.api.core.service.foodtruck.store.dto.request.GetAroundBossStoresRequest
import com.depromeet.threedollar.api.core.service.foodtruck.store.dto.response.BossStoreAroundInfoResponse
import com.depromeet.threedollar.api.core.service.foodtruck.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.LocationValue
import io.swagger.annotations.ApiOperation

@RestController
class BossStoreController(
    private val bossStoreCommonService: BossStoreCommonService,
    private val bossStoreOpenService: BossStoreOpenService,
    private val bossStoreService: BossStoreService,
) {

    @ApiOperation("특정 거리 안에 위치한 가게 목록을 조회합니다.", notes = "distanceKm=1: 반경 1km 이내의 가게 목록을 조회 (최대 2km 제한 중)")
    @Auth(optional = true)
    @GetMapping("/v1/boss/stores/around")
    fun getAroundBossStores(
        @MapLocation mapLocation: LocationValue,
        @Valid request: GetAroundBossStoresRequest,
        @BossId bossId: String?,
    ): ApiResponse<List<BossStoreAroundInfoResponse>> {
        return ApiResponse.success(bossStoreCommonService.getAroundBossStores(
            request = request,
            mapLocation = mapLocation,
            bossId = bossId
        ))
    }

    @ApiOperation("[인증] 가게를 영업 정보를 시작/갱신합니다.", notes = "30분간 갱신하지 않으면 자동 영업 종료 처리되니 주기적으로 갱신해야 합니다.")
    @Auth
    @PutMapping("/v1/boss/store/{bossStoreId}/open")
    fun openBossStore(
        @PathVariable bossStoreId: String,
        @BossId bossId: String,
        @MapLocation mapLocation: LocationValue,
    ): ApiResponse<String> {
        bossStoreOpenService.openBossStore(bossStoreId, bossId, mapLocation)
        return ApiResponse.OK
    }

    @ApiOperation("[인증] 가게를 강제로 영업 종료합니다")
    @Auth
    @PutMapping("/v1/boss/store/{bossStoreId}/close")
    fun closeBossStore(
        @PathVariable bossStoreId: String,
        @BossId bossId: String,
    ): ApiResponse<String> {
        bossStoreOpenService.closeBossStore(bossStoreId, bossId)
        return ApiResponse.OK
    }

    @ApiOperation("[인증] 사장님 자신이 운영중인 가게를 조회합니다.")
    @Auth
    @GetMapping("/v1/boss/store/me")
    fun getMyBossStore(
        @BossId bossId: String,
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreService.getMyBossStore(bossId))
    }

    @ApiOperation("특정 가게의 상세 정보를 조회합니다")
    @Auth
    @GetMapping("/v1/boss/store/{bossStoreId}")
    fun getBossStoreDetail(
        @PathVariable bossStoreId: String,
        @MapLocation(required = false) mapLocation: LocationValue,
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreCommonService.getBossStore(bossStoreId, mapLocation))
    }

    @ApiOperation("[인증] 사장님 자신의 가게의 정보를 수정합니다 (전체 수정)")
    @Auth
    @PutMapping("/v1/boss/store/{bossStoreId}")
    fun updateBossStoreInfo(
        @PathVariable bossStoreId: String,
        @Valid @RequestBody request: UpdateBossStoreInfoRequest,
        @BossId bossId: String,
    ): ApiResponse<String> {
        bossStoreService.updateBossStoreInfo(bossStoreId, request, bossId)
        return ApiResponse.OK
    }

    @ApiOperation("[인증] 사장님 자신의 가게의 정보를 수정합니다 (명시한 리소스만 수정)")
    @Auth
    @PatchMapping("/v1/boss/store/{bossStoreId}")
    fun patchBossStoreInfo(
        @PathVariable bossStoreId: String,
        @Valid @RequestBody request: PatchBossStoreInfoRequest,
        @BossId bossId: String,
    ): ApiResponse<String> {
        bossStoreService.patchBossStoreInfo(bossStoreId, request, bossId)
        return ApiResponse.OK
    }

}
