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

    @ApiOperation("거리 내의 주변 가게 목록을 조회합니다.")
    @GetMapping("/boss/v1/stores/near")
    fun getNearStores(
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
    @PutMapping("/boss/v1/store/open/{storeId}")
    fun openBossStore(
        @PathVariable storeId: String
    ): ApiResponse<String> {
        bossStoreOpenService.openStore(storeId)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("[인증] 가게를 강제로 영업 종료합니다")
    @Auth(role = STORE_OWNER)
    @DeleteMapping("/boss/v1/store/close/{storeId}")
    fun closeBossStore(
        @PathVariable storeId: String
    ): ApiResponse<String> {
        bossStoreOpenService.closeStore(storeId)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("사장님 자신이 운영중인 가게를 조회합니다.")
    @Auth
    @GetMapping("/boss/v1/store/me")
    fun getMyBossStore(
        @BossId bossId: String
    ): ApiResponse<BossStoreInfoResponse> {
        return ApiResponse.success(bossStoreService.getMyBossStore(bossId))
    }

}
