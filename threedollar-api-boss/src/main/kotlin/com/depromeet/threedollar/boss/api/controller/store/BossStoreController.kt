package com.depromeet.threedollar.boss.api.controller.store

import com.depromeet.threedollar.application.common.dto.ApiResponse
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
        return ApiResponse.success(bossStoreService.getNearBossStores(
            mapCoordinate = mapCoordinate,
            distanceKm = distanceKm
        ))
    }

    @ApiOperation("[인증] 가게의 오픈 정보를 갱신한다")
    @Auth(role = STORE_OWNER)
    @PutMapping("/boss/v1/store/renew/open/{storeId}")
    fun renewBossStoreOpenInfo(
        @PathVariable storeId: String
    ): ApiResponse<String> {
        bossStoreOpenService.renewBossStoreOpen(storeId)
        return ApiResponse.SUCCESS
    }

}
