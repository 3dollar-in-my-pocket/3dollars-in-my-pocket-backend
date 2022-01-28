package com.depromeet.threedollar.boss.api.controller.store

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.config.resolver.BossId
import com.depromeet.threedollar.boss.api.config.resolver.Auth
import com.depromeet.threedollar.boss.api.config.resolver.MapCoordinate
import com.depromeet.threedollar.boss.api.config.resolver.Role.STORE_OWNER
import com.depromeet.threedollar.boss.api.service.store.BossStoreOpenService
import com.depromeet.threedollar.boss.api.service.store.BossStoreService
import com.depromeet.threedollar.boss.api.service.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.CoordinateValue
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*

@RestController
class BossStoreController(
    private val bossStoreService: BossStoreService,
    private val bossStoreOpenService: BossStoreOpenService
) {

    @ApiOperation("특정 거리 안에 위치한 가게 목록을 조회합니다.")
    @GetMapping("/boss/v1/boss-stores/near")
    fun getNearBossStores(
        @MapCoordinate mapCoordinate: CoordinateValue,
        @RequestParam distanceKm: Double
    ): ApiResponse<List<BossStoreInfoResponse>> {
        return ApiResponse.success(
            bossStoreService.getNearBossStores(
                mapCoordinate = mapCoordinate,
                distanceKm = distanceKm
            )
        )
    }

    @ApiOperation("[인증] 가게를 영업 정보를 시작/갱신합니다. (30분간 갱신하지 않으면 자동 종료되며, 주기적으로 갱신해야 합니다.)")
    @Auth(role = STORE_OWNER)
    @PutMapping("/boss/v1/boss-store/{bossStoreId}/open")
    fun openBossStore(
        @PathVariable bossStoreId: String
    ): ApiResponse<String> {
        bossStoreOpenService.openBossStore(bossStoreId)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("[인증] 가게를 강제로 영업 종료합니다")
    @Auth(role = STORE_OWNER)
    @DeleteMapping("/boss/v1/boss-store/{bossStoreId}/close")
    fun closeBossStore(
        @PathVariable bossStoreId: String
    ): ApiResponse<String> {
        bossStoreOpenService.closeBossStore(bossStoreId)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("[인증] 사장님 자신이 운영중인 가게를 조회합니다.")
    @Auth
    @GetMapping("/boss/v1/boss-store/me")
    fun getMyBossStore(
        @BossId bossId: String
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreService.getMyBossStore(bossId))
    }

    @ApiOperation("[인증] 특정 가게의 상세 정보를 조회합니다")
    @Auth
    @GetMapping("/boss/v1/boss-store/{bossStoreId}")
    fun getBossStoreDetail(
        @PathVariable bossStoreId: String
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreService.getBossStore(bossStoreId))
    }

}
