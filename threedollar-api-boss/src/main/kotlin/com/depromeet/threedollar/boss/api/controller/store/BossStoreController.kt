package com.depromeet.threedollar.boss.api.controller.store

import com.depromeet.threedollar.boss.api.config.resolver.MapCoordinate
import com.depromeet.threedollar.boss.api.service.store.BossStoreService
import com.depromeet.threedollar.boss.api.service.store.dto.response.BossStoreInfoResponse
import com.depromeet.threedollar.common.model.CoordinateValue
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BossStoreController(
    private val bossStoreService: BossStoreService
) {

    @ApiOperation("거리 내의 주변 가게 목록을 조회합니다.")
    @GetMapping("/boss/v1/stores/near")
    fun getNearStores(
        @MapCoordinate mapCoordinate: CoordinateValue,
        @RequestParam distanceKm: Double
    ): List<BossStoreInfoResponse> {
        return bossStoreService.getNearBossStores(
            mapCoordinate = mapCoordinate,
            distanceKm = distanceKm
        )
    }

}
